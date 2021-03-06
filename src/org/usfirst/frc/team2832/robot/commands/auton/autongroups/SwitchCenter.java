package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 * Drive from center to a switch side
 */
public class SwitchCenter extends CommandGroup {
	
	public SwitchCenter() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		addSequential(new DriveDistance(0.7d, -20d, 10d));
		addParallel(new MoveLiftTime(1.2, 1, 1.5));
		
		if (gameData.charAt(0) == 'R') {
			addSequential(new TurnPID(45, 2.0));
			addSequential(new DriveDistance(0.6d, -62d, 10d));
			addSequential(new TurnPID(-45, 2.0));
			addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(0.6d, -30d, 2d));
			addSequential(new ExpelCube());
		} else {
			addSequential(new TurnPID(-45, 2.0));
			addSequential(new DriveDistance(0.6d, -70d, 10d));
			addSequential(new TurnPID(45, 2.0));
			addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(0.6d, -30d, 2d));
			addSequential(new ExpelCube());
		}
		
	}
}
