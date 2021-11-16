package com.file;

import java.io.File;
import java.net.URL;

/**
 * @author http://zouyujie.top
 * @date 2021/11/15 22:57
 */
public class test {
    public static void main(String[] args) {
        File file = new File("com/file/application.properties");
        System.out.println(file.getPath());

    }
}
