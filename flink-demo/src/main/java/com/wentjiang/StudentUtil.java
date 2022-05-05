package com.wentjiang;

import com.wentjiang.model.Student;

import java.util.List;

public class StudentUtil {

    public static List<Student> getStudents() {
        return List.of(new Student(1, "zhang3", 12, 3),
                new Student(2, "li4", 13, 4),
                new Student(3, "wang5", 14, 5),
                new Student(4, "zhao6", 15, 4));
    }

}
