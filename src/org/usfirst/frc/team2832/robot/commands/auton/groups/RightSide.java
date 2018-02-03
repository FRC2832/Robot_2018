package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.Lift.POSITION;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Command seqeuence for right side autonomous
 */
public class RightSide extends CommandGroup {

    public RightSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
		//addSequential(new SensorFailsafe(0.5d, 120d, ()->Robot.driveTrain.getEncoderPosition(ENCODER.LEFT), ()->Robot.driveTrain.getEncoderPosition(ENCODER.RIGHT), ()->Robot.driveTrain.getPigeonYaw()));
    	if (gameData.charAt(0) == 'R') { //If the switch is on our side
    		addParallel(new MoveLiftPID(POSITION.SWITCH));
    		addSequential(new DriveDistance(0.6d, 150d, 10d)); //go forward to switch
    		addSequential(new TurnDegrees(90f, false)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'R') { //If the scale is on our side
    		addParallel(new MoveLiftPID(POSITION.SCALE));
    		addSequential(new DriveDistance(0.6d, 291d, 10d)); // go forward to scale
    		addSequential(new TurnDegrees(90f, false)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //if neither the scale or switch is on our side
    		addSequential(new DriveDistance(0.5d, 120d, 10d)); //go forward past the line
    		
    	}
    	
    }
}
