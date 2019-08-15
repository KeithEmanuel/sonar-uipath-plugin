package com.uipath.sonar.plugin.uipath;

import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WorkflowArgument represents an argument belonging to a workflow.
 */
public class WorkflowArgument {

    private static final Logger LOG = Loggers.get(WorkflowArgument.class);

    public enum Direction { In, Out, InOut, }

    private String rawXml;
    private String name;
    private Direction direction;
    private String rawType;
    private String rawArgType;

    public WorkflowArgument(String rawXml, String name, String rawType, String rawArgType, Direction direction){
        this.rawXml = rawXml;
        this.name = name;
        this.rawType = rawType;
        this.rawArgType = rawArgType;
        this.direction = direction;
    }

    public WorkflowArgument(String name, String type, Direction direction){
        this.name = name;
        this.rawArgType = type;
        this.direction = direction;
    }

    public static ArrayList<WorkflowArgument> LoadFromWorkflow(Workflow workflow){

        LOG.debug("Loading workflow " + workflow.getName());

        ArrayList<WorkflowArgument> args = new ArrayList<>();

        List<Node> nodes = workflow.getXamlDocument().selectNodes("xa:Activity/x:Members/x:Property");

        LOG.debug(nodes.size() + " elements found.");

        for(Node node : nodes){
            Element element = (Element)node;

            Optional<WorkflowArgument> arg = fromElement(element);
            arg.ifPresent(args::add);
        }

        return args;
    }

    public static Optional<WorkflowArgument> fromElement(Element element){
        try{
            String rawXml = element.asXML();
            String name = element.attributeValue("Name");
            String rawType = element.attributeValue("Type");

            Pattern pattern = Pattern.compile("^(?<direction>In|Out|InOut)Argument\\((?<type>.+)\\)$", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(rawType);

            if (matcher.find()) {
                String rawArgType = matcher.group("type");
                Direction direction = Direction.valueOf(matcher.group("direction"));

                return Optional.of(new WorkflowArgument(rawXml, name, rawType, rawArgType, direction));
            }

            return Optional.empty();
        }
        catch(Exception e){
            LOG.error("Could not parse WorkflowArgument. RawXML: " + element.asXML(), e);
            return Optional.empty();
        }
    }

    public String getRawXml(){
        return rawXml;
    }

    public String getName(){
        return name;
    }

    public String getRawType(){
        return rawType;
    }

    public String getArgType(){
        return rawArgType;
    }

    public Direction getDirection(){
        return direction;
    }

    @Override
    public boolean equals(Object o){
        WorkflowArgument other = (WorkflowArgument)o;

        return
            other.getName().equals(getName())
            && other.getDirection().equals(getDirection())
            && other.getArgType().equals(getArgType());
    }

    @Override
    public String toString(){
        return String.format("{ name: %s, direction: %s, type: %s }", getName(), getDirection(), getRawType());
    }

    /*public boolean matches(String name, String rawArgType, Direction direction){

        return
            name.equals(getName())
            && rawArgType.equals(getArgType())
            && direction == getDirection();
    }*/
}
