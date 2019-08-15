package com.uipath.sonar.plugin.uipath;

import com.google.common.io.Files;
import com.uipath.sonar.plugin.HasInputFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.sonar.api.batch.fs.InputFile;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Workflow represents a XAML workflow file.
 * This class wraps an InputFile object, which represents the underlying XAML file. Issues created using reportIssue
 * will be created on this underlying inputFile.
 *
 * This getXamlDocument method provides a DOM representation of the underlying XAML file. It uses the dom4j library.
 * Refer to dom4j documentation for usage. Most use cases should be easily fulfilled by a simple XPATH query.
 */
public class Workflow implements HasInputFile {

    private File file;
    //private Path relativePath;
    private Project project;
    private InputFile inputFile;
    private Document xamlDocument;
    private ArrayList workflowArguments;

    public Workflow(Project project, File file) throws DocumentException {
        this.project = project;
        this.file = file;
        //this.relativePath = file.toURI()

        // Create the xaml document
        SAXReader saxReader = new SAXReader();
        xamlDocument = saxReader.read(file);
        xamlDocument.getRootElement().addNamespace("xa","http://schemas.microsoft.com/netfx/2009/xaml/activities");

        // Parse and set the workflow arguments
        workflowArguments = WorkflowArgument.LoadFromWorkflow(this);

    }

    public Workflow(Project project, File file, InputFile inputFile) throws DocumentException {
        this(project, file);
        this.inputFile = inputFile;
    }

    public List<WorkflowArgument> getArguments(){
        return workflowArguments;
    }

    public Document getXamlDocument(){
        return xamlDocument;
    }

    public Project getProject(){
        return project;
    }

    public File getFile() { return file; }

    public Path getPath(){
        return file.toPath();
    }

    public String getName(){
        return Files.getNameWithoutExtension(file.getName());
    }

    public String getFileName(){
        return file.getName();
    }

    public boolean hasInputFile(){
        return inputFile != null;
    }

    public InputFile getInputFile(){
        return inputFile;
    }

    public void setInputFile(InputFile inputFile){ this.inputFile = inputFile; }
}
