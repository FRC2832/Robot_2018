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
			
			addSequential(new ScoreScale(SIDE.LEFTSIDE));
			
			addSequential(new DriveDistance(-.6, 25, 3));
			addSequential(new TurnPID(50));
			addParallel(new MoveLiftTime(3, -1, 0));
			addSequential(new DriveDistance(.6, -50, 3)); //TODO: Set this distance
			addParallel(new LowerIngestor(.2));
			addSequential(new TurnPID(70));
			addParallel(new ExpelCube(-1));
			addSequential(new DriveDistance(.5, -40, 3)); //TODO: Set this distance
			addParallel(new MoveLiftTime(4, 1, 0));
			addSequential(new TurnPID(180));
			addSequential(new DriveDistance(.5, -50, 3)); //TODO: Set this distance
			addSequential(new ExpelCube());    
		}
    }
}
