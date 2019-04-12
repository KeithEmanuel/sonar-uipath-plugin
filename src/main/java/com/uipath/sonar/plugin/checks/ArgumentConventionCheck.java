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
    private static final String IN_ARGUMENT_FORMAT_DEFAULT_VALUE = "in_[PascalCase]";
    private static final String OUT_ARGUMENT_FORMAT_KEY = "uipath.check.argumentconventioncheck.outargformat";
    private static final String OUT_ARGUMENT_FORMAT_DEFAULT_VALUE = "out_[PascalCase]";
    private static final String IO_ARGUMENT_FORMAT_KEY = "uipath.check.argumentconventioncheck.ioargformat";
    private static final String IO_ARGUMENT_FORMAT_DEFAULT_VALUE = "io_[PascalCase]";

    public ArgumentConventionCheck(){
        super();
    }

    @Override
    public List<PropertyDefinition> getProperties(){

        return Arrays.asList(
            PropertyDefinition.builder(IN_ARGUMENT_FORMAT_KEY)
                .defaultValue(IN_ARGUMENT_FORMAT_DEFAULT_VALUE)
                //.category(UiPathLanguage.UIPATH_LANGUAGE_NAME)
                .name("InArgument Convention Format")
                .description("Naming convention format for input arguments. Accepts [PascalCase], [camelCase], [UPPERCASE], and [lowercase], case sensitive.")
                .onQualifiers(Qualifiers.PROJECT)
                .build(),
            PropertyDefinition.builder(OUT_ARGUMENT_FORMAT_KEY)
                .defaultValue(OUT_ARGUMENT_FORMAT_DEFAULT_VALUE)
                //.category(UiPathLanguage.UIPATH_LANGUAGE_NAME)
                .name("OutArgument Convention Format")
                .description("Naming convention format for output arguments. Accepts [PascalCase], [camelCase], [UPPERCASE], and [lowercase], case sensitive.")
                .onQualifiers(Qualifiers.PROJECT)
                .build(),
            PropertyDefinition.builder(IO_ARGUMENT_FORMAT_KEY)
                .defaultValue(IO_ARGUMENT_FORMAT_DEFAULT_VALUE)
                //.category(UiPathLanguage.UIPATH_LANGUAGE_NAME)
                .name("InOutArgument Convention Format")
                .description("Naming convention format for input/output arguments. Accepts [PascalCase], [camelCase], [UPPERCASE], and [lowercase], case sensitive.")
                .onQualifiers(Qualifiers.PROJECT)
                .build()
        );
    }

    @Override
    public void execute(Project project, Workflow workflow) {

        Pattern inPattern = Utils.createRegexPatternForConvention(getInArgFormat());
        Pattern outPattern = Utils.createRegexPatternForConvention(getOutArgFormat());
        Pattern inOutPattern = Utils.createRegexPatternForConvention(getInOutArgFormat());

        for(WorkflowArgument arg : workflow.getArguments()) {
            String name = arg.getName();

            switch(arg.getDirection()){
                case In:
                    if(!inPattern.matcher(name).find())
                        reportIssue(workflow, arg);
                    break;
                case Out:
                    if(!outPattern.matcher(name).find())
                        reportIssue(workflow, arg);
                    break;
                case InOut:
                    if(!inOutPattern.matcher(name).find())
                        reportIssue(workflow, arg);
                    break;
            }
        }
    }

    private void reportIssue(Workflow workflow, WorkflowArgument arg){

        String label = arg.getDirection() == Direction.InOut ? "In/Out" : arg.getDirection().toString();

        Issues.report(workflow, getRuleKey(), "Argument '" + arg.getName()
            + "' does not follow naming convention. "
            + label + " arguments should follow the convention '"
            + getArgFormat(arg.getDirection())
            + "'.");
    }

    public String getInArgFormat(){
        return getPropertyValue(IN_ARGUMENT_FORMAT_KEY);
    }

    public String getOutArgFormat(){
        return getPropertyValue(OUT_ARGUMENT_FORMAT_KEY);
    }

    public String getInOutArgFormat(){
        return getPropertyValue(IO_ARGUMENT_FORMAT_KEY);
    }

    public String getArgFormat(Direction direction){
        return direction == Direction.In ? getInArgFormat()
            : direction == Direction.Out ? getOutArgFormat()
            : direction == Direction.InOut ? getInOutArgFormat()
            : null;  // This should never happen.
    }
}
