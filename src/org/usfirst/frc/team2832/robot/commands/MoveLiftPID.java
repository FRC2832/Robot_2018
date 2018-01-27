package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Moves lift to desired height
 */
public class MoveLiftPID extends Command implements PIDOutput, PIDSource {
		
	private PIDSourceType sourceType;
	private PIDController controller;
	private double speeed, targetHeight;
	private int counter;

	private final double P = 0.1;
	private final double I = 0.00;
	private final double D = 0.15;
	private final double F = 0.00;
	
	private final double TOLERANCE_INCHES = 1.5f;
	private final int PATIENCE = 20;
	
	public MoveLiftPID(Lift.POSITION position) {
		this(position.height);
	}
	
    public MoveLiftPID(double targetHeight) {
    	requires(Robot.lift);
    	this.targetHeight = targetHeight;
    	sourceType = PIDSourceType.kDisplacement;
		controller = new PIDController(P, I, D, F, this, this);
		controller.setOutputRange(-0.7, 0.7);
		controller.setContinuous(true);
		// Maybe uncomment if doesn't work
		// controller.setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
		controller.setAbsoluteTolerance(TOLERANCE_INCHES);
		controller.setInputRange(0, Lift.RAIL_HEIGHT);
		controller.setSetpoint(targetHeight);
		controller.enable();
    }
    
    protected void initialize() {
    	
    }

    protected void execute() {
    	if(!controller.isEnabled()) {
    		controller.setSetpoint(targetHeight);
    		controller.enable();
    		speeed = 0;
    	}
    	Robot.lift.setLiftPower(speeed);
    }

    protected boolean isFinished() {
    	if ((Math.abs(Robot.lift.getLiftPosition() - targetHeight)) <= TOLERANCE_INCHES)
			counter++;
		else
			counter = 0;
		return (counter > PATIENCE);
    }

    protected void end() {
    	Robot.lift.setLiftPower(0);
    }

    protected void interrupted() {
    	Robot.lift.setLiftPower(0);
    }
    

	@Override
	public void pidWrite(double output) {
		speeed = output;
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
		return Robot.lift.getLiftPosition();
	}
}
