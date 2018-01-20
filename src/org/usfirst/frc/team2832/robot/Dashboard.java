package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.DriveTime;

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
		chooser.addDefault("AutonTest", AUTON_MODE.SCALE_LEFT);
		SmartDashboard.putData("Autonomous mode chooser", chooser);
	}

	/**
	 * 
	 * @return An instance of the currently selected command
	 */
	public Command getSelectedCommand() {
		return chooser.getSelected().getCommand();
	}

	/**
	 * An enum for the different autonomous modes
	 */
	public enum AUTON_MODE {
		SCALE_LEFT, SCALE_RIGHT, SWITCH_LEFT, SWITCH_RIGHT;

		public Command getCommand() {
			switch (this) {
			case SCALE_LEFT: return new DriveTime(Robot.driveTrain, 0.4d, 3d);
			case SCALE_RIGHT: return null;
			case SWITCH_LEFT: return null;
			case SWITCH_RIGHT: return null;
			default: return null;
			}
		}
	}
}
