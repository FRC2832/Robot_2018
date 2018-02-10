package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.SensorTest;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 * A command to test if sensorTests are working and driveforward to line if not
 */
public class DiagnoseSensors extends Command {

	private final double TEST_DURATION = 0.3d;
	private final double SPEEEED = 0.4;
	
	private double startTime;
	private SensorTest[] sensorTests;

	public DiagnoseSensors(SensorTest... sensorTests) {
		requires(Robot.driveTrain);
		requires(Robot.lift);
		this.sensorTests = sensorTests;
	}

	protected void initialize() {
		startTime = Timer.getFPGATimestamp();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < sensorTests.length; i++) {
			if (i < sensorTests.length - 1)
				builder.append(sensorTests[i].getFlag().name() + (i == sensorTests.length - 2 ? ", and " : ", "));
			else
				builder.append(sensorTests[i].getFlag().name());
		}
		Robot.logger.log("Diagnose Sensors", "Testing " + builder);
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
			if(test.getSensor()) {
				test.getSubsystem().addFlag(test.getFlag());
				Robot.logger.critical("Sensor Failure", test.getFlag().name() + "has failed on " + test.getSubsystem().getName());
			}
	}

	/**
	 * I have forgotten this one too many times
	 */
	protected void end() {
		Robot.logger.log("Diagnose Sensors", "Ended");
		Robot.driveTrain.arcadeDrive(0, 0);
		Robot.lift.setLiftPower(0);
		testSensors();
	}

	/**
	 * Never again
	 */
	protected void interrupted() {
		Robot.logger.log("Diagnose Sensors", "Interrupted");
		Robot.driveTrain.arcadeDrive(0, 0);
		Robot.lift.setLiftPower(0);
		testSensors();
	}
}
