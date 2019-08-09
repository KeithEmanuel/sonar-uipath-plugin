package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.languages.UiPathLanguage;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.WorkflowArgument;
import com.uipath.sonar.plugin.uipath.WorkflowArgument.Direction;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Rule(
    key = "AvoidChangingCurrentDirectory",
    name = "Changing the Environment.CurrentDirectory property should be avoided.",
    description =  "Checks whether assign activities that alter Environment.CurrentDirectory are used.",
    status = "BETA",
    priority = Priority.MAJOR,
    tags = {"workflow"}
)
public class AvoidChangingCurrentDirectory extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(AvoidChangingCurrentDirectory.class);

    public AvoidChangingCurrentDirectory(){
        super();
    }

    public void execute(Project project, Workflow workflow){
        List<Node> nodes = workflow.getXamlDocument().selectNodes("//xa:Assign/xa:Assign.To/xa:OutArgument");

        for(Node node : nodes){
           Element element = (Element)node;

           String text = element.getText();

           if(text.equals("[Environment.CurrentDirectory]")){
               reportIssue(workflow, "Altering the Environment.CurrentDirectory property should be avoided.");
           }
        }
    }
}
