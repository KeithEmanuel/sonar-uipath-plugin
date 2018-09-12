package com.uipath.sonarqube.plugin;

import com.uipath.sonarqube.plugin.hooks.DisplayIssuesInScanner;
import com.uipath.sonarqube.plugin.languages.UiPathLanguage;
import com.uipath.sonarqube.plugin.languages.UiPathQualityProfile;
import com.uipath.sonarqube.plugin.rules.UiPathRulesDefinition;
import org.sonar.api.Plugin;

public class UiPathPlugin implements Plugin {

    public static final String FILE_SUFFIXES_KEY = "sonar.UiPath.file.suffixes";

    @Override
    public void define(Context context){
        context.addExtensions(
            UiPathSensor.class,
            UiPathLanguage.class,
            UiPathQualityProfile.class,
            UiPathRulesDefinition.class,
            DisplayIssuesInScanner.class);
    }
}
