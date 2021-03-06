import java.io.*;
import java.lang.*;
import java.util.*;

import com.sun.beans.editors.ByteEditor;
import org.apache.commons.io.*;

public class ByteParser{
    static String line = null;
    // variable to track the current line number.
    static int lineNumber = 0;
    static File fileName = new File("input.txt");
    static String[] parsedInstruction = new String[3];

    // main function that runs program from the start
    public static void main(String []args){
        try {
            /**
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
             */
            Runtime Runtime = new Runtime();

            while((line = FileUtils.readLines(fileName, "UTF-8").get(lineNumber)) != null) {
                // parse the instruction one line at a time
                lineNumber++;
                parsedInstruction = Runtime.parseSymbols(line);
                // decide action
                Runtime.performAction(parsedInstruction);
                }
            // final state of the symbol table
            // Runtime.printGlobalSymbolTable();
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}

class Runtime {

    private String statement = new String("");
    private String opCode = new String("");
    private String operands = new String("");
    private String operand1 = new String("");
    private String operand2 = new String("");
    private String[] instruction = new String[2];
    boolean result = false;
    // hashmap serving as a global symbol table
    static HashMap<String, String> globalSymbolTable = new HashMap<>();
    // intermediate value stack
    static private Deque<Integer> intermediateStack = new ArrayDeque<>();
    // preceedence stack
    static private Deque<Integer> preceedenceStack = new ArrayDeque<>();
    // call stack for functions
    static private Deque<String> callStack = new ArrayDeque<>();
    // return value stack
    static private Deque<Integer> returnValueStack = new ArrayDeque<>();


    // method that parses the program and produces the output
    public String[] parseSymbols(String parse) {
        // remove the leading and trailing spaces in instruction
        statement = parse.trim();
        statement = statement.substring(0, parse.length());

        // converting the array of strings to string object
        StringBuilder builder = new StringBuilder();
        for (String s : statement.split(" ")) {
            builder.append(s);
            break;
        }

        // opcode now has the opcode in the statement
        opCode = builder.toString();

        opCode.toString();
        operands = statement.substring((statement.indexOf(opCode)), statement.length());

        instruction[0] = opCode;
        instruction[1] = operands;

        return instruction;

    }

    public void performAction(String[] instructionArray) {
        instructionArray[0] = opCode;
        instructionArray[1] = operands;

        // parsing for the first operand
        int startIndex = operands.indexOf(" ");
        int endIndex = operands.indexOf(", ");


        if (startIndex != -1 && endIndex != -1) {
            operand1 = statement.substring(startIndex + 1, endIndex);
            // insert operand values into hash map
        } else if (startIndex != -1 && endIndex == -1) {
            endIndex = operands.length();
            operand1 = statement.substring(startIndex + 1, endIndex);
        }

        // setting first operand to null for instructions with no operands
        else {
            operand1 = null;
        }

        // parsing for the second operand
        startIndex = operands.indexOf(", ");
        endIndex = operands.length();
        if (startIndex != -1 && endIndex != -1) {
            operand2 = statement.substring(startIndex + 2, endIndex);

        } else if (startIndex == -1) {
            operand2 = null;
        }

        switch (opCode) {

            case "PSTART":
                this.pStart();
                break;
            case "PEND":
                this.pEnd();
                break;
            case "ADD":
                this.add();
                break;
            case "SUB":
                this.sub();
                break;
            case "MUL":
                this.mul();
                break;
            case "DIV":
                this.div();
                break;
            case "REM":
                this.rem();
                break;
            case "LOAD":
                break;
            case "EQL":
                this.eql();
                break;
            /** still thinking on these methods
             case "FUN":
             this.fun();
             break;
             case "ENDFUN":
             this.endfun();
             break;

             case "CALL":
             break;
             */
            case "IFT":
                this.ift();
                break;
            case "LOOP":
                this.loop();
                break;
            case "PRINT":
                this.print();
                break;
            case "READ":
                break;

        }
        operand1 = "";
        operand2 = "";
    }

    // method to print the parsed string just for debug purposes
    public void printParseData() {
        System.out.println("Current Op Code is:" + opCode);
        System.out.println("Current operand1 is:" + operand1);
        System.out.println("Current operand2 is:" + operand2);
    }

    public void printGlobalSymbolTable() {
        // get the iterator for hash map
        // iterate through the hash map
        // Get a set of the entries
        Set set = globalSymbolTable.entrySet();
        // Get an iterator to the hash map
        Iterator i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
    }

    public void pStart() {
        callStack.addFirst("main");
    }

    public void pEnd() {
        if (callStack.peekFirst() == "main") {
            callStack.removeFirst();
            System.out.println("Program finished with exit code 0");
        } else {
            System.out.println("Program finished with exit code 1");
        }
    }

    public void add() {
        // first get the appropriate object to update the appropriate symbol
        String currentScope = callStack.peekFirst();
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");
        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
            currentOperand2 = operand2;
        } else {
            currentOperand1 = currentScope + operand1;
            currentOperand2 = currentScope + operand2;
        }
        // checking for the presence of the symbol in symbol table, otherwise throw error
        if (globalSymbolTable.containsKey(currentOperand1)) {
            if (globalSymbolTable.containsKey(currentOperand2)) {
                // add the two values in the hash map
                int a = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                int b = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                int c = a + b;
                globalSymbolTable.put(currentOperand1, Integer.toString(c));
            } else {
                System.out.println("Error: Could not resolve the second operand");
            }
        } else {
            System.out.println("Error: Could not resolve the first operand");
        }
    }

    public void sub() {
        // first get the appropriate object to update the appropriate symbol
        String currentScope = callStack.peekFirst();
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");
        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
            currentOperand2 = operand2;
        } else {
            currentOperand1 = currentScope + operand1;
            currentOperand2 = currentScope + operand2;
        }
        // checking for the presence of the symbol in symbol table, otherwise throw error
        if (globalSymbolTable.containsKey(currentOperand1)) {
            if (globalSymbolTable.containsKey(currentOperand2)) {
                // add the two values in the hash map
                int a = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                int b = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                int c = a - b;
                globalSymbolTable.put(currentOperand1, Integer.toString(c));
            } else {
                System.out.println("Error: Could not resolve the second operand");
            }
        } else {
            System.out.println("Error: Could not resolve the first operand");
        }
    }

    public void mul() {
        // first get the appropriate object to update the appropriate symbol
        String currentScope = callStack.peekFirst();
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");
        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
            currentOperand2 = operand2;
        } else {
            currentOperand1 = currentScope + operand1;
            currentOperand2 = currentScope + operand2;
        }
        // checking for the presence of the symbol in symbol table, otherwise throw error
        if (globalSymbolTable.containsKey(currentOperand1)) {
            if (globalSymbolTable.containsKey(currentOperand2)) {
                // add the two values in the hash map
                int a = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                int b = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                int c = a * b;
                globalSymbolTable.put(currentOperand1, Integer.toString(c));
            } else {
                System.out.println("Error: Could not resolve the second operand");
            }
        } else {
            System.out.println("Error: Could not resolve the first operand");
        }
    }

    public void div() {
        // first get the appropriate object to update the appropriate symbol
        String currentScope = callStack.peekFirst();
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");
        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
            currentOperand2 = operand2;
        } else {
            currentOperand1 = currentScope + operand1;
            currentOperand2 = currentScope + operand2;
        }
        // checking for the presence of the symbol in symbol table, otherwise throw error
        if (globalSymbolTable.containsKey(currentOperand1)) {
            if (globalSymbolTable.containsKey(currentOperand2)) {
                // add the two values in the hash map
                int a = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                int b = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                int c = a / b;
                globalSymbolTable.put(currentOperand1, Integer.toString(c));
            } else {
                System.out.println("Error: Could not resolve the second operand");
            }
        } else {
            System.out.println("Error: Could not resolve the first operand");
        }

    }

    public void rem() {
        // first get the appropriate object to update the appropriate symbol
        String currentScope = callStack.peekFirst();
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");
        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
            currentOperand2 = operand2;
        } else {
            currentOperand1 = currentScope + operand1;
            currentOperand2 = currentScope + operand2;
        }
        // checking for the presence of the symbol in symbol table, otherwise throw error
        if (globalSymbolTable.containsKey(currentOperand1)) {
            if (globalSymbolTable.containsKey(currentOperand2)) {
                // add the two values in the hash map
                int a = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                int b = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                int c = a % b;
                globalSymbolTable.put(currentOperand1, Integer.toString(c));
            } else {
                System.out.println("Error: Could not resolve the second operand");
            }
        } else {
            System.out.println("Error: Could not resolve the first operand");
        }
    }

    public void eql() {
        // first get the appropriate object to update the appropriate symbol table
        String currentScope = callStack.peekFirst();
        // if current operands do not have numbers
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");

        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
            currentOperand2 = operand2;
        } else {
            currentOperand1 = currentScope + operand1;
            char c = operand2.charAt(0);
            if (Character.isLetter(c)) {
                currentOperand2 = currentScope + operand2;
            } else {
                currentOperand2 = operand2;
            }
        }

        // check if the second operand is numeric or a variable
        char secondOperand = operand2.charAt(0);
        if (Character.isLetter(secondOperand)) {
            // if the operand exists, update the value in hash map or insert a new entry
            globalSymbolTable.put(currentOperand1, globalSymbolTable.get(currentOperand2));
        } else {
            globalSymbolTable.put(currentOperand1, currentOperand2);
        }

    }

    public void print() {
        // first get the appropriate object to update the appropriate symbol table
        String currentScope = callStack.peekFirst();
        String currentOperand1 = new String("");
        if (currentScope.equals("main")) {
            currentOperand1 = operand1;
        } else {
            currentOperand1 = currentScope + operand1;
        }
        if (globalSymbolTable.get(currentOperand1) != null) {
            System.out.println(globalSymbolTable.get(currentOperand1));
        } else {

            System.out.println("The variable " + operand1 + " was never initialized in this scope");
        }
    }

    public void fun() {
        callStack.addFirst(operand1);
    }

    public void endfun() {
        callStack.removeFirst();
    }

    public void ift() {
        // if true
        // get the operands
        String currentScope = callStack.peekFirst();
        // if current operands do not have numbers
        String currentOperand1 = new String("");
        String currentOperand2 = new String("");

        String ifOperand1 = new String("");
        String ifOperand2 = new String("");

        String ifOperator = new String("");
        // the expression is in operand1
        // parse operand1
        // parsing for the first operand in the conditional expression
        int startIndex = 3;
        int endIndex = 0;
        if (operands.indexOf(">") != -1) {
            endIndex = operands.indexOf(">");
            ifOperator = ">";
        } else if (operands.indexOf("<") != -1) {
            endIndex = operands.indexOf("<");
            ifOperator = "<";
        } else if (operands.indexOf("<=") != -1) {
            endIndex = operands.indexOf("<=");
            ifOperator = "<=";
        } else if (operands.indexOf(">=") != -1) {
            endIndex = operands.indexOf(">=");
            ifOperator = ">=";
        } else if (operands.indexOf("==") != -1) {
            endIndex = operands.indexOf("==");
            ifOperator = "==";
        } else if (operands.indexOf("!=") != -1) {
            endIndex = operands.indexOf("!=");
            ifOperator = "!=";
        }

        if (startIndex != -1 && endIndex != -1) {
            ifOperand1 = statement.substring(startIndex + 1, endIndex).trim();

        } else if (startIndex != -1 && endIndex == -1) {
            endIndex = operands.length();
            ifOperand1 = statement.substring(startIndex + 1, endIndex).trim();
        }

        // setting first operand to null for instructions with no operands
        else {
            ifOperand1 = null;
        }

        // parsing for the second operand
        startIndex = endIndex;
        endIndex = operands.length();
        if (startIndex != -1 && endIndex != -1) {
            ifOperand2 = statement.substring(startIndex + 2, endIndex).trim();

        } else if (startIndex == -1) {
            ifOperand2 = null;
        }

        int intOperand1 = 0;
        int intOperand2 = 0;

        if (ifOperand1 != null && ifOperand2 != null) {
            // check if both are variables or values
            // check if the second operand is numeric or a variable
            if (currentScope.equals("main")) {
                currentOperand1 = ifOperand1;
                currentOperand2 = ifOperand2;

                intOperand1 = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                // resolve the value of intOperand 2 if current scope is main
                char c = ifOperand2.charAt(0);
                if (Character.isLetter(c)) {
                    intOperand2 = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                } else {
                    intOperand2 = Integer.parseInt(currentOperand2);
                }

            } else {
                currentOperand1 = currentScope + ifOperand1;
                intOperand1 = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                char c = ifOperand2.charAt(0);
                if (Character.isLetter(c)) {
                    currentOperand2 = currentScope + ifOperand2;
                    intOperand2 = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                } else {
                    currentOperand2 = ifOperand2;
                    intOperand2 = Integer.parseInt(ifOperand2);
                }
            }
            // evaluate the expression
            switch (ifOperator) {
                case ">":
                    if (intOperand1 > intOperand2) {
                        result = true;

                    }
                    break;
                case ">=":
                    if (intOperand1 >= intOperand2) {
                        result = true;

                    }
                    break;
                case "<":
                    if (intOperand1 < intOperand2) {
                        result = true;
                    }
                    break;
                case "<=":
                    if (intOperand1 <= intOperand2) {
                        result = true;

                    }
                    break;
                case "==":
                    if (intOperand1 == intOperand2) {
                        result = true;

                    }
                    break;
                case "!=":
                    if (intOperand1 != intOperand2) {
                        result = true;

                    }
                    break;
                default:
                    System.out.println("Invalid Conditional Expression!");
                    break;
            }
            // if expression is true, execute statements until you encounter endift
            if (result) {
                // execute until you reach endift beginning with ift
                // read the previous lines until the current line.
                Runtime Runtime1 = new Runtime();
                try {
                    while ((ByteParser.line = FileUtils.readLines(ByteParser.fileName, "UTF-8").get(ByteParser.lineNumber)) != null) {
                        if (ByteParser.line.equals("ENDIFT")) {
                            ByteParser.lineNumber++;
                            break;
                        } else {
                            // get the current instruction parsed
                            ByteParser.parsedInstruction = Runtime1.parseSymbols(ByteParser.line);
                            Runtime1.performAction(ByteParser.parsedInstruction);
                        }
                        ByteParser.lineNumber++;
                    }

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // for debug purposes
                // System.out.println("Last read byte code line is:" + ByteParser.lineNumber);

            }
            // if expression is false, search for els statement following endift and execute till endels
            else {
                // execute until you reach endels beginning with els
                Runtime Runtime1 = new Runtime();

                try {
                    ByteParser.lineNumber++;
                    while ((ByteParser.line = FileUtils.readLines(ByteParser.fileName, "UTF-8").get(ByteParser.lineNumber)) != null) {
                        if (ByteParser.line.equals("ELS")) {
                            // repeat the while loop
                            while ((ByteParser.line = FileUtils.readLines(ByteParser.fileName, "UTF-8").get(ByteParser.lineNumber)) != null) {
                                if (ByteParser.line.equals("ENDELS")) {
                                    ByteParser.lineNumber++;
                                    break;
                                } else {
                                    ByteParser.parsedInstruction = Runtime1.parseSymbols(ByteParser.line);
                                    Runtime1.performAction(ByteParser.parsedInstruction);
                                }
                                ByteParser.lineNumber++;
                            }
                            break;
                        }
                        ByteParser.lineNumber++;
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // method to implement while loop
    public void loop() {
        int lineWhileEncountered = ByteParser.lineNumber;
        boolean loopComplete = false;
        while (!loopComplete) {
            // get the operands
            String currentScope = callStack.peekFirst();
            // if current operands do not have numbers
            String currentOperand1 = new String("");
            String currentOperand2 = new String("");

            String ifOperand1 = new String("");
            String ifOperand2 = new String("");

            String ifOperator = new String("");
            // the expression is in operand1
            // parse operand1
            // parsing for the first operand in the conditional expression
            int startIndex = 3;
            int endIndex = 0;
            if (operands.indexOf(">") != -1) {
                endIndex = operands.indexOf(">");
                ifOperator = ">";
            } else if (operands.indexOf("<") != -1) {
                endIndex = operands.indexOf("<");
                ifOperator = "<";
            } else if (operands.indexOf("<=") != -1) {
                endIndex = operands.indexOf("<=");
                ifOperator = "<=";
            } else if (operands.indexOf(">=") != -1) {
                endIndex = operands.indexOf(">=");
                ifOperator = ">=";
            } else if (operands.indexOf("==") != -1) {
                endIndex = operands.indexOf("==");
                ifOperator = "==";
            } else if (operands.indexOf("!=") != -1) {
                endIndex = operands.indexOf("!=");
                ifOperator = "!=";
            }

            if (startIndex != -1 && endIndex != -1) {
                ifOperand1 = statement.substring(startIndex + 1, endIndex).trim();

            } else if (startIndex != -1 && endIndex == -1) {
                endIndex = operands.length();
                ifOperand1 = statement.substring(startIndex + 1, endIndex).trim();
            }

            // setting first operand to null for instructions with no operands
            else {
                ifOperand1 = null;
            }

            // parsing for the second operand
            startIndex = endIndex;
            endIndex = operands.length();
            if (startIndex != -1 && endIndex != -1) {
                ifOperand2 = statement.substring(startIndex + 2, endIndex).trim();

            } else if (startIndex == -1) {
                ifOperand2 = null;
            }

            int intOperand1 = 0;
            int intOperand2 = 0;

            if (ifOperand1 != null && ifOperand2 != null) {
                // check if both are variables or values
                // check if the second operand is numeric or a variable
                if (currentScope.equals("main")) {
                    currentOperand1 = ifOperand1;
                    currentOperand2 = ifOperand2;

                    intOperand1 = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                    // resolve the value of intOperand 2 if current scope is main
                    char c = ifOperand2.charAt(0);
                    if (Character.isLetter(c)) {
                        intOperand2 = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                    } else {
                        intOperand2 = Integer.parseInt(currentOperand2);
                    }

                } else {
                    currentOperand1 = currentScope + ifOperand1;
                    intOperand1 = Integer.parseInt(globalSymbolTable.get(currentOperand1));
                    char c = ifOperand2.charAt(0);
                    if (Character.isLetter(c)) {
                        currentOperand2 = currentScope + ifOperand2;
                        intOperand2 = Integer.parseInt(globalSymbolTable.get(currentOperand2));
                    } else {
                        currentOperand2 = ifOperand2;
                        intOperand2 = Integer.parseInt(ifOperand2);
                    }
                }
                // evaluate the expression
                switch (ifOperator) {
                    case ">":
                        if (intOperand1 > intOperand2) {
                            result = true;
                        }
                        else {
                            result = false;
                        }
                        break;
                    case ">=":
                        if (intOperand1 >= intOperand2) {
                            result = true;
                        }
                        else {
                            result = false;
                        }
                        break;
                    case "<":
                        if (intOperand1 < intOperand2) {
                            result = true;
                        }
                        else {
                            result = false;
                        }
                        break;
                    case "<=":
                        if (intOperand1 <= intOperand2) {
                            result = true;
                        }
                        else {
                            result = false;
                        }
                        break;
                    case "==":
                        if (intOperand1 == intOperand2) {
                            result = true;
                        }
                        else {
                            result = false;
                        }
                        break;
                    case "!=":
                        if (intOperand1 != intOperand2) {
                            result = true;
                        }
                        else {
                            result = false;
                        }
                        break;
                    default:
                        System.out.println("Invalid conditional expression!");
                        break;
                }
                // if expression is true, execute statements until you encounter endift
                if (result) {
                    // execute until you reach elop beginning with loop
                    // read the previous lines until the current line.
                    Runtime Runtime1 = new Runtime();
                    try {
                        while ((ByteParser.line = FileUtils.readLines(ByteParser.fileName, "UTF-8").get(ByteParser.lineNumber)) != null) {
                            if (ByteParser.line.equals("ELOP")) {
                                ByteParser.lineNumber = lineWhileEncountered;
                                break;
                            } else {
                                // get the current instruction parsed
                                ByteParser.parsedInstruction = Runtime1.parseSymbols(ByteParser.line);
                                Runtime1.performAction(ByteParser.parsedInstruction);
                                ByteParser.lineNumber++;
                            }

                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                // if expression is false, search for els statement following endift and execute till endels
                else {
                    try {
                        while ((ByteParser.line = FileUtils.readLines(ByteParser.fileName, "UTF-8").get(ByteParser.lineNumber)) != null) {
                            if (ByteParser.line.equals("ELOP")) {
                                ByteParser.lineNumber++;
                                break;
                            }
                            ByteParser.lineNumber++;
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    loopComplete = true;
                }


            }

        }

    }
}
