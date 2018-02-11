package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import edu.wpi.first.wpilibj.command.PIDCommand;
import org.usfirst.frc.team2832.robot.Robot;

public class TurnDegreesPid extends PIDCommand {

    private static final double P = 0.1;
    private static final double I = 0.00;
    private static final double D = 0.15;

    private double degrees, target;

    public TurnDegreesPid(double degrees) {
        super(P, I, D);
        this.degrees = degrees;
    }

    @Override
    protected void initialize() {
        this.target = Robot.driveTrain.getPigeonYaw() + degrees;
    }

    @Override
    protected double returnPIDInput() {
        return Robot.driveTrain.getPigeonYaw();
    }

    @Override
    protected void usePIDOutput(double output) {
        Robot.driveTrain.arcadeDrive(0, output);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(target - Robot.driveTrain.getPigeonYaw()) < 1;
    }

    @Override
    protected void interrupted() {
        Robot.driveTrain.arcadeDrive(0, 0);
    }

    @Override
    protected void end() {
        Robot.driveTrain.arcadeDrive(0, 0);
    }
}
