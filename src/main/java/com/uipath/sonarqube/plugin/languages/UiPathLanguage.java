package com.uipath.sonarqube.plugin.languages;

import com.uipath.sonarqube.plugin.UiPathPlugin;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import java.util.ArrayList;
import java.util.List;

public class UiPathLanguage extends AbstractLanguage {
    private static final String[] DEFAULT_SUFFIXES = {".json", ".xaml"};

    public static final String KEY = "uipath";

    public static final String XAML_LANGUAGE_NAME = "UiPath";

    private Configuration configuration;

    public UiPathLanguage(Configuration config){
        super(KEY, XAML_LANGUAGE_NAME);
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
