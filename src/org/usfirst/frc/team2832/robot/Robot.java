/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.DriveTime;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main robot class and location for static objects like subsystems and dashboard
 */

public class Robot extends TimedRobot {

	//Subsystems
	public static DriveTrain driveTrain;
	public static Lift lift;
	
	public static Controls controls;
	public static Dashboard dashboard;
		
	@Override
	public void robotInit() {
		controls = new Controls();
		
		driveTrain = new DriveTrain();
		lift = new Lift();
		
		dashboard = new Dashboard(); //Make sure that this is after all subsystems and controls
	}

	/**
	 * Called once when robot is disabled
	 */
	@Override
	public void disabledInit() {

	}

	/**
	 * Called periodically when robot is disabled
	 */
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * Called once when robot enters autonomous
	 */
	@Override
	public void autonomousInit() {	
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
	 * Called periodically while in test mode
	 */
	@Override
	public void testPeriodic() {
	}
}
