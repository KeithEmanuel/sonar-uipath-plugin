package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Utils;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
    key = "InvokeWorkflowFileExistsCheck",
    name = "Check that File referenced in 'Invoke Workflow File' activity exists",
    description =  "Checks that an invoked workflow referenced in an 'Invoke Workflow File' activity exists at the specified path.",
    status = "BETA",
    priority = Priority.BLOCKER,
    tags = {"activity"}
)
public class InvokeWorkflowFileExistsCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(ArgumentConventionCheck.class);

    public InvokeWorkflowFileExistsCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:InvokeWorkflowFile");

        for(Node node : nodes) {
            Element element = (Element) node;
            String workflowFilename = element.attributeValue("WorkflowFileName");

            if(Utils.nodeIsCode(workflowFilename)){
                continue;  // This is code, not a literal string.
            }

            boolean workflowExists = project.getWorkflowWithPath(element.attributeValue("WorkflowFileName")).isPresent();

            if(!workflowExists){
                String displayName = element.attributeValue("DisplayName");

                Issues.report(workflow, getRuleKey(), "Invoked workflow '" + workflowFilename + "' for activity '" + displayName +"' does not exist.");
            }
        }
    }
}