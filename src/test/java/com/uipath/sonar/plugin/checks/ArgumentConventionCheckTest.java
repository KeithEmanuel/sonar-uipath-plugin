package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentConventionCheckTest {

    private ArgumentConventionCheck check = new ArgumentConventionCheck ();
    private Project argsAndVars;
    private Workflow allCamelCase;
    private Workflow allPascalCase;
    private Workflow allUpperCase;
    private Workflow allLowerCase;

    @Before
    public void setUp() throws Exception {
        argsAndVars = LoadProject.argsAndVars();
        allCamelCase = argsAndVars.getWorkflowNamed("allCamelCase").get();
        allPascalCase = argsAndVars.getWorkflowNamed("AllPascalCase").get();
        allUpperCase = argsAndVars.getWorkflowNamed("ALLUPPERCASE").get();
        allLowerCase = argsAndVars.getWorkflowNamed("alllowercase").get();
    }

    @Test
    public void execute() {
        testPascalCase();
        testCamelCase();
        testUpperCase();
        testLowerCase();
    }

    private void testPascalCase(){
        check.execute(argsAndVars, allPascalCase);
        assertEquals(0, Issues.getCount());
        Issues.clear();
    }

    private void testCamelCase(){

    }

    private void testUpperCase(){

    }

    private void testLowerCase(){

    }
}