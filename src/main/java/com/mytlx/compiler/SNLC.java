package com.mytlx.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static com.mytlx.compiler.GUI.ParseForGUI.getSyntaxTree;
import static com.mytlx.compiler.GUI.ParseForGUI.getToken;
import static com.mytlx.compiler.utils.PropertiesUtils.getProperty;

/**
 * SNL编译器的控制台版本，主启动函数
 *
 * @author TLX
 * @date 2019.5.26
 * @time 9:44
 */
public class SNLC {

    public static void getEnter() throws IOException {//停顿
        System.out.println("按回车继续...");
        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int btn = 0;
        boolean flag = true;
        String file = null;
        while (flag) {
            System.out.println("=============================================");
            System.out.println("请输入相应的命令序号：");
            System.out.println("1. 生成Token序列");
            System.out.println("2. 生成语法分析树");
            System.out.println("0. 退出");
            System.out.println("=============================================");
            btn = scanner.nextInt();
            switch (btn) {
                case 1:
                    System.out.println("==================词法分析===================");
                    System.out.println("请输入要分析文件的全路径名：");
                    file = scanner.next();
                    getToken(file, getProperty("tokenPath"));
                    getEnter();
                    break;
                case 2:
                    System.out.println("==================语法分析===================");
                    System.out.println("请输入要分析文件的全路径名：");
                    file = scanner.next();
                    getSyntaxTree(file, getProperty("treePath"));
                    getEnter();
                    break;
                case 0:
                    System.out.println("退出");
                    flag = false;
                    break;
                default:
                    System.out.println("无效命令，请重新输入！");
                    break;
            }
        }
    }
}
