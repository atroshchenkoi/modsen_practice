package org.example;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static Pattern PATTERN_TO_DOLLARS = Pattern.compile("toDollars\\((([0-9]*\\.?[0-9]+)\\р)\\)");
    final static Pattern PATTERN_TO_RUBLES = Pattern.compile("toRubles\\((\\$([0-9]*\\.?[0-9]+))\\)");
    final static Pattern PATTERN_OPERATION_WITH_DOLLARS = Pattern.compile("\\$([0-9]*\\.?[0-9]+)\\s([+-])\\s\\$([0-9]*\\.?[0-9]+)");
    final static Pattern PATTERN_OPERATION_WITH_RUBLES = Pattern.compile("([0-9]*\\.?[0-9]+)р\\s([+-])\\s([0-9]*\\.?[0-9]+)р");
    final static Pattern PATTERN_RESULTING_STRING = Pattern.compile("^\\$?([0-9]*\\.?[0-9]+)р?$");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder(sc.nextLine());
        Matcher currentlyMatcher;
        while(!stringBuilder.toString().matches(PATTERN_RESULTING_STRING.pattern())){
            if ((currentlyMatcher = PATTERN_TO_DOLLARS.matcher(stringBuilder)).find()) {
                StringBuilder sb = new StringBuilder();
                currentlyMatcher.appendReplacement(sb, Calculator.toDollars(Double.parseDouble(currentlyMatcher.group(2))));
                currentlyMatcher.appendTail(sb);
                stringBuilder = new StringBuilder(sb.toString());
            }
            if ((currentlyMatcher = PATTERN_TO_RUBLES.matcher(stringBuilder)).find()) {
                StringBuilder sb = new StringBuilder();
                currentlyMatcher.appendReplacement(sb, Calculator.toRubles(Double.parseDouble(currentlyMatcher.group(2))));
                currentlyMatcher.appendTail(sb);
                stringBuilder = new StringBuilder(sb.toString());
            }
            if ((currentlyMatcher = PATTERN_OPERATION_WITH_DOLLARS.matcher(stringBuilder)).find()) {
                StringBuilder sb = new StringBuilder();
                currentlyMatcher.appendReplacement(sb,"$" + Calculator.operation(
                        Double.parseDouble(currentlyMatcher.group(1)),
                        Double.parseDouble(currentlyMatcher.group(3)),
                        currentlyMatcher.group(2)));
                currentlyMatcher.appendTail(sb);
                stringBuilder = new StringBuilder(sb.toString());
            }
            if ((currentlyMatcher = PATTERN_OPERATION_WITH_RUBLES.matcher(stringBuilder)).find()) {
                StringBuilder sb = new StringBuilder();
                currentlyMatcher.appendReplacement(sb, Calculator.operation(
                        Double.parseDouble(currentlyMatcher.group(1)),
                        Double.parseDouble(currentlyMatcher.group(3)),
                        currentlyMatcher.group(2)) + 'р');
                currentlyMatcher.appendTail(sb);
                stringBuilder = new StringBuilder(sb.toString());
            }
        }
        char ch = stringBuilder.charAt(0) == '$' ? '$' : 'р';
        double res = Calculator.round(Double.parseDouble(stringBuilder.toString().replaceAll("\\$?\\р?", "")));
        System.out.println(ch == '$' ? String.valueOf(ch) + res : res + String.valueOf(ch));
    }
}