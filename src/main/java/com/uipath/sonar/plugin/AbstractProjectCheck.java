package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.uipath.Project;
import org.dom4j.XPathException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * AbstractProjectCheck is the base class for any checks performed at a project level.
 * The execute method is called for each project.json file.
 * Be sure to call super() in any derived classes to initialize the Rule metadata.
 */
public abstract class AbstractProjectCheck extends AbstractCheck {

    private static Logger LOG = Loggers.get(AbstractProjectCheck.class);

    protected AbstractProjectCheck(){
        super();
    }

    public abstract void execute(Project project);

    public void executeIgnoreCommonExceptions(Project project){
        try{
            execute(project);
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
