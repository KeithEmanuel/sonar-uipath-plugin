package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.XPathException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * AbstractWorkflowCheck is the base class for checks executed at the workflow or activity level.
 * The execute function is called for each xaml file in the project directory and subdirectories.
 * Be sure to call super() in any derived classes to initialize the Rule metadata.
 */
public abstract class AbstractWorkflowCheck extends AbstractCheck {

    private static Logger LOG = Loggers.get(AbstractWorkflowCheck.class);

    protected AbstractWorkflowCheck(){
        super();
    }

    public abstract void execute(Project project, Workflow workflow);

    public void executeIgnoreCommonExceptions(Project project, Workflow workflow){
        try{
            execute(project, workflow);
        }
        catch (XPathException e) {
            // This will catch errors where XPath queries are ran in a document that doesn't contain a namespace used in the XPath query.
            if(e.getMessage().contains("XPath expression uses unbound namespace prefix")){
                LOG.debug("Encountered XPath exception when executing check '" + getRule().name() + "'. This may or may not be an issue.", e);
            }
            else {
                throw e;
            }
        }
    }
}
