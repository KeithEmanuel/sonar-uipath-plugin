package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentConventionCheckTest {

    private ArgumentConventionCheck check;
    private Project argsAndVars;
    private Workflow main;
    private Workflow allCamelCase;
    private Workflow allPascalCase;
    private Workflow allUpperCase;
    private Workflow allLowerCase;

    @Before
    public void setUp() throws Exception {
        check = new ArgumentConventionCheck();
        argsAndVars = LoadProject.withPath("ArgsAndVars");
        main = argsAndVars.getWorkflowNamed("Main").get();
        allCamelCase = argsAndVars.getWorkflowNamed("allCamelCase").get();
        allPascalCase = argsAndVars.getWorkflowNamed("AllPascalCase").get();
        allUpperCase = argsAndVars.getWorkflowNamed("ALLUPPERCASE").get();
        allLowerCase = argsAndVars.getWorkflowNamed("alllowercase").get();
    }

    @Test
    public void execute() {
        check.executeIgnoreCommonExceptions(argsAndVars, main);
        assertEquals(0, Issues.getCount());
        Issues.clear();

        check.execute(argsAndVars, allPascalCase);
        assertEquals(0, Issues.getCount());
        Issues.clear();

        check.execute(argsAndVars, allCamelCase);
        assertTrue(Issues.getCount() > 0);
        Issues.clear();

        /*check.execute(argsAndVars, allUpperCase);
        assertTrue(Issues.getCount() > 0);
        Issues.clear();*/

        check.execute(argsAndVars, allLowerCase);
        assertTrue(Issues.getCount() > 0);
        Issues.clear();
    }
}