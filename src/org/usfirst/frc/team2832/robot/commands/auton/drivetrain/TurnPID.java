package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import org.usfirst.frc.team2832.robot.Dashboard;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Turns desired degrees using PID
 */
public class TurnPID extends Command implements PIDOutput, PIDSource {
	
	private final double P = 0.06;
	private final double I = 0.000185;
	private final double D = 0.5;
	private double F = 0.00;//A feed forward the same for both directions makes no sense.
	
	private final double TOLERANCE_DEGREES = 3f; //Accepted distance from target angle
	private final int PATIENCE = 5; // Minimum "frames" where it is within angle range, I think

	private PIDSourceType sourceType;
	private PIDController controller;
	private double targetAngle, degrees, startAngle;
	private int counter;
	
	private double startTime;
	private final double TIMEOUT = 3;

	public TurnPID(double degrees) {
		F = .0003*degrees;
		requires(Robot.driveTrain);
		this.degrees = degrees;
		sourceType = PIDSourceType.kDisplacement;
		controller = new PIDController(P, I, D, F, this, this, 0.02);
		controller.setOutputRange(-0.7, 0.7);
		controller.setInputRange(-Double.MAX_VALUE, Double.MAX_VALUE);
		controller.setContinuous(true);
		controller.setAbsoluteTolerance(TOLERANCE_DEGREES);
		controller.disable();
	}

	protected void initialize() {
		controller.reset();
		startAngle = Robot.driveTrain.getPigeonYaw();		
		targetAngle = Robot.driveTrain.getPigeonYaw() + degrees;

		System.out.println("Initial yaw angle " + Robot.driveTrain.getPigeonYaw());
		Robot.logger.log("Turn PID", "Turning " + degrees + " degrees");
		
		startTime = Timer.getFPGATimestamp();
	}

	protected void execute() {
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Starting angle", startAngle);
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Target angle", targetAngle);
		// Move to initialize() if this works
		if (!controller.isEnabled()) {
			controller.setSetpoint(targetAngle);
			controller.enable();
		}
	}

	protected boolean isFinished() {
		if (Timer.getFPGATimestamp() - startTime > TIMEOUT)
			return true;
		if ((Math.abs(Robot.driveTrain.getPigeonYaw() - targetAngle)) <= TOLERANCE_DEGREES)
			counter++;
		else
			counter = 0;
		return (counter > PATIENCE);
	}

	protected void end() {
		Robot.logger.log("Turn PID", "Ended");
		Robot.driveTrain.tankDrive(0, 0);
		controller.disable();
	}

	protected void interrupted() {
		Robot.logger.log("Turn PID", "Interrupted");
		Robot.driveTrain.tankDrive(0, 0);
		controller.disable();
	}

	@Override
	public void pidWrite(double output) {
		//TODO test if changing the signs on this fixed it. I think it will have done, since positive is now right.
		Robot.driveTrain.tankDrive(output, -output);
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
