package org.usfirst.frc.team2832.robot.statemachine;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;

import java.util.*;

public abstract class State<T extends State> {

    private boolean interruptable, running, canceled, inDisableable, initialized;
    private double startTime, timeout;
    private Set<Subsystems> requirements = new HashSet<>();
    private String name = "";

    public State(String name) {
        this.name = name;
    }

    public State() {
        this.name = getClass().getSimpleName();
    }

    public State<T> requires(Subsystems... subsystems) {
        requirements.addAll(Arrays.asList(subsystems));
        return this;
    }

    public abstract Module getRunningModuleOfType(Class clazz);

    public Set<Subsystems> getRequirements() {
        return requirements;
    }

    public State<T> setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    abstract void initialize();

    void start() {
        if(!initialized)
            initialize();
        startTime = Timer.getFPGATimestamp();
        running = true;
    }

    double getRunningTime() {
        return Timer.getFPGATimestamp() - startTime;
    }

    abstract void execute();

    void end(boolean interrupted) {
        running = false;
    }

    public boolean isInterruptable() {
        return interruptable;
    }

    public State<T> setInDisableable(boolean inDisableable) {
        this.inDisableable = inDisableable;
        return this;
    }

    public State<T> setInterruptable(boolean interruptable) {
        this.interruptable = interruptable;
        return this;
    }

    public State<T> setTimeout(double timeout) {
        this.timeout = timeout;
        return this;
    }

    protected boolean isFinished() {
        //System.out.println(this.getClass().getSimpleName() + ": " + timeout + " : " + Timer.getFPGATimestamp() + " : " + startTime);
        return timeout > 0 && Timer.getFPGATimestamp() > startTime + timeout;
    }

    public boolean run() {
        if(!inDisableable && RobotState.isDisabled())
            cancel();
        if(canceled)
            return true;
        execute();
        return isFinished();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel() {
        canceled = true;
    }
}
