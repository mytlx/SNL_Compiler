package com.mytlx.compiler.GUI;

import com.mytlx.compiler.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.mytlx.compiler.utils.PropertiesUtils.getProperty;



/**
 * GUI结构
 *
 * @author Kevin Guo
 * @date 2019-05-23
 * @time 11:43
 */
public class SNLCompilerGUI extends JFrame implements ActionListener {

    private static Logger LOG = LoggerFactory.getLogger(SNLCompilerGUI.class);

    /**
     * 组件声明
     */
    private JSplitPane textAreaSplitPane;//分割面板，包含两个文本区
    private JSplitPane outputSplitPane;//分割面板，包含输出文本区和调试信息区
    private JPanel snlLabelPanel;//snl标签面板
    private JPanel outputLabelPanel;//输出区标签面板
    private JPanel debugLabelPanel;//调试信息区标签面板
    private JTabbedPane debugTabbedPane;//调试信息区选项卡面板
    private JTextArea snlArea;//SNL源文件输入区
    private JTextArea outputArea;//token输出区
    private JTextArea errorArea;//错误信息区
    private JTextArea logArea;//日志信息区
    private JButton openFileButton;//"打开"按钮
    private JButton tokenButton;//"生成token序列"按钮
    private JButton syntaxButton;//"生成语法分析树"按钮
    private JLabel snlLabel;//SNL源文件输入区标签
    private JLabel outputLabel;//输出区标签
    private JLabel debugLabel;//调试信息区标签
    private JPanel buttonPanel;//按钮面板
    private JScrollPane snlScrollPane;//SNL源文件输入区滚动面板
    private JScrollPane outputScrollPane;//token输出区滚动面板
    private JScrollPane errorScrollPane;//错误信息滚动面板
    private JScrollPane logScrollPane;//日志信息滚动面板
    private JPanel snlPanel;//snl区面板
    private JPanel outputPanel;//输出区面板
    private JPanel debugPanel;//调试信息区面板
    private JFileChooser fileChooser=new JFileChooser();//文件选择器(全局变量)

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
        outputLabelPanel = new JPanel();
        debugLabelPanel = new JPanel();
        snlLabel = new JLabel("SNL源文件区");
        outputLabel = new JLabel("输出结果区");
        debugLabel = new JLabel("调试信息区");
        snlArea = new JTextArea();
        outputArea = new JTextArea();
        errorArea = new JTextArea();
        logArea = new JTextArea();
        snlScrollPane = new JScrollPane(snlArea);
        outputScrollPane = new JScrollPane(outputArea);
        errorScrollPane = new JScrollPane(errorArea);
        logScrollPane = new JScrollPane(logArea);
        snlPanel = new JPanel(new BorderLayout());
        outputPanel = new JPanel(new BorderLayout());
        debugPanel = new JPanel(new BorderLayout());
        outputSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputPanel, debugPanel);
        textAreaSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, snlPanel, outputSplitPane);
        debugTabbedPane =new JTabbedPane();
        buttonPanel = new JPanel();
        openFileButton = new JButton("打开SNL源文件");
        tokenButton = new JButton("生成Token序列");
        syntaxButton = new JButton("生成语法分析树");

        //向容器中添加组件
        add(textAreaSplitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        snlPanel.add(snlLabelPanel, BorderLayout.NORTH);
        snlPanel.add(snlScrollPane, BorderLayout.CENTER);
        outputPanel.add(outputLabelPanel, BorderLayout.NORTH);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        debugPanel.add(debugLabelPanel, BorderLayout.NORTH);
        debugPanel.add(debugTabbedPane, BorderLayout.CENTER);
        debugTabbedPane.addTab("日志信息", logScrollPane);
        debugTabbedPane.addTab("错误信息", errorScrollPane);
        snlLabelPanel.add(snlLabel, BorderLayout.CENTER);
        outputLabelPanel.add(outputLabel, BorderLayout.CENTER);
        debugLabelPanel.add(debugLabel, BorderLayout.CENTER);
        buttonPanel.add(openFileButton);
        buttonPanel.add(tokenButton);
        buttonPanel.add(syntaxButton);

        //为组件添加监听器
        openFileButton.addActionListener(this);
        tokenButton.addActionListener(this);
        syntaxButton.addActionListener(this);

        //设置窗口以及组件的属性
        textAreaSplitPane.setDividerLocation(500);
        outputSplitPane.setDividerLocation(500);
        snlScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        snlScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        errorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        errorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        snlArea.setEditable(true);
        outputArea.setEditable(false);
        errorArea.setEditable(false);
        logArea.setEditable(false);
        snlArea.setFont(new Font("Courier",Font.PLAIN,18));
        outputArea.setFont(new Font("Courier",Font.PLAIN,14));
        errorArea.setFont(new Font("Arial", Font.PLAIN, 14));
        errorArea.setForeground(Color.red);
        logArea.setFont(new Font("Arial", Font.PLAIN, 14));
        logArea.setForeground(Color.blue);

        //snl文本区显示行号
        LineNumberHeaderView lineNumberHeader = new LineNumberHeaderView();
        //设置行号的高度，如果和textarea的行高不匹配，可以修改此值
        lineNumberHeader.setLineHeight(19);
        snlScrollPane.setRowHeaderView(lineNumberHeader);

        setTitle("SNL编译器");//窗口名
        setLocation(150, 40);
        setSize(1400, 910);
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
            LOG.trace("GUI点击“生成Token序列");
            if (snlArea.getText().length() == 0) {
                outputArea.setText("请打开一个SNL源文件");
            } else {
                outputLabel.setText("输出结果区：Token序列");
                writeSNLFile(snlArea, getProperty("inPath"));//保存snlArea上文本到文件
                clearLogFile(getProperty("logPath"));
                clearErrorLogFile(getProperty("errorLogPath"));
                LOG.info("=======开始词法分析========");
                LOG.info("源文件：" + getProperty("inPath"));
                LOG.info("输出文件：" + getProperty("tokenPath"));
                LOG.info("===========================");
                ParseForGUI.getToken(getProperty("inPath"), getProperty("tokenPath"));
                readTokenListFile(outputArea, getProperty("tokenPath"));
                readLogFile(logArea, getProperty("logPath"));
                readErrorFile(errorArea,getProperty("errorLogPath"));

            }
        }

        if (e.getSource() == syntaxButton) {
            LOG.trace("GUI点击“生成语法分析树");
            if (snlArea.getText().length() == 0) {
                outputArea.setText("请打开一个SNL源文件");
            } else {
                outputLabel.setText("输出结果区：语法分析树");
                writeSNLFile(snlArea, getProperty("inPath"));//保存snlArea上文本到文件
                clearLogFile(getProperty("logPath"));
                clearErrorLogFile(getProperty("errorLogPath"));
                LOG.info("=======开始语法分析========");
                LOG.info("源文件：" + getProperty("inPath"));
                LOG.info("输出文件：" + getProperty("treePath"));
                LOG.info("===========================");
                ParseForGUI.getSyntaxTree(getProperty("inPath"), getProperty("treePath"));
                readSyntaxTreeFile(outputArea, getProperty("treePath"));
                readLogFile(logArea, getProperty("logPath"));
                readErrorFile(errorArea,getProperty("errorLogPath"));

            }
        }
    }

    /**
     * 显示打开文件对话框
     *
     * @return 在选择文件对话框选择的文件
     */
    private File selectFile() {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showDialog(new JLabel(), "打开SNL源文件");
        File selectedFile = fileChooser.getSelectedFile();
        fileChooser.setCurrentDirectory(selectedFile);
        return selectedFile;
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
            fileUtils.readFileByLine(textArea, selectedFile);
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
        fileUtils.readFileByLine(textArea, new File(fileName));
    }

    /**
     * 读tree文件到OutputArea中
     *
     * @param textArea
     * @param fileName
     */
    private void readSyntaxTreeFile(JTextArea textArea, String fileName) {
        fileUtils.readFileByLine(textArea, new File(fileName));
    }

    /**
     * 读日志文件到LogArea中
     *
     * @param textArea
     * @param fileName
     */
    private void readLogFile(JTextArea textArea, String fileName) {
        fileUtils.readFileByLine(textArea, new File(fileName));
    }

    /**
     * 读错误文件到ErrorArea中
     *
     * @param textArea
     * @param fileName
     */
    private void readErrorFile(JTextArea textArea, String fileName) {
        fileUtils.readFileByLine(textArea, new File(fileName));
    }

    /**
     * 清空日志文件
     *
     * @param fileName
     */
    private void clearLogFile(String fileName) {
        FileUtils.clearFile(new File(fileName));
    }

    /**
     * 清空错误日志文件
     *
     * @param fileName
     */
    private void clearErrorLogFile(String fileName) {
        FileUtils.clearFile(new File(fileName));
    }

    /**
     * 生成界面，单元测试
     *
     * @param args
     */
    public static void main(String[] args) {
        new SNLCompilerGUI();
    }
}

