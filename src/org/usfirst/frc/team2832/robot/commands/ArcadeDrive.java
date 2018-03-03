package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Dashboard;
import org.usfirst.frc.team2832.robot.LinearInterpolation;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
     * Max rate of change is .5 * pi / FADE_TIME
     */
	private static final double FADE_TIME = .75;
	/**Stores the speed from the interpolation tables from the last time the joystick moved.*/
	private double prevDDChange = 0;
	private double dDTo = 0;
	/**Stores when previousSpeed was last set, in milliseconds.*/
	private long timeDDChanged = Long.MIN_VALUE;
	
	private boolean doingFadeCurve = false;
	
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
        double fadedDD = dD;
        if(Math.abs(dD - prevDDChange) > .01) { //speed has changed since the end of the last started fade.
        	if(System.currentTimeMillis() > FADE_TIME * 1000 + timeDDChanged) {
        		//have to start the fade curve
        		if(!doingFadeCurve) {
        			timeDDChanged = System.currentTimeMillis();
        			dDTo = dD;//used only for mid-curve change detection
        			doingFadeCurve = true;
        		}else{
        			//have to end the fade curve
        			prevDDChange = dD;
        			doingFadeCurve = false;
        		}
        	}else{
        		if(Math.abs(dD - dDTo) > .0001) { //driver demand changed again mid curve, so we will restart 
        						 //the curve. Better than reversing it mid-curve, most of the time.
        			prevDDChange = fade(prevDDChange, dD, ((double)(System.currentTimeMillis()-timeDDChanged))/1000d);
        			timeDDChanged = System.currentTimeMillis();
        			dDTo = dD;
        		}
        		fadedDD = fade(prevDDChange, dD, ((double)(System.currentTimeMillis()-timeDDChanged))/1000d);
        	}
        }
        
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
                Robot.controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight));
        else
        	Robot.driveTrain.arcadeDrive(0, 0);
    }

    /**
	 * Produces the fade curve for regulating velocity over time
	 * 
	 * @param start the speed at time=0
	 * @param end the speed at time=FADE_TIME
	 * @param time the current time, relative to the start of the curve. 
	 */
	
	public static double fade(double start, double end, double time) {
		return (start + end) / 2 + .5 * (start - end) * Math.cos(Math.PI * time/FADE_TIME);
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
