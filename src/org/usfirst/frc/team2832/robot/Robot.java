
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.subsystems.Climber;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.Ingestor;
import org.usfirst.frc.team2832.robot.subsystems.Lift;
import org.usfirst.frc.team2832.robot.subsystems.Unpacker;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main robot class and location for static objects like subsystems and dashboard
 */

public class Robot extends TimedRobot {

	private final static int ROBOT_TYPE_PIN = 3;

	//Subsystems
	public static DriveTrain driveTrain;
	public static Lift lift;
	public static Ingestor ingestor;
	public static Climber climber;
	public static Unpacker unpacker;

	//Other
	public static Controls controls;
	public static Dashboard dashboard;

	private static RobotType robotType;
	private AnalogInput robotTypeInput;

	/**
	 * Gets which robot the code is running on
	 * @return type of robot
	 */
	public static RobotType getRobotType() {
		return robotType;
	}

	/**
	 * Called when the robot is initialized
	 */
	@Override
	public void robotInit() {
		controls = new Controls();

		driveTrain = new DriveTrain();
		lift = new Lift();
		ingestor = new Ingestor();
		climber = new Climber();
		unpacker = new Unpacker();

		dashboard = new Dashboard(); //Make sure that this is after all subsystems and controls

		robotTypeInput = new AnalogInput(ROBOT_TYPE_PIN);
		robotType = RobotType.Competition; //Default to competition
	}

	@Override
	public void robotPeriodic() {
		SmartDashboard.putString(Dashboard.PREFIX_DRIVER + "RobotType", robotType.name());
	}

	/**
	 * Called when when robot is disabled
	 */
	@Override
	public void disabledInit() {
		Robot.driveTrain.setBrakeMode(false);
		controls.clearRumbles();
	}

	/**
	 * Called periodically when robot is disabled
	 */
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		driveTrain.setPigeonYaw(0);

		// Set the type of robot based on the average voltage of a pin
		double value = robotTypeInput.getAverageVoltage();
		if(3.75d <= value && value <= 5d) {
			robotType = RobotType.Programming;
		} else if(1.25d < value && value < 3.75d) {
			robotType = RobotType.Practice;
		} else {
			robotType = RobotType.Competition;
		}
	}

	/**
	 * Called when robot enters autonomous
	 */
	@Override
	public void autonomousInit() {
		Robot.driveTrain.setBrakeMode(true);
		Robot.lift.unpack();
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().add(dashboard.getSelectedCommand());
	}

	/**
	 * Called periodically when robot is in autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * Called when robot enters teleop
	 */
	@Override
	public void teleopInit() {
		Robot.driveTrain.setBrakeMode(true);
		Scheduler.getInstance().removeAll();
	}

	/**
	 * Called periodically when robot is in teleop
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		controls.update();
	}

	/**
	 * Called when test mode begins
	 */
	@Override
	public void testInit() {

	}

	/**
	 * Called periodically while in test mode
	 */
	@Override
	public void testPeriodic() {

	}

	public enum RobotType {
		Programming, Practice, Competition;
	}
}
