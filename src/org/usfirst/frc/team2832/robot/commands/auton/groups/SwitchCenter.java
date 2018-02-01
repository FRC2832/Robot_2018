package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive from center to a switch side
 */
public class SwitchCenter extends CommandGroup {

	public SwitchCenter() {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		//addSequential(new SensorFailsafe(0.5d, 120d, ()->Robot.driveTrain.getEncoderPosition(ENCODER.LEFT), ()->Robot.driveTrain.getEncoderPosition(ENCODER.RIGHT), ()->Robot.driveTrain.getPigeonYaw()));
		addSequential(new DriveDistance(0.5d, 40d, 10d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'R'))); //turn towards our switch
		addSequential(new DriveDistance(0.5d, 62d, 10d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'L'))); //turn straight back to the switch
		
	}
}
