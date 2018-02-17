package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.autongroups.LeftSide;
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
		chooser.addDefault("Left Side", AUTON_MODE.LEFTSIDE);
		chooser.addObject("Right Side", AUTON_MODE.RIGHTSIDE);
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
		LEFTSIDE, RIGHTSIDE, CENTER, TEST, DRIVEFORWARD, NOTHING;

		public Command getCommand() {
			switch (this) {

			case LEFTSIDE: return new LeftSide();
			case RIGHTSIDE: return new RightSide();
			case CENTER: return new SwitchCenter();
			//case TEST: return new DriveDistance(0.6d, 288d, 15);
			case TEST: return new TurnPID(90);
			case DRIVEFORWARD: return new DriveDistance(.7, 100.0, 10.0);
			case NOTHING: return null;
			default: return new DriveDistance(.7, 150.0, 10.0);
			
			}
		}
	}
}
