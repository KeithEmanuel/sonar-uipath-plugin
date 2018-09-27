# Creating a new check

Let look at creating a new check for UiPath.
Browse through some existing checks to get an idea of what they look like.
They are located at [/src/main/java/com/uipath/sonar/plugin/checks](https://github.com/KeithEmanuel/sonar-uipath-plugin/tree/master/src/main/java/com/uipath/sonar/plugin/checks)

### Extension points

To create a check, you will have to create a new class that extends either **[AbstractProjectCheck](https://github.com/KeithEmanuel/sonar-uipath-plugin/blob/master/src/main/java/com/uipath/sonar/plugin/AbstractProjectCheck.java)** or **[AbstractWorkflowCheck](https://github.com/KeithEmanuel/sonar-uipath-plugin/blob/master/src/main/java/com/uipath/sonar/plugin/AbstractWorkflowCheck.java)**.
**AbstractProjectChecks** are executed once per Project. They are not limited to only looking at project.json, but the issues they  report will be on the project.json file.
**AbstractWorkflowChecks** are executed once on each workflow. Issues will be made on the XAML files.

Both abstract classes have an abstract **execute** function, but the function signatures differ slightly:

*AbstractProjectCheck.cs*
`public abstract void execute(Project project);`
AbstractWorkflowCheck.cs
`public abstract void execute(Project project, Workflow workflow);`

Issues are reported on the Project and Workflow objects. The report issue function looks like
`public void reportIssue(RuleKey ruleKey, String message);`

**AbstractCheck**, the base class of **AbstractProjectCheck** and **AbstractWorkflowCheck** provides a **getRuleKey** function. Let's see what a check that reports an issue on every workflow file looks like.


    @Rule(
        key = "IssuePerWorkflowCheck",
        name = "Issue Per Workflow Check",
        description =  "Create an issue on every workflow for no reason.",
        status = "BETA",
        priority = Priority.MINOR,
        tags = {"workflow"}
    )
    public class IssuePerWorkflowCheck extends AbstractWorkflowCheck {

        public IssuePerWorkflowCheck(){
            super();
        }

        @Override
        public void execute(Project project, Workflow workflow) {
            workflow.reportIssue(getRuleKey(), "Pointless issue!");
        }
    }

One last thing that needs to be done before SonarQube will see this check is to register it with the **[CheckRepository](https://github.com/KeithEmanuel/sonar-uipath-plugin/blob/master/src/main/java/com/uipath/sonar/plugin/CheckRepository.java)**. Open up the CheckRepository.cs in your editor. Find the function **getWorkflowChecks** and add a new instance of your class to the List that is returned.

    // Add new WorkflowChecks here!
    public static List<AbstractWorkflowCheck> getWorkflowChecks(){
        return Arrays.asList(
            // ...
            new IssuePerWorkflowCheck()
        );
    }

Implementing a AbstractProjectCheck is very similar.

Run **mvn clean package** at the command line in the project root to build the jar. Copy the sonar-uipath-plugin-*.jar that was created in *%PROJECT_ROOT%*/target to *%SONAR_ROOT%*/extensions/plugins, then restart SonarQube Server, and run the scanner on a project. you should see that the issues are created.

### Actually checking stuff

Well that was cool, but not very useful. For most checks, your are going to have to traverse the XAML document, examine some nodes/elements, and determines if there is an issue.
This plugin uses [dom4j](https://dom4j.github.io/) to help explore the XAML files. Workflow objects have a **getXamlDocument** method, which returns a DOM representation of the XAML file. The easiest way to traverse the elements is probably using [XPath queries](https://www.w3schools.com/xml/xpath_syntax.asp).

For example, if you wanted a list of every InvokeWorkflowFile element, you could call:
`workflow.getXamlDocument().selectNodes("//ui:InvokeWorkflowFile");`
which would return a list of [Node](https://dom4j.github.io/javadoc/2.0.1/org/dom4j/Node.html)s.

If the node is an XML element, you will need to cast it to an [Element](https://dom4j.github.io/javadoc/2.1.0/org/dom4j/Element.html) to do useful things with it.
Here's a short snippet that grabs the arguments out of a workflow:

    List<Node> nodes = workflow.getXamlDocument().selectNodes("/Activity/x:Members/x:Property");

    for(Node node : nodes) {
        Element element = (Element)node;
        String elementName = element.getName();
        String argName = element.attributeValue("Name");
        String rawType = element.attributeValue("Type");
    }

## Best Practices

- **Keep the scope of your rules/checks small**
This will let allow them to be more customizable. It is fine if a rule checks multiple things, but be aware that they must be enabled or disabled as a group, and they share the same metadata, like priority, tags, etc...
- **Extend the uipath classes when appropriate**
If you end up writing a function that may be useful in future checks, try to added in a place where it can be shared, like the Project or Workflow class. Feel free to create new classes when you feel that it makes sense.

## Remarks

- At the moment, every check belongs to the default UiPath quality profile, defined in *com.uipath.sonar.plugin.languages.UiPathQualityProfile*. This may change in the future, and there might be an additional step to register a check to the default profile.
