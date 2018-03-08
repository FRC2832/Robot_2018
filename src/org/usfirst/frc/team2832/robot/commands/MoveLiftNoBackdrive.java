package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

public class MoveLiftNoBackdrive extends Command {

	private static final int TOLERANCE = 12;

	private double target;
	private boolean moving;
	private boolean ended;
	/**A value used to prevent spam by preventing repeating the same message*/
	int lastLogged = -1;

	public MoveLiftNoBackdrive() {
		requires(Robot.lift);
	}

	@Override
	protected void initialize() {
		target = Robot.lift.getLiftEncoderPosition();
		moving = false;
	}

	@Override
	protected void execute() {

		/*
		 * currently only runs on manual control manual controls are entered with
		 * controller 2's triggers
		 */

		// double [] triggers = Robot.lift.getTriggers();

		if (Lift.liftTriggerR > 0.1) { // if commanded to rise
			Robot.lift.setLiftPower(1d);
			if(lastLogged != 0) {
				Robot.logger.log("Lift", "Moving up at " + -Robot.lift.getLiftPower());
				lastLogged = 0;
			}

		} else if (Lift.liftTriggerL > 0.1) { // else if commanded to fall
			Robot.lift.setLiftPower(-1d);
			if(lastLogged != 1) {
				Robot.logger.log("Lift", "Moving down at " + Robot.lift.getLiftPower());
				lastLogged = 1;
			}
		} else { // if not being commanded
			Robot.lift.setLiftPower(0.1);
			if(lastLogged != 2) {
				Robot.logger.log("Lift", "Holding position with " + -Robot.lift.getLiftPower());
				lastLogged = 2;
				
				
			}
		}
	}

	@Override
	protected void end() {
		Robot.lift.setLiftPower(0);
		Robot.logger.log("Lift", "Ended");

	}

	@Override
	protected void interrupted() {
		Robot.lift.setLiftPower(0);
		Robot.logger.log("Lift", "Interrupted");
	}

	@Override
	protected boolean isFinished() {
		if (Lift.liftTriggerR < 0.1d && Lift.liftTriggerL < 0) {
			ended = true;
		} else {
			ended = false;
		}
		return ended;
	}
}
