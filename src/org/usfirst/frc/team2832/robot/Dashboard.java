package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.groups.LeftSide;
import org.usfirst.frc.team2832.robot.commands.auton.groups.RightSide;
import org.usfirst.frc.team2832.robot.commands.auton.groups.SwitchCenter;
import org.usfirst.frc.team2832.robot.commands.auton.groups.time.LeftSideTime;
import org.usfirst.frc.team2832.robot.commands.auton.groups.time.RightSideTime;
import org.usfirst.frc.team2832.robot.commands.auton.groups.time.SwitchCenterTime;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class handles interaction with the dashboard
 */
public class Dashboard {

	private SendableChooser<AUTON_MODE> chooser;

	public static final String PREFIX_DRIVER = "Drivers";
	public static final String PREFIX_PROG = "Prog";
	
	public Dashboard() {
		chooser = new SendableChooser<AUTON_MODE>();
		chooser.addDefault("Left Side", AUTON_MODE.LEFTSIDE);
		chooser.addObject("Right Side", AUTON_MODE.RIGHTSIDE);
		chooser.addObject("Center", AUTON_MODE.CENTER);
		chooser.addObject("PROGRAMMING TESTING", AUTON_MODE.TEST);
		SmartDashboard.putData(PREFIX_DRIVER + "Autonomous mode chooser", chooser);
	}

	/**
	 * Gets the selected command from the {@link SendableChooser}
	 * 
	 * @return An instance of the currently selected command
	 */
	public Command getSelectedCommand() {
		return chooser.getSelected().getCommand();
	}

	/**
	 * An enumeration for the different autonomous modes
	 */
	public enum AUTON_MODE {		
		LEFTSIDE, RIGHTSIDE, CENTER, TEST;

		public Command getCommand() {
			switch (this) {

			case LEFTSIDE: return new LeftSideTime();
			case RIGHTSIDE: return new RightSideTime();
			case CENTER: return new SwitchCenterTime();
			//case TEST: return new DriveDistance(0.6d, 288d, 15);
			case TEST: return new DriveDistance(0.6d, 120.5d, 15);
			default: return null;
			
			}
		}
	}
}
