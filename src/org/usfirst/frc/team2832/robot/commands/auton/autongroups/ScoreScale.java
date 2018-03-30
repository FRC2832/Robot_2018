package org.usfirst.frc.team2832.robot.commands.auton.autongroups;

import org.usfirst.frc.team2832.robot.Dashboard.SIDE;
import org.usfirst.frc.team2832.robot.commands.LowerIngestor;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * Sequence of commands to score on the SCALE from either side
 * takes a side as a parameter
 */
public class ScoreScale extends CommandGroup {

    public ScoreScale(SIDE side) {
    	if (side == SIDE.LEFTSIDE) {
    		addSequential(new TimedCommand(.5));
			addParallel(new MoveLiftTime(4, 1));
			addSequential(new DriveDistance(.8f, -278d, 10)); 
			addSequential(new LowerIngestor(.1));
			addSequential(new TimedCommand(.5));
			addSequential(new TurnPID(50f)); 
			addSequential(new ExpelCube());
    	}
    	
    	
    	if (side == SIDE.RIGHTSIDE) {
    		addSequential(new TimedCommand(.5));
    		addParallel(new MoveLiftTime(4, 1));
    		addSequential(new DriveDistance(.8f, -278d, 10)); 
    		addSequential(new LowerIngestor(.1));
            addSequential(new TimedCommand(.5));
    		addSequential(new TurnPID(-50f));
    		addSequential(new ExpelCube());
    	}
    }
}
