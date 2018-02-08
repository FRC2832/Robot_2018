package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.SensorTest;

/**
 * A command to test if sensorTests are working and driveforward to line if not
 */
public class DiagnoseSensors extends Command {

	private final double TEST_DURATION = 0.3d;
	private final double SPEEEED = 0.4;
	
	private double startTime;
	private boolean done;
	private SensorTest[] sensorTests;

	public DiagnoseSensors(SensorTest... sensorTests) {
		requires(Robot.driveTrain);
		requires(Robot.lift);
		this.sensorTests = sensorTests;
	}

	protected void initialize() {
		startTime = Timer.getFPGATimestamp();
	}

	/**
	 * Move motors at set speed
	 */
	protected void execute() {
		if (Timer.getFPGATimestamp() < startTime + TEST_DURATION / 2d) {
			Robot.driveTrain.arcadeDrive(SPEEEED, 0);
			Robot.lift.setLiftPower(SPEEEED);
		} else {
			Robot.driveTrain.arcadeDrive(-SPEEEED, 0);
			Robot.lift.setLiftPower(-SPEEEED);
		}
	}

	/**
	 * Terminate when duration has elapsed
	 */
	protected boolean isFinished() {
		return Timer.getFPGATimestamp() > startTime + TEST_DURATION;
	}

	/**
	 * Flag susbsystem if sensor is still 0
	 */
	private void testSensors() {
		for(SensorTest test: sensorTests)
			if(test.getSensor())
				test.getSubsystem().addFlag(test.getFlag());
	}

	/**
	 * I have forgotten this one too many times
	 */
	protected void end() {
		Robot.driveTrain.arcadeDrive(0, 0);
		Robot.lift.setLiftPower(0);
		testSensors();
	}

	/**
	 * Never again
	 */
	protected void interrupted() {
		Robot.driveTrain.arcadeDrive(0, 0);
		Robot.lift.setLiftPower(0);
		testSensors();
	}
}
