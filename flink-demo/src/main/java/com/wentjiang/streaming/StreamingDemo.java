package com.wentjiang.streaming;

import com.wentjiang.StudentUtil;
import com.wentjiang.model.Student;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamingDemo {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<Student> studentStream = env.fromCollection(StudentUtil.getStudents());
        DataStream<Student> filteredStudentStream = studentStream.filter((FilterFunction<Student>) student -> student.getAge() > 13);
        filteredStudentStream.print();
        env.execute();
    }

}
