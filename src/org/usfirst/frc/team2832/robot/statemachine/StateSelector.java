package org.usfirst.frc.team2832.robot.statemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public abstract class StateSelector extends State {

    private State selected;
    private Supplier supplier;

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
