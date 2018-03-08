package org.usfirst.frc.team2832.robot.commands.auton.lift;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.Robot;

public class MoveLiftTime extends Command {

    private double startTime, duration, speeeed;

    public MoveLiftTime(double duration, double speeeed) {
        requires(Robot.lift);
        this.duration = duration;
        this.speeeed = speeeed;
    }

    @Override
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        Robot.lift.setLiftPower(speeeed);
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
        return Timer.getFPGATimestamp() > startTime + duration;
    }
}
