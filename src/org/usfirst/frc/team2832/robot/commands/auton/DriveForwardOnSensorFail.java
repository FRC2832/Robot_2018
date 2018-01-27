package org.usfirst.frc.team2832.robot.commands.auton;

import java.util.function.Supplier;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.Encoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A command to test if sensors are working and driveforward to line if not
 */
public class DriveForwardOnSensorFail extends Command {

	private final double testDuration = 0.3d;
	
	private double startTime, duration, speeed;
	private int stage;
	private boolean done;
	Supplier<Double>[] sensors;

	public DriveForwardOnSensorFail(double duration, double speeed, Supplier<Double>...sensors) {
		requires(Robot.driveTrain);
		this.duration = duration;
		this.speeed = speeed;
		this.sensors = sensors;
	}

	protected void initialize() {
		startTime = Timer.getFPGATimestamp();
	}

	/**
	 * Drive straight at set speed
	 */
	protected void execute() {
		switch (stage) {
		case 0: // Brief drive forward
			Robot.driveTrain.arcadeDrive(0.4, 0);
			if(Timer.getFPGATimestamp() > startTime + testDuration) {
				Robot.driveTrain.arcadeDrive(0, 0);
				startTime = Timer.getFPGATimestamp();
				stage++;
			}
			break;
		case 1: // Check sensors
			boolean failed = false;
			for(int i = 0; i < sensors.length; i++) {
				if(sensors[i].get() == 0)
					failed = true;
			}
			if(failed) {
				stage++;
				startTime = Timer.getFPGATimestamp();
			} else {
				stage = 9001; // It's over 9000!
				done = true;
			}
			break;
		case 2: // Move forward time if sensors failed
			if(startTime + duration > Timer.getFPGATimestamp())
				Robot.driveTrain.arcadeDrive(speeed, 0);
			else
				Robot.driveTrain.arcadeDrive(0, 0);
			break;
		}
	}

	/**
	 * Terminate if sensors are working
	 */
	protected boolean isFinished() {
		return done;
	}

	/**
	 * I have forgotten this one too many times
	 */
	protected void end() {
		Robot.driveTrain.arcadeDrive(0, 0);
	}

	/**
	 * Never again
	 */
	protected void interrupted() {
		Robot.driveTrain.arcadeDrive(0, 0);
	}
}
