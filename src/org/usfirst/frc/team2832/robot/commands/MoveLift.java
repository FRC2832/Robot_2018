package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLift extends Command {
	/**
	*All these heights are in inches, accounting for height the trolley starts at relative to ground and ~3" margin
	*of error.  They may need to be adjusted during testing.
	*/
	private double currentHeight; //Will be in inches based on encoders
	private double heightScaleHigh = 67d;
	private double heightScaleMiddle = 55d;
	private double heightScaleLow = 43d;
	private double heightSwitch = 15d;
	private double heightIntake = 4d;
	
	private final ButtonMapping LEVEL_UP = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_RIGHT);
	private final ButtonMapping LOWER_TO_BOTTOM = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_LEFT);
	
	private boolean upPressed = Robot.controls.getButton(LEVEL_UP);
	private boolean downPressed = Robot.controls.getButton(LOWER_TO_BOTTOM);
	
	private int position;
	//Add something about encoders and converting to inches
	public MoveLift() {
		requires(Robot.lift);
	}
	//Working on math for the range here
	//Assuming + for up, - for down
	private void moveToScaleHigh() {
		if (currentHeight < (heightScaleHigh - 3)) {
			Robot.lift.setLiftPower(0.3d);
			position = 4;
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
	}
	private void moveToScaleMiddle() {
		if (currentHeight < (heightScaleMiddle - 3)) {
			Robot.lift.setLiftPower(0.3d);
			position = 3;
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		
	}
	private void moveToScaleLow() {
		if (currentHeight < (heightScaleLow - 3)) {
			Robot.lift.setLiftPower(0.3d);
			position = 2;
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
	}
	private void moveToSwitch() {
		if (currentHeight < (heightSwitch - 3)) {
			Robot.lift.setLiftPower(0.3d);
			position = 1;
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		
	}
	private void moveToIntake() {
		if (currentHeight > (heightIntake + 4)) { //Lowest Height
			Robot.lift.setLiftPower(-0.3d);
			position = 0;
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		
	}

	protected void execute() {
		currentHeight = Robot.lift.getLiftPosition();
		if (upPressed == true) {
			switch(position) {
				case 0:
					moveToSwitch();
					break;
				case 1:
					moveToScaleLow();
					break;
				case 2:
					moveToScaleMiddle();
					break;
				case 3:
					moveToScaleHigh();
					break;
				case 4:
					Robot.lift.setLiftPower(0.0d);
					break;
			}				
		} else if (downPressed == true) {
			moveToIntake();
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		

		
	}
	//Figure something out for this
	@Override
	protected boolean isFinished() {
		return true;
	}

	protected void end() {
		Robot.lift.setLiftPower(0);
	}

	protected void interrupted() {
		Robot.lift.setLiftPower(0);
	}
}