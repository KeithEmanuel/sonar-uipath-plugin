package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.Node;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
    key = "AvoidQueuesCheck",
    name = "'Add Queue Item' activity should be avoided",
    description =  "Checks if the Add Queue Item is used.",
    status = "BETA",
    priority = Priority.MAJOR,
    tags = {"activity"}
)
public class AvoidQueuesCheck extends AbstractWorkflowCheck {

    public AvoidQueuesCheck(){
        super();
    }

    public void execute(Project project, Workflow workflow){
        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:AddQueueItem");

        if(nodes.size() > 0){
            reportIssue(workflow, "Avoid using the 'Add Queue Item' activity.");
        }
    }
}
