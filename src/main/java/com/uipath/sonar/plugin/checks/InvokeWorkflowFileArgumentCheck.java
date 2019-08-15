package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Utils;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.WorkflowArgument;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Rule(
    key = "InvokeWorkflowFileArgumentCheck",
    name = "Check 'Invoke Workflow File' arguments match",
    description =  "Verifies that the arguments of Invoke Workflow activities are valid.",
    status = "BETA",
    priority = Priority.CRITICAL,
    tags = {"activity", "bug"}
)
public class InvokeWorkflowFileArgumentCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(ArgumentConventionCheck.class);

    private static final List<String> ARG_ELEMENT_NAMES = Arrays.asList( "InArgument", "OutArgument", "InOutArgument" );

    public InvokeWorkflowFileArgumentCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow){
        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:InvokeWorkflowFile");

        for(Node node : nodes) {
            Element element = (Element) node;

            if(Utils.nodeIsCode(element.attributeValue("WorkflowFileName"))){
                continue;
            }

            InvokeWorkflowFileMetadata iwfMetadata = extractMetadataFromInvokeWorkflowFile(element);
            executeExtraArgs(project, workflow, iwfMetadata);
            executeMissingArgs(project, workflow, iwfMetadata);
        }
    }

    private InvokeWorkflowFileMetadata extractMetadataFromInvokeWorkflowFile(Element invokeWorkflowFileElement){

        List<Node> descendantNodes = invokeWorkflowFileElement.selectNodes("ui:InvokeWorkflowFile.Arguments/*");

        String activityName = invokeWorkflowFileElement.attributeValue("DisplayName");
        String filePath = invokeWorkflowFileElement.attributeValue("WorkflowFileName");

        ArrayList<WorkflowArgument> arguments = new ArrayList<>();

        for(Node descendantNode : descendantNodes) {

            Element descendant = (Element) descendantNode;

            if (ARG_ELEMENT_NAMES.contains(descendant.getName())) {

                String name = descendant.attributeValue("Key");
                String type = descendant.attributeValue("TypeArguments");

                WorkflowArgument.Direction direction =
                    descendant.getName().equals("InArgument") ? WorkflowArgument.Direction.In
                        : descendant.getName().equals("OutArgument") ? WorkflowArgument.Direction.Out
                        : WorkflowArgument.Direction.InOut;

                arguments.add(new WorkflowArgument(name, type, direction));
            }
        }

        return new InvokeWorkflowFileMetadata(activityName, filePath, arguments);
    }

    private void executeExtraArgs(Project project, Workflow workflow, InvokeWorkflowFileMetadata iwfMetadata) {
        Workflow invokedWorkflow = project.getWorkflowWithPath(iwfMetadata.getPath()).get();  // TODO, handle

        for(WorkflowArgument arg : iwfMetadata.getArguments()){
            if(!invokedWorkflow.getArguments().contains(arg)){
                reportIssue(workflow,
                    "Invalid Invocation of '" + iwfMetadata.path
                        + "' in activity '" + iwfMetadata.getActivityName() + "'. Supplied argument '" + arg.getName() + "' does not exist.");
            }
        }
    }

    private void executeMissingArgs(Project project, Workflow workflow, InvokeWorkflowFileMetadata iwfMetadata) {
        Workflow invokedWorkflow = project.getWorkflowWithPath(iwfMetadata.path).get();  // TODO, handle

        for(WorkflowArgument arg : invokedWorkflow.getArguments()){
            if(!iwfMetadata.getArguments().contains(arg)){
                reportIssue(workflow,
                    "Invalid Invocation of '" + iwfMetadata.path
                        + "' in activity '" + iwfMetadata.getActivityName() + "'. Argument '" + arg.getName() + "' was not supplied.");
            }
        }
    }

    private class InvokeWorkflowFileMetadata {
        private String activityName;
        private String path;
        private ArrayList<WorkflowArgument> arguments;

        public InvokeWorkflowFileMetadata(String activityName, String path, ArrayList<WorkflowArgument> arguments){
            this.activityName = activityName;
            this.path = path;
            this.arguments = arguments;
        }

        public String getActivityName(){
            return activityName;
        }

        public String getPath(){
            return path;
        }

        public ArrayList<WorkflowArgument> getArguments(){
            return arguments;
        }
    }
}
