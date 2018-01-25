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
		LEFTSIDE, RIGHTSIDE, CENTER;

		public Command getCommand() {
			switch (this) {
<<<<<<< HEAD
			case LEFTSIDE: return new LeftSide();
			case RIGHTSIDE: return new RightSide();
=======
			case DRIVE_FORWARD: new DriveDistance(.5f, 48f);
			case SCALE_LEFT: return null; //new DriveDistance(0.4d, 24d);
			case SCALE_RIGHT:
			group.addSequential(new DriveStraightForwardPigeon(.5f, 9f));
			group.addSequential(new TurnDegrees(90f, false));
			return group;
			case SWITCH_LEFT: return null;
			case SWITCH_RIGHT: return new DeploySwitchRight();
>>>>>>> refs/remotes/origin/master
			case CENTER: return new SwitchCenter();
			default: return null;
			}
		}
	}
}
