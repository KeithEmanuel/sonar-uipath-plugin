package com.uipath.sonar.plugin.languages;

import com.uipath.sonar.plugin.AbstractCheck;
import com.uipath.sonar.plugin.CheckRepository;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

/**
 * UiPathQualityProfile defines the rules included in the default quality profile.
 * Right now, it just includes all available rules. This may need to be adjusted in the future.
 */
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
