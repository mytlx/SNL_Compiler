# SNL_Compiler 
> JLU &nbsp; 2016级 &nbsp; 计算机科学与技术学院 &nbsp; 编译原理课程设计  
> SNL语言的词法分析，语法分析  
> 参考：https://github.com/YouthLin/SNL-Compiler  

### 主要功能
1. 词法分析：直接转向法
2. 语法分析：自顶向下的非递归语法分析（预测分析表）
3. GUI  
  
### 使用方法
1. 运行`com.mytlx.compiler`包下的`SNLCforGUI`
2. 图形界面左半部可以直接输入SNL，也可以选择文件输入
3. 点击“生成Token序列”按钮，右上部会输出相应的Token序列
4. 点击“生成语法分析树”按钮，右上部会输出相应的语法分析树
5. 点击上述两按钮的同时，右下部会输出日志信息和错误信息
  
### 运行截图
1. 初始界面
  ![GUI.png](https://raw.githubusercontent.com/mytlx/Image-Hosting/master/SNL_Compiler/GUI.png)
2. 打开文件
  ![open_file.png](https://raw.githubusercontent.com/mytlx/Image-Hosting/master/SNL_Compiler/open_file.png)
3. 成功例子
  ![success_example1.png](https://raw.githubusercontent.com/mytlx/Image-Hosting/master/SNL_Compiler/success_example1.png)
  ![success_example2.png](https://raw.githubusercontent.com/mytlx/Image-Hosting/master/SNL_Compiler/success_example2.png)
4. 错误例子
  ![error_example1.png](https://raw.githubusercontent.com/mytlx/Image-Hosting/master/SNL_Compiler/error_example1.png)
  ![error_example2.png](https://raw.githubusercontent.com/mytlx/Image-Hosting/master/SNL_Compiler/error_example2.png)



### 文件结构
* log/  
    * error.log  
    * log.log  
* src/    
    * main/  
        * java/  
            * com.mytlx.compiler/   
                * GUI/  
                    * LineNumberHeaderView.java  
                    * ParseForGUI.java：GUI用到的词法分析和语法分析  
                    * SNLCompilerGUI.java：GUI结构 
                * lexer/  
                    * Lexer.java：词法分析，直接转向法  
                    * LexerResult.java：词法分析结果  
                    * Token.java：Token格式  
                    * TokenType.java：词法单元枚举  
                * syntax/  
                    * LL1/  
                        * LexParse.java：语法分析之前所需的词法分析，以及语法分析中需要用到的函数  
                        * LL1Parse.java：自顶向下的非递归语法分析  
                    * symbol/  
                        * Symbol.java：终结符和非终结符的父类  
                        * Terminal.java：终结符  
                        * NonTerminal.java：非终结符  
                        * NON_TERMINAL.java：预测分析表  
                    * tree/  
                        * SyntaxTree.java：语法树，打印函数  
                        * TreeNode.java：树结点  
                * utils/  
                    * FileUtils.java：GUI用到的文件操作相关函数  
                    * PropertiesUtils.java：读取properties配置文件的工具类  
                    * ToStringUtils.java：list的toString  
                * SNLCforGUI.java：主启动类，GUI版本 
                * SNLC.java：主启动类，控制台版本 
        * resources/  
            * SNL语言例子/：测试用例  
            * tmp/：GUI用到的中间临时文件  
                * snlText.txt：源文件临时存储
                * tokenList.txt：词法分析生成的Token列表的存储文件
                * syntaxTree.txt：语法分析生成的语法树的存储文件
            * logback.xml：日志的配置文件  
            * path.properties：配置文件，存储文件路径  
            * p.snl：测试用例 
                        
### bug
* windows下，GUI中的汉字可能会乱码
* 词法分析中出现`{}}}`时，第一个`}`被识别，其他的会当作错误符号 

### 勘误：pdf中的产生式和预测集有以下错误
* 产生式48，预测集应为`)`右括号，而不是左括号
    * 用例：c1.txt 第9行：`procedure sd(integer a); `
* 产生式67，预测集应添加`[`左方括号
    * 用例：c1.txt 第16行：`y[ss]:=ss;`
* 产生式93，预测集应添加`]`右方括号
    * 用例：c1.txt 第16行：`y[ss]:=ss;`
* 产生式67，预测集应添加`.`点号
    * 用例：c1.txt 第22行：`a.x:=c;`
* 产生式43，应为：`ProcDecMore ::= ProcDec`，而不是`ProcDeclaration`
    * 用例：c1.txt 第20行：`procedure sss(integer a);`
