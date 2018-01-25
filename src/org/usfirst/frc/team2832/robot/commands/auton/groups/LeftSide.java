package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LeftSide extends CommandGroup {

    public LeftSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	if (gameData.charAt(0) == 'L') { //If the switch is on our side
    		addSequential(new DriveStraightForwardPigeon(.5f, 5f)); //go forward to switch
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		addSequential(new DriveStraightForwardPigeon(.5f, 9f)); // go forward to scale
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    	} else { //If neither is on our side
    		addSequential(new DriveStraightForwardPigeon(.5f, 6f)); //go forward past the line
    	}
    	
    }
}
