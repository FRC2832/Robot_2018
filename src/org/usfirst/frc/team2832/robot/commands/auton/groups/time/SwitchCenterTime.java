package org.usfirst.frc.team2832.robot.commands.auton.groups.time;

import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.Lift.POSITION;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive from center to a switch side
 */
public class SwitchCenterTime extends CommandGroup {

	public SwitchCenterTime() {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		addParallel(new MoveLiftPID(POSITION.SWITCH));
		addSequential(new DriveStraightForwardPigeon(.5d, 1d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'R'))); //turn towards our switch
		addSequential(new DriveStraightForwardPigeon(.5d, 3d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'L'))); //turn straight back to the switch
		addSequential(new ExpelCube());
		
	}
}
