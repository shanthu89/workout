import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class ExcelMathCalc {
    public enum CellStatus{
        DONE,
        INPROGRESS,
        READY,
        CIRCULAR_DEP_ERROR,
        EVAL_ERROR // to handle/print any other error when evaluating (IllegalArgumentException etc)
    }

    // Custom exception to detect circular detection
    static class CircularDependencyException extends RuntimeException {
    }

    static class Cell{
        String inputExpression;
        String cellName;
        double result;
        CellStatus status;
        Cell(String name,String input){
            this.inputExpression = input;
            this.cellName = name;
            this.status = CellStatus.READY;
        }
    }

    static HashMap<String,Cell> map = new HashMap<>();
    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner s = new Scanner(System.in);
        int ncol = s.nextInt();
        int nrow = s.nextInt();
        s.nextLine();
        double dimension = ncol*nrow;
        for (char r = 'A'; r < 'A'+nrow; r++) {
            for (int c = 1; c <= ncol; c++) {
                String key = String.format("%c%d", r, c);
                String expr = s.nextLine();
                map.put(key, new Cell(key, expr));
            }
        }
        // Disable this if we need to continue evaluating cells that does not have circular dependency
        // and print them
        // Eg: if cell A2 has circular dependency with A4
        // 20.00000
        // Error: Circular dependency!
        // 5.00000
        // Error: Circular dependency!
        // 3.87000
        // ....
        boolean stopOnFirstError = true;

        for(Map.Entry<String, Cell> c: map.entrySet()) {
            Cell cell = c.getValue();
            try {
                evaluateCell(cell.cellName, cell.inputExpression);
            } catch (CircularDependencyException e) {
                if (stopOnFirstError) {
                    System.out.print("Error: Circular dependency!");
                    return;
                }
            }
        }

        for (char r = 'A'; r < 'A'+nrow; r++) {
            for (int c = 1; c <= ncol; c++) {
                String key = String.format("%c%d", r, c);
                Cell curr = map.get(key);

                if (curr.status == CellStatus.DONE) {
                    System.out.printf("%.5f\n",curr.result);
                } else if (curr.status == CellStatus.CIRCULAR_DEP_ERROR) {
                    System.out.println("Error: Circular dependency!");
                }
            }
        }
    }

    public static void evaluateCell(String cellName,String input) throws Exception{
        String[] operands = input.split(" ");
        Stack<Double> s = new Stack<>();
        Cell current = map.get(cellName);

        // did we already evaluate this cell?
        if (current.status == CellStatus.DONE) {
            return; //current.result;
        }

        // Did we encounter error with this cell already?
        if (current.status == CellStatus.CIRCULAR_DEP_ERROR) {
            throw new CircularDependencyException();
        }

        if (current.status == CellStatus.INPROGRESS) {
            current.status = CellStatus.CIRCULAR_DEP_ERROR;
            throw new CircularDependencyException();
        }

        // mark in-progress
        current.status = CellStatus.INPROGRESS;
        for(String i:operands){
            if(isOperator(i)){
                if(s.size()<2)
                    throw new IllegalArgumentException();
                //String second = s.pop();
                double secArg = s.pop();
                //String first = s.pop();
                double firstArg = s.pop();
                char op = i.charAt(0);
                if(op == '+')
                    s.push(firstArg+secArg);
                else if(op == '-')
                    s.push(firstArg-secArg);
                else if(op == '*')
                    s.push(firstArg*secArg);
                else if(op == '/')
                    s.push(firstArg/secArg);
                else
                    throw new IllegalArgumentException("Unknown operator");
            }
            else if(isCell(i)){//if cell data is expression evaluate recursively.
                double val;
                Cell temp = map.get(i);
                try {
                    evaluateCell(temp.cellName,temp.inputExpression);
                    s.push(temp.result);
                }
                catch (CircularDependencyException e) {
                    current.status = CellStatus.CIRCULAR_DEP_ERROR;
                    // propagate exception
                    throw e;
                }
            }
            else {
                s.push(Double.parseDouble(i));
            }
        }
        // make sure the expression was valid. Stack should have only one element i.e. result if evaluation was successful
        if(s.size()!=1)
            throw new IllegalArgumentException();
        current.result = s.peek();
        current.status = CellStatus.DONE;
    }

    private static boolean isOperator(String operator){
        if(operator.length()!=1)
            return false;
        char op = operator.charAt(0);
        return op == '+' || op == '-' || op == '*' || op == '/';
    }

    private static boolean isCell(String val){
        return Pattern.matches("[A-Z][0-9]+",val);
    }
}