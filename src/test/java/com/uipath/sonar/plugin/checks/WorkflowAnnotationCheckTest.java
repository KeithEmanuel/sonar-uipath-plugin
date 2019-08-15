package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkflowAnnotationCheckTest {

    private WorkflowAnnotationCheck check;
    private Project project;
    private Workflow main;
    private Workflow shouldPass;
    private Workflow shouldFail;
    private Workflow shouldFailNoNamespace;

    @Before
    public void setUp() throws Exception {
        check = new WorkflowAnnotationCheck();
        project = LoadProject.withPath("WorkflowAnnotation");
        main = project.getWorkflowNamed("Main").get();
        shouldPass = project.getWorkflowNamed("ShouldPass").get();
        shouldFail = project.getWorkflowNamed("ShouldFail").get();
        shouldFailNoNamespace = project.getWorkflowNamed("ShouldFailNoNamespace").get();
    }

    @Test
    public void execute() {
        check.execute(project, shouldPass);
        assertEquals(0, Issues.getCount());

        check.execute(project, main);
        assertEquals(1, Issues.getCount());
        Issues.clear();

        check.execute(project, shouldFail);
        assertEquals(1, Issues.getCount());
        Issues.clear();

        check.execute(project, shouldFailNoNamespace);
        assertEquals(1, Issues.getCount());
        Issues.clear();
    }
}