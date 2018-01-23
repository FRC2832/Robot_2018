package org.usfirst.frc.team2832.robot.commands.auton.groups;

import org.usfirst.frc.team2832.robot.commands.auton.DriveStraightForwardPigeon;
import org.usfirst.frc.team2832.robot.commands.auton.TurnDegrees;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Possibly use this instead of manually building command groups
 */
public class DeploySwitchRight extends CommandGroup {

    public DeploySwitchRight() {
    	addSequential(new DriveStraightForwardPigeon(.5f, 5f));
		addSequential(new TurnDegrees(90f, false));
    }
}
