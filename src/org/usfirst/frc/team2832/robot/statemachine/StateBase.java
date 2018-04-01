package org.usfirst.frc.team2832.robot.statemachine;

import edu.wpi.first.wpilibj.RobotState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class StateBase {

    /*

    ABORT: It's too similar to commands

    Rewrite so that there is a wrapper around the contents of the state, which resemble a command, and store the respective
    variables like start time and timeout, so that they aren't exposed to the child class


     */

    private boolean initialized, interruptable, running, canceled, inDisabled;
    private double startTime, timeout;
    private List<WarriorSubsystem> requirements = new ArrayList<>();

    public void requres(WarriorSubsystem... subsystems) {
        requirements.addAll(Arrays.asList(subsystems));
    }

    /**
     * Called when state is first started
     */
    void initialize() {

    }

    void start() {

    }

    void execute() {

    }

    void end(boolean interrupted) {

    }

    protected abstract boolean isFinished();

    public boolean run() {
        if(!inDisabled && RobotState.isDisabled())
            cancel();

        execute();
        return isFinished();
    }

    public void cancel() {
        canceled = true;
    }
}
