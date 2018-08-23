package com.adapter.resourcePath;

import java.io.*;
import java.net.URL;

public class GetReaourceDemo {
    public static void main(String[] args) throws IOException {
        URL url = GetReaourceDemo.class.getResource("/resourcePath/demo01.properties");
        System.out.println("url : " + url.getPath());
        File file = new File(url.getPath());
        InputStream inputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader);
        String temp = null;
        while ( (temp = br.readLine()) != null){
            System.out.println(temp);
        }
    }
}