package com.timonsarakinis.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileReaderWriter {
    public static List<String> readFile(String filePath) {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
            System.out.println("Successfully read vm file");
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void writeToFile(byte[] line, Path path) {
        try {
            Files.write(path, line, CREATE, APPEND);
            System.out.println("Successfully written line to output file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getOutputPath() {
        Path path = Paths.get("src/main/resources/hackAssembly.asm");
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
