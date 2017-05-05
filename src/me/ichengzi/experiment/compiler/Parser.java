package me.ichengzi.experiment.compiler;

import java.util.Arrays;
import java.util.List;

/**
 * Coding is pretty charming when you love it!
 *
 *
 *  语法分析器。
 *  对于同一个非终结符的多个产生式，在进行选择的时候还是要向前进行多步预测，是否有更好的办法？
 *
 * @author Chengzi Start
 * @date 2017/5/4
 * @time 19:54
 */
public class Parser {

    private List errorList ;
    private List<Token> tokenList;

    private int lineCount = 1;
    private Token current;


    public Parser(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    private void next(){
        current = tokenList.remove(0);
        while (current.getTokenKind()== Token.TokenKind.EOLN){
            lineCount++;
            current = tokenList.remove(0);
        }
    }


    /**
     * 用于判断多个候选式的时候向前预测
     * @param index
     * @return
     */
    private Token peek(int index){
        int len = tokenList.size();
        int i = 0;
        Token result = null;
        while(index >=0 && i<len){
            result = tokenList.get(i++);
            if (result.getTokenKind() != Token.TokenKind.EOLN){
                index--;
            }
        }
        if (index>=0){
            return null;
        }else{
            return result;
        }
    }

    private Token peek(){
        return peek(0);
    }

    public void parse(){
        program();
    }



    private void program(){
        subProbgram();
    }

    private void subProbgram(){
        next();
        if (current.getTokenKind() == Token.TokenKind.BEGIN){
            declarationTable();
            next();
            if (current.getTokenKind() == Token.TokenKind.SEMI){
                executeTable();
//                System.out.println("执行完executeTable之后:"+current);
                next();
                if (current.getTokenKind() == Token.TokenKind.END){
                    // successful
                }else {
                    error("程序缺少end关键字");
                }
            }else{
                error("此处需要一个分号");
            }
        }else{
            error("程序缺少begin关键字");
        }
    }

    private void declarationTable(){
        declarationStatement();
        declarationTable2();
    }

    private void declarationTable2(){
        Token token = peek();
        Token token1 = peek(1);
        if (token.getTokenKind() == Token.TokenKind.SEMI && token1.getTokenKind() == Token.TokenKind.INTEGER){
            next();
            declarationStatement();
            declarationTable2();
        }else{
            // 此处识别的是终结符$
        }
    }

    private void declarationStatement(){
        Token token = peek();
//        printTokenList();
        if (token.getTokenKind()== Token.TokenKind.INTEGER){
            Token token1 = peek(1);
            if (token1.getTokenKind() == Token.TokenKind.FUNCTION){
                functionDeclaration();
            }else{
                variableDeclaration();
            }
        }else{

//            System.out.println("token:"+token.getSymbol()+",tokenKind:"+token.getTokenKind());
//            error("声明语句的开头应该是integer");
            // $ ,这里默认应该是声明语句表和执行语句表的分解符
        }
    }

    private void variableDeclaration(){
        next();//因为在declarationStatement方法中判定已经是integer了

        variable();
    }

    private void variable(){
        identifier();
    }

    private void identifier(){

        next();

        if (current.getTokenKind() == Token.TokenKind.IDENTIFIER){
            // successful
        }else{
            error("此处应该是标识符");
        }
    }



//    private void identifier(){
//        alphabet();
//        identifier2();
//    }
//
//    private void identifier2(){
//        Token token = peek();
//
//    }
//
//    private void alphabet(){
//
//    }
//
//    private void number(){
//
//    }

    private void functionDeclaration(){
        next();
        next();
        next();
        if (current.getTokenKind() == Token.TokenKind.IDENTIFIER){
            next();
            if (current.getTokenKind() == Token.TokenKind.LPAREN){
                parameter();
                next();
                if (current.getTokenKind() == Token.TokenKind.RPAREN){
                    next();
                    if (current.getTokenKind() == Token.TokenKind.SEMI){
                         functionBody();
                    }else{
                        error("此处需要';'");
                    }
                }else{
                    error("此处需要‘）’");
                }
            }else{
                error("此处需要‘（’");
            }
        }else{
            error("此处需要标识符");
        }
    }

    private void parameter(){
        variable();
    }

    private void functionBody(){
        next();
        if (current.getTokenKind() == Token.TokenKind.BEGIN){
            declarationTable();
            next();

            if (current.getTokenKind() == Token.TokenKind.SEMI){
                executeTable();
                next();
                if (current.getTokenKind() == Token.TokenKind.END){
                    //successful
                }else{
                    error("方法体需要end结尾");
                }
            }else{
                error("此处需要';'");
            }
        }else{
            error("方法体需要begin开头");
        }
    }

    private void executeTable(){
        executeStatement();
        executeTable2();
    }

    private void executeTable2(){
        Token semiToken = peek();
        if (semiToken.getTokenKind() == Token.TokenKind.SEMI){
            next();
            Token token = peek();
            Token token1 = peek(1);
            if (token.getTokenKind() == Token.TokenKind.READ
                    || token.getTokenKind() == Token.TokenKind.WRITE
                    || token.getTokenKind() == Token.TokenKind.IF
                    || token.getTokenKind() == Token.TokenKind.IDENTIFIER && token1.getTokenKind() == Token.TokenKind.ASSIGNMENT){
                executeStatement();
                executeTable2();
            }else{
                error("未识别的执行语句");
            }
        }else{
            // $
        }

    }

    private void executeStatement(){
        Token token = peek();
        if (token.getTokenKind() == Token.TokenKind.READ){
            readStatement();
        }else if(token.getTokenKind() == Token.TokenKind.WRITE){
            writeStatement();
        }else if(token.getTokenKind() == Token.TokenKind.IF){
            conditionStatement();
        }else{//在方法executeTable2中已经判断过了
            assignmentStatement();
        }
    }

    private void readStatement(){
        next();
        if (current.getTokenKind() == Token.TokenKind.READ){
            next();
            if (current.getTokenKind() == Token.TokenKind.LPAREN){
                variable();
                next();
                if (current.getTokenKind() == Token.TokenKind.RPAREN){
                    // successful
                }else{
                    error("此处需要右括号");
                }
            }else{
                error("此处需要左括号");
            }
        }else{
            error("read语句需要有read开头");
        }
    }

    private void writeStatement(){
        next();
        if (current.getTokenKind() == Token.TokenKind.WRITE){
            next();
            if (current.getTokenKind() == Token.TokenKind.LPAREN){
                variable();
                next();
                if (current.getTokenKind() == Token.TokenKind.RPAREN){
                    // successful
                }else{
                    error("此处需要右括号");
                }
            }else{
                error("此处需要左括号");
            }
        }else{
            error("write语句需要有write开头");
        }
    }

    private void assignmentStatement(){
        variable();
        next();
        if (current.getTokenKind() == Token.TokenKind.ASSIGNMENT){
            arithmeticExpression();
        }else{
            error("此处需要赋值符号");
        }
    }

    private void arithmeticExpression(){
        item();
        arithmeticExpression2();
    }

    private void arithmeticExpression2(){
        Token token = peek();
        if (token.getTokenKind() == Token.TokenKind.SUB){
            next();
            item();
            arithmeticExpression2();
        }else{
            // $
        }
    }

    private void item(){
        factor();
        item2();
    }

    private void item2(){
        Token token = peek();
        if (token.getTokenKind() == Token.TokenKind.MUL){
            next();
            factor();
            item2();
        }else{
            // $
        }
    }

    private void factor(){
        Token token = peek();
        Token token1 = peek(1);
        if (token.getTokenKind() == Token.TokenKind.IDENTIFIER && token1.getTokenKind() == Token.TokenKind.LPAREN){
            functionCall();
        }else if(token.getTokenKind() == Token.TokenKind.IDENTIFIER || token.getTokenKind() == Token.TokenKind.CONSTANT){
            next();
            // successful
        }else{
            error("不合法的因子表示形式");
        }
    }

    private void functionCall(){
        next();
        if (current.getTokenKind() == Token.TokenKind.IDENTIFIER){
            next();
            if (current.getTokenKind() == Token.TokenKind.LPAREN){
                arithmeticExpression();
                next();
                if (current.getTokenKind() == Token.TokenKind.RPAREN){
                    // successful
                }else{
                    error("此处需要右括号");
                }
            }else{
                error("此处需要左括号");
            }
        }else{
            error("方法调用需要方法名标识符");
        }
    }
//
//    private void constant(){
//
//    }
//
//    private void unsignedInteger(){
//
//    }
//
//    private void unsignedInteger2(){
//
//    }

    private void conditionStatement(){
        next();
        if (current.getTokenKind() == Token.TokenKind.IF){
            conditionExpression();
            next();
            if (current.getTokenKind() == Token.TokenKind.THEN){
                executeStatement();
                next();
                if (current.getTokenKind() == Token.TokenKind.ELSE){
                    executeStatement();
                }else{
                    error("此处需要else");
                }
            }else{
                error("此处需要then");
            }
        }else{
            error("条件语句需要if开头");
        }
    }

    private void conditionExpression(){
        arithmeticExpression();
        relationOperator();
        arithmeticExpression();

    }

    private void relationOperator(){
        next();
        if (current.getTokenKind() == Token.TokenKind.LESS
                || current.getTokenKind() == Token.TokenKind.LESS_OR_EQUAL
                || current.getTokenKind() == Token.TokenKind.GREATER
                || current.getTokenKind() == Token.TokenKind.GREATER_OR_EQUAL
                || current.getTokenKind() == Token.TokenKind.EQUAL
                || current.getTokenKind() == Token.TokenKind.NOT_EQUAL){
            // successful
        }else{
            error("非法的比较运算符");
        }
    }


    private void error(String err_msg){
        System.out.println("line:"+lineCount+"  "+err_msg+" currentToken:"+current.getSymbol());
    }


    private void printTokenList(){
        System.out.println(Arrays.toString(tokenList.toArray()));
    }

}
