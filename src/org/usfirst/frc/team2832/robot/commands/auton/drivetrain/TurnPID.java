package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Turns desired degrees using PID
 */
public class TurnPID extends Command implements PIDOutput, PIDSource {

	private final double P = 0.1;
	private final double I = 0.00;
	private final double D = 0.15;
	private final double F = 0.00;

	private final double TOLERANCE_DEGREES = 1.5f; //Accepted distance from target angle
	private final int PATIENCE = 20; // Minimum "frames" where it is within angle range, I think

	private PIDSourceType sourceType;
	private PIDController controller;
	private double targetAngle, degrees, turnRate = 0;
	private int counter;

	public TurnPID(double degrees) {
		requires(Robot.driveTrain);
		this.degrees = degrees;
		sourceType = PIDSourceType.kDisplacement;
		controller = new PIDController(P, I, D, F, this, this);
		controller.setOutputRange(-0.7, 0.7);
		controller.setContinuous(true);
		//Maybe uncomment if doesn't work
		//controller.setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
		controller.setAbsoluteTolerance(TOLERANCE_DEGREES);
		controller.disable();
	}

	protected void initialize() {
		targetAngle = Robot.driveTrain.getPigeonYaw() + degrees;
		Robot.logger.log("Turn PID", "Turning " + degrees + " degrees");
	}

	protected void execute() {
		// Move to initialize() if this works
		if (!controller.isEnabled()) {
			controller.setSetpoint(targetAngle);
			controller.enable();
			turnRate = 0;
		}
		
		Robot.driveTrain.tankDrive(turnRate, -turnRate);
	}

	protected boolean isFinished() {
		if ((Math.abs(Robot.driveTrain.getPigeonYaw() - targetAngle)) <= TOLERANCE_DEGREES)
			counter++;
		else
			counter = 0;
		return (counter > PATIENCE);
	}

	protected void end() {
		Robot.logger.log("Turn PID", "Ended");
		Robot.driveTrain.tankDrive(0, 0);
	}

	protected void interrupted() {
		Robot.logger.log("Turn PID", "Interrupted");
		Robot.driveTrain.tankDrive(0, 0);
	}

	@Override
	public void pidWrite(double output) {
		turnRate = output;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		sourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return sourceType;
	}

	@Override
	public double pidGet() {
		return Robot.driveTrain.getPigeonYaw();
	}
}
