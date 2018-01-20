/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.DriveTime;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

	public static DriveTrain driveTrain;
	public static Controls controls;
	public static Dashboard dashboard;
	
	Command autonomousCommand;
	
	@Override
	public void robotInit() {
		controls = new Controls();
		driveTrain = new DriveTrain(controls);
		dashboard = new Dashboard(); //Make sure that this is after all subsystems and controls
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {	
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().add(dashboard.getSelectedCommand());

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		Scheduler.getInstance().removeAll();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		controls.update();
	}

	@Override
	public void testPeriodic() {
	}
}
