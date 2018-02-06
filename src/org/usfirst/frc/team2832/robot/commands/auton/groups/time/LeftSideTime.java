package org.usfirst.frc.team2832.robot.commands.auton.groups.time;

import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.Lift.POSITION;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LeftSideTime extends CommandGroup {

    public LeftSideTime() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	if (gameData.charAt(0) == 'L') { //If the switch is on our side
    		addParallel(new MoveLiftPID(POSITION.SWITCH));
    		addSequential(new DriveStraightForwardPigeon(.5f, 5f)); //go forward to switch
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		addParallel(new MoveLiftPID(POSITION.SCALE));
    		addSequential(new DriveStraightForwardPigeon(.5f, 9f)); // go forward to scale
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //If neither is on our side
    		addSequential(new DriveStraightForwardPigeon(.5f, 6f)); //go forward past the line
    	}
    	
    }
}
