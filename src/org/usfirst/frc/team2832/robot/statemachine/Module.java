package org.usfirst.frc.team2832.robot.statemachine;

public interface Module {

    void initialize();

    void start();

    void execute();

    void end();

    void interrupted();

    boolean isFinished() ;
}
