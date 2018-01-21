package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.DriveTime;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;

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
		chooser.addDefault("DriveTime", AUTON_MODE.DRIVE_FORWARD);
		chooser.addObject("Drive Distance", AUTON_MODE.SCALE_LEFT);
		chooser.addObject("Turn 90", AUTON_MODE.SCALE_RIGHT);
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
		DRIVE_FORWARD, SCALE_LEFT, SCALE_RIGHT, SWITCH_LEFT, SWITCH_RIGHT;

		public Command getCommand() {
			switch (this) {
			case DRIVE_FORWARD: return new DriveTime(0.4d, 3d);
			case SCALE_LEFT: return new DriveDistance(0.4d, 24d);
			case SCALE_RIGHT: return new TurnDegrees(90, false);
			case SWITCH_LEFT: return null;
			case SWITCH_RIGHT: return null;
			default: return null;
			}
		}
	}
}
