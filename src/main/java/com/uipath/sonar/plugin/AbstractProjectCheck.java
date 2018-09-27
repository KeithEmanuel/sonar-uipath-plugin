package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.uipath.Project;

/**
 * AbstractProjectCheck is the base class for any checks performed at a project level.
 * The execute method is called for each project.json file.
 * Be sure to call super() in any derived classes to initialize the Rule metadata.
 */
public abstract class AbstractProjectCheck extends AbstractCheck {

    protected AbstractProjectCheck(){
        super();
    }

    public abstract void execute(Project project);
}
