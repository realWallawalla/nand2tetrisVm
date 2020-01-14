package com.timonsarakinis.commands;

public interface Command {
    CommandType getCommandType();

    String getOperator();

    String getArg1();

    int getArg2();
}
