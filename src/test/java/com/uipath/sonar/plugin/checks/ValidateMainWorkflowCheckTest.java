package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issue;
import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.testprojects.LoadProject;
import com.uipath.sonar.plugin.uipath.Project;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateMainWorkflowCheckTest {

    private ValidateMainWorkflowCheck check;
    private Project shouldPass;
    private Project shouldFail;

    @Before
    public void setUp() throws Exception {
        check = new ValidateMainWorkflowCheck();
        shouldPass = LoadProject.withPath("ValidateMainWorkflow/ShouldPass");
        shouldFail = LoadProject.withPath("ValidateMainWorkflow/ShouldFail");
    }

    @Test
    public void execute() {
        check.execute(shouldPass);
        assertEquals(0, Issues.getCount());

        check.execute(shouldFail);
        assertEquals(1, Issues.getCount());

        Issues.clear();
    }
}