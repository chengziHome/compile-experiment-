package me.ichengzi.experiment.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Coding is pretty charming when you love it!
 *
 * 单词解析器
 *
 * @author Chengzi Start
 * @date 2017/4/26
 * @time 20:16
 */
public class Tokenizer {


    private CharReader reader;
    private int line = 1;

    private List<Token> tokenList = new ArrayList<>();
    private List<Error> errorList = new ArrayList<>();

    public Tokenizer(CharReader reader) {
        this.reader = reader;
    }


    public Token readToken(){

        while(true){
            reader.sp = 0;
            char ch = reader.ch;
            switch (ch){
                /*
                    要识别的类型依次是:
                        0,处理空白字符
                        1,标识符(保留字或者变量)
                        2,常数(这里只考虑整数)
                        3,各类符号(一个或者两个组成)
                 */
                case ' ':case '\t':
                    processWhiteSpace();
                    continue;

                case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':case 'G':
                case 'H':case 'I':case 'J':case 'K':case 'L':case 'M':case 'N':
                case 'O':case 'P':case 'Q':case 'R':case 'S':case 'T':
                case 'U':case 'V':case 'W':case 'X':case 'Y':case 'Z':
                case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':
                case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':case 'n':
                case 'o':case 'p':case 'q':case 'r':case 's':case 't':
                case 'u':case 'v':case 'w':case 'x':case 'y':case 'z':
                    scanIdent();
                    break;

                case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':case '0':
                    scanInteger();
                    String integer = reader.chars();
                    return new Token(integer, Token.TokenKind.CONSTANT);
                case '=':
                    reader.scanChar();
                    return new Token("=", Token.TokenKind.EQUAL);
                case '<':
                    reader.scanChar();
                    char letter = reader.ch;
                    if(letter=='>'){
                        reader.scanChar();
                        return new Token("<>", Token.TokenKind.NOT_EQUAL);
                    }else if(letter=='='){
                        reader.scanChar();
                        return new Token("<=", Token.TokenKind.LESS_OR_EQUAL);
                    }else{
                        return new Token("<", Token.TokenKind.LESS);
                    }
                case '>':
                    reader.scanChar();
                    char letter1 = reader.ch;
                    if(letter1=='='){
                        reader.scanChar();
                        return new Token(">=", Token.TokenKind.GREATER_OR_EQUAL);
                    }else{
                        return new Token(">", Token.TokenKind.GREATER);
                    }
                case '-':
                    reader.scanChar();
                    return new Token("-", Token.TokenKind.SUB);
                case '*':
                    reader.scanChar();
                    return new Token("*", Token.TokenKind.MUL);
                case ':':
                    reader.scanChar();
                    char letter2 = reader.ch;
                    reader.scanChar();
                    if(letter2=='='){
                        return new Token(":=", Token.TokenKind.ASSIGNMENT);
                    }else{
                        //错误一
                        error("符号':'不匹配");
                    }
                case '(':
                    reader.scanChar();
                    return new Token("(", Token.TokenKind.LPAREN);
                case ')':
                    reader.scanChar();
                    return new Token(")", Token.TokenKind.RPAREN);
                case ';':
                    reader.scanChar();
                    return new Token(";", Token.TokenKind.SEMI);

                /*
                    回车换行在不同的系统里面会有些诧异
                    回车CR(13),换行LF(10);
                    Unix系统里面用LF
                    MacOS用CR
                    Windows用CR+LF
                 */

                case 13:
                    reader.scanChar();
                    char letter3 = reader.ch;
                    if (letter3 == 10){
                        reader.scanChar();
                    }
                    line++;
                    return new Token("EOLN", Token.TokenKind.EOLN);
                case 10:
                    reader.scanChar();
                    line++;
                    return new Token("EOLN", Token.TokenKind.EOLN);
                default:
                    //错误二
                    error("非法字符:"+ch);
            }
            String name = reader.chars();
            switch (name){
                case "begin":
                    return new Token("begin",Token.TokenKind.BEGIN);
                case "end":
                    return new Token("end", Token.TokenKind.END);
                case "integer":
                    return new Token("integer", Token.TokenKind.INTEGER);
                case "if":
                    return new Token("if", Token.TokenKind.IF);
                case "then":
                    return new Token("then", Token.TokenKind.THEN);
                case "else":
                    return new Token("else", Token.TokenKind.ELSE);
                case "function":
                    return new Token("function", Token.TokenKind.FUNCTION);
                case "read":
                    return new Token("read", Token.TokenKind.READ);
                case "write":
                    return new Token("write", Token.TokenKind.WRITE);
                default:
                    return new Token(name, Token.TokenKind.IDENTIFIER);
            }


        }

    }

    private void processWhiteSpace(){

        do {
            reader.scanChar();
        }while(reader.ch==' ' || reader.ch=='\t');
    }

    /**
     * 识别一个整数
     */
    private void scanInteger(){

        loop:
        do {
            reader.putChar();
            char ch = reader.ch;
            switch (ch){
                case '1':case '2':case '3':case '4':case '5':
                case '6':case '7':case '8':case '9':case '0':
                    break;
                default:
                    break loop;
            }
        }while (true);

    }


    /**
     * 识别一个标识符。
     */
    private void scanIdent(){
        loop:
        do {
            reader.putChar();
            char ch = reader.ch;
            switch (ch){
                case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':case 'G':
                case 'H':case 'I':case 'J':case 'K':case 'L':case 'M':case 'N':
                case 'O':case 'P':case 'Q':case 'R':case 'S':case 'T':
                case 'U':case 'V':case 'W':case 'X':case 'Y':case 'Z':
                case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':
                case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':case 'n':
                case 'o':case 'p':case 'q':case 'r':case 's':case 't':
                case 'u':case 'v':case 'w':case 'x':case 'y':case 'z':
                case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
                    break;
                default:
                    break loop;
            }

        }while(true);

    }

    private void error(String err_msg){
        errorList.add(new Error(line++,err_msg));
    }

    class Error{

        int line;
        String msg;

        public Error(int line, String msg) {
            this.line = line;
            this.msg = msg;
        }

        public int getLine() {
            return line;
        }

        public String getMsg() {
            return msg;
        }
    }


    /**
     * 识别reader.buf中所有的字符，辨别出token并放入到token序列中，
     * 直到识别完毕，或者有错误产生。
     * @return
     */
    public boolean readAllToken(){

        while(reader.bp<reader.buflen  && errorList.isEmpty()){
            Token tmp = null;
            if ((tmp=readToken())!=null){
                tokenList.add(tmp);
            }
        }
        if (errorList.isEmpty()){//正确识别完所有单词
            tokenList.add(new Token("EOF", Token.TokenKind.EOF));
            return true;
        }else{
            return false;
        }
    }

    List<Token> getTokenList(){
        return this.tokenList;
    }

    List<Error> getErrorList(){
        return this.errorList;
    }



}
