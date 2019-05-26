package com.mytlx.compiler.utils;

import javax.swing.*;
import java.io.*;

/**
 * 用于GUI的一些文件操作
 *
 * @author Kevin Guo
 * @date 2019-05-23
 * @time 17:08
 */
public class FileUtils {
    /**
     * 以字符为单位读文件全部内容到JTextArea上
     *
     * @param file
     * @param textArea
     */
    public void readFileByChar(JTextArea textArea, File file) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件不存在...");
        }

        try {
            textArea.setText("");
            int ch;
            while ((ch = fileReader.read()) != -1) {
                textArea.append(String.valueOf((char) ch));
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("文件读取失败...");
        }
    }

    /**
     * 以行为单位读文件全部内容到JTextArea上
     *
     * @param textArea
     * @param file
     */
    public void readFileByLine(JTextArea textArea, File file) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader br = new BufferedReader(fileReader)
        ) {
            textArea.setText("");
            String line;
            while ((line = br.readLine()) != null) {
                textArea.append(line+"\n");
                textArea.paintImmediately(textArea.getBounds());
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("文件读取失败...");
        }
    }

    /**
     * 写JTextArea中文本到文件中
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

    /**
     * 清空文件内容
     *
     * @param file
     */
    public static void clearFile(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
