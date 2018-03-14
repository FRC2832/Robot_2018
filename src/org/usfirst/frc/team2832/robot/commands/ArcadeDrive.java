package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Dashboard;
import org.usfirst.frc.team2832.robot.LinearInterpolation;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 * Command for driving around in teleop with arcade drive
 */
public class ArcadeDrive extends Command {

    private final double SMOOTHING_CONSTANT_UP = 0.04;
    private final double SMOOTHING_CONSTANT_DOWN = 0.074;
    private final double LIFT_LIMIT = 0.82;

    private LinearInterpolation joystickToDD, joystickToDDPrecision, upshift, downshift;
    private double prevVelocity = 0;
    private boolean firstIteration = true, prevLowGear = true;
    
    /**Time for the fade curve to complete, in seconds.
     * Max rate of change is .5 * pi / time
     */
	private static final double FADE_TIME_FORWARD = 1.0, FADE_TIME_BACK = .85;
	/**Stores the speed from the interpolation tables from the last time the joystick moved.*/
	private double prevDDChange = 0;
	private double dDTo = 0;
	/**Stores when previousSpeed was last set, in milliseconds.*/
	private long timeDDChanged = Long.MIN_VALUE;
	
	private boolean doingFadeCurve = false;
	
    public ArcadeDrive() {
        requires(Robot.driveTrain);

        joystickToDD = new LinearInterpolation(new double[]{0, 0.1, 0.4, 1}, new double[]{0, 0.2, 0.7, 1});
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
        //apply fade curve
        double fadedDD = dD;
        /* Add another / at the start of this line to uncomment this quickly
        long time = (long) Timer.getFPGATimestamp() * 1000;
        double fadeTime = dD < prevDDChange ? FADE_TIME_BACK : FADE_TIME_FORWARD;
        if(Math.abs(dD - prevDDChange) > .08) { //speed has changed since the end of the last started fade.
        	if(time > fadeTime * 1000 + timeDDChanged) {//beyond the edge of the last curve to start
        		if(!doingFadeCurve) {
        			//have to start the fade curve, since the last one to start already ended
        			timeDDChanged = time;
        			dDTo = dD;//used only for mid-curve change detection
        			doingFadeCurve = true;
        		}else{
        			//have to end the fade curve, since the last one to start is still running
        			prevDDChange = dD;
        			doingFadeCurve = false;
        		}
        	}else{
        		if(Math.abs(dD - dDTo) > .1 && Math.signum(dD-dDTo) == Math.signum(prevDDChange-dDTo)) { 
        			//driver demand changed again mid curve, so we will restart 
        			// the curve. Better than reversing it mid-curve, most of the time.
        			// However, it is worse if the curve is changing the same direction that it already did. 
        			// for instance, if the driver demand was fading between .2 and .5, and is now going to .7.
        			
        			//set curve starting point to the value the fade curve currently produces
        			prevDDChange = fade(prevDDChange, dD, ((double)(time-timeDDChanged))/1000d, fadeTime);
        			timeDDChanged = time;
        			dDTo = dD;
        		}
        		fadedDD = fade(prevDDChange, dD, ((double)(time-timeDDChanged))/1000d, fadeTime);
        	}
        }else {//small enough of a difference to not need to worry about. 
        	prevDDChange = dD;
        }
        //*/
        /*SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "FilteredVelocity", velocity);
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "Velocity", Math.abs(Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.AVERAGE)));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "driverDemand", dD);
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityUpshift", upshift.interpolate(dD));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityDownshift", downshift.interpolate(dD));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityLeft", Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.LEFT));
        SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "velocityRight", Robot.driveTrain.getEncoderVelocity(DriveTrain.Encoder.RIGHT));*/
        if((Math.abs(Robot.controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight)) < 0.2)) {
            if (velocity >= upshift.interpolate(fadedDD)) {
                SmartDashboard.putBoolean("High Gear", true);
                Robot.driveTrain.shift(DriveTrain.GEAR.HIGH);
            } else if (velocity <= downshift.interpolate(fadedDD)) {
                SmartDashboard.putBoolean("High Gear", false);
                Robot.driveTrain.shift(DriveTrain.GEAR.LOW);
            }
        }
        if(!DriverStation.getInstance().isAutonomous())
        	Robot.driveTrain.arcadeDrive(-Math.signum(Robot.controls.getJoystickY(Controllers.CONTROLLER_MAIN, Hand.kLeft)) * fadedDD,
        				Robot.controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight) * LIFT_LIMIT);
        else
        	Robot.driveTrain.arcadeDrive(0, 0);
    }

    /**
	 * Produces the fade curve for regulating velocity and acceleration over time when driver demand changes
	 * Acceleration is changed to and from 0 gradually through the use of a cosine curve.
	 * 
	 * @param start the speed at time=0
	 * @param end the speed at time=FADE_TIME
	 * @param time the current time in seconds, relative to the start of the curve. 
	 * @param fadeTime the current time the curve will take to complete. 
	 */
	
	public static double fade(double start, double end, double time, double fadeTime) {
		return (start + end) / 2 + .5 * (start - end) * Math.cos(Math.PI * time/fadeTime);
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
