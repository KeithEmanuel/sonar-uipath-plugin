package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;

/**
 * AbstractWorkflowCheck is the base class for checks executed at the workflow or activity level.
 * The execute function is called for each xaml file in the project directory and subdirectories.
 * Be sure to call super() in any derived classes to initialize the Rule metadata.
 */
public abstract class AbstractWorkflowCheck extends AbstractCheck {

    protected AbstractWorkflowCheck(){
        super();
    }

    public abstract void execute(Project project, Workflow workflow);
}
