package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.ENCODER;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDistance extends Command {

	private double start, distance, speeed;
	private DriveTrain driveTrain;

	public DriveDistance(DriveTrain driveTrain, double speeed, double distance) {
        requires(driveTrain);
        this.driveTrain = driveTrain;
        this.speeed = speeed;
    }

    protected void initialize() {
    	start = driveTrain.getEncoderPosition(ENCODER.LEFT) + driveTrain.getEncoderPosition(ENCODER.RIGHT) / 2d;
    	driveTrain.arcadeDrive(speeed, 0);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return driveTrain.getEncoderPosition(ENCODER.LEFT) + driveTrain.getEncoderPosition(ENCODER.RIGHT) / 2d > start + distance;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
