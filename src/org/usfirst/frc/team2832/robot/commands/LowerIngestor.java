package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.statemachine.SubsystemModule;
import org.usfirst.frc.team2832.robot.statemachine.Subsystems;

public class LowerIngestor extends SubsystemModule {

    private double startTime;
    private double duration;

    public LowerIngestor() {
        this(2d);
    }

    public LowerIngestor(double time) {
        requires(Subsystems.Ingestor);
    	duration = time;
    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        Robot.subsystemHandler.get(Subsystems.Ingestor);
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {

    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + duration;
    }
}
