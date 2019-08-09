package com.uipath.sonar.plugin.uipath;

import com.uipath.sonar.plugin.testprojects.LoadProject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;
import static org.junit.Assert.*;

public class ProjectTest {

    private Project argsAndVars;

    @Before
    public void setUp() throws Exception {
        argsAndVars = LoadProject.withPath("ArgsAndVars");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getWorkflows() {
        Project argsAndVars = LoadProject.withPath("ArgsAndVars");
        assertEquals(5, argsAndVars.getWorkflows().size());
    }

    @Test
    public void getProjectJson() {
        ProjectJson projectJson = argsAndVars.getProjectJson();
        assertEquals(projectJson.name, "ArgsAndVars");
        assertNotNull(projectJson.main);
    }

    @Test
    public void getWorkflowNamed() {

        Optional<Workflow> optionalWorkflow = argsAndVars.getWorkflowNamed("allCamelCase");
        assertTrue(optionalWorkflow.isPresent());
    }

    @Test
    public void getName() {
        assertTrue(argsAndVars.getName().equals("ArgsAndVars"));
    }

    @Test
    public void getDirectory() {
        File dir = argsAndVars.getDirectory();
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }

    @Test
    public void getWorkflowWithPath() {
        Optional<Workflow> optionalWorkflow = argsAndVars.getWorkflowWithPath("allCamelCase.xaml");
        assertTrue(optionalWorkflow.isPresent());

        // TODO: Make this work
        //optionalWorkflow = argsAndVars.getWorkflowWithPath("allCamelCase.xaml");
        //assertTrue(optionalWorkflow.isPresent());
    }
}