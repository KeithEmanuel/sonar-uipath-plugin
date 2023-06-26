package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.Node;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(
	    key = "AvoidGetRobotCredentialCheck",
	    name = "'Get Robot Credential' activity should be avoided",
	    description =  "Checks if the Get Robot Credential activity is used.",
	    status = "BETA",
	    priority = Priority.MAJOR,
	    tags = {"activity"}
)
public class AvoidGetRobotCredentialCheck extends AbstractWorkflowCheck {
	
	public AvoidGetRobotCredentialCheck() {
		super();
	}
	
	public void execute(Project project, Workflow workflow){
        List<Node> nodes = workflow.getXamlDocument().selectNodes("//ui:GetRobotCredential");

        if(nodes.size() > 0){

        	for(Node node : nodes){
        		// Allow for vault credential retrieval only
        		if(node.getStringValue().contains("AssetName=\"vault\"") == false){
                    reportIssue(workflow, "Avoid using the 'Get Robot Credential' activity.");
        		}
        	}
        }
    }
	
}
