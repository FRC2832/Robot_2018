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
public class RightSideScale extends CommandGroup {

    public RightSideScale() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if (gameData.charAt(1) == 'R') { // If the scale is on our side
			addSequential(new ScoreScale(SIDE.RIGHTSIDE));
		} else {	
			addSequential(new TimedCommand(.5));
			addSequential(new DriveDistance(.7f, -200, 10)); 
			addSequential(new TurnPID(-89));
			addParallel(new MoveLiftTime(2.7, 1));
			addSequential(new DriveDistance(.7f, -170d, 10)); //TODO tune this distance
			addSequential(new TurnPID(90.0));
			addSequential(new DriveDistance(.6f, -36d, 10));
			//Maybe another addSequential(new TurnPID(-50.0));
			addSequential(new LowerIngestor(.1));
			addSequential(new ExpelCube(0.3));
			
		}
    }
}
