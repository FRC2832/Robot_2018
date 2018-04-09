package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Dashboard.AUTON_PRIORITY;
import org.usfirst.frc.team2832.robot.Dashboard.SIDE;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DrivePastLine;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Command sequence for LEFT side autonomous
 * Takes a priority as a parameter
 * If Switch is the priority, it will go for the switch first then for the scale, then just forward
 * If Scale is the priority, it will go for the switch first then for the scale, then just forward
 * 
 * This command group is only the logic to determine which block of code to run
 *
 */
public class LeftSide extends CommandGroup {

	public LeftSide(AUTON_PRIORITY priority) {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (priority == AUTON_PRIORITY.SWITCH) {

			if (gameData.charAt(0) == 'L') {
				addSequential(new ScoreSwitch(SIDE.LEFTSIDE));
			} else if (gameData.charAt(1) == 'L') { 
				addSequential(new ScoreScale(SIDE.LEFTSIDE));
			} else { 
				//addSequential(new DriveDistance(0.7d, -120d, 10d));
			}
		}

		if (priority == AUTON_PRIORITY.SCALE) {
			if (gameData.charAt(1) == 'L') { 
				addSequential(new ScoreScale(SIDE.LEFTSIDE));
			} else if (gameData.charAt(0) == 'L') { 
				addSequential(new ScoreSwitch(SIDE.LEFTSIDE));
			}  else {
				//addSequential(new DriveDistance(0.7d, -120d, 10d));
			}
		}
		
	}

}
