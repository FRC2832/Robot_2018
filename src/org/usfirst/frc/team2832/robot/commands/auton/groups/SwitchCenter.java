package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.Lift.POSITION;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive from center to a switch side
 */
public class SwitchCenter extends CommandGroup {

	public SwitchCenter() {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		//addSequential(new SensorFailsafe(0.5d, 120d, ()->Robot.driveTrain.getEncoderPosition(ENCODER.LEFT), ()->Robot.driveTrain.getEncoderPosition(ENCODER.RIGHT), ()->Robot.driveTrain.getPigeonYaw()));
		addParallel(new MoveLiftPID(POSITION.SWITCH));
		addSequential(new DriveDistance(0.5d, 40d, 10d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'R'))); //turn towards our switch
		addSequential(new DriveDistance(0.5d, 62d, 10d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'L'))); //turn straight back to the switch
		addSequential(new ExpelCube());
		
	}
}
