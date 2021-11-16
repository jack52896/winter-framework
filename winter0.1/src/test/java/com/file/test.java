package com.file;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author http://zouyujie.top
 * @date 2021/11/16 18:31
 */
public class test
{
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Person person = Person.class.getConstructor().newInstance();
        Field[] declaredFields = Person.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            declaredField.set(person, new Student());
        }
        Method[] declaredMethods = Person.class.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.invoke(person);
        }
    }
}
