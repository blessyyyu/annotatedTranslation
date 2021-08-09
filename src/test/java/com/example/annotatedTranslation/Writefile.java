package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Yu Shaoqing
 * @date 2021/8/9/16:14
 */
public class Writefile {
    @Test
    public static void main(String[] args) throws IOException {
        File writeMessage = new File(".\\Count_and_Time.txt");
        if(!writeMessage.exists()){
            writeMessage.createNewFile();
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(writeMessage,true));
        Date day=new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(day));

        out.write("\n" + df.format(day) + " try");
        out.close();
    }
}
