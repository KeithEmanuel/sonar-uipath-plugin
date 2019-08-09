package com.uipath.sonar.plugin.uipath;

import com.uipath.sonar.plugin.testprojects.LoadProject;
import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class WorkflowTest {

    private Project argsAndVars;
    private Workflow allCamelCase;

    @Before
    public void setUp() throws Exception {
        argsAndVars = LoadProject.withPath("ArgsAndVars");
        allCamelCase = argsAndVars.getWorkflowNamed("allCamelCase").get();
    }

    @Test
    public void getArguments() {
        assertEquals(3, allCamelCase.getArguments().size());
    }

    @Test
    public void getXamlDocument() {
        Document doc = allCamelCase.getXamlDocument();
        assertNotNull(doc);
        assertNotNull(doc.getRootElement());
    }

    @Test
    public void getProject() {
        assertEquals(argsAndVars, allCamelCase.getProject());
    }

    @Test
    public void getPath() {
        System.out.println(allCamelCase.getPath());
        // TODO change from src/../allCamelCase.xaml to ./allCamelCase.xaml
    }

    @Test
    public void getName() {
        System.out.println(allCamelCase.getName());
        assertEquals("allCamelCase", allCamelCase.getName());
    }

    @Test
    public void getFileName() {
        assertEquals("allCamelCase.xaml", allCamelCase.getFileName());
    }
}