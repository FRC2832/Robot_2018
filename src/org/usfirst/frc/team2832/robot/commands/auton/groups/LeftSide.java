package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.auton.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.DriveForwardOnSensorFail;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.Encoder;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftSide extends CommandGroup {

    public LeftSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
		//addSequential(new SensorFailsafe(0.5d, 120d, ()->Robot.driveTrain.getEncoderPosition(Encoder.LEFT), ()->Robot.driveTrain.getEncoderPosition(Encoder.RIGHT), ()->Robot.driveTrain.getPigeonYaw()));
    	if (gameData.charAt(0) == 'L') { //If the switch is on our side
    		addSequential(new DriveDistance(.6f, 150d, 10)); //go forward to switch
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		addSequential(new DriveDistance(.6f, 291d, 10)); // go forward to scale
    		addSequential(new TurnDegrees(90f, true)); //turn 90 degrees
    	} else { //If neither is on our side
    		addSequential(new DriveDistance(.5f, 120d, 10)); //go forward past the line
    	}
    }
}
