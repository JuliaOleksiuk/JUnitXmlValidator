package org.example;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.*;
import java.io.File;

import static org.example.JUnitXMLValidator.validateTestSuite;
import static org.example.JUnitXMLValidator.validateTestSuites;

public class Main {
    public static void main(String[] args) {
        String filePath = "path/to/your/junit-report.xml";

        try {
            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            // Normalize the document
            document.getDocumentElement().normalize();

            // Validate the structure starting from the root
            Element root = document.getDocumentElement();
            if ("testsuites".equals(root.getNodeName())) {
                validateTestSuites(root);
            } else if ("testsuite".equals(root.getNodeName())) {
                validateTestSuite(root);
            } else {
                throw new Exception("Invalid JUnit XML: Root element must be <testsuites> or <testsuite>");
            }

            System.out.println("JUnit XML validation completed successfully.");
        } catch (Exception e) {
            System.err.println("Error validating JUnit XML: " + e.getMessage());
        }
    }
}