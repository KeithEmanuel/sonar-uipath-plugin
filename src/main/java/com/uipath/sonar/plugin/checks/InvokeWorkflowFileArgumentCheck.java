package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.uipath.Utils;
import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.WorkflowArgument;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.List;

@Rule(
    key = "InvokeWorkflowFileArgumentCheck",
    name = "Invoke Workflow File Argument Check",
    description =  "Verifies that the arguments of Invoke Workflow activities are valid.",
    status = "BETA",
    priority = Priority.CRITICAL,
    tags = {"activity"}
)
public class InvokeWorkflowFileArgumentCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(WorkflowArgumentsCheck.class);

    private static final List<String> ARG_ELEMENT_NAMES = Arrays.asList( "InArgument", "OutArgument", "InOutArgument" );

    public InvokeWorkflowFileArgumentCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:InvokeWorkflowFile");

        for(Node node : nodes) {
            Element element = (Element) node;
            String workflowFilename = element.attributeValue("WorkflowFileName");

            if(Utils.nodeIsCode(workflowFilename)){
                break;  // This is code, not a literal string.
            }

            Workflow invokedWorkflow = project.getWorkflowWithPath(workflowFilename).orElse(null);

            if(invokedWorkflow != null){

                List<Node> descendantNodes = element.selectNodes("ui:InvokeWorkflowFile.Arguments/*");

                for(Node descendantNode : descendantNodes){

                    Element descendant = (Element)descendantNode;

                    if(ARG_ELEMENT_NAMES.contains(descendant.getName())){

                        String name = descendant.attributeValue("Key");
                        String type = descendant.attributeValue("TypeArguments");

                        WorkflowArgument.Direction direction =
                            descendant.getName().equals("InArgument") ? WorkflowArgument.Direction.In
                                : descendant.getName().equals("OutArgument") ? WorkflowArgument.Direction.Out
                                : WorkflowArgument.Direction.InOut;

                        boolean hasMatch = invokedWorkflow.getArguments().stream().anyMatch(a -> a.matches(name, type, direction));

                        if(!hasMatch){
                            String displayName = element.attributeValue("DisplayName");

                            workflow.reportIssue(getRuleKey(), "Invalid Invocation of '" + workflowFilename
                                + "' in activity '" + displayName + "'. Supplied argument '" + name + "' does not exist.");
                        }
                    }
                }
            }
        }
    }
}
