package org.usfirst.frc.team2832.robot.statemachine;

public interface StateComponent {

    StateComponent getParent();

    boolean hasParent();

    void setParent(StateContainer component);

    String getHierarchy();

    StringBuilder buildHierarchy(StringBuilder builder, int depth);
}
