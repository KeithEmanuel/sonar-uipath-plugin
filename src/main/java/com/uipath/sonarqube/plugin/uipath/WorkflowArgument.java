package com.uipath.sonarqube.plugin.uipath;

import com.google.common.base.Splitter;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;
import java.util.List;

public class WorkflowArgument {

    private static final Logger LOG = Loggers.get(WorkflowArgument.class);

    public enum Direction { In, Out, InOut, }

    private String name;
    private Direction direction;
    public String typeNamespace;
    private String type;
    private Namespace xmlNamespace;

    public WorkflowArgument(String name, Direction direction, Namespace namespace, String typeNamespace, String type){
        this.name = name;
        this.direction = direction;
        this.typeNamespace = typeNamespace;
        this.type = type;
        this.xmlNamespace = namespace;
    }

    public static List<WorkflowArgument> LoadFromWorkflow(Workflow workflow){
        ArrayList<WorkflowArgument> args = new ArrayList<>();

        List<Node> nodes = workflow.getXamlDocument().selectNodes("/Activity/x:Members/x:Property");

        LOG.info(String.format("In file '%s'. %s arguments detected.", workflow.getName(), nodes.size()));

        for(Node node : nodes){
            Element element = (Element)node;
            String elementName = element.getName();
            String name = element.attributeValue("Name");
            String rawType = element.attributeValue("Type");

            LOG.debug(elementName +".Name = " + name);
            LOG.debug(elementName +".Type = " + rawType);

            List<String> split = Splitter.onPattern("(\\(|\\)|:)")
                .trimResults()
                .omitEmptyStrings()
                .splitToList(rawType);

            String argDirection = split.get(0);
            //String argTypeNamespace = split.get(1);
            String argType = split.get(2);

            Namespace namespace = element.getNamespace();
            String ns = namespace.getText();
            String typeNamespace = "";

            if(ns.startsWith("clr-namespace:")){
                typeNamespace = Splitter.onPattern("(:|;)")
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(ns)
                    .get(1);
            }

            Direction direction = argDirection.equals("InArgument") ? Direction.In
                : argDirection.equals("OutArgument") ? Direction.Out
                : argDirection.equals("InOutArgument") ? Direction.InOut
                : null;

            if(direction != null){
                args.add(new WorkflowArgument(name, direction, namespace, typeNamespace, argType));
            }
        }

        return args;
    }

    public String getName(){
        return name;
    }

    public String getType() {

        if(!getTypeNamespace().isEmpty()){
            return getTypeNamespace() + "." + getTypeWithoutNamespace();
        }

        return getTypeWithoutNamespace();
    }

    public String getTypeNamespace(){
        return typeNamespace;
    }

    public String getTypeWithoutNamespace(){
        return type;
    }

    public String getNamespacePrefix(){
        return xmlNamespace.getPrefix();
    }

    public String getXmlNamespacedType(){
        return xmlNamespace.getPrefix() + ":" + type;
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
            && other.getType().equals(getType());
    }

    @Override
    public String toString(){
        return String.format("{ name: %s, direction: %s, type: %s }", getName(), getDirection(), getType());
    }

    public boolean matches(String name, String typeWithNamespacePrefix, Direction direction){
        return
            name.equals(getName())
            && typeWithNamespacePrefix.equals(getXmlNamespacedType())
            && direction == getDirection();
    }
}
