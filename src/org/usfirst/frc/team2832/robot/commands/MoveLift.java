package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLift extends Command {

	//All these heights are in inches, accounting for height the trolley starts at relative to ground and ~3" margin
	//of error.  They may need to be adjusted during testing.
	private double currentHeight; //Will be in inches based on encoders
	private double heightScaleHigh = 67d;
	private double heightScaleMiddle = 55d;
	private double heightScaleLow = 43d;
	private double heightSwitch = 15d;
	private double heightIntake = 4d;
	
	private int position;
	//Add something about encoders and converting to inches
	public MoveLift() {
		requires(Robot.lift);
	}
	//Working on math for the range here
	//Assuming + for up, - for down
	private void moveToScaleHigh() {
		if ((heightScaleHigh - 3) < currentHeight && heightScaleHigh < currentHeight) { //max height, shouldn't ever need to go higher
			Robot.lift.setLiftPower(0.3d);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
	}
	private void moveToScaleMiddle() {
		if ((heightScaleMiddle - 3) < currentHeight && heightScaleMiddle < currentHeight) { //If it's too low
			Robot.lift.setLiftPower(0.3d);
		} else if ((heightScaleMiddle + 3) > currentHeight && heightScaleMiddle < currentHeight) { //If it's too high
			Robot.lift.setLiftPower(-0.3d);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		
	}
	private void moveToScaleLow() {
		if ((heightScaleLow - 3) < currentHeight && heightScaleLow < currentHeight) { //If it's too low
			Robot.lift.setLiftPower(0.3d);
		} else if ((heightScaleLow + 3) > currentHeight && heightScaleLow > currentHeight) { //If it's too high
			Robot.lift.setLiftPower(-0.3d);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
	}
	private void moveToSwitch() {
		if ((heightSwitch - 3) < currentHeight && heightSwitch < currentHeight) { //If it's too low
			Robot.lift.setLiftPower(0.3d);
		} else if ((heightSwitch + 3) > currentHeight && heightSwitch > currentHeight) { //If it's too high
			Robot.lift.setLiftPower(-0.3d);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		
	}
	private void moveToIntake() {
		if ((heightIntake - 4) < currentHeight && heightIntake < currentHeight) { //Lowest Height
			Robot.lift.setLiftPower(-0.3d);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		
	}

	protected void execute() {

		
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.lift.setLiftPower(0);
	}

	protected void interrupted() {
		Robot.lift.setLiftPower(0);
	}
}