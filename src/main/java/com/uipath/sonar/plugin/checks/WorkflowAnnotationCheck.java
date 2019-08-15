package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.Node;
import org.dom4j.XPathException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
    key = "WorkflowAnnotationCheck",
    name = "Workflows should have top level annotations",
    description =  "Checks that workflow files have a top level annotation.",
    status = "BETA",
    priority = Priority.INFO,
    tags = {"workflow"}
)
public class WorkflowAnnotationCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(ArgumentConventionCheck.class);

    public WorkflowAnnotationCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow){

        try{
            List<Node> nodes = workflow.getXamlDocument().selectNodes("/xa:Activity/*[@sap2010:Annotation.AnnotationText]");
            LOG.debug("SIZE: " + nodes.size());

            if(nodes.size() == 0){
                reportIssue(workflow, "Workflow '" + workflow.getName() + "' should have a top level annotation.");
            }
        }
        catch (XPathException e) {
            // This will catch errors where XPath queries are ran in a document that doesn't contain a namespace used in the XPath query.
            if(e.getMessage().contains("XPath expression uses unbound namespace prefix")){
                // This means that there is no top level annotation.
                reportIssue(workflow, "Workflow '" + workflow.getName() + "' should have a top level annotation.");
            }
            else {
                throw e;
            }
        }

    }
}
