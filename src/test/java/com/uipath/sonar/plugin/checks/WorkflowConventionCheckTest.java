package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkflowConventionCheckTest {

    private Project project;
    private Workflow ok1;
    private Workflow ok2;
    private Workflow ok3;
    private Workflow notOk1;
    private Workflow notOk2;
    private Workflow notOk3;

    @Before
    public void setUp() throws Exception {
        project = LoadProject.workflowConventions();
        ok1 = project.getWorkflowNamed("_Ok").get();
        ok2 = project.getWorkflowNamed("AlsoOk").get();
        ok3 = project.getWorkflowNamed("Still_Ok").get();
        notOk1 = project.getWorkflowNamed("_notOk").get();
        notOk2 = project.getWorkflowNamed("Not_ok").get();
        notOk3 = project.getWorkflowNamed("notOk").get();
    }

    @Test
    public void execute() {
        WorkflowConventionCheck check = new WorkflowConventionCheck();

        check.execute(project, ok1);
        check.execute(project, ok2);
        check.execute(project, ok3);
        assertEquals(0, Issues.getCount());

        check.execute(project, notOk1);
        check.execute(project, notOk2);
        check.execute(project, notOk3);
        assertEquals(3, Issues.getCount());
        Issues.clear();
    }
}