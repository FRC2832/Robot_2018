package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.SensorTest;
import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.DiagnoseSensors;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnDegrees;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

/**
 * Drive from center to a switch side
 */
public class SwitchCenter extends CommandGroup {

	public SwitchCenter() {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		addParallel(new MoveLiftPID(Lift.Position.SWITCH));
		addSequential(new DriveDistance(0.5d, 40d, 10d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'R'))); //turn towards our switch
		addSequential(new DriveDistance(0.5d, 62d, 10d));
		addSequential(new TurnDegrees(45, (gameData.charAt(0) == 'L'))); //turn straight back to the switch
		addSequential(new ExpelCube());
		
	}
}
