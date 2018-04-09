package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**To be run on a full field, in center position.*/
public class SensorTest extends CommandGroup {
	public SensorTest(boolean onCart) {
		if(onCart) {
			
		}else {
			addSequential(new DriveDistance(.5, 12, 5));
			addSequential(new TurnPID(90));
			addSequential(new DriveDistance(.5, 30, 10));
		}		
	}
}
