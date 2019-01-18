package com.uipath.sonar.plugin.settings;

import com.uipath.sonar.plugin.languages.UiPathLanguage;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import java.util.List;
import java.util.Arrays;

public class UiPathLanguageProperties {

    public static final String CATEGORY = "UiPath";

    public static final String PROJECT_FILE_PREDICATE_KEY = "uipath.project.file.predicate";
    public static final String PROJECT_FILE_PREDICATE_DEFAULT_VALUE = "**/project.json";

    public static final String WORKFLOW_FILE_PREDICATE_KEY = "uipath.workflow.file.predicate";
    public static final String WORKFLOW_FILE_PREDICATE_DEFAULT_VALUE = "**/*.xaml";

    private UiPathLanguageProperties() {}

    public static List<PropertyDefinition> getProperties() {

        return Arrays.asList(
            PropertyDefinition.builder(PROJECT_FILE_PREDICATE_KEY)
                .defaultValue(PROJECT_FILE_PREDICATE_DEFAULT_VALUE)
                //.category(UiPathLanguage.UIPATH_LANGUAGE_NAME)
                .name("Project File Predicate")
                .description("Predicate pattern used to identify project files.")
                .build(),
            PropertyDefinition.builder(WORKFLOW_FILE_PREDICATE_KEY)
                .defaultValue(WORKFLOW_FILE_PREDICATE_DEFAULT_VALUE)
                //.category(UiPathLanguage.UIPATH_LANGUAGE_NAME)
                .name("Workflow File Predicate")
                .description("Predicate pattern used to identify workflow files.")
                .build()
        );
    }
}
