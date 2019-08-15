package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvokeWorkflowFileArgumentCheckTest {

    private InvokeWorkflowFileArgumentCheck check;
    private Project project;
    private Workflow main;
    private Workflow shouldPass1;
    private Workflow shouldFail1;
    private Workflow shouldFail2;

    @Before
    public void setUp() throws Exception {
        check = new InvokeWorkflowFileArgumentCheck();
        project = LoadProject.withPath("InvokeWorkflowFileArgument");
        main = project.getWorkflowNamed("Main").get();
        shouldPass1 = project.getWorkflowNamed("ShouldPass1").get();
        shouldFail1 = project.getWorkflowNamed("ShouldFail1").get();
        shouldFail2 = project.getWorkflowNamed("ShouldFail2").get();
    }

    @Test
    public void execute() {
        check.executeIgnoreCommonExceptions(project, main);
        assertEquals(0, Issues.getCount());

        check.execute(project, shouldPass1);
        assertEquals(0, Issues.getCount());

        check.execute(project, shouldFail1);
        assertEquals(1, Issues.getCount());
        Issues.clear();

        check.execute(project, shouldFail2);
        assertEquals(1, Issues.getCount());
        Issues.clear();
    }
}