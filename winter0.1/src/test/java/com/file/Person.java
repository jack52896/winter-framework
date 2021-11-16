package com.file;

/**
 * @author http://zouyujie.top
 * @date 2021/11/16 18:30
 */
public class Person {
    private Student student;
    public String get(){
        System.out.println("调用成功");
       return student.get();
    }
}
