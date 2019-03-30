package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.UiPathSensor;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
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
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class VariableConventionCheckTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void execute() throws IOException {
        testCamelCase();
        testPascalCase();
        testUpperCase();
        testLowerCase();
    }

    private void testCamelCase() throws IOException {
        VariableConventionCheck check = new VariableConventionCheck();
        Project project = loadCamelCaseProject();

        for(Workflow workflow : project.getWorkflows()){
            check.execute(project, workflow);
        }

        assertEquals(((SensorContextTester)project.getSensorContext()).allIssues().size(), 3);
    }

    private void testPascalCase(){

    }

    private void testUpperCase(){

    }

    private void testLowerCase(){

    }
    private Project loadCamelCaseProject() throws IOException {
        File baseDir = folder.newFolder();
        SensorContextTester tester = SensorContextTester.create(baseDir);


        InputFile inputFile = new TestInputFileBuilder("filename", baseDir, new File(baseDir, "Main.xaml"))
            .setLanguage("uipath")
            .setContents(xaml)
            .build();

        InputFile inputFile2 = new TestInputFileBuilder("filename2", baseDir, new File(baseDir, "project.json"))
            .setLanguage("uipath")
            .setContents(project)
            .build();

        DefaultFileSystem dfs = new DefaultFileSystem(baseDir);
        dfs.add(inputFile);
        dfs.add(inputFile2);
        tester.setFileSystem(dfs);
        return Project.FromSensorContext(new UiPathSensor(), tester);
    }

    private Project noloadCamelCaseProject() throws IOException {
        File file = Paths.get("C:\\Users\\Keith Emanuel\\Repositories\\UiPathSonarQubePlugin\\src\\test\\java\\com\\uipath\\sonar\\plugin\\checks\\TestProjects\\CamelCaseProject").toFile();
        SensorContextTester sensorContextTester = SensorContextTester.create(file);
        sensorContextTester.setFileSystem(new DefaultFileSystem(file));
        //SensorContextTester sensorContextTester = SensorContextTester.create(Paths.get("./TestProjects/CamelCaseProject"));
        return Project.FromSensorContext(new UiPathSensor(), sensorContextTester);

    }

    String xaml = "<Activity mc:Ignorable=\"sap sap2010 sads\" x:Class=\"Main\" mva:VisualBasic.Settings=\"{x:Null}\"\n" +
        " xmlns=\"http://schemas.microsoft.com/netfx/2009/xaml/activities\"\n" +
        " xmlns:av=\"http://schemas.microsoft.com/winfx/2006/xaml/presentation\"\n" +
        " xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\"\n" +
        " xmlns:mva=\"clr-namespace:Microsoft.VisualBasic.Activities;assembly=System.Activities\"\n" +
        " xmlns:sads=\"http://schemas.microsoft.com/netfx/2010/xaml/activities/debugger\"\n" +
        " xmlns:sap=\"http://schemas.microsoft.com/netfx/2009/xaml/activities/presentation\"\n" +
        " xmlns:sap2010=\"http://schemas.microsoft.com/netfx/2010/xaml/activities/presentation\"\n" +
        " xmlns:scg=\"clr-namespace:System.Collections.Generic;assembly=mscorlib\"\n" +
        " xmlns:sco=\"clr-namespace:System.Collections.ObjectModel;assembly=mscorlib\"\n" +
        " xmlns:ui=\"http://schemas.uipath.com/workflow/activities\"\n" +
        " xmlns:x=\"http://schemas.microsoft.com/winfx/2006/xaml\">\n" +
        "  <TextExpression.NamespacesForImplementation>\n" +
        "    <sco:Collection x:TypeArguments=\"x:String\">\n" +
        "      <x:String>System.Activities</x:String>\n" +
        "      <x:String>System.Activities.Statements</x:String>\n" +
        "      <x:String>System.Activities.Expressions</x:String>\n" +
        "      <x:String>System.Activities.Validation</x:String>\n" +
        "      <x:String>System.Activities.XamlIntegration</x:String>\n" +
        "      <x:String>Microsoft.VisualBasic</x:String>\n" +
        "      <x:String>Microsoft.VisualBasic.Activities</x:String>\n" +
        "      <x:String>System</x:String>\n" +
        "      <x:String>System.Collections</x:String>\n" +
        "      <x:String>System.Collections.Generic</x:String>\n" +
        "      <x:String>System.Data</x:String>\n" +
        "      <x:String>System.Diagnostics</x:String>\n" +
        "      <x:String>System.Drawing</x:String>\n" +
        "      <x:String>System.IO</x:String>\n" +
        "      <x:String>System.Linq</x:String>\n" +
        "      <x:String>System.Net.Mail</x:String>\n" +
        "      <x:String>System.Xml</x:String>\n" +
        "      <x:String>System.Xml.Linq</x:String>\n" +
        "      <x:String>UiPath.Core</x:String>\n" +
        "      <x:String>UiPath.Core.Activities</x:String>\n" +
        "      <x:String>System.Windows.Markup</x:String>\n" +
        "    </sco:Collection>\n" +
        "  </TextExpression.NamespacesForImplementation>\n" +
        "  <TextExpression.ReferencesForImplementation>\n" +
        "    <sco:Collection x:TypeArguments=\"AssemblyReference\">\n" +
        "      <AssemblyReference>System.Activities</AssemblyReference>\n" +
        "      <AssemblyReference>Microsoft.VisualBasic</AssemblyReference>\n" +
        "      <AssemblyReference>mscorlib</AssemblyReference>\n" +
        "      <AssemblyReference>System.Data</AssemblyReference>\n" +
        "      <AssemblyReference>System</AssemblyReference>\n" +
        "      <AssemblyReference>System.Drawing</AssemblyReference>\n" +
        "      <AssemblyReference>System.Core</AssemblyReference>\n" +
        "      <AssemblyReference>System.Xml</AssemblyReference>\n" +
        "      <AssemblyReference>System.Xml.Linq</AssemblyReference>\n" +
        "      <AssemblyReference>WindowsBase</AssemblyReference>\n" +
        "      <AssemblyReference>System.Xaml</AssemblyReference>\n" +
        "      <AssemblyReference>PresentationFramework</AssemblyReference>\n" +
        "      <AssemblyReference>PresentationCore</AssemblyReference>\n" +
        "      <AssemblyReference>UiPath.UiAutomation.Activities</AssemblyReference>\n" +
        "      <AssemblyReference>UiPath.System.Activities</AssemblyReference>\n" +
        "    </sco:Collection>\n" +
        "  </TextExpression.ReferencesForImplementation>\n" +
        "  <Flowchart>\n" +
        "    <Flowchart.Variables>\n" +
        "      <Variable x:TypeArguments=\"x:String\" Name=\"var\" />\n" +
        "      <Variable x:TypeArguments=\"x:String\" Name=\"camelCaseVar\" />\n" +
        "    </Flowchart.Variables>\n" +
        "    <Flowchart.StartNode>\n" +
        "      <x:Reference>__ReferenceID0</x:Reference>\n" +
        "    </Flowchart.StartNode>\n" +
        "    <FlowStep x:Name=\"__ReferenceID0\">\n" +
        "      <ui:InvokeWorkflowFile ContinueOnError=\"{x:Null}\" DisplayName=\"Invoke Workflow File\" UnSafe=\"False\" WorkflowFileName=\"InvokeMe.xaml\">\n" +
        "        <ui:InvokeWorkflowFile.Arguments>\n" +
        "          <scg:Dictionary x:TypeArguments=\"x:String, Argument\" />\n" +
        "        </ui:InvokeWorkflowFile.Arguments>\n" +
        "        <sap2010:WorkflowViewState.IdRef>InvokeWorkflowFile_1</sap2010:WorkflowViewState.IdRef>\n" +
        "      </ui:InvokeWorkflowFile>\n" +
        "      <sap2010:WorkflowViewState.IdRef>FlowStep_1</sap2010:WorkflowViewState.IdRef>\n" +
        "    </FlowStep>\n" +
        "    <sap2010:WorkflowViewState.IdRef>Flowchart_1</sap2010:WorkflowViewState.IdRef>\n" +
        "    <sads:DebugSymbol.Symbol>dz5DOlxVc2Vyc1xLZWl0aCBFbWFudWVsXERvY3VtZW50c1xVaVBhdGhcUHdDXFVuaXRUZXN0XE1haW4ueGFtbAM5A0wPAgEBQgdHHwIBAkJ8QosBAgED</sads:DebugSymbol.Symbol>\n" +
        "  </Flowchart>\n" +
        "  <sap2010:WorkflowViewState.IdRef>Main_1</sap2010:WorkflowViewState.IdRef>\n" +
        "  <sap2010:WorkflowViewState.ViewStateManager>\n" +
        "    <sap2010:ViewStateManager>\n" +
        "      <sap2010:ViewStateData Id=\"InvokeWorkflowFile_1\" sap:VirtualizedContainerService.HintSize=\"200,52.8\">\n" +
        "        <sap:WorkflowViewStateService.ViewState>\n" +
        "          <scg:Dictionary x:TypeArguments=\"x:String, x:Object\">\n" +
        "            <x:Boolean x:Key=\"IsExpanded\">True</x:Boolean>\n" +
        "          </scg:Dictionary>\n" +
        "        </sap:WorkflowViewStateService.ViewState>\n" +
        "      </sap2010:ViewStateData>\n" +
        "      <sap2010:ViewStateData Id=\"FlowStep_1\">\n" +
        "        <sap:WorkflowViewStateService.ViewState>\n" +
        "          <scg:Dictionary x:TypeArguments=\"x:String, x:Object\">\n" +
        "            <av:Point x:Key=\"ShapeLocation\">200,127.7</av:Point>\n" +
        "            <av:Size x:Key=\"ShapeSize\">200,52.8</av:Size>\n" +
        "          </scg:Dictionary>\n" +
        "        </sap:WorkflowViewStateService.ViewState>\n" +
        "      </sap2010:ViewStateData>\n" +
        "      <sap2010:ViewStateData Id=\"Flowchart_1\" sap:VirtualizedContainerService.HintSize=\"614.4,636.8\">\n" +
        "        <sap:WorkflowViewStateService.ViewState>\n" +
        "          <scg:Dictionary x:TypeArguments=\"x:String, x:Object\">\n" +
        "            <x:Boolean x:Key=\"IsExpanded\">False</x:Boolean>\n" +
        "            <av:Point x:Key=\"ShapeLocation\">270,2.5</av:Point>\n" +
        "            <av:Size x:Key=\"ShapeSize\">60,75.2</av:Size>\n" +
        "            <av:PointCollection x:Key=\"ConnectorLocation\">300,77.7 300,127.7</av:PointCollection>\n" +
        "          </scg:Dictionary>\n" +
        "        </sap:WorkflowViewStateService.ViewState>\n" +
        "      </sap2010:ViewStateData>\n" +
        "      <sap2010:ViewStateData Id=\"Main_1\" sap:VirtualizedContainerService.HintSize=\"654.4,716.8\" />\n" +
        "    </sap2010:ViewStateManager>\n" +
        "  </sap2010:WorkflowViewState.ViewStateManager>\n" +
        "</Activity>";

    String project = "{\n" +
        "  \"name\": \"UnitTest\",\n" +
        "  \"description\": \"Unit test project for SonarQube plugin\",\n" +
        "  \"main\": \"Main.xaml\",\n" +
        "  \"dependencies\": {\n" +
        "    \"UiPath.UIAutomation.Activities\": \"[18.3.6877.28298]\",\n" +
        "    \"UiPath.System.Activities\": \"[18.3.6877.28276]\",\n" +
        "    \"UiPath.Excel.Activities\": \"[2.4.6863.30657]\",\n" +
        "    \"UiPath.Mail.Activities\": \"[1.2.6863.29868]\"\n" +
        "  },\n" +
        "  \"schemaVersion\": \"3.2\",\n" +
        "  \"studioVersion\": \"18.3.2.0\",\n" +
        "  \"projectVersion\": \"1.0.0.0\",\n" +
        "  \"runtimeOptions\": {\n" +
        "    \"autoDispose\": false,\n" +
        "    \"excludedLoggedData\": [\n" +
        "      \"Private:*\",\n" +
        "      \"*password*\"\n" +
        "    ]\n" +
        "  },\n" +
        "  \"projectType\": \"Workflow\",\n" +
        "  \"libraryOptions\": {\n" +
        "    \"includeOriginalXaml\": false,\n" +
        "    \"privateWorkflows\": []\n" +
        "  }\n" +
        "}";
}