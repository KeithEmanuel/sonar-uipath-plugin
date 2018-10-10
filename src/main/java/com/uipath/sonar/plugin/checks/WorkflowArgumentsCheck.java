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

@Rule(
    key = "WorkflowArgumentsCheck",
    name = "Workflow Arguments Check",
    description =  "Checks that workflow arguments follow naming conventions.",
    status = "BETA",
    priority = Priority.MINOR,
    tags = {"workflow"}
)
public class WorkflowArgumentsCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(WorkflowArgumentsCheck.class);

    private static final String IN_PREFIX = "in_";
    private static final String OUT_PREFIX = "out_";
    private static final String IN_OUT_PREFIX = "io_";

    public WorkflowArgumentsCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow) {
        for(WorkflowArgument arg : workflow.getArguments()) {

            if(arg.getDirection() == Direction.In){

                if(!arg.getName().startsWith(IN_PREFIX)){
                    reportIssue(workflow, arg);
                }
                else if (Character.isLowerCase(arg.getName().charAt(IN_PREFIX.length()))){
                    reportIssue(workflow, arg);
                }
            }
            else if(arg.getDirection() == Direction.Out) {
                if(!arg.getName().startsWith(OUT_PREFIX)){
                    reportIssue(workflow, arg);
                }
                else if (Character.isLowerCase(arg.getName().charAt(OUT_PREFIX.length()))){
                    reportIssue(workflow, arg);
                }
            }
            else if(arg.getDirection() == Direction.InOut) {
                if(!arg.getName().startsWith(IN_OUT_PREFIX)){
                    reportIssue(workflow, arg);
                }
                else if (Character.isLowerCase(arg.getName().charAt(IN_OUT_PREFIX.length()))){
                    reportIssue(workflow, arg);
                }
            }
            else {
                reportIssue(workflow, arg);
            }
        }
    }

    private void reportIssue(Workflow workflow, WorkflowArgument arg){

        String label = "UNKNOWN_DIRECTION";
        String prefix = "UNKNOWN_DIRECTION";

        if(arg.getDirection() == Direction.In){
            label = "Input";
            prefix = IN_PREFIX;
        }
        else if(arg.getDirection() == Direction.Out){
            label = "Output";
            prefix = OUT_PREFIX;
        }
        else if(arg.getDirection() == Direction.InOut){
            label = "InOut";
            prefix = IN_OUT_PREFIX;
        }

        workflow.reportIssue(getRuleKey(), "Argument '" + arg.getName()
            + "' does not follow naming convention. "
            + label + " arguments should start with '"
            + prefix + "' prefix followed by the PascalCase name.");
    }
}
