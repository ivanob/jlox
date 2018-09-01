package jlox;

public class Token {
    protected TokenType type;
    protected String lexeme;
    private int line;
    private Object literal;

    public Token(TokenType type, String lexeme, int line, Object literal){
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.literal = literal;
    }

    public String toString(){
        return type + " " + lexeme + " " + literal;
    }
}
