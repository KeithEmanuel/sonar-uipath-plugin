package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.WorkflowArgument;
import com.uipath.sonar.plugin.uipath.WorkflowArgument.Direction;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Dictionary;
import java.util.HashMap;

@Rule(
    key = "ArgumentConventionCheck",
    name = "Arguments should follow naming convention",
    description =  "Checks that workflow arguments follow naming conventions.",
    status = "BETA",
    priority = Priority.MINOR,
    tags = {"workflow"}
)
public class ArgumentConventionCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(ArgumentConventionCheck.class);

    private static HashMap<Direction, String> DirectionPrefixMap = new HashMap<>();

    static {
        DirectionPrefixMap.put(Direction.In, "in_");
        DirectionPrefixMap.put(Direction.Out, "out_");
        DirectionPrefixMap.put(Direction.InOut, "io_");
    }

    public ArgumentConventionCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow) {
        for(WorkflowArgument arg : workflow.getArguments()) {

            String directionPrefix = DirectionPrefixMap.get(arg.getDirection());

            if(!arg.getName().startsWith(directionPrefix)){
                reportIssue(workflow, arg);
            }
            else if(Character.isLowerCase(arg.getName().charAt(directionPrefix.length()))){
                reportIssue(workflow, arg);
            }
        }
    }

    private void reportIssue(Workflow workflow, WorkflowArgument arg){

        String label = arg.getDirection() == Direction.InOut ? "In/Out" : arg.getDirection().toString();

        workflow.reportIssue(getRuleKey(), "Argument '" + arg.getName()
            + "' does not follow naming convention. "
            + label + " arguments should start with '"
            + DirectionPrefixMap.get(arg.getDirection()) + "' prefix followed by the PascalCase name.");
    }
}
