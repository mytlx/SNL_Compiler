package com.mytlx.compiler.FileUtils;

import javax.swing.*;
import java.io.*;

/**
 * @author Kevin Guo
 * @date 2019-05-23
 * @time 17:08
 */
public class FileUtils {
    /**
     * 读"./src/main/resources/"目录下的文件到JTextArea上
     *
     * @param file
     * @param textArea
     */
    public void readFile(JTextArea textArea, File file) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件不存在...");
        }
        int c;
        try {
            textArea.setText("");
            while ((c = fileReader.read()) != -1) {
                textArea.setText(textArea.getText() + (char) c);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("文件读取失败...");
        }
    }

    /**
     * 写JTextArea中文本到"./src/main/resources/"目录下的文件中
     *
     * @param textArea
     * @param file
     */
    public void writeFile(JTextArea textArea, File file) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件写入失败...");
        }
        try {
            String[] strLineArr = textArea.getText().split("\n");
            for (String s : strLineArr) {
                fileWriter.write(s);
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件写入失败...");
        }
    }
}
