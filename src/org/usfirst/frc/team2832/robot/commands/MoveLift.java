package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
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

	private int liftPosition;

	private boolean upPressed = Robot.controls.getButton(ButtonMapping.LEVEL_UP);
	private boolean downPressed = Robot.controls.getButton(ButtonMapping.LOWER_TO_BOTTOM); 
	
	private boolean positionChangeActive = false;
	private PositionChangeType positionChangeType = PositionChangeType.RAISE;
	
	private boolean stop = false;
	//Add something about encoders and converting to inches
	public MoveLift() {
		requires(Robot.lift);
	}
	//Working on math for the range here
	//Assuming + for up, - for down
	private void moveToScaleHigh() {
		if (currentHeight < (HEIGHT_SCALE_HIGH - 3)) {
			Robot.lift.setLiftPower(-0.5d);
		} else {
			Robot.lift.setLiftPower(-0.2d);
			setLiftPosition(4);
			positionChangeActive = false;
			stop = true;
		}

	}

	protected void initialize() {
		Robot.logger.log("Move Lift", "Started");
	}

	private void moveToScaleMiddle() {
		if (currentHeight < (HEIGHT_SCALE_MIDDLE - 3)) {
			Robot.lift.setLiftPower(-0.5d);
		} else {
			Robot.lift.setLiftPower(-0.2d);
			setLiftPosition(3);
			positionChangeActive = false;
			stop = true;
		}

	}
	private void moveToScaleLow() {
		if (currentHeight < (HEIGHT_SCALE_LOW - 3)) {
			Robot.lift.setLiftPower(-0.5d);
		} else {
			Robot.lift.setLiftPower(-0.2d);
			setLiftPosition(2);
			positionChangeActive = false;
			stop = true;
		}

	}
	private void moveToSwitch() {
		if (currentHeight < (HEIGHT_SWITCH - 3)) {
			Robot.lift.setLiftPower(-0.5d);
		} else {
			Robot.lift.setLiftPower(-0.2d);
			setLiftPosition(1);
			positionChangeActive = false;
			stop = true;
		}

	}
	private void moveToIntake() {
		if (currentHeight > (HEIGHT_INTAKE + 4)) { //Lowest Height
			Robot.lift.setLiftPower(0.5d);

		} else {
			Robot.lift.setLiftPower(-0.2d);
			setLiftPosition(0);
			positionChangeActive = false;
			stop = true;
		}

	}
	
	private void incrementPosition() {
		if(getLiftPosition() + 1 <= 4) {
			switch(getLiftPosition() + 1) {
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
			}
		}
	}
	
	private void decrementPosition() {
		moveToIntake();
	}
	

	protected void execute() {
		
		int pov = Robot.controls.getPOV(Controllers.CONTROLLER_MAIN);
		if(!Robot.lift.getPacked()) {
			currentHeight = Robot.lift.getLiftEncoderPosition();
			if(positionChangeActive) {
				if(positionChangeType.equals(PositionChangeType.RAISE)) incrementPosition();
				else decrementPosition();
			}	else {
					if(upPressed) {
						incrementPosition();
						positionChangeType = PositionChangeType.RAISE;
						positionChangeActive = true;
					}
					else if(downPressed) {
						decrementPosition();
						positionChangeType = PositionChangeType.LOWER;
						positionChangeActive = true;
					}
				}
		} else if(pov != -1) {

			if(pov > 90 && pov < 270)
				Robot.lift.setLiftPower(-1d);
			else
				Robot.lift.setLiftPower(1d);
		} else 
			Robot.lift.setLiftPower(-.2);
		
/*		  if (Robot.controls.getButton(ButtonMapping.LEVEL_UP)) {
				Robot.lift.setLiftPower(-.7);
			} else if (Robot.controls.getButton(ButtonMapping.LOWER_TO_BOTTOM)) {
				Robot.lift.setLiftPower(0.7);
			} else {
				Robot.lift.setLiftPower(-0.2);
			}
*/
		
	}
	@Override
	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.logger.log("Move Lift", "Ended");
		Robot.lift.setLiftPower(0);
	}

	protected void interrupted() {
		Robot.logger.log("Move Lift", "Interrupted");
		Robot.lift.setLiftPower(0);
	}
	
	private enum PositionChangeType {
		RAISE, LOWER
	}

	public int setLiftPosition(int a) {
		liftPosition = a;
		return liftPosition;
	}

	public int getLiftPosition() {
		return liftPosition;
	}
}