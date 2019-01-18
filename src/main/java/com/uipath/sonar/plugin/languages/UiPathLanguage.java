package com.uipath.sonar.plugin.languages;

import com.uipath.sonar.plugin.UiPathPlugin;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import java.util.ArrayList;
import java.util.List;

/**
 * UiPathLanguage defines "UiPath" as a language in SonarQube, so that rules can be created on the language.
 * This class doesn't do much other than that.
 */
public class UiPathLanguage extends AbstractLanguage {
    private static final String[] DEFAULT_SUFFIXES = {".json", ".xaml"};

    public static final String KEY = "uipath";

    public static final String UIPATH_LANGUAGE_NAME = "UiPath";

    private Configuration configuration;

    public UiPathLanguage(Configuration config){
        super(KEY, UIPATH_LANGUAGE_NAME);
        this.configuration = config;
    }

    @Override
    public String[] getFileSuffixes(){

        String[] suffixes = filterEmptyStrings(configuration.getStringArray(UiPathPlugin.FILE_SUFFIXES_KEY));
        if(suffixes.length == 0){
            suffixes = DEFAULT_SUFFIXES;
        }

        return suffixes;
    }

    private static String[] filterEmptyStrings(String[] stringArray){
        List<String> nonEmptyString = new ArrayList<>();
        for(String string : stringArray){
            if(StringUtils.isNotBlank(string.trim())){
                nonEmptyString.add(string.trim());
            }
        }

        return nonEmptyString.toArray(new String[nonEmptyString.size()]);
    }
}
