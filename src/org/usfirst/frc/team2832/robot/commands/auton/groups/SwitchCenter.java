package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive from center to a switch side
 */
public class SwitchCenter extends CommandGroup {

    public SwitchCenter() {
    	addSequential(new DriveStraightForwardPigeon(.5f, 1f));
		addSequential(new TurnDegrees(45f, true));
		addSequential(new DriveStraightForwardPigeon(.5f, 2f));
		addSequential(new TurnDegrees(45f, false));
    }
}
