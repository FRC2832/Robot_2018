package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Robot;

public class MoveLiftNoBackdrive extends Command {
	
	private static final double [] targets = {0, 32*20, 32*30, 32*84};
	private static final int TOLERANCE = 12;
	
	private int autoMotion; // 0 is not moving, -1 is moving down, 1 is moving up
	private double target;
    
    public MoveLiftNoBackdrive() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        target = Robot.lift.getEncVal();
    	autoMotion = 0;
    }

    @Override
    protected void execute() {
    	
    	if (Robot.controls.getButtonPressed(ButtonMapping.RAISE_LIFT)) {
    		autoMotion = 1;
    	}
    	else if (Robot.controls.getButtonPressed(ButtonMapping.LOWER_LIFT)) {
    		autoMotion = -1;
    	}
    	
    	double [] triggers = Robot.lift.getTriggers();
    	
    	if (autoMotion == 0) {
    		/*
        	 * Runs manual control using left & right 
        	 * triggers on the secondary controller.
        	 * 
        	 * Takes priority over prepositioned control.
        	 * 
        	 * Moving lift up takes priority over moving
        	 * lift down.
        	 */
	    	if (triggers[0] > 0.05) {
	    		Robot.lift.setLiftPower(triggers[0]);
	    		Robot.logger.log("lift", "moving up");
	    	}
	    	else if (triggers[1] > 0.05) {
	    		Robot.lift.setLiftPower(-triggers[1]);
	    		Robot.logger.log("lift", "moving down");
	    	}
	    	else {
	    		Robot.lift.setLiftPower(0.1); // testing
	    	}
    	}
    	else {
    		/*
    		 * Runs prepositioned control using A and 
    		 * B buttons on the secondary controller.
    		 */
    		if (autoMotion == 1) {
    			Robot.lift.setLiftPower(0.7);
    			Robot.logger.log("lift", "moving up automatic");
    			target = getNextTarget();
    		}
    		else {
    			Robot.lift.setLiftPower(-0.35);
    			Robot.logger.log("lift", "moving down automatic");
    			target = getNextTarget();
    		}
    	}
    	/*
    	 * Terminates prepositioned motion
    	 * if close enough to the target.
    	 */
    	if (Math.abs(target - Robot.lift.getEncVal()) < TOLERANCE) {
    		autoMotion = 0;
    	}
    	
    	SmartDashboard.putNumber("autoMotionMLNB", autoMotion);
    	SmartDashboard.putNumberArray("triggersLR", triggers);
    	
    }
    
    private double getNextTarget() {
    	
    	double encVal = Robot.lift.getEncVal();
    	
    	if (autoMotion == 1) {
    		/*
        	 * Returns the next highest
        	 * target encoder count value.
        	 */
    		for (int i = 0; i < targets.length; i++) {
    			if (encVal < targets[i]) {
    				return targets[i];
    			}
    		}
    	}
    	else {
    		/*
        	 * Returns the next lowest
        	 * target encoder count value.
        	 */
    		for (int i = targets.length - 1; i >= 0; i--) {
    			if (encVal > targets[i]) {
    				return targets[i];
    			}
    		}
    	}
    	/*
    	 * If no valid targets found
    	 * returns current position.
    	 */
    	return encVal;
    }

    @Override
    protected void end() {
        Robot.lift.setLiftPower(0);
    }

    @Override
    protected void interrupted() {
        Robot.lift.setLiftPower(0);
    }
    
    @Override
    protected boolean isFinished() {
        return false;
    }


}
