package org.usfirst.frc.team2832.robot.commands.auton.groups.time;

import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive from center to a switch side
 */
public class SwitchCenterTime extends CommandGroup {

	public SwitchCenterTime() {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		addSequential(new DriveStraightForwardPigeon(.5d, 1d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'R'))); //turn towards our switch
		addSequential(new DriveStraightForwardPigeon(.5d, 3d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'L'))); //turn straight back to the switch
		
	}
}
