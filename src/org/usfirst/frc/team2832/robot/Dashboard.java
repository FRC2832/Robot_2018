package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DrivePastLine;
import org.usfirst.frc.team2832.robot.commands.auton.autongroups.LeftSide;
import org.usfirst.frc.team2832.robot.commands.auton.autongroups.LeftSideScale;
import org.usfirst.frc.team2832.robot.commands.auton.autongroups.RightSide;
import org.usfirst.frc.team2832.robot.commands.auton.autongroups.SwitchCenter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;

/**
 * This class handles interaction with the dashboard
 */
public class Dashboard {

	private SendableChooser<AUTON_MODE> chooser;

	public static final String PREFIX_DRIVER = "Drivers-";
	public static final String PREFIX_PROG = "Prog-";
	
	public Dashboard() {
		chooser = new SendableChooser<>();
		chooser.addDefault("Left Side Priority Switch", AUTON_MODE.LEFTSIDE_PRIORITYSWITCH);
		chooser.addDefault("Left Side Priority Scale", AUTON_MODE.LEFTSIDE_PRIORITYSCALE);
		chooser.addObject("Right Side Priority Switch", AUTON_MODE.RIGHTSIDE_PRIORITYSWITCH);
		chooser.addObject("Right Side Priority Scale", AUTON_MODE.RIGHTSIDE_PRIORITYSCALE);
		chooser.addObject("Center", AUTON_MODE.CENTER);
		chooser.addObject("Just Drive Forward", AUTON_MODE.DRIVEFORWARD);
		chooser.addObject("Do Nothing", AUTON_MODE.NOTHING);
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
		LEFTSIDE_PRIORITYSCALE, LEFTSIDE_PRIORITYSWITCH, RIGHTSIDE_PRIORITYSWITCH, RIGHTSIDE_PRIORITYSCALE, CENTER, TEST, DRIVEFORWARD, NOTHING;

		public Command getCommand() {
			switch (this) {

			case LEFTSIDE_PRIORITYSCALE: return new LeftSide(AUTON_PRIORITY.SCALE);
			case LEFTSIDE_PRIORITYSWITCH: return new LeftSide(AUTON_PRIORITY.SWITCH);
			case RIGHTSIDE_PRIORITYSCALE: return new RightSide(AUTON_PRIORITY.SCALE);
			case RIGHTSIDE_PRIORITYSWITCH: return new RightSide(AUTON_PRIORITY.SWITCH);
			
			case CENTER: return new SwitchCenter();
			//case TEST: return new DriveDistance(0.6d, 288d, 15);
			case TEST: return new LeftSideScale();
			case DRIVEFORWARD: return new DrivePastLine();
			case NOTHING: return new DriveDistance(0, 0, 0); //returning null as a command breaks things. 
			default: return new DriveDistance(.7, 150.0, 10.0);
			
			}
		}
	}
	
	public enum SIDE {
		RIGHTSIDE, LEFTSIDE;
	}
	public enum AUTON_PRIORITY {
		SWITCH, SCALE;
	}
}
