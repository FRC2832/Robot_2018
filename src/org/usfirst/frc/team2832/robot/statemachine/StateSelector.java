package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;
import java.util.function.Supplier;

public abstract class StateSelector extends State implements StateContainer {

    private State selected;
    private Supplier supplier;

    protected StateSelector(Collection<State> states) {
        for(State state: states)
            state.setParent(this);
    }

    @Override
    void end(boolean interrupted) {
        super.end(interrupted);
        selected.end(interrupted);
    }

    @Override
    void execute() {
        selected.execute();
    }

    abstract State getSelected();

    @Override
    public Module getRunningModuleOfType(Class clazz) {
        return selected.getRunningModuleOfType(clazz);
    }

    @Override
    void start() {
        selected = getSelected();
        super.start();
        selected.start();
    }

    @Override
    void initialize() {
        selected.initialize();
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() || selected.isFinished();
    }
}
