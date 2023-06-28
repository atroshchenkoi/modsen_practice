package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Calculator {

    static final Double EXRATE;

    static {
        try {
            Scanner sc = new Scanner(new FileReader("C:\\Users\\atros\\IdeaProjects\\modsen_practice\\src\\rate.txt"));
            EXRATE = sc.nextDouble();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Double round(Double num) {
        return Math.round(num * 100.0) / 100.0;
    }
    public static String toRubles(Double dollars) {
        return Math.round(dollars * EXRATE * 10000.0) / 10000.0 + "р";
    }

    public static String toDollars(Double rubles) {
        return "\\$" + Math.round(rubles / EXRATE * 10000.0) / 10000.0;
    }

    public static String operation(Double rublesOne, Double rublesTwo, String operator)  {
        if(!(operator.equals("+") || operator.equals("-"))) throw new RuntimeException("Error operator... " + rublesOne + " <" + operator + ">? " + rublesTwo);
        return operator.equals("+") ? String.valueOf(rublesOne + rublesTwo) : String.valueOf(rublesOne - rublesTwo);
    }

}
