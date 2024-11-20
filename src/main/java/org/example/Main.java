package org.example;

import java.util.Objects;

import static org.example.JUnitXMLValidator.validateXML;

public class Main {
    public static void main(String[] args) {
        try {
            String filePath = Objects.requireNonNull(Main.class.getClassLoader().getResource("test-results-1.xml")).getPath();

            long startTime = System.nanoTime();
            validateXML(filePath);
            long endTime = System.nanoTime();

            long durationInMillis = (endTime - startTime) / 1_000_000;
            System.out.println("Processing time: " + durationInMillis + " ms");

        } catch (Exception e) {
            System.err.println("Error locating or validating XML: " + e.getMessage());
        }
    }
}