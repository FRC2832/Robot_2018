package org.usfirst.frc.team2832.robot.statemachine;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.Robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * A wrapper for using commands in statemachines.
 *
 * You need to specify required subsystems externally due to
 * the differing types of Subsystem objects involved.
 */
public class CommandState extends State {

    private Command command;

    public CommandState(Command command) {
        super(command.getClass().getSimpleName());
        this.command = command;
    }

    @Override
    public Module getRunningModuleOfType(Class type) {
        return null;
    }

    @Override
    public Set<Subsystems> getRequirements() {
       return new HashSet<>(super.getRequirements());
    }

    @Override
    void end(boolean interrupted) {
        super.end(interrupted);
        if(interrupted)
            runMethod("interrupted");
        else
            runMethod("end");
    }

    @Override
    void execute() {
        runMethod("execute");
    }

    @Override
    void start() {
        super.start();
        runMethod("start");
    }

    @Override
    void initialize() {
        runMethod("initialize");
    }

    @Override
    protected boolean isFinished() {
        try {
            Method method = Command.class.getDeclaredMethod("isFinished");
            method.setAccessible(true);
            if((boolean)method.invoke(command))
                return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Robot.logger.critical("CommandState", "Couldn't access isFinished() in '" + getName() + "'");
        }
        return super.isFinished();
    }

    private void runMethod(String name) {
        Method method = null;
        try {
            method = Command.class.getDeclaredMethod(name);
            method.setAccessible(true);
            method.invoke(command);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Robot.logger.critical("CommandState", "Couldn't run method '" + name + "' in '" + getName() + "'");
        }
    }
}
