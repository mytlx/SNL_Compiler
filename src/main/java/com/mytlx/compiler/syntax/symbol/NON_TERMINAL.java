package com.mytlx.compiler.syntax.symbol;

import com.mytlx.compiler.lexer.Token;
import com.mytlx.compiler.lexer.TokenType;

import java.util.List;

import static com.mytlx.compiler.lexer.TokenType.*;
import static com.mytlx.compiler.syntax.symbol.NonTerminal.nonFactory;
import static com.mytlx.compiler.syntax.symbol.Terminal.terFactory;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * 所有非终结符，根据预测分析表返回产生式右侧
 *
 * @author TLX
 * @date 2019.5.20
 * @time 21:47
 */
public enum NON_TERMINAL {
    // ======================总程序===============================
    // (1)
    Program(new NonTerminal("Program")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == PROGRAM) {
                return asList(nonFactory("ProgramHead"), nonFactory("DeclarePart"),
                        nonFactory("ProgramBody"), terFactory(EOF));
            }
            return null;
        }
    },
    // ======================程序头===============================
    // (2)
    ProgramHead(new NonTerminal("ProgramHead")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == PROGRAM) {
                return asList(terFactory(PROGRAM), nonFactory("ProgramName"));
            }
            return null;
        }
    },
    // (3)
    ProgramName(new NonTerminal("ProgramName")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == ID) {
                return singletonList(terFactory(ID));
            }
            return null;
        }
    },
    // ======================程序声明===============================
    // (4)
    DeclarePart(new NonTerminal("DeclarePar")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == TYPE || type == VAR || type == PROCEDURE || type == BEGIN) {
                return asList(nonFactory("TypeDecpart"), nonFactory("VarDecpart"), nonFactory("ProcDecpart"));
            }
            return null;
        }
    },
    // ======================类型声明===============================
    // (5),(6)
    TypeDecpart(new NonTerminal("TypeDecpart")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == VAR || type == PROCEDURE || type == BEGIN) {
                return singletonList(nonFactory("blank"));
            } else if (type == TYPE) {
                return singletonList(nonFactory("TypeDec"));
            }
            return null;
        }
    },
    // (7)
    TypeDec(new NonTerminal("TypeDec")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == TYPE) {
                return asList(terFactory(TYPE), nonFactory("TypeDecList"));
            }
            return null;
        }
    },
    // (8)
    TypeDecList(new NonTerminal("TypeDecList")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == ID) {
                return asList(nonFactory("TypeId"), terFactory(EQ), nonFactory("TypeDef"),
                        terFactory(SEMI), nonFactory("TypeDecMore"));
            }
            return null;
        }
    },
    // (9),(10)
    TypeDecMore(new NonTerminal("TypeDecMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == VAR || type == PROCEDURE || type == BEGIN) {
                return singletonList(nonFactory("blank"));
            } else if (type == ID) {
                return singletonList(nonFactory("TypeDecList"));
            }
            return null;
        }
    },
    // (11)
    TypeId(new NonTerminal("TypeId")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == ID) {
                return singletonList(terFactory(ID));
            }
            return null;
        }
    },
    // ======================类型===============================
    // (12), (13), (14)
    TypeDef(new NonTerminal("TypeDef")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == INTEGER || type == CHAR) {
                return singletonList(nonFactory("BaseType"));
            } else if (type == ARRAY || type == RECORD) {
                return singletonList(nonFactory("StructureType"));
            } else if (type == ID) {
                return singletonList(terFactory(ID));
            }
            return null;
        }
    },
    // (15), (16)
    BaseType(new NonTerminal("BaseType")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == INTEGER) {
                return singletonList(terFactory(INTEGER));
            } else if (type == CHAR) {
                return singletonList(terFactory(CHAR));
            }
            return null;
        }
    },
    // (17), (18)
    StructureType(new NonTerminal("StructureType")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ARRAY) {
                return singletonList(nonFactory("ArrayType"));
            } else if (type == RECORD) {
                return singletonList(nonFactory("RecType"));
            }
            return null;
        }
    },
    // (19)
    ArrayType(new NonTerminal("ArrayType")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ARRAY) {
                return asList(terFactory(ARRAY), terFactory(LMIDPAREN), nonFactory("Low"), terFactory(UNDERRANGE),
                        nonFactory("Top"),terFactory(RMIDPAREN) ,terFactory(OF), nonFactory("BaseType"));
            }
            return null;
        }
    },
    // (20)
    Low(new NonTerminal("Low")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == INTC) {
                return singletonList(terFactory(INTC));
            }
            return null;
        }
    },
    // (21)
    Top(new NonTerminal("Top")) {
        @Override
        public List<Symbol> predict(Token token) {
            if (token.getType() == INTC) {
                return singletonList(terFactory(INTC));
            }
            return null;
        }
    },
    // (22)
    RecType(new NonTerminal("RecType")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == RECORD) {
                return asList(terFactory(RECORD), nonFactory("FieldDecList"), terFactory(END));
            }
            return null;
        }
    },
    // (23), (24)
    FieldDecList(new NonTerminal("FieldDecList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == INTEGER || type == CHAR) {
                return asList(nonFactory("BaseType"), nonFactory("IdList"), terFactory(SEMI), nonFactory("FieldDecMore"));
            } else if (type == ARRAY) {
                return asList(nonFactory("ArrayType"), nonFactory("IdList"), terFactory(SEMI), nonFactory("FieldDecMore"));
            }
            return null;
        }
    },
    // (25), (26)
    FieldDecMore(new NonTerminal("FieldDecMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == END) {
                return singletonList(nonFactory("blank"));
            } else if (type == INTEGER || type == CHAR || type == ARRAY) {
                return singletonList(nonFactory("FieldDecList"));
            }
            return null;
        }
    },
    // (27)
    IdList(new NonTerminal("IdList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return asList(terFactory(ID), nonFactory("IdMore"));
            }
            return null;
        }
    },
    // (28), (29)
    IdMore(new NonTerminal("IdMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == SEMI) {
                return singletonList(nonFactory("blank"));
            } else if (type == COMMA) {
                return asList(terFactory(COMMA), nonFactory("IdList"));
            }
            return null;
        }
    },
    // ======================变量声明===============================
    // (30), (31)
    VarDecpart(new NonTerminal("VarDecpart")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == PROCEDURE || type == BEGIN) {
                return singletonList(nonFactory("blank"));
            } else if (type == VAR) {
                return singletonList(nonFactory("VarDec"));
            }
            return null;
        }
    },
    // (32)
    VarDec(new NonTerminal("VarDec")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == VAR) {
                return asList(terFactory(VAR), nonFactory("VarDecList"));
            }
            return null;
        }
    },
    // (33)
    VarDecList(new NonTerminal("VarDecList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == INTEGER || type == CHAR || type == ARRAY || type == RECORD || type == ID) {
                return asList(nonFactory("TypeDef"), nonFactory("VarIdList"), terFactory(SEMI), nonFactory("VarDecMore"));
            }
            return null;
        }
    },
    // (34), (35)
    VarDecMore(new NonTerminal("VarDecMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == PROCEDURE || type == BEGIN) {
                return singletonList(nonFactory("blank"));
            } else if (type == INTEGER || type == CHAR || type == ARRAY || type == RECORD || type == ID) {
                return singletonList(nonFactory("VarDecList"));
            }
            return null;
        }
    },
    // (36)
    VarIdList(new NonTerminal("VarIdList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return asList(terFactory(ID), nonFactory("VarIdMore"));
            }
            return null;
        }
    },
    // (37), (38)
    VarIdMore(new NonTerminal("VarIdMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == SEMI) {
                return singletonList(nonFactory("blank"));
            } else if (type == COMMA) {
                return asList(terFactory(COMMA), nonFactory("VarIdList"));
            }
            return null;
        }
    },
    // ======================过程声明===============================
    // (39), (40)
    ProcDecpart(new NonTerminal("ProcDecpart")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == BEGIN) {
                return singletonList(nonFactory("blank"));
            } else if (type == PROCEDURE) {
                return singletonList(nonFactory("ProcDec"));
            }
            return null;
        }
    },
    // (41)
    ProcDec(new NonTerminal("ProcDec")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == PROCEDURE) {
                return asList(terFactory(PROCEDURE), nonFactory("ProcName"), terFactory(LPAREN),
                        nonFactory("ParamList"), terFactory(RPAREN), terFactory(SEMI), nonFactory("ProcDecPart"),
                        nonFactory("ProcBody"), nonFactory("ProcDecMore"));
            }
            return null;
        }
    },
    // (42), (43)
    ProcDecMore(new NonTerminal("ProcDecMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == BEGIN) {
                return singletonList(nonFactory("blank"));
            } else if (type == PROCEDURE) {
                return singletonList(nonFactory("ProcDec"));  // correction: 应该是ProcDec而不是ProcDeclaration
            }
            return null;
        }
    },
    // (44)
    ProcName(new NonTerminal("ProcName")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return singletonList(terFactory(ID));
            }
            return null;
        }
    },
    // ======================参数声明===============================
    // (45), (46)
    ParamList(new NonTerminal("ParamList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case RPAREN:
                    return singletonList(nonFactory("blank"));
                case INTEGER:
                case CHAR:
                case ARRAY:
                case RECORD:
                case ID:
                case VAR:
                    return singletonList(nonFactory("ParamDecList"));
            }

            return null;
        }
    },
    // (47)
    ParamDecList(new NonTerminal("ParamDecList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == INTEGER || type == CHAR || type == ARRAY || type == RECORD || type == ID
                    || type == VAR) {
                return asList(nonFactory("Param"), nonFactory("ParamMore"));
            }
            return null;
        }
    },
    // (48), (49)
    ParamMore(new NonTerminal("ParamMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == RPAREN) {       // correction: 书上为左括号，有错误
                return singletonList(nonFactory("blank"));
            } else if (type == SEMI) {
                return asList(terFactory(SEMI), nonFactory("ParamDecList"));
            }
            return null;
        }
    },
    // (50), (51)
    Param(new NonTerminal("Param")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == INTEGER || type == CHAR || type == ARRAY || type == RECORD || type == ID) {
                return asList(nonFactory("TypeDef"), nonFactory("FormList"));
            } else if (type == VAR) {
                return asList(terFactory(VAR), nonFactory("TypeDef"), nonFactory("FormList"));
            }
            return null;
        }
    },
    // (52)
    FormList(new NonTerminal("FormList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return asList(terFactory(ID), nonFactory("FidMore"));
            }
            return null;
        }
    },
    // (53), (54)
    FidMore(new NonTerminal("FidMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case SEMI:
                case RPAREN:
                    return singletonList(nonFactory("blank"));
                case COMMA:
                    return asList(terFactory(COMMA), nonFactory("FormList"));
            }
            return null;
        }
    },
    // ======================过程中的声明部分===============================
    // (55)
    ProcDecPart(new NonTerminal("ProcDecPart")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case TYPE:
                case VAR:
                case PROCEDURE:
                case BEGIN:
                    return singletonList(nonFactory("DeclarePart"));
            }
            return null;
        }
    },
    // ======================过程体===============================
    // (56)
    ProcBody(new NonTerminal("ProcBody")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == BEGIN) {
                return singletonList(nonFactory("ProgramBody"));
            }
            return null;
        }
    },
    // ======================主程序体===============================
    // (57)
    ProgramBody(new NonTerminal("ProgramBody")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == BEGIN) {
                return asList(terFactory(BEGIN), nonFactory("StmList"), terFactory(END));
            }
            return null;
        }
    },
    // ======================语句序列===============================
    // (58)
    StmList(new NonTerminal("StmList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case ID:
                case IF:
                case WHILE:
                case RETURN:
                case READ:
                case WRITE:
                    return asList(nonFactory("Stm"), nonFactory("StmMore"));
            }
            return null;
        }
    },
    // (59), (60)
    StmMore(new NonTerminal("StmMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case ELSE:
                case FI:
                case END:
                case ENDWH:
                    return singletonList(nonFactory("blank"));
                case SEMI:
                    return asList(terFactory(SEMI), nonFactory("StmList"));
            }
            return null;
        }
    },
    // ======================语句===============================
    // (61), (62), (63), (64), (65), (66)
    Stm(new NonTerminal("Stm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case IF:
                    return singletonList(nonFactory("ConditionalStm"));
                case WHILE:
                    return singletonList(nonFactory("LoopStm"));
                case READ:
                    return singletonList(nonFactory("InputStm"));
                case WRITE:
                    return singletonList(nonFactory("OutputStm"));
                case RETURN:
                    return singletonList(nonFactory("ReturnStm"));
                case ID:
                    return asList(terFactory(ID), nonFactory("AssCall"));
            }
            return null;
        }
    },
    // (67), (68)
    AssCall(new NonTerminal("AssCall")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LMIDPAREN:     // correction: 应该有个左方括号的情况
                case DOT:           // correction: 应该有个点号的情况
                case ASSIGN:
                    return singletonList(nonFactory("AssignmentRest"));
                case LPAREN:
                    return singletonList(nonFactory("CallStmRest"));
            }
            return null;
        }
    },
    // ======================赋值语句===============================
    // (69)
    AssignmentRest(new NonTerminal("AssignmentRest")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LMIDPAREN:
                case DOT:
                case ASSIGN:
                    return asList(nonFactory("VariMore"), terFactory(ASSIGN), nonFactory("Exp"));
            }
            return null;
        }
    },
    // ======================条件语句===============================
    // (70)
    ConditionalStm(new NonTerminal("ConditionalStm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == IF) {
                return asList(terFactory(IF), nonFactory("RelExp"), terFactory(THEN), nonFactory("StmList"),
                        terFactory(ELSE), nonFactory("StmList"), terFactory(FI));
            }
            return null;
        }
    },
    // ======================循环语句===============================
    // (71)
    LoopStm(new NonTerminal("LoopStm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == WHILE) {
                return asList(terFactory(WHILE), nonFactory("RelExp"), terFactory(DO),
                        nonFactory("StmList"), terFactory(ENDWH));
            }
            return null;
        }
    },
    // ======================循环语句===============================
    // (72)
    InputStm(new NonTerminal("InputStm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == READ) {
                return asList(terFactory(READ), terFactory(LPAREN),
                        nonFactory("Invar"), terFactory(RPAREN));
            }
            return null;
        }
    },
    // (73)
    Invar(new NonTerminal("Invar")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return singletonList(terFactory(ID));
            }
            return null;
        }
    },
    // ======================输出语句===============================
    // (74)
    OutputStm(new NonTerminal("OutputStm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == WRITE) {
                return asList(terFactory(WRITE), terFactory(LPAREN),
                        nonFactory("Exp"), terFactory(RPAREN));
            }
            return null;
        }
    },
    // ======================返回语句===============================
    // (75)
    ReturnStm(new NonTerminal("ReturnStm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == RETURN) {
                return singletonList(terFactory(RETURN));
            }
            return null;
        }
    },
    // ======================过程调用语句===============================
    // (76)
    CallStmRest(new NonTerminal("CallStmRest")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == LPAREN) {
                return asList(terFactory(LPAREN), nonFactory("ActParamList"), terFactory(RPAREN));
            }
            return null;
        }
    },
    // (77), (78)
    ActParamList(new NonTerminal("ActParamList")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case RPAREN:
                    return singletonList(nonFactory("blank"));
                case LPAREN:
                case INTC:
                case ID:
                    return asList(nonFactory("Exp"), nonFactory("ActParamMore"));
            }
            return null;
        }
    },
    // (79), (80)
    ActParamMore(new NonTerminal("ActParamMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case RPAREN:
                    return singletonList(nonFactory("blank"));
                case COMMA:
                    return asList(terFactory(COMMA), nonFactory("ActParamList"));
            }
            return null;
        }
    },
    // ======================条件表达式===============================
    // (81)
    RelExp(new NonTerminal("RelExp")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LPAREN:
                case INTC:
                case ID:
                    return asList(nonFactory("Exp"), nonFactory("OtherRelE"));
            }
            return null;
        }
    },
    // (82)
    OtherRelE(new NonTerminal("OtherRelE")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == EQ || type == LT) {
                return asList(nonFactory("CmpOp"), nonFactory("Exp"));
            }
            return null;
        }
    },
    // ======================算术表达式===============================
    // (83)
    Exp(new NonTerminal("Exp")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LPAREN:
                case INTC:
                case ID:
                    return asList(nonFactory("Term"), nonFactory("OtherTerm"));
            }
            return null;
        }
    },
    // (84), (85)
    OtherTerm(new NonTerminal("OtherTerm")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LT:
                case EQ:
                case RMIDPAREN:
                case THEN:
                case ELSE:
                case FI:
                case DO:
                case ENDWH:
                case RPAREN:
                case END:
                case SEMI:
                case COMMA:
                    return singletonList(nonFactory("blank"));
                case PLUS:
                case MINUS:
                    return asList(nonFactory("AddOp"), nonFactory("Exp"));
            }
            return null;
        }
    },
    // ======================项===============================
    // (86)
    Term(new NonTerminal("Term")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LPAREN:
                case INTC:
                case ID:
                    return asList(nonFactory("Factor"), nonFactory("OtherFactor"));
            }
            return null;
        }
    },
    // (87), (88)
    OtherFactor(new NonTerminal("OtherFactor")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case PLUS:
                case MINUS:
                case LT:
                case EQ:
                case RMIDPAREN:
                case THEN:
                case ELSE:
                case FI:
                case DO:
                case ENDWH:
                case RPAREN:
                case END:
                case SEMI:
                case COMMA:
                    return singletonList(nonFactory("blank"));
                case TIMES:
                case DIVIDE:
                    return asList(nonFactory("MultOp"), nonFactory("Term"));
            }
            return null;
        }
    },
    // ======================因子===============================
    // (89), (90), (91)
    Factor(new NonTerminal("Factor")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case LPAREN:
                    return asList(terFactory(LPAREN), nonFactory("Exp"), terFactory(RPAREN));
                case INTC:
                    return singletonList(terFactory(INTC));
                case ID:
                    return singletonList(nonFactory("Variable"));
            }
            return null;
        }
    },
    // (92)
    Variable(new NonTerminal("Variable")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return asList(terFactory(ID), nonFactory("VariMore"));
            }
            return null;
        }
    },
    // (93), (94), (95)
    VariMore(new NonTerminal("VariMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case RMIDPAREN:     // correction: 应添加右方括号
                case ASSIGN:
                case TIMES:
                case DIVIDE:
                case PLUS:
                case MINUS:
                case LT:
                case EQ:
                case THEN:
                case ELSE:
                case FI:
                case DO:
                case ENDWH:
                case RPAREN:
                case END:
                case SEMI:
                case COMMA:
                    return singletonList(nonFactory("blank"));
                case LMIDPAREN:
                    return asList(terFactory(LMIDPAREN), nonFactory("Exp"), terFactory(RMIDPAREN));
                case DOT:
                    return asList(terFactory(DOT), nonFactory("FieldVar"));
            }
            return null;
        }
    },
    // (96)
    FieldVar(new NonTerminal("FieldVar")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == ID) {
                return asList(terFactory(ID), nonFactory("FieldVarMore"));
            }
            return null;
        }
    },
    // (97), (98)
    FieldVarMore(new NonTerminal("FieldVarMore")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            switch (type) {
                case ASSIGN:
                case TIMES:
                case DIVIDE:
                case PLUS:
                case MINUS:
                case LT:
                case EQ:
                case THEN:
                case ELSE:
                case FI:
                case DO:
                case ENDWH:
                case RPAREN:
                case END:
                case SEMI:
                case COMMA:
                    return singletonList(nonFactory("blank"));
                case LMIDPAREN:
                    return asList(terFactory(LMIDPAREN), nonFactory("Exp"), terFactory(RMIDPAREN));
            }
            return null;
        }
    },
    // (99), (100)
    CmpOp(new NonTerminal("CmpOp")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == LT) {
                return singletonList(terFactory(LT));
            } else if (type == EQ) {
                return singletonList(terFactory(EQ));
            }
            return null;
        }
    },
    // (101), (102)
    AddOp(new NonTerminal("AddOp")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == PLUS) {
                return singletonList(terFactory(PLUS));
            } else if (type == MINUS) {
                return singletonList(terFactory(MINUS));
            }
            return null;
        }
    },
    // (103), (104)
    MultOp(new NonTerminal("MultOp")) {
        @Override
        public List<Symbol> predict(Token token) {
            TokenType type = token.getType();
            if (type == TIMES) {
                return singletonList(terFactory(TIMES));
            } else if (type == DIVIDE) {
                return singletonList(terFactory(DIVIDE));
            }
            return null;
        }
    };


    NON_TERMINAL(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public NonTerminal nonTerminal;

    /**
     * 用所给的token类型查预测分析表，返回产生式右部
     *
     * @param token 要查的token
     * @return 产生式右部，列表
     */
    public abstract List<Symbol> predict(Token token);
}
