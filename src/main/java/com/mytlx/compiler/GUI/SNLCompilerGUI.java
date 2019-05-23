package com.mytlx.compiler.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import com.mytlx.compiler.FileUtils.FileUtils;

/**
 * @author Kevin Guo
 * @date 2019-05-23
 * @time 11:43
 */
public class SNLCompilerGUI extends JFrame implements ActionListener {
    /**
     * 组件声明
     */
    private JSplitPane splitPane;//分割面板，包含两个文本区
    private JPanel snlLabelPanel;//snl标签面板
    private JPanel tokenLabelPanel;//token标签面板
    private JTextArea snlArea;//SNL源文件输入区
    private JTextArea outputArea;//token输出区
    private JButton openFileButton;//"打开"按钮
    private JButton tokenButton;//"生成token序列"按钮
    private JButton syntaxButton;//"生成语法分析树"按钮
    private JLabel snlLabel;//SNL源文件输入区标签
    private JLabel outputLabel;//输出区标签
    private JPanel buttonPanel;//按钮面板
    private JScrollPane snlScrollPane;//SNL源文件输入区滚动面板
    private JScrollPane outputScrollPane;//token输出区滚动面板
    private JPanel snlPanel;//snl区面板
    private JPanel outputPanel;//输出区面板
    private JFileChooser fileChooser;//文件选择器

    private FileUtils fileUtils;//文件工具包对象

    /**
     * 构造方法
     */
    public SNLCompilerGUI() {
        guiInit();
        fileUtilsInit();
    }

    /**
     * GUI初始化
     */
    private void guiInit() {
        //新建组件
        snlLabelPanel = new JPanel();
        tokenLabelPanel = new JPanel();
        snlLabel = new JLabel("SNL源文件区");
        outputLabel = new JLabel("输出结果区");
        snlArea = new JTextArea();
        outputArea = new JTextArea();
        snlScrollPane = new JScrollPane(snlArea);
        outputScrollPane = new JScrollPane(outputArea);
        snlPanel = new JPanel(new BorderLayout());
        outputPanel = new JPanel(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, snlPanel, outputPanel);
        buttonPanel = new JPanel();
        openFileButton = new JButton("打开SNL源文件");
        tokenButton = new JButton("生成Token序列");
        syntaxButton = new JButton("生成语法分析树");

        //向容器中添加组件
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        snlPanel.add(snlLabelPanel, BorderLayout.NORTH);
        snlPanel.add(snlScrollPane, BorderLayout.CENTER);
        outputPanel.add(tokenLabelPanel, BorderLayout.NORTH);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        snlLabelPanel.add(snlLabel, BorderLayout.CENTER);
        tokenLabelPanel.add(outputLabel, BorderLayout.CENTER);
        buttonPanel.add(openFileButton);
        buttonPanel.add(tokenButton);
        buttonPanel.add(syntaxButton);

        //为组件添加监听器
        openFileButton.addActionListener(this);
        tokenButton.addActionListener(this);
        syntaxButton.addActionListener(this);

        //设置窗口以及组件的属性
        splitPane.setDividerLocation(450);
        snlScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        snlScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        snlArea.setEditable(true);
        outputArea.setEditable(false);

        //snl文本区显示行号
        LineNumberHeaderView lineNumberHeader = new LineNumberHeaderView();
        //设置行号的高度，如果和textarea的行高不匹配，可以修改此值
        lineNumberHeader.setLineHeight(16);
        snlScrollPane.setRowHeaderView(lineNumberHeader);

        setTitle("SNL编译器");//窗口名
        setLocation(350, 120);
        setSize(900, 700);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 文件工具包初始化
     */
    private void fileUtilsInit() {
        fileUtils = new FileUtils();
    }

    /**
     * 事件监听器
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openFileButton) {
            readSNLFile(snlArea);
        }
        if (e.getSource() == tokenButton) {
            if (snlArea.getText().length() == 0) {
                outputArea.setText("请打开一个SNL源文件");
            } else {
                outputLabel.setText("Token序列");
                writeSNLFile(snlArea, "./src/main/resources/tmp/snlText.txt");//保存snlArea上文本到文件

                readTokenListFile(outputArea, "./src/main/resources/tmp/tokenList.txt");

            }
        }
        if (e.getSource() == syntaxButton) {
            if (snlArea.getText().length() == 0)    {
                outputArea.setText("请打开一个SNL源文件");
            } else {
                outputLabel.setText("语法分析树");
                writeSNLFile(snlArea, "./src/main/resources/tmp/snlText.txt");//保存snlArea上文本到文件

                readSyntaxTreeFile(outputArea, "./src/main/resources/tmp/tree.txt");

            }
        }
    }

    /**
     * 显示打开文件对话框
     *
     * @return 在选择文件对话框选择的文件
     */
    private File selectFile() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.showDialog(new JLabel(), "打开SNL源文件");
        return fileChooser.getSelectedFile();
    }

    /**
     * 读SNL文件到snl区的JTextArea
     *
     * @param textArea
     */
    private void readSNLFile(JTextArea textArea) {
        File selectedFile = selectFile();
        if (selectedFile == null || selectedFile.isDirectory()) {
            System.out.println("请选择文件...");

        } else {
            fileUtils.readFile(textArea, selectedFile);
        }
    }

    /**
     * 写snlArea的文本到文件中
     *
     * @param textArea
     * @param fileName
     */
    private void writeSNLFile(JTextArea textArea, String fileName) {
        fileUtils.writeFile(textArea, new File(fileName));
    }

    /**
     * 读tokenList文件到OutputArea中
     *
     * @param textArea
     * @param fileName
     */
    private void readTokenListFile(JTextArea textArea, String fileName) {
        fileUtils.readFile(textArea, new File(fileName));
    }

    /**
     * 读tree文件到OutputArea中
     *
     * @param textArea
     * @param fileName
     */
    private void readSyntaxTreeFile(JTextArea textArea, String fileName) {
        fileUtils.readFile(textArea, new File(fileName));
    }

    /**
     * 生成界面
     *
     * @param args
     */
    public static void main(String[] args) {
        new SNLCompilerGUI();
    }
}

