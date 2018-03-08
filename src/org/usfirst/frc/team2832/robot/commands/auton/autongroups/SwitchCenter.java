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
		addSequential(new DriveDistance(0.5d, -20d, 10d));
		addParallel(new MoveLiftTime(1.2, 1));
		
		if (gameData.charAt(0) == 'R') {
			addSequential(new TurnPID(45));
			addSequential(new DriveDistance(0.5d, -62d, 10d));
			addSequential(new TurnPID(-45));
			addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(0.5d, -25d, 10d));
			addSequential(new ExpelCube());
		} else {
			addSequential(new TurnPID(-45));
			addSequential(new DriveDistance(0.5d, -90d, 10d));
			addSequential(new TurnPID(45));
			addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(0.5d, -5d, 4d));
			addSequential(new ExpelCube());
		}
		
	}
}
