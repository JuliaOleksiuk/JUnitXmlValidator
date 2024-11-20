package org.example;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class JUnitXMLValidator {

    public static void validateXML(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();
        if ("testsuites".equals(root.getNodeName())) {
            validateTestSuites(root);
        } else if ("testsuite".equals(root.getNodeName())) {
            validateTestSuite(root);
        } else {
            throw new Exception("Invalid JUnit XML: Root element must be <testsuites> or <testsuite>");
        }

        System.out.println("JUnit XML validation completed successfully.");
    }

    public static void validateTestSuites(Element testsuites) throws Exception {
        NodeList suites = testsuites.getElementsByTagName("testsuite");
        for (int i = 0; i < suites.getLength(); i++) {
            if (suites.item(i).getParentNode() == testsuites) { // Validate direct children
                validateTestSuite((Element) suites.item(i));
            }
        }
    }

    public static void validateTestSuite(Element testsuite) throws Exception {
        if (!testsuite.hasAttribute("name") || !testsuite.hasAttribute("tests") || !testsuite.hasAttribute("time")) {
            throw new Exception("Invalid <testsuite>: Must contain 'name', 'tests', and 'time' attributes.");
        }

        NodeList nestedSuites = testsuite.getElementsByTagName("testsuite");
        for (int i = 0; i < nestedSuites.getLength(); i++) {
            if (nestedSuites.item(i).getParentNode() == testsuite) { // Validate direct children
                validateTestSuite((Element) nestedSuites.item(i));
            }
        }

        NodeList testCases = testsuite.getElementsByTagName("testcase");
        for (int i = 0; i < testCases.getLength(); i++) {
            if (testCases.item(i).getParentNode() == testsuite) { // Validate direct children
                validateTestCase((Element) testCases.item(i));
            }
        }
    }

    public static void validateTestCase(Element testcase) throws Exception {
        // Validate required attributes: name, classname, time
        if (!testcase.hasAttribute("name") || !testcase.hasAttribute("classname") || !testcase.hasAttribute("time")) {
            throw new Exception("Invalid <testcase>: Must contain 'name', 'classname', and 'time' attributes.");
        }

        // Check for optional <failure> or <error> elements
        NodeList failures = testcase.getElementsByTagName("failure");
        NodeList errors = testcase.getElementsByTagName("error");
        if (failures.getLength() > 0) {
            System.out.println("<testcase> contains a <failure> element.");
        }
        if (errors.getLength() > 0) {
            System.out.println("<testcase> contains an <error> element.");
        }
    }
}
