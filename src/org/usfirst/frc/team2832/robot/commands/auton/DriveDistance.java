package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.ENCODER;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward desired distance
 */
public class DriveDistance extends Command {

	private double start, distance, speeed;

	public DriveDistance(double speeed, double distance) {
        requires(Robot.driveTrain);
        this.speeed = speeed;
        this.distance = distance;
    }

    protected void initialize() {
    	start = Math.abs(Robot.driveTrain.getEncoderPosition(ENCODER.BOTH));
    }

    protected void execute() {
    	Robot.driveTrain.arcadeDrive(speeed, 0);
    }

    protected boolean isFinished() {
    	System.out.println(start + ": " + Math.abs(Robot.driveTrain.getEncoderPosition(ENCODER.BOTH)));
        return Math.abs(Robot.driveTrain.getEncoderPosition(ENCODER.BOTH)) > start + distance;
    }

    protected void end() {
    	Robot.driveTrain.arcadeDrive(0, 0);
    }

    protected void interrupted() {
    	Robot.driveTrain.arcadeDrive(0, 0);
    }
}
