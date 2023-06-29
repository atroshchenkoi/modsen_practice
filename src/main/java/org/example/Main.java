package org.example;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    @FunctionalInterface
    private interface Functional {
        public String calculate(Matcher currentMatcher);
    }
    final static Pattern PATTERN_TO_DOLLARS = Pattern.compile("toDollars\\(((\\-?[0-9]*\\.?[0-9]+)\\р)\\)");
    final static Pattern PATTERN_TO_RUBLES = Pattern.compile("toRubles\\((\\$(\\-?[0-9]*\\.?[0-9]+))\\)");
    final static Pattern PATTERN_OPERATION_WITH_DOLLARS = Pattern.compile("\\$(\\-?[0-9]*\\.?[0-9]+)\\s([+-])\\s\\$(\\-?[0-9]*\\.?[0-9]+)");
    final static Pattern PATTERN_OPERATION_WITH_RUBLES = Pattern.compile("(\\-?[0-9]*\\.?[0-9]+)р\\s([+-])\\s(\\-?[0-9]*\\.?[0-9]+)р");
    final static Pattern PATTERN_RESULTING_STRING = Pattern.compile("^\\$?(\\-?[0-9]*\\.?[0-9]+)р?$");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder(sc.nextLine());
        Matcher currentMatcher;
        boolean flag = false;
        while(!stringBuilder.toString().matches(PATTERN_RESULTING_STRING.pattern())){
            if ((currentMatcher = PATTERN_TO_DOLLARS.matcher(stringBuilder)).find()) {
                stringBuilder = new StringBuilder(expressionHelping(currentMatcher, currentMatcherExpression ->
                        Calculator.toDollars(Double.parseDouble(currentMatcherExpression.group(2)))));
                flag = true;
            }
            if ((currentMatcher = PATTERN_TO_RUBLES.matcher(stringBuilder)).find()) {
                stringBuilder = new StringBuilder(expressionHelping(currentMatcher, currentMatcherExpression ->
                        Calculator.toRubles(Double.parseDouble(currentMatcherExpression.group(2)))));
                flag = true;
            }
            if ((currentMatcher = PATTERN_OPERATION_WITH_DOLLARS.matcher(stringBuilder)).find()) {
                stringBuilder = new StringBuilder(expressionHelping(currentMatcher, currentMatcherExpression ->
                        "\\$" + Calculator.operation(
                                Double.parseDouble(currentMatcherExpression.group(1)),
                                Double.parseDouble(currentMatcherExpression.group(3)),
                                currentMatcherExpression.group(2))));
                flag = true;
            }
            if ((currentMatcher = PATTERN_OPERATION_WITH_RUBLES.matcher(stringBuilder)).find()) {
                stringBuilder = new StringBuilder(expressionHelping(currentMatcher, currentMatcherExpression ->
                        Calculator.operation(
                                Double.parseDouble(currentMatcherExpression.group(1)),
                                Double.parseDouble(currentMatcherExpression.group(3)),
                                currentMatcherExpression.group(2)) + 'р'));
                flag = true;
            }
            if(!flag) {
                throw new RuntimeException(stringBuilder + "\n <- the reduction cannot be performed...check the expression.");
            }
            flag = false;

        }
        char ch = stringBuilder.charAt(0) == '$' ? '$' : 'р';
        double res = Calculator.round(Double.parseDouble(stringBuilder.toString().replaceAll("\\$?\\р?", "")));
        System.out.println(ch == '$' ? String.valueOf(ch) + res : res + String.valueOf(ch));
    }

    public static StringBuilder expressionHelping(Matcher currentMatcher, Functional functional) {
        StringBuilder sb = new StringBuilder(currentMatcher.replaceFirst(functional.calculate(currentMatcher)));
        return sb;
    }
}