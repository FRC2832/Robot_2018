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
public class RightSideTime extends CommandGroup {

    public RightSideTime() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	if (gameData.charAt(0) == 'R') { //If the switch is on our side
    		addParallel(new MoveLiftPID(POSITION.SWITCH));
    		addSequential(new DriveStraightForwardPigeon(.5f, 5f)); //go forward to switch
    		addSequential(new TurnDegrees(90f, false)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'R') { //If the scale is on our side
    		addParallel(new MoveLiftPID(POSITION.SCALE));
    		addSequential(new DriveStraightForwardPigeon(.5f, 9f)); // go forward to scale
    		addSequential(new TurnDegrees(90f, false)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //if neither the scale or switch is on our side
    		addSequential(new DriveStraightForwardPigeon(.5f, 6f)); //go forward past the line
    	}
    	
    }
}
