package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveTime;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnDegrees;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;
import org.usfirst.frc.team2832.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftSide extends CommandGroup {

    public LeftSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	Robot.robotPosition.startingOnLeft();
    	
		if (gameData.charAt(0) == 'L') { //If the switch is on our side
			//addParallel(new MoveLiftPID(Lift.Position.SWITCH));
			addParallel(new LowerIngestor());
			addSequential(new DriveDistance(.6f, -150d, 10)); //go forward to switch
    		addSequential(new TurnPID(90f)); //turn 90 degrees
			addParallel(new MoveLiftTime(1d, 1));
			addSequential(new DriveTime(1, 1));
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		//addParallel(new MoveLiftPID(Lift.Position.SCALE));
			addParallel(new MoveLiftTime(1.8d, 1));
    		addSequential(new DriveDistance(0.8d, -291d, 20)); // go forward to scale
    		addSequential(new TurnPID(45d)); //turn 90 degrees
			addParallel(new MoveLiftTime(1, 1));
			addParallel(new LowerIngestor());
			addSequential(new DriveTime(0.7, 0.5));
			addSequential(new ExpelCube());
    		
    	} else { //If neither is on our side
    		addSequential(new DriveDistance(.6f, -150d, 15)); //go forward past the line
    		
    		
    	}
    }
}
