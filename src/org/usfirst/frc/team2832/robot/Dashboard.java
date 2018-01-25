package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.groups.LeftSide;
import org.usfirst.frc.team2832.robot.commands.auton.groups.RightSide;
import org.usfirst.frc.team2832.robot.commands.auton.groups.SwitchCenter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class handles interaction with the dashboard
 */
public class Dashboard {

	private SendableChooser<AUTON_MODE> chooser;

	public Dashboard() {
		chooser = new SendableChooser<AUTON_MODE>();
		chooser.addDefault("Left Side", AUTON_MODE.LEFTSIDE);
		chooser.addObject("Right Side", AUTON_MODE.RIGHTSIDE);
		chooser.addObject("Center", AUTON_MODE.CENTER);
		chooser.addObject("PROGRAMMING TESTING", AUTON_MODE.TEST);
		SmartDashboard.putData("Autonomous mode chooser", chooser);
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

			case LEFTSIDE: return new LeftSide();
			case RIGHTSIDE: return new RightSide();
			case CENTER: return new SwitchCenter();
			case TEST: return null;
			default: return null;
			
			}
		}
	}
}
