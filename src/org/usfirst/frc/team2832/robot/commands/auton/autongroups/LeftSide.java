package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnDegrees;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class LeftSide extends CommandGroup {

    public LeftSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.charAt(0) == 'L') { //If the switch is on our side
    		addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(.6f, -150d, 10)); //go forward to switch
			addParallel(new MoveLiftTime(1.2, 1));
    		addSequential(new TurnPID(90f)); //turn 90 degrees
			addSequential(new DriveDistance(.6f, -10d, 10)); //go forward to switch
			addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		addSequential(new TimedCommand(.5));
    		addParallel(new MoveLiftTime(2.7, 1));
    		addSequential(new DriveDistance(.6f, -288d, 10)); // go forward to scale    		
    		addSequential(new LowerIngestor(.1));
    		addSequential(new TimedCommand(.5));
    		addSequential(new TurnPID(50f)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //If neither is on our side
    		addSequential(new DriveDistance(.6f, -150d, 15)); //go forward past the line
    		
    		
    	}
    }
}
