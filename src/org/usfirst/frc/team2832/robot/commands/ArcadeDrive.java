package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Command for driving around in teleop with arcade drive
 */
public class ArcadeDrive extends Command {


	public ArcadeDrive() {
		requires(Robot.driveTrain);
	}

	protected void initialize() {
	}

	/**
	 * Drive based on input from left and right joysticks
	 */
	protected void execute() {
		Robot.driveTrain.arcadeDrive(Robot.controls.getJoystickY(Controllers.CONTROLLER_MAIN, Hand.kLeft),
				Robot.controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight));
	}

	protected boolean isFinished() {
		return false;
	}

	/**
	 * Just a little sanity check
	 */
	protected void end() {
		Robot.driveTrain.arcadeDrive(0, 0);
	}

	/**
	 * Yep, still sane.
	 */
	protected void interrupted() {
		Robot.driveTrain.arcadeDrive(0, 0);
	}
}
