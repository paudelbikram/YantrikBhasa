package com.yantrik.bhasa;

import java.util.ArrayList;
import java.util.List;

import static com.yantrik.bhasa.TokenType.*;

public class Parser
{
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    /*
    public List<Stmt> parse()
    {
        List<Stmt> statements = new ArrayList<>();

        while(!isAtEnd())
        {
            statements.add(declaration());
        }
        return statements;
    }


    private Expr expression()
    {
        return assignment();
    }



    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(SUPER)) {
            Token keyword = previous();
            consume(DOT, "Expect '.' after 'super'.");
            Token method = consume(IDENTIFIER,
                    "Expect superclass method name.");
            return new Expr.Super(keyword, method);
        }

        if (match(THIS)) return new Expr.This(previous());

        if (match(IDENTIFIER)) {
            return new Expr.Variable(previous());
        }
        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    */

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }


    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }


    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }


    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }


    private ParseError error(Token token, String message) {
        YantrikBhasa.error(token, message);
        return new ParseError();
    }



    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }
}
