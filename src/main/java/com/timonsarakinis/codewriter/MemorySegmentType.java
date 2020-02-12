package com.timonsarakinis.codewriter;

public enum MemorySegmentType {
    CONSTANT("@SP"),
    LOCAL("@LCL"),
    ARGUMENT("@ARG"),
    THIS("@THIS"),
    THAT("@THAT"),
    STATIC("static"),
    TEMP("temp"),
    POINTER("pointer");

    MemorySegmentType(String symbol) {

    }
}
