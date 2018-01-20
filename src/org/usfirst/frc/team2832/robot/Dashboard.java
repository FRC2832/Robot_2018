package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.DriveTime;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {

	private SendableChooser<Command> chooser;
	
	public Dashboard() {
		chooser = new SendableChooser<Command>();
		chooser.addDefault("AutonTest", new DriveTime(Robot.driveTrain, 3d));
		SmartDashboard.putData("Autonomous mode chooser", chooser);
	}
	
	public Command getSelectedCommand() {
		return chooser.getSelected();
	}
}
