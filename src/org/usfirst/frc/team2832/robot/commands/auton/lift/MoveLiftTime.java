package org.usfirst.frc.team2832.robot.commands.auton.lift;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.statemachine.SubsystemModule;

public class MoveLiftTime extends SubsystemModule {

    private double startTime, duration, speeeed, delay;

    public MoveLiftTime(double duration, double speeeed) {
        this(duration, speeeed, 0);
    }

    public MoveLiftTime(double duration, double speeeed, double delay) {
        //requires(Robot.lift);
        this.duration = duration;
        this.speeeed = speeeed;
        this.delay = delay;
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
    	if (Timer.getFPGATimestamp() - startTime > delay) {
    		Robot.lift.setLiftPower(speeeed);
    	}
    }

    @Override
    public void end() {
        Robot.lift.setLiftPower(.1);
    }

    @Override
    public void interrupted() {
        Robot.lift.setLiftPower(.1);
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + duration + delay;
    }
}
