package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Dashboard.SIDE;
import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.IngestCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class LeftScaleDualCube extends CommandGroup {

    public LeftScaleDualCube() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
		double forwardYaw = Robot.driveTrain.getPigeonYaw();
		
		if (gameData.charAt(1) == 'L') { 
			
			addSequential(new TimedCommand(.5));
			addParallel(new MoveLiftTime(4, 1, 1));
			addSequential(new DriveDistance(.8f, -278d, 8d)); 
			addSequential(new LowerIngestor(.1));
			addSequential(new TimedCommand(.5));
			addSequential(new TurnPID(50f)); 
			addSequential(new ExpelCube());
			
			addSequential(new TurnPID(30));
			addParallel(new MoveLiftTime(4, -1, 0));
			addSequential(new DriveDistance(.8, -50, 3)); //TODO: Set this distance
			addParallel(new LowerIngestor(.2));
			addSequential(new TurnPID(90));
			addParallel(new IngestCube(4));
			addSequential(new DriveDistance(.3, -40, 3)); //TODO: Set this distance
			addParallel(new MoveLiftTime(4, 1, 0));
			addSequential(new TurnPID(180));
			addSequential(new DriveDistance(.5, .50, 3)); //TODO: Set this distance
			addSequential(new ExpelCube());
		}
    	
    }
}
