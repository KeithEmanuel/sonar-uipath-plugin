package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.WorkflowArgument;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
    key = "InvokeWorkflowFileArgumentCheck",
    name = "Invoke Workflow File Argument Check",
    description =  "Verifies that the arguments of Invoke Workflow activities are valid.",
    status = "BETA",
    priority = Priority.BLOCKER,
    tags = {"activity"}
)
public class InvokeWorkflowFileArgumentCheck extends AbstractWorkflowCheck {

    public InvokeWorkflowFileArgumentCheck(){
        super();
    }

    // TODO! This is not complete

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:InvokeWorkflowFile");

        for(Node node : nodes) {
            Element element = (Element) node;
            String workflowFileName = element.attributeValue("WorkflowFileName");

            Workflow invokedWorkflow = project.getWorkflowWithPath(workflowFileName).orElse(null);

            for(Element descendant : element.elements()){

                String name = descendant.attributeValue("x:Key");
                String type = descendant.attributeValue("x:TypeArguments");

                WorkflowArgument.Direction direction =
                    descendant.getName() == "InArgument" ? WorkflowArgument.Direction.In
                        : descendant.getName() == "OutArgument" ? WorkflowArgument.Direction.Out
                        : WorkflowArgument.Direction.InOut;

                boolean hasMatch = invokedWorkflow.getArguments().stream().anyMatch(a -> a.matches(name, type, direction));

                if(!hasMatch){
                    String displayName = element.attributeValue("DisplayName");

                    workflow.reportIssue(getRuleKey(), "Invalid Invocation of '" + workflowFileName
                        + "' in activity '" + displayName + "'. Supplied argument '" + name + "' does not exist.");
                }
            }
        }
    }
}
