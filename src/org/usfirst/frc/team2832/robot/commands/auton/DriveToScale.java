package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This was intended to be a hybrid of commands to drive forward a distance, adjust angle with pigeon, 
 * look for lines, and possibly watch the distance from the wall
 */
public class DriveToScale extends Command {

    public DriveToScale(DriveTrain driveTrain) {
        requires(driveTrain);
    }

    protected void initialize() {
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
