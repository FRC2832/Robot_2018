package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.Lift.POSITION;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftSide extends CommandGroup {

    public LeftSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
		//addSequential(new SensorFailsafe(0.5d, 120d, ()->Robot.driveTrain.getEncoderPosition(Encoder.LEFT), ()->Robot.driveTrain.getEncoderPosition(Encoder.RIGHT), ()->Robot.driveTrain.getPigeonYaw()));
    	if (gameData.charAt(0) == 'L') { //If the switch is on our side
    		addParallel(new MoveLiftPID(POSITION.SWITCH));
    		addSequential(new DriveDistance(.6f, 150d, 10)); //go forward to switch
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		addParallel(new MoveLiftPID(POSITION.SCALE));
    		addSequential(new DriveDistance(.6f, 291d, 10)); // go forward to scale
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //If neither is on our side
    		addSequential(new DriveDistance(.5f, 120d, 10)); //go forward past the line
    		
    		
    	}
    }
}
