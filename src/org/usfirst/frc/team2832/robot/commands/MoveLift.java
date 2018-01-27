package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLift extends Command {

	private double currentPos;
	private double heightScale;
	private double heightSwitch;
	private double heightIntake;
	//Number of encoder counts to raise to
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MoveLift() {
		requires(Robot.lift);
	}
	
	protected void execute() {
		currentPos = Robot.lift.getLiftPosition();

		if(Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) == 90) { //D-Pad down (maybe)
			if(currentPos < heightScale) {
				while(currentPos < heightScale) {
					Robot.lift.setLiftPower(0.1);
				}
			}
			else if(currentPos > heightScale) {
				while(currentPos > heightScale) {
					Robot.lift.setLiftPower(-0.1);
				}
			}
			
		}
		if(Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) == 180) { //D-pad left (hopefully)
			if(currentPos < heightSwitch) {
				while(currentPos < heightSwitch) {
					Robot.lift.setLiftPower(0.2);
				}
			}
			
		}
		if(Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) == 270) { //D-Pad up (at least, it should be)
			if(currentPos > heightIntake) {
				while(currentPos > heightIntake) {
					Robot.lift.setLiftPower(0.1);
				}
			}
		}
	}
}