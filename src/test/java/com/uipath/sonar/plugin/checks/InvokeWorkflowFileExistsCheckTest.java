package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvokeWorkflowFileExistsCheckTest {

    private InvokeWorkflowFileExistsCheck check;
    private Project project;
    private Workflow shouldPass;
    private Workflow shouldFail;

    @Before
    public void setUp() throws Exception {
        check = new InvokeWorkflowFileExistsCheck();
        project = LoadProject.withPath("InvokeWorkflowFileExists");
        shouldPass = project.getWorkflowNamed("ShouldPass").get();
        shouldFail = project.getWorkflowNamed("ShouldFail").get();
    }

    @Test
    public void execute() {
        check.execute(project, shouldPass);
        assertEquals(0, Issues.getCount());

        check.execute(project, shouldFail);
        assertEquals(1, Issues.getCount());

        Issues.clear();
    }
}