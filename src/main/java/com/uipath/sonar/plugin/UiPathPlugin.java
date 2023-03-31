package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.languages.UiPathLanguage;
import com.uipath.sonar.plugin.languages.UiPathQualityProfile;
import com.uipath.sonar.plugin.rules.UiPathRulesDefinition;
import com.uipath.sonar.plugin.settings.UiPathLanguageProperties;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;

/**
 * UiPathPlugin simply defines what classes are used for this plugin.
 * UiPathSensor is the main entry point for scanning UiPath projects.
 */
public class UiPathPlugin implements Plugin {

    public static final String FILE_SUFFIXES_KEY = "sonar.UiPath.file.suffixes";

    @Override
    public void define(Context context){
        context.addExtensions(
            UiPathSensor.class,
            UiPathLanguage.class,
            UiPathQualityProfile.class,
            UiPathRulesDefinition.class);

        for(PropertyDefinition prop : UiPathLanguageProperties.getProperties()){
            context.addExtension(prop);
        }

        for(PropertyDefinition prop : CheckRepository.getAllProperties()){
            context.addExtension(prop);
        }
    }
}
