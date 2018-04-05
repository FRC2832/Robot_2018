package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Dashboard.SIDE;
import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Sequence of commands to score on the SWITCH from either side
 * takes a side as a parameter
 */
public class ScoreSwitch extends CommandGroup {

    public ScoreSwitch(SIDE side) {
    	
        if (side == SIDE.RIGHTSIDE) {
        	addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(.75f, -155d, 2d)); 
			addParallel(new MoveLiftTime(1.2, 1));
    		addSequential(new TurnPID(-90f)); 
    		addSequential(new DriveDistance(.6f, -4d, 2d)); 
			addSequential(new ExpelCube());
        }
        
        if (side == SIDE.LEFTSIDE) {
        	addParallel(new LowerIngestor(.5));
			addSequential(new DriveDistance(.75f, -155d, 2d)); 
			addParallel(new MoveLiftTime(1.2, 1));
    		addSequential(new TurnPID(90f)); 
			addSequential(new DriveDistance(.6f, -4d, 2d)); 
			addSequential(new ExpelCube());
        }
    }
    
}
