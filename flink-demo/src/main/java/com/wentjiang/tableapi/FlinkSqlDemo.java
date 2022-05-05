package com.wentjiang.tableapi;

import com.wentjiang.StudentUtil;
import com.wentjiang.model.Student;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class FlinkSqlDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        DataStream<Student> studentStream = env.fromCollection(StudentUtil.getStudents());
        Table studentTable = tableEnv.fromDataStream(studentStream);
        tableEnv.createTemporaryView("student", studentTable);
        studentTable.printSchema();
        Table resultTable = tableEnv.sqlQuery("select * from student where age > 13");
        DataStream<Student> resultStream = tableEnv.toDataStream(resultTable, DataTypes.of(Student.class));
        resultStream.print();
        env.execute();
    }

}
