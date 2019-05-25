package com.uipath.sonar.plugin;

import org.sonar.api.batch.fs.InputFile;

public interface HasInputFile {
    boolean hasInputFile();
    InputFile getInputFile();
}
