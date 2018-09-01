package jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jlox.TokenType.*;

public class Scanner {
    private String source;
    private List<Token> tokens;
    private int start = 0, current = 0, line = 1;
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    public Scanner(String source){
        this.source = source;
        tokens = new ArrayList<Token>();
    }

    public List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", line, null));
        return tokens;
    }

    private void scanToken(){
        char c = advance();
        switch(c){
            //Single character token
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            //Single and double character token
            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
            case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
            //Comments
            case '/':
                if(match('/')) {
                    while (peek(1) != '\n' && !isAtEnd()) advance();
                }
                else{
                    addToken(SLASH);
                }
                break;
            //White characters
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"': readString(); break;
            default:
                if(isDigit(c)) {
                    readNumber();
                }else if(isAlpha(c)){
                    readIdentifier();
                }
                else {
                    Lox.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    private void readIdentifier(){
        while(isAlphaNumeric(peek(1))) advance();
        //See if the identifier is a reserved word
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if(type != null) type = IDENTIFIER;
        addToken(type);
    }

    private boolean isAlphaNumeric(char c){
        return (isAlpha(c) || isDigit(c));
    }

    private boolean isAlpha(char c){
        return ((c >= 'a' && c<='z') ||
            (c >= 'A' && c<='Z') ||
            (c == '_'));
    }

    private void readNumber(){
        while(isDigit(peek(1))) advance();
        if(peek(1)=='.' && isDigit(peek(2))){
            advance(); //consume the '.'
            while(isDigit(peek(1))) advance();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean isDigit(char c){
        if(c >= '0' && c <= '9') return true;
        return false;
    }

    private void readString(){
        while(peek(1) != '"' && !isAtEnd()){
            if(peek(1)=='\n') line++;
            advance();
        }
        if(isAtEnd()){
            Lox.error(line, "Unterminated string.");
            return;
        }
        advance(); //pass the closing '"'
        String value = source.substring(start+1, current-1); //trim the '"' characters
        addToken(STRING, value);
    }

    /**
     * Returns the num-th character but without counting it (without moving the current index)
     * @return
     */
    private char peek(int num){
        if(current+num >= source.length()) return '\0';
        return source.charAt(current+num);
    }

    /**
     * Checks if the next character to be read is an specific character received as a parameter
     * @param expectedChar
     * @return
     */
    private boolean match(char expectedChar){
        if(isAtEnd()) return false;
        else{
            current++;
            return source.charAt(current-1) == expectedChar;
        }
    }

    /**
     * The advance() method consumes the next character in the source file and returns it
     * @return
     */
    private char advance(){
        char c = source.charAt(current);
        current += 1;
        return c;
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    /**
     * Grabs the text of the current lexeme and creates a new token for it
     * @param type
     * @param literal
     */
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, line, literal));
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }
}
