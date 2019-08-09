package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.languages.UiPathLanguage;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Utils;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.WorkflowArgument;
import com.uipath.sonar.plugin.uipath.WorkflowArgument.Direction;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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

    private static final String IN_ARGUMENT_FORMAT_KEY = "uipath.check.argumentconventioncheck.inargformat";
    private static final String IN_ARGUMENT_FORMAT_DEFAULT_VALUE = "^in_[A-Z][\\w\\d]+$";
    private static final String OUT_ARGUMENT_FORMAT_KEY = "uipath.check.argumentconventioncheck.outargformat";
    private static final String OUT_ARGUMENT_FORMAT_DEFAULT_VALUE = "^out_[A-Z][\\w\\d]+$";
    private static final String IO_ARGUMENT_FORMAT_KEY = "uipath.check.argumentconventioncheck.ioargformat";
    private static final String IO_ARGUMENT_FORMAT_DEFAULT_VALUE = "^io_[A-Z][\\w\\d]+$";

    public ArgumentConventionCheck(){
        super();
    }

    @Override
    public List<PropertyDefinition> defineProperties(){

        return Arrays.asList(
            PropertyDefinition.builder(IN_ARGUMENT_FORMAT_KEY)
                .defaultValue(IN_ARGUMENT_FORMAT_DEFAULT_VALUE)
                .name("InArgument Convention Format")
                .description("Naming convention format for input arguments, defined as a regular expression.")
                .onQualifiers(Qualifiers.PROJECT)
                .build(),
            PropertyDefinition.builder(OUT_ARGUMENT_FORMAT_KEY)
                .defaultValue(OUT_ARGUMENT_FORMAT_DEFAULT_VALUE)
                .name("OutArgument Convention Format")
                .description("Naming convention format for output arguments, defined as a regular expression.")
                .onQualifiers(Qualifiers.PROJECT)
                .build(),
            PropertyDefinition.builder(IO_ARGUMENT_FORMAT_KEY)
                .defaultValue(IO_ARGUMENT_FORMAT_DEFAULT_VALUE)
                .name("InOutArgument Convention Format")
                .description("Naming convention format for input/output arguments, defined as a regular expression.")
                .onQualifiers(Qualifiers.PROJECT)
                .build()
        );
    }

    @Override
    public void execute(Project project, Workflow workflow) {


        for(WorkflowArgument arg : workflow.getArguments()) {
            String name = arg.getName();
            System.out.println("testing " + arg);
            switch(arg.getDirection()){
                case In:
                    Pattern inPattern = Pattern.compile(getInArgFormat());
                    if(!inPattern.matcher(name).find())
                        reportIssue(workflow, arg);
                    break;
                case Out:
                    Pattern outPattern = Pattern.compile(getOutArgFormat());
                    if(!outPattern.matcher(name).find())
                        reportIssue(workflow, arg);
                    break;
                case InOut:
                    Pattern inOutPattern = Pattern.compile(getInOutArgFormat());
                    if(!inOutPattern.matcher(name).find())
                        reportIssue(workflow, arg);
                    break;
            }
        }
    }

    private void reportIssue(Workflow workflow, WorkflowArgument arg){
        System.out.println("BADBADBAD");
        String label = arg.getDirection() == Direction.InOut ? "In/Out" : arg.getDirection().toString();

        reportIssue(workflow,
            "Argument '" + arg.getName()
                + "' does not follow naming convention. "
                + label + " arguments should follow the convention '"
                + getArgFormat(arg.getDirection())
                + "'.");
    }

    public String getInArgFormat(){
        return getPropertyValue(IN_ARGUMENT_FORMAT_KEY, IN_ARGUMENT_FORMAT_DEFAULT_VALUE);
    }

    public String getOutArgFormat(){
        return getPropertyValue(OUT_ARGUMENT_FORMAT_KEY, OUT_ARGUMENT_FORMAT_DEFAULT_VALUE);
    }

    public String getInOutArgFormat(){
        return getPropertyValue(IO_ARGUMENT_FORMAT_KEY, IO_ARGUMENT_FORMAT_DEFAULT_VALUE);
    }

    public String getArgFormat(Direction direction){
        return direction == Direction.In ? getInArgFormat()
            : direction == Direction.Out ? getOutArgFormat()
            : direction == Direction.InOut ? getInOutArgFormat()
            : null;  // This should never happen.
    }
}
