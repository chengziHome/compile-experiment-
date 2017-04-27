package me.ichengzi.experiment.compiler;

/**
 * Coding is pretty charming when you love it!
 *
 * 代表一个合法的单词。
 *
 * @author Chengzi Start
 * @date 2017/4/26
 * @time 19:47
 */
public class Token {

    private String symbol;
    private TokenKind tokenKind;


    public Token(String name,TokenKind kind) {
        symbol = name;
        tokenKind = kind;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public TokenKind getTokenKind() {
        return tokenKind;
    }

    public void setTokenKind(TokenKind tokenKind) {
        this.tokenKind = tokenKind;
    }


    /**
     * 单词所有的种类
     */
    protected enum TokenKind{

        BEGIN("begin",1),
        END("end",2),
        INTEGER("integer",3),
        IF("if",4),
        THEN("then",5),
        ELSE("else",6),
        FUNCTION("function",7),
        READ("read",8),
        WRITE("write",9),
        IDENTIFIER("identifier",10),
        CONSTANT("constant",11),
        EQUAL("==",12),
        NOT_EQUAL("<>",13),
        LESS_OR_EQUAL("<=",14),
        LESS("<",15),
        GREATER_OR_EQUAL(">=",16),
        GREATER(">",17),
        SUB("-",18),
        MUL("*",19),
        ASSIGNMENT(":=",20),
        LPAREN("(",21),
        RPAREN(")",22),
        SEMI(";",23),
        EOLN("EOLN",24),
        EOF("EOF",25);


        public final String name;
        public final int no;

        TokenKind(String name, int no) {
            this.name = name;
            this.no = no;
        }

    }


}
