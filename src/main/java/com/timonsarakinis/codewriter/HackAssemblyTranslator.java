package com.timonsarakinis.codewriter;

import com.timonsarakinis.commands.Command;
import com.timonsarakinis.io.FileReaderWriter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static com.timonsarakinis.codewriter.ArithmaticType.*;

public class HackAssemblyTranslator {
    private static final String SP = "@SP";
    private static int uniquePrefix;
    private Map<String, String> segmentRamAddressMapping;
    private final Path outPutPath;
    private final static StringJoiner output = new StringJoiner(System.lineSeparator());;

    public HackAssemblyTranslator(Path outPutPath) {
        this.outPutPath = outPutPath;
        segmentRamAddressMapping = initMapping();
        setupStack();
    }

    private void setupStack() {
        output.add("// setup address for stackpointer");
        output.add("@256");
        output.add("D=A");
        output.add(SP);
        output.add("M=D" + System.lineSeparator());
    }


    private Map<String, String> initMapping() {
        HashMap<String, String> segmentRamAddressMapping = new HashMap<>();
        segmentRamAddressMapping.put("constant", "@SP");
        segmentRamAddressMapping.put("local", "@LCL");
        segmentRamAddressMapping.put("argument", "@ARG");
        segmentRamAddressMapping.put("this", "@THIS");
        segmentRamAddressMapping.put("that", "@THAT");
        return segmentRamAddressMapping;
    }

    public void writeArithmetic(String operator) {
        //write to outputfile the assembly code that implements the given arithmetic command
        ArithmaticType arithmaticType = valueOf(operator.toUpperCase());
        uniquePrefix = uniquePrefix + 1;
        switch (arithmaticType) {
            case ADD:
                buildAdd();
                break;
            case SUB:
                buildSub();
                break;
            case NEG:
                buildNeg();
                break;
            case EQ:
                buildEq();
                break;
            case LT:
                buildLessThen();
                break;
            case GT:
                buildGreaterThen();
                break;
            case AND:
                buildAnd();
                break;
            case OR:
                buildOr();
                break;
            case NOT:
                buildNot();
                break;
        }
        incrementStackPointer();
    }

    private void buildAdd() {
        //pops arguments from stack then push result to stack
        output.add("//ADD");
        popTwoFromStack();
        output.add("M=M+D");
    }

    private void popTwoFromStack() {
        output.add(SP);
        output.add("M=M-1");
        output.add("A=M");
        output.add("D=M");
        output.add(SP);
        output.add("M=M-1");
        output.add("A=M");
    }

    private void incrementStackPointer() {
        output.add(SP);
        output.add("M=M+1" + System.lineSeparator());
    }

    private void buildSub() {
        output.add("//SUB");
        popTwoFromStack();
        output.add("M=M-D");
    }

    private void buildNeg() {
        output.add("//NEG");
        output.add(SP);
        output.add("M=M-1");
        output.add("A=M");
        output.add("D=M");
        output.add("M=-M");
    }

    private void buildEq() {
        output.add("//EQ");
        popTwoFromStack();
        output.add("D=M-D"); // kolla om noll
        output.add("M=-1");
        output.add("@IS_EQ" + uniquePrefix);
        output.add("D;JEQ");
        output.add(SP);
        output.add("A=M");
        output.add("M=0");
        output.add("(IS_EQ" + uniquePrefix + ")");
        output.add(SP);
    }

    private void buildLessThen() {
        output.add("//LT");
        popTwoFromStack();
        output.add("D=M-D"); // kolla om m är mindre än d sant om d blir neg
        output.add("M=-1");
        output.add("@IS_LT" + uniquePrefix);
        output.add("D;JLT");
        output.add(SP);
        output.add("A=M");
        output.add("M=0");
        output.add("(IS_LT" + uniquePrefix  + ")");
        output.add(SP);
    }

    private void buildGreaterThen() {
        output.add("//GT");
        popTwoFromStack();
        output.add("D=M-D"); // kolla om m är större än d sant om d blir pos
        output.add("M=-1");
        output.add("@IS_GT" + uniquePrefix);
        output.add("D;JGT");
        output.add(SP);
        output.add("A=M");
        output.add("M=0");
        output.add("(IS_GT" + uniquePrefix  + ")");
        output.add(SP);
    }

    private void buildAnd() {
        output.add("//AND");
        popTwoFromStack();
        output.add("M=M&D");
    }

    private void buildOr() {
        output.add("//OR");
        popTwoFromStack();
        output.add("M=M|D");
    }

    private void buildNot() {
        output.add("//NOT");
        output.add(SP);
        output.add("M=M-1");
        output.add("A=M");
        output.add("M=!M");
    }

    public void writePush(Command command) {
        //write to outputfile the assembly code that implements the given push or pop command
        String vmSegment = segmentRamAddressMapping.get(command.getArg1());
        int value = command.getArg2();
        output.add("//PUSH");
        output.add("@" + value);
        output.add("D=A");
        output.add(vmSegment);
        output.add("A=M");
        output.add("M=D");
        output.add(vmSegment);
        output.add("M=M+1" + System.lineSeparator());

    }

    public void writePop(Command command) {
        //write to outputfile the assembly code that implements the given push or pop command
        String vmSegment = segmentRamAddressMapping.get(command.getArg1());

        output.add(vmSegment);
        output.add("M=M-1");
        output.add("D=M");

    }

    public void close() {
        FileReaderWriter.writeToFile(output.toString().getBytes(), outPutPath);
        System.out.println("wrote to file successfully");
    }
}
