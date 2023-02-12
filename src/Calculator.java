

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Calculator {
    int token;

    void error(String message) {
        System.out.println("Parse Error: " + message);
        System.exit(1);
    }

    void getToken(BufferedReader sc) {
        try {
            token = (char) sc.read();
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
    }

    void match(char c, String message, BufferedReader sc) {
        if(token == c) {
            getToken(sc);
        } else {
            error(message);
        }
    }

    int digit(BufferedReader sc) {
        int result = 0;
        if(Character.isDigit(token)) {
            result = token - '0';
            match((char)token, " ( expected", sc);
        }
        else {
            error("digit expected");
        }
        return result;
    }

    int number(BufferedReader sc) {
        int result = digit(sc);
        while(Character.isDigit(token)) {
            result = 10*result+digit(sc);
        }
        return result;
    }

    int factor(BufferedReader sc) {
        int result = 0;
        if(token == '(') {
            match('(', " ( expected", sc);
            result = expr(sc);
            match(')', " ) expected", sc);
        }
        else {
            result = number(sc);
        }
        return result;
    }


    int power(BufferedReader sc) {
        int result = factor(sc);

        while(token == '^') {
            match('^', " ^ expected", sc);
            int value = 1;
            int index = power(sc);
            for(int i = 0; i < index; i++) {
                value *= result;
            }
            result = value;
        }
        return result;
    }

    int term(BufferedReader sc) {
        int result = power(sc);
        while(token == '*' || token == '/' || token == '%') {
            if(token == '*') {
                match('*', "* expected", sc);
                result *= power(sc);
            } else if(token == '/') {
                match('/', "/ expected", sc);
                result /= power(sc);
            } else if(token == '%') {
                match('%', "% expected", sc);
                result = result%power(sc);
            }
        }
        return result;
    }

    int expr(BufferedReader sc) {
        int result = term(sc);
        while(token == '+' || token == '-') {
            if(token == '+') {
                match('+', "+ expected", sc);
                result += term(sc);
            } else if(token == '-') {
                match('-', "- expected", sc);
                result -= term(sc);
            }
        }
        return result;
    }

    void command(BufferedReader sc) {
        int result = expr(sc);
        if(token=='\n') {
            System.out.println("The result is: " + result);
        } else {
            error("tokens after end of expression");
        }
    }

    public static void main(String[] args) throws IOException {
        Calculator func = new Calculator();
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        String string = sc.readLine();
        System.out.println(string);


        func.getToken(sc);
        func.command(sc);

        sc.close();
    }
}
