package com.timonsarakinis;

import com.timonsarakinis.codewriter.HackAssemblyTranslator;
import com.timonsarakinis.io.FileReaderWriter;
import com.timonsarakinis.parser.HackVmParser;

import static com.timonsarakinis.commands.CommandType.*;

/**
 *
 * Parser module: parses each VM command into its lexical elements
 • CodeWriter module: writes the assembly code that implements the parsed command
 • Main: drives the process (VMTranslator)
 *
 **/
public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/StackTest.vm";
        HackVmParser parser = new HackVmParser(filePath);
        HackAssemblyTranslator translator = new HackAssemblyTranslator(FileReaderWriter.getOutputPath());
        while (parser.hasMoreCommands()) {
            parser.advance();
            if (parser.getCurrentCommand() == null) {
                continue;
            } else if (parser.commandType() == PUSH) {
                translator.writePush(parser.getCurrentCommand());
            } else if (parser.commandType() == ARITHMETIC) {
                translator.writeArithmetic(parser.operator());
            } else if (parser.commandType() == POP) {
                translator.writePop(parser.getCurrentCommand());
            }
        }
        translator.close();
    }
}
