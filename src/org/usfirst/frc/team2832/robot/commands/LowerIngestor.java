package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.Robot;

public class LowerIngestor extends Command {

    private double startTime;
    private double duration;

    public LowerIngestor() {
        requires(Robot.ingestor);
        duration = 2d;
    }

    public LowerIngestor(double time) {
    	requires(Robot.ingestor);
    	duration = time;
    }
    
    @Override
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        Robot.ingestor.lowerTilt();
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
