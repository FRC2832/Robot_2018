package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnDegrees;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftSide extends CommandGroup {

    public LeftSide() {
    	
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.charAt(0) == 'L') { //If the switch is on our side
			addParallel(new MoveLiftPID(Lift.Position.SWITCH));
			addSequential(new DriveDistance(.6f, -150d, 10)); //go forward to switch
    		addSequential(new TurnPID(90f)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else if (gameData.charAt(1) == 'L') { //If the scale is on our side
    		addParallel(new MoveLiftPID(Lift.Position.SCALE));
    		addSequential(new DriveDistance(.6f, -291d, 10)); // go forward to scale
    		addSequential(new TurnPID(45f)); //turn 90 degrees
    		addSequential(new ExpelCube());
    		
    	} else { //If neither is on our side
    		addSequential(new DriveDistance(.6f, -150d, 15)); //go forward past the line
    		
    		
    	}
    }
}
