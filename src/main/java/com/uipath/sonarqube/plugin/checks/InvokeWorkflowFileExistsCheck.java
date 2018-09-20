package com.uipath.sonarqube.plugin.checks;

import com.uipath.sonarqube.plugin.AbstractWorkflowCheck;
import com.uipath.sonarqube.plugin.uipath.Project;
import com.uipath.sonarqube.plugin.uipath.Workflow;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
    key = "InvokeWorkflowFileExistsCheck",
    name = "Invoke Workflow File Exists Check",
    description =  "Checks that an invoked workflow is exists in the specified path.",
    status = "BETA",
    priority = Priority.BLOCKER,
    tags = {"activity"}
)
public class InvokeWorkflowFileExistsCheck extends AbstractWorkflowCheck {

    public InvokeWorkflowFileExistsCheck(){
        super();
    }

    // TODO! This is not complete

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:InvokeWorkflowFile");

        for(Node node : nodes) {
            Element element = (Element) node;
            String workflowFileName = element.attributeValue("WorkflowFileName");

            boolean workflowExists = project.getWorkflowWithPath(element.attributeValue("WorkflowFileName")).isPresent();

            if(!workflowExists){
                String displayName = element.attributeValue("DisplayName");

                workflow.reportIssue(getRuleKey(), "Invoked workflow '" + workflowFileName + "' for activity '" + displayName +"' does not exist.");
            }
        }
    }
}