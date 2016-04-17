import java.io.*;
import java.lang.*;

public class ByteParser{

    public static void main(String []args){
        String fileName = "input.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ProgramParser programParser = new ProgramParser();
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                programParser.parseSymbols(line);
                programParser.printParseData();
            }
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ProgramParser {
    private String prefix = new String("\t");
    private String statement = new String("") ;
    private String opCode = new String("") ;
    private String operands = new String("") ;
    private String[] operand1 ;
    private String operand2 = new String("");
    // hashmap serving as a global symbol table
    // generic stack to evaluate the program

    // method that parses the program and produces the output
    public void parseSymbols(String parse) {
        statement = parse.substring(parse.indexOf(prefix), parse.length());

        // converting the array of strings to string object
        StringBuilder builder = new StringBuilder();
        for(String s : statement.split(" ")) {
            builder.append(s);
        }

        // opcode now has the opcode in the statement
        opCode = builder.toString();

        opCode.toString();
        operands = statement.substring((statement.indexOf(opCode)+1),  statement.length());
        operand1 = operands.split(", ");
        operand1.toString();
        operand2 = statement.replaceAll("[^0-9]","");
    }

    // method to print the parsed string just for debug purposes
    public void printParseData() {
        System.out.println("Current Op Code is:"+ opCode);
        System.out.println("Current operand1 is:" + operand1);
        System.out.println("Current operand2 is:" + operand2);
    }

    /** method to print the global symbol table
     public void printHashTable();
     */

    /** method to print the program execution stack
     public void printProgramStack();
     */
}
