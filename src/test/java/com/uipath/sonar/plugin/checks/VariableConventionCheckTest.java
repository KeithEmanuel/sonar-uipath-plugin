package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.UiPathSensor;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.DocumentException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;


import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class VariableConventionCheckTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void execute() throws IOException, DocumentException {
        testCamelCase();
        testPascalCase();
        testUpperCase();
        testLowerCase();
    }

    private void testCamelCase() throws IOException, DocumentException {
        Project project = Project.FromDirectory(Paths.get("./src/test/java/com/uipath/sonar/plugin/checks/TestProjects/CamelCaseProject"));
        VariableConventionCheck check = new VariableConventionCheck();

        System.out.println(Issues.getIssueCount());
        check.execute(project, project.getWorkflows().get(0));
        System.out.println(Issues.getIssueCount());

    }

    private void testPascalCase(){

    }

    private void testUpperCase(){

    }

    private void testLowerCase(){

    }
}