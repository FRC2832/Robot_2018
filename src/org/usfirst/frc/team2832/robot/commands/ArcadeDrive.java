package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Dashboard;
import org.usfirst.frc.team2832.robot.LinearInterpolation;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 * Command for driving around in teleop with arcade drive
 */
public class ArcadeDrive extends Command {

    private static double SMOOTHING_CONSTANT_UP = 0.04;
    private static double SMOOTHING_CONSTANT_DOWN = 0.074;

    private LinearInterpolation joystickToDD, joystickToDDPrecision, upshift, downshift;
    private double prevVelocity = 0;
    private boolean firstIteration = true, prevLowGear = true;

    public ArcadeDrive() {
        requires(Robot.driveTrain);

        joystickToDD = new LinearInterpolation(new double[]{0, 0.4, 1}, new double[]{0.2, 0.7, 1});
        joystickToDDPrecision = new LinearInterpolation(new double[]{0, 0.6, 1}, new double[]{0.2, 0.5, 1});
        upshift = new LinearInterpolation(new double[]{0.2, 0.8, 0.801, 1}, new double[]{1, 4.5, 6, 7});
        downshift = new LinearInterpolation(new double[]{0.2, 0.8, 0.801, 1}, new double[]{0.5, 4, 5.5, 6.5});
    }

    protected void initialize() {
        Robot.logger.log("Arcade Drive", "Starting");
    }

    /**
     * Drive based on input from left and right joysticks
     */
    protected void execute() {
        double dD = joystickToDD.interpolate(Math.abs(Robot.controls.getJoystickY(Controllers.CONTROLLER_MAIN, Hand.kLeft))); //Driver demand
        double velocity;
        if(firstIteration) {
            velocity = Math.abs(Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE));
            firstIteration = false;
        } else if(prevVelocity > Math.abs(Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE)))
            velocity = Math.abs(Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE)) * SMOOTHING_CONSTANT_DOWN + (1 - SMOOTHING_CONSTANT_DOWN) * prevVelocity;
        else
            velocity = Math.abs(Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE)) * SMOOTHING_CONSTANT_UP + (1 - SMOOTHING_CONSTANT_UP) * prevVelocity;
        prevVelocity = velocity;

        /*SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "FilteredVelocity", velocity);
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "Velocity", Math.abs(Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE)));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "driverDemand", dD);
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityUpshift", upshift.interpolate(dD));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityDownshift", downshift.interpolate(dD));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityLeft", Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.LEFT));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityRight", Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.RIGHT));*/
        if((Math.abs(Robot.controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight)) < 0.2)) {
            if (velocity >= upshift.interpolate(dD)) {
                SmartDashboard.putBoolean("High Gear", true);
                Robot.driveTrain.shift(DriveTrain.GEAR.HIGH);
            } else if (velocity <= downshift.interpolate(dD)) {
                SmartDashboard.putBoolean("High Gear", false);
                Robot.driveTrain.shift(DriveTrain.GEAR.LOW);
            }
        }

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
        Robot.logger.log("Arcade Drive", "Ended");
        Robot.driveTrain.arcadeDrive(0, 0);
    }

    /**
     * Yep, still sane.
     */
    protected void interrupted() {
        Robot.logger.log("Arcade Drive", "Interrupted");
        Robot.driveTrain.arcadeDrive(0, 0);
    }
}
