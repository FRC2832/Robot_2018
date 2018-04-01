package org.usfirst.frc.team2832.robot.statemachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    private List<WarriorSubsystem> requirements = new ArrayList<>();

    final protected void requires(WarriorSubsystem...subsystems) {
        requirements.addAll(Arrays.asList(subsystems));
    }

    final List<WarriorSubsystem> getRequired() {
        return requirements;
    }

    abstract void initialize();

    abstract void start();

    abstract void execute();

    abstract void end();

    abstract void interrupted();

    abstract boolean isFinished() ;
}
