package com.uipath.sonarqube.plugin.checks;

import com.uipath.sonarqube.plugin.AbstractWorkflowCheck;
import com.uipath.sonarqube.plugin.uipath.Project;
import com.uipath.sonarqube.plugin.uipath.Workflow;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(
    key = "InvokeWorkflowCheck",
    name = "Validate workflow arguments.",
    description =  "Verifies that the arguments of Invoke Workflow activities are valid and that the invoked workflow exists.",
    status = "BETA",
    priority = Priority.BLOCKER,
    tags = {"activity"}
)
public class InvokeWorkflowCheck extends AbstractWorkflowCheck {

    @Override
    public void execute(Project project, Workflow workflow){

        workflow.reportIssue( getRuleKey(), "InvokeWorkflowCheck issue!");
    }
}
