package com.timonsarakinis.parser;

import com.timonsarakinis.io.FileReaderWriter;

import java.util.List;
import java.util.ListIterator;

/**
 * Handles the parsing of a single .vm file.
 * Reads a VM command, parses the command into its lexical components, and provides convenient access to these components
 * Ignores all white space and comments
 */
public class HackVmParser {
    private ListIterator<String> iterator;
    private Object currentCommand;

    public HackVmParser(String inputFilePath) {
        this.iterator = FileReaderWriter.readFile(inputFilePath).listIterator();
    }

    public boolean hasMoreCommands() {
        return iterator.hasNext();
    }

    public void advance() {
        this.currentCommand = iterator.next();
    }

    public CommandType commandType() {
        return CommandType.ARITHMETIC;
    }

    public String arg1() {
        return "";
    }

    public int arg2() {
        return 0;
    }
}