package org.usfirst.frc.team2832.robot.commands.auton.lift;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.Robot;

public class MoveLiftTime extends Command {

    private double startTime, duration, speeeed, delay;

    public MoveLiftTime(double duration, double speeeed) {
        this(duration, speeeed, 0);
    }

    public MoveLiftTime(double duration, double speeeed, double delay) {
        requires(Robot.lift);
        this.duration = duration;
        this.speeeed = speeeed;
        this.delay = delay;
    }
    
    @Override
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
    	if (Timer.getFPGATimestamp() - startTime > delay) {
    		Robot.lift.setLiftPower(speeeed);
    	}
    }

    @Override
    protected void end() {
        Robot.lift.setLiftPower(.1);
    }

    @Override
    protected void interrupted() {
        Robot.lift.setLiftPower(.1);
    }

    @Override
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + duration + delay;
    }
}
