package com.uipath.sonarqube.plugin.languages;

import com.uipath.sonarqube.plugin.AbstractCheck;
import com.uipath.sonarqube.plugin.CheckRepository;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public final class UiPathQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context){
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("UiPath rules", UiPathLanguage.KEY);

        profile.setDefault(true);

        // Add all rules to the default profile.
        for(AbstractCheck check : CheckRepository.getAllChecks()){
            profile.activateRule(CheckRepository.REPOSITORY_KEY, check.getRuleKeyString());
        }

        profile.done();
    }
}
