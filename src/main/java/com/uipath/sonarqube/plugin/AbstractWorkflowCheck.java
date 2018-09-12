package com.uipath.sonarqube.plugin;

import com.uipath.sonarqube.plugin.uipath.Project;
import com.uipath.sonarqube.plugin.uipath.Workflow;
import org.sonar.api.rule.RuleKey;

public abstract class AbstractWorkflowCheck extends AbstractCheck {

    public abstract void execute(Project project, Workflow workflow);
}
