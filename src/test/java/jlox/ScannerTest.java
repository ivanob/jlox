package jlox;

import junit.framework.TestCase;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static jlox.TokenType.*;

public class ScannerTest extends TestCase {

    private String readFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return (new String(bytes, Charset.defaultCharset()));
    }

    public void testSimpleTokens() {
        { //testSingleCharTokens
            try {
                Scanner scanner = new Scanner(readFile("./src/test/java/jlox/resources/singleCharTokens.lox"));
                List<Token> tokens = scanner.scanTokens();
                List<TokenType> expectedTokens = Arrays.asList(LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
                        COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, EOF);
                int i = 0;
                for (TokenType t : expectedTokens) {
                    assertTrue("Expected " + t.name(), tokens.get(i).type == t);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        { //Comparation tokens
            try {
                Scanner scanner = new Scanner(readFile("./src/test/java/jlox/resources/comparationTokens.lox"));
                List<Token> tokens = scanner.scanTokens();
                List<TokenType> expectedTokens = Arrays.asList(BANG, BANG_EQUAL,
                        EQUAL, EQUAL_EQUAL,
                        GREATER, GREATER_EQUAL,
                        LESS, LESS_EQUAL, EOF);
                int i = 0;
                for (TokenType t : expectedTokens) {
                    assertTrue("Expected " + t.name(), tokens.get(i).type == t);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            { //Literal tokens
                try {
                    Scanner scanner = new Scanner(readFile("./src/test/java/jlox/resources/literalsAndKeywords.lox"));
                    List<Token> tokens = scanner.scanTokens();
                    List<TokenType> expectedTokens = Arrays.asList(IDENTIFIER, IDENTIFIER, IDENTIFIER, IDENTIFIER, IDENTIFIER,
                            IDENTIFIER, IDENTIFIER, STRING, STRING, STRING, NUMBER, NUMBER, EOF);
                    int i = 0;
                    for (TokenType t : expectedTokens) {
                        assertTrue("Expected " + t.name(), tokens.get(i).type == t);
                        i++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        {
            { //Literal tokens
                try {
                    Scanner scanner = new Scanner(readFile("./src/test/java/jlox/resources/keywords.lox"));
                    List<Token> tokens = scanner.scanTokens();
                    List<TokenType> expectedTokens = Arrays.asList(AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
                            PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE, EOF);
                    int i = 0;
                    for (TokenType t : expectedTokens) {
                        assertTrue("Expected " + t.name(), tokens.get(i).type == t);
                        i++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
