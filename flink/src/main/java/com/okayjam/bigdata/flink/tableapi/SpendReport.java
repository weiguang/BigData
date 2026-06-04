package com.okayjam.bigdata.flink.tableapi;

import static org.apache.flink.table.api.Expressions.$;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.expressions.TimeIntervalUnit;

/**
 * 可以 根据这里 启动docker验证
 * <a href="https://github.com/apache/flink-playgrounds/blob/master/table-walkthrough/docker-compose.yml#L28">...</a>
 *  注意修改下  command: standalone-job 为   command: jobmanager ， 不然启动会失败
 *  然kafka和mysql的端口对外开放
 *      ports:
 *       - "9092:9092"
 *       - "3306:3306"
 * com.okayjam.bigdata.flink.tableapi
 *
 * @author JamChen jamchen@tencent.com
 * @date 2026/06/04 10:44
 **/
public class SpendReport {
    public static void main(String[] args) {
        EnvironmentSettings settings = EnvironmentSettings.inStreamingMode();
        TableEnvironment tEnv = TableEnvironment.create(settings);

        tEnv.executeSql("CREATE TABLE transactions (\n" +
                "    account_id  BIGINT,\n" +
                "    amount      BIGINT,\n" +
                "    transaction_time TIMESTAMP(3),\n" +
                "    WATERMARK FOR transaction_time AS transaction_time - INTERVAL '5' SECOND\n" +
                ") WITH (\n" +
                "    'connector' = 'kafka',\n" +
                "    'topic'     = 'transactions',\n" +
                "    'properties.bootstrap.servers' = '192.168.159.133:9092',\n" +
                "    'properties.group.id' = 'spend-report-group',\n" +
                "    'scan.startup.mode' = 'earliest-offset',\n" +
                "    'format'    = 'csv'\n" +
                ")");

        tEnv.executeSql("CREATE TABLE spend_report (\n" +
                "    account_id BIGINT,\n" +
                "    log_ts     TIMESTAMP(3),\n" +
                "    amount     BIGINT\n," +
                "    PRIMARY KEY (account_id, log_ts) NOT ENFORCED" +
                ") WITH (\n" +
                "   'connector'  = 'jdbc',\n" +
                "   'url'        = 'jdbc:mysql://192.168.159.133:3306/sql-demo',\n" +
                "   'table-name' = 'spend_report',\n" +
                "   'driver'     = 'com.mysql.jdbc.Driver',\n" +
                "   'username'   = 'sql-demo',\n" +
                "   'password'   = 'demo-sql'\n" +
                ")");

        Table transactions = tEnv.from("transactions");
        org.apache.flink.table.api.TableResult result = report(transactions).executeInsert("spend_report");
        result.getJobClient().ifPresent(jobClient -> {
            System.out.println("Job submitted: " + jobClient.getJobID());
            jobClient.getJobStatus().thenAccept(status -> System.out.println("Job status: " + status));
        });
    }

    public static Table report(Table transactions) {
        return transactions
                // ✨ 1. 直接进行分组，把 floor 转换直接写在分组依据里
                .groupBy(
                        $("account_id"),
                        $("transaction_time").floor(TimeIntervalUnit.HOUR).as("log_ts")
                )
                // ✨ 2. 一步到位选择你需要的计算结果
                .select(
                        $("account_id"),
                        $("log_ts"),
                        $("amount").sum().as("amount")
                );
    }
}
