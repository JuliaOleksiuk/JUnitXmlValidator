package org.example;

import org.w3c.dom.*;


public class JUnitXMLValidator {

    public static void validateTestSuites(Element testsuites) throws Exception {
        NodeList suites = testsuites.getElementsByTagName("testsuite");
        for (int i = 0; i < suites.getLength(); i++) {
            if (suites.item(i).getParentNode() == testsuites) { // Validate direct children
                validateTestSuite((Element) suites.item(i));
            }
        }
    }

    public static void validateTestSuite(Element testsuite) throws Exception {
        // Validate required attributes: name, tests, time
        if (!testsuite.hasAttribute("name") || !testsuite.hasAttribute("tests") || !testsuite.hasAttribute("time")) {
            throw new Exception("Invalid <testsuite>: Must contain 'name', 'tests', and 'time' attributes.");
        }

        // Validate nested <testsuite> elements
        NodeList nestedSuites = testsuite.getElementsByTagName("testsuite");
        for (int i = 0; i < nestedSuites.getLength(); i++) {
            if (nestedSuites.item(i).getParentNode() == testsuite) { // Validate direct children
                validateTestSuite((Element) nestedSuites.item(i));
            }
        }

        // Validate <testcase> elements
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
