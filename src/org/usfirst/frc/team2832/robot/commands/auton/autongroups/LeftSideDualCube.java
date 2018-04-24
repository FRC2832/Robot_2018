package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Dashboard.SIDE;
import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class LeftSideDualCube extends CommandGroup {

    public LeftSideDualCube() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if (gameData.charAt(1) == 'L') { 
			
			addParallel(new MoveLiftTime(2.8, 1, 1.5));
			addSequential(new DriveDistance(.8f, -250d, 8d)); 
			addParallel(new LowerIngestor(.1));
			addSequential(new TurnPID(50f, 1.5)); 
			addSequential(new ExpelCube(.6, 0.8));
			
			addSequential(new TurnPID(80, 1.5));
			addParallel(new MoveLiftTime(2.8, -1));
			addSequential(new DriveDistance(.7, -50, 3)); //TODO: Set this distance
			addParallel(new LowerIngestor(.2));
			addSequential(new TurnPID(40, 1.5));
			addParallel(new ExpelCube(-1, 2.0));
			addSequential(new DriveDistance(.6, -40, 2)); //TODO: Set this distance
			addParallel(new MoveLiftTime(2.8, 1));
			addSequential(new TurnPID(165, 2.0));
			addSequential(new DriveDistance(.7, -40, 3)); //TODO: Set this distance
			addSequential(new ExpelCube(0.6));
			
		}
    }
}
