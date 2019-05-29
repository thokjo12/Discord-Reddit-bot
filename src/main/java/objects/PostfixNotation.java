package objects;

/**
 * Created by Thomas on 28.01.2018.
 */
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostfixNotation {
    private static Pattern trigPattern = Pattern.compile("(a?(?:tan|sin|cos))");

    public static double evaluateExpression(String expression, boolean isRadian) throws Exception {
        double totalSum;
        String operators = " +-/*%";
        Queue<String> input = new LinkedBlockingQueue<>();
        ArrayDeque<String> operatorStack = new ArrayDeque<>();
        ArrayDeque<Double> evalStack = new ArrayDeque<>();
        Pattern mathregex = Pattern.compile("(\\d+(?:\\.\\d+)?)|([+\\-*/%()])|" + trigPattern.toString());
        Matcher matcher = mathregex.matcher(expression);
        try {
            scanExpressionOperandsAndNumber(matcher, operators, input, operatorStack);
            totalSum = evaluateStack(input, operators, evalStack, isRadian);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return totalSum;
    }

    public static double evaluateStack(Queue<String> input, String operands, ArrayDeque<Double> evalStack, boolean isRadian) throws Exception {
        while (!input.isEmpty()) {
            String token = input.poll();
            if (!operands.contains(token) && !trigPattern.matcher(token).matches()) {
                evalStack.push(Double.parseDouble(token));
            } else if (trigPattern.matcher(token).matches()) {
                evalStack.push(processTrig(token, evalStack.pop(), isRadian));
            } else {
                if (evalStack.size() < 2) throw new Exception("insufficient amount of values");
                double value1 = evalStack.pop();
                double value2 = evalStack.pop();
                evalStack.push(processOperator(token, value1, value2));
            }
        }
        if (evalStack.size() == 1) {
            return evalStack.pop();
        } else {
            throw new Exception("insufficient amount of values");
        }
    }

    private static void scanExpressionOperandsAndNumber(Matcher matcher, String operands, Queue<String> input, ArrayDeque<String> operatorStack) throws Exception {
        String token;
        boolean trigToken = false;
        while (matcher.find()) {
            if (matcher.group(1) != null)
                token = matcher.group(1);
            else if (matcher.group(2) != null)
                token = matcher.group(2);
            else {
                token = matcher.group(3);
                trigToken = true;
            }

            if (token.length() == 0) {
                continue;
            }
            if (operands.contains(token) || trigToken) {
                while (!operatorStack.isEmpty() && precedence(token) < precedence(operatorStack.peek())) {
                    input.offer(operatorStack.pop());
                }
                operatorStack.push(token);
                trigToken = false;
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    input.offer(operatorStack.pop());
                }
                if (!operatorStack.peek().equals("(")) {
                    throw new Exception("No matching parenthesis found");
                }
                operatorStack.pop();
            } else {
                input.offer(token);
            }
        }
        while (!operatorStack.isEmpty()) {

            if (operatorStack.peek().equals("(")) {
                throw new Exception("No matching parenthesis found");
            }
            input.offer(operatorStack.pop());
        }
    }

    public static double processTrig(String operand, double value, boolean isRadian) {
        if (!isRadian) value = StrictMath.toRadians(value);
        switch (operand) {
            case "sin":
                return StrictMath.sin(value);
            case "cos":
                return StrictMath.cos(value);
            case "tan":
                return StrictMath.tan(value);
            case "asin":
                return StrictMath.asin(value);
            case "acos":
                return StrictMath.acos(value);
            case "atan":
                return StrictMath.atan(value);
            default:
                return 0;
        }

    }

    public static double processOperator(String operand, double value1, Double value2) {
        switch (operand) {
            case "-":
                return value2 - value1;
            case "+":
                return value1 + value2;
            case "/":
                return value2 / value1;
            case "%":
                return value2 % value1;
            case "*":
                return value1 * value2;
            default:
                return 0;
        }
    }

    public static int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "/":
            case "*":
                return 2;
            case "%":
                return 3;
            case "tan":
            case "sin":
            case "cos":
            case "atan":
            case "acos":
            case "asin":
                return 4;
            default:
                return 0;
        }
    }
}

