package com.uipath.sonarqube.plugin.uipath;

public class WorkflowArgument {

    public enum Direction { In, Out, InOut, }

    private String name;
    private Class type;
    private Direction direction;

    public WorkflowArgument(String name, Class type, Direction direction){
        this.name = name;
        this.type = type;
        this.direction = direction;
    }

    public WorkflowArgument(String name, String type, Direction direction) throws ClassNotFoundException {
        this(name, Class.forName(type), direction);
    }

    private String getName(){
        return name;
    }

    public Class getType() {
        return type;
    }

    public Direction getDirection(){
        return direction;
    }
}
