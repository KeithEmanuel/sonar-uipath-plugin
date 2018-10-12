package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.Project;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

/**
 * VariableConventionCheck verifies that variables in a workflow follow naming conventions.
 *
 * TODO: Determine naming convention and implement.
 */

@Rule(
    key = "VariableConventionCheck",
    name = "Variable Convention Check",
    description =  "Checks that workflow variables follow naming conventions.",
    status = "BETA",
    priority = Priority.MINOR,
    tags = {"workflow"}
)
public class VariableConventionCheck extends AbstractWorkflowCheck {

    public VariableConventionCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("//Variable");

        for(Node node : nodes){
           Element element = (Element)node;

           String name = element.attributeValue("Name");

           // TODO: determine naming convention
        }
    }
}
