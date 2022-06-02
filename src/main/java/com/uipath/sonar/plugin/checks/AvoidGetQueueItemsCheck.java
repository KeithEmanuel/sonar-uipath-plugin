package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.Node;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
    key = "AvoidGetQueueItemsCheck",
    name = "'Get Queue Items' activity should be avoided",
    description =  "Checks if the Get Queue Items activity is used.",
    status = "BETA",
    priority = Priority.MAJOR,
    tags = {"activity"}
)
public class AvoidGetQueueItemsCheck extends AbstractWorkflowCheck {

    public AvoidGetQueueItemsCheck(){
        super();
    }

    public void execute(Project project, Workflow workflow){
        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:GetQueueItems");

        if(nodes.size() > 0){
            reportIssue(workflow, "Avoid using the 'Get Queue Items' activity.");
        }
    }
}
