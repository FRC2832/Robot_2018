package org.usfirst.frc.team2832.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.DriveTime;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.commands.auton.groups.DeploySwitchRight;
import org.usfirst.frc.team2832.robot.commands.auton.groups.SwitchCenter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class handles interaction with the dashboard
 */
public class Dashboard {

	private SendableChooser<AUTON_MODE> chooser;

	public Dashboard() {
		chooser = new SendableChooser<AUTON_MODE>();
		chooser.addDefault("Drive Forward", AUTON_MODE.DRIVE_FORWARD);
		chooser.addObject("Scale Left", AUTON_MODE.SCALE_LEFT);
		chooser.addObject("Scale Right", AUTON_MODE.SCALE_RIGHT);
		chooser.addObject("Switch left", AUTON_MODE.SWITCH_LEFT);
		chooser.addObject("Switch Right", AUTON_MODE.SWITCH_RIGHT);
		chooser.addObject("Center Right", AUTON_MODE.CENTER);
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
		DRIVE_FORWARD, SCALE_LEFT, SCALE_RIGHT, SWITCH_LEFT, SWITCH_RIGHT, CENTER;

		public Command getCommand() {
			CommandGroup group = new CommandGroup();
			switch (this) {
			case DRIVE_FORWARD: new DriveStraightForwardPigeon(.5f, 5f);
			case SCALE_LEFT: return null; //new DriveDistance(0.4d, 24d);
			case SCALE_RIGHT:
			group.addSequential(new DriveStraightForwardPigeon(.5f, 9f));
			group.addSequential(new TurnDegrees(90f, false));
			return group;
			case SWITCH_LEFT: return null;
			case SWITCH_RIGHT: return new DeploySwitchRight();
			case CENTER: return new SwitchCenter();
			default: return null;
			}
		}
	}
}
