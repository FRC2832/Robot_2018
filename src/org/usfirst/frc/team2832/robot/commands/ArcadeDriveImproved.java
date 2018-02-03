package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.LinearInterpolation;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 * Command for driving around in teleop with arcade drive
 */
public class ArcadeDriveImproved extends Command {

    private LinearInterpolation joystickToDD, joystickToDDPrecision, upshift, downshift;

    public ArcadeDriveImproved() {
        requires(Robot.driveTrain);

        joystickToDD = new LinearInterpolation(new double[]{0, 0.4, 1}, new double[]{0.2, 0.7, 1});
        joystickToDDPrecision = new LinearInterpolation(new double[]{0, 0.6, 1}, new double[]{0.2, 0.5, 1});
        upshift = new LinearInterpolation(new double[]{0, 1}, new double[]{0, 1});
        downshift = new LinearInterpolation(new double[]{0, 1}, new double[]{0, 1});
    }

    protected void initialize() {
    }

    /**
     * Drive based on input from left and right joysticks
     */
    protected void execute() {
        double dD = joystickToDD.interpolate(Math.abs(Robot.controls.getJoystickY(Controllers.CONTROLLER_MAIN, Hand.kLeft)));
        System.out.println(dD);
        double velocity = Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE);
        if(velocity >= upshift.interpolate(dD))
            Robot.driveTrain.shift(DriveTrain.GEAR.HIGH);
        else if(velocity <= downshift.interpolate(dD))
            Robot.driveTrain.shift(DriveTrain.GEAR.LOW);

        Robot.driveTrain.arcadeDrive(-Math.signum(Robot.controls.getJoystickY(Controllers.CONTROLLER_MAIN, Hand.kLeft)) * dD,
                Robot.controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight));
    }

    protected boolean isFinished() {
        return false;
    }

    /**
     * Just a little sanity check
     */
    protected void end() {
        Robot.driveTrain.arcadeDrive(0, 0);
    }

    /**
     * Yep, still sane.
     */
    protected void interrupted() {
        Robot.driveTrain.arcadeDrive(0, 0);
    }
}
