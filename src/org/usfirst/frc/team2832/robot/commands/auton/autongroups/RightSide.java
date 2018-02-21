package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnDegrees;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Command seqeuence for right side autonomous
 */
public class RightSide extends CommandGroup {

    public RightSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
		addParallel(new LowerIngestor());
		//addSequential(new SensorFailsafe(0.5d, 120d, ()->Robot.driveTrain.getEncoderPosition(ENCODER.LEFT), ()->Robot.driveTrain.getEncoderPosition(ENCODER.RIGHT), ()->Robot.driveTrain.getPigeonYaw()));
    	if (gameData.charAt(0) == 'R') { //If the switch is on our side
    		addParallel(new MoveLiftPID(Lift.Position.SWITCH));
    		addSequential(new DriveDistance(0.6d, -150d, 10d)); //go forward to switch
    		addSequential(new TurnPID(-90f)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'R') { //If the scale is on our side
    		addParallel(new MoveLiftPID(Lift.Position.SCALE));
    		addSequential(new DriveDistance(0.6d, -291d, 10d)); // go forward to scale
    		addSequential(new TurnPID(-90f)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //if neither the scale or switch is on our side
    		addSequential(new DriveDistance(0.5d, -120d, 1d)); //go forward past the line
    		
    	}
    	
    }
}
