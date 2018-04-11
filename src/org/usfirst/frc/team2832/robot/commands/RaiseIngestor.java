package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.Robot;

public class RaiseIngestor extends Command {

    private double startTime;
    private double duration;

    public RaiseIngestor() {
    	this(2);
    }

    public RaiseIngestor(double time) {
    	requires(Robot.ingestor);
    	duration = time;
    }
    
    @Override
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        Robot.ingestor.raiseTilt();
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }

    @Override
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + duration;
    }
}
