package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLift extends Command {
	/**
	*All these heights are in inches, accounting for height the trolley starts at relative to ground and ~3" margin
	*of error.  They may need to be adjusted during testing.
	*/
	private double currentHeight; //Will be in inches based on encoders
	private double HEIGHT_SCALE_HIGH = 67d;
	private double HEIGHT_SCALE_MIDDLE = 55d;
	private double HEIGHT_SCALE_LOW = 43d;
	private double HEIGHT_SWITCH = 15d;
	private double HEIGHT_INTAKE = 4d;
	
	private boolean upPressed = Robot.controls.getButton(ButtonMapping.LEVEL_UP);
	private boolean downPressed = Robot.controls.getButton(ButtonMapping.LOWER_TO_BOTTOM); 
	

	
	private boolean stop = false;
	//Add something about encoders and converting to inches
	public MoveLift() {
		requires(Robot.lift);
	}
	//Working on math for the range here
	//Assuming + for up, - for down
	private void moveToScaleHigh() {
		if (currentHeight < (HEIGHT_SCALE_HIGH - 3)) {
			Robot.lift.setLiftPower(0.3d);
			Robot.lift.setLiftPosition(4);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		stop = true;
	}

	protected void initialize() {
		Robot.logger.log("Move Lift", "Started");
	}

	private void moveToScaleMiddle() {
		if (currentHeight < (HEIGHT_SCALE_MIDDLE - 3)) {
			Robot.lift.setLiftPower(0.3d);
			Robot.lift.setLiftPosition(3);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		stop = true;

	}
	private void moveToScaleLow() {
		if (currentHeight < (HEIGHT_SCALE_LOW - 3)) {
			Robot.lift.setLiftPower(0.3d);
			Robot.lift.setLiftPosition(2);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		stop = true;

	}
	private void moveToSwitch() {
		if (currentHeight < (HEIGHT_SWITCH - 3)) {
			Robot.lift.setLiftPower(0.3d);
			Robot.lift.setLiftPosition(1);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		stop = true;

	}
	private void moveToIntake() {
		if (currentHeight > (HEIGHT_INTAKE + 4)) { //Lowest Height
			Robot.lift.setLiftPower(-0.3d);
			Robot.lift.setLiftPosition(0);
		} else {
			Robot.lift.setLiftPower(0.0d);
		}
		stop = true;

	}
	
	private void incrementPosition() {
		if(Robot.lift.setLiftPosition(Robot.lift.getLiftPosition() + 1) <= 5) {
			switch(Robot.lift.getLiftPosition()) {
				case 1:
					moveToSwitch();
					break;
				case 2:
					moveToScaleLow();
					break;
				case 3:
					moveToScaleMiddle();
					break;
				case 4:
					moveToScaleHigh();
					break;
				case 5:
					Robot.lift.setLiftPower(0d);
					break;
			}
		}
	}
	
	private void decrementPosition() {
		Robot.lift.setLiftPosition(0);
		moveToIntake();
	}
	

	protected void execute() {
		if(!Robot.lift.getPacked()) {
			currentHeight = Robot.lift.getLiftEncoderPosition();
			if (upPressed == true) incrementPosition();
			else if (downPressed == true) decrementPosition();
			else Robot.lift.setLiftPower(0d);
		} else Robot.lift.setLiftPower(0d);
		

		
	}
	@Override
	protected boolean isFinished() {
		return stop;
	}

	protected void end() {
		Robot.logger.log("Move Lift", "Ended");
		Robot.lift.setLiftPower(0);
	}

	protected void interrupted() {
		Robot.logger.log("Move Lift", "Interrupted");
		Robot.lift.setLiftPower(0);
	}
}