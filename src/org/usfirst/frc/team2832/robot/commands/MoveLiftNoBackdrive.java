package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

public class MoveLiftNoBackdrive extends Command {
	
	private static final double [] targets = {0, 32*20, 32*30, 32*84};
	private static final int TOLERANCE = 12;
	
	private String autoMotion;
	private double target;
    
    public MoveLiftNoBackdrive() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        target = Robot.lift.getEncVal();
    	autoMotion = "not moving";
    }

    @Override
    protected void execute() {
    	
    	if (Robot.controls.getButtonPressed(ButtonMapping.LIFT_RAISE)) {
    		autoMotion = "moving up";
    	}
    	else if (Robot.controls.getButtonPressed(ButtonMapping.LIFT_LOWER)) {
    		autoMotion = "moving down";
    	}
    	
    	if (autoMotion.equals("not moving")) {
    		/*
        	 * runs manual control using left & right 
        	 * triggers on the secondary controller
        	 */
	    	double [] triggers = Robot.lift.getTriggers();
	    	
	    	if (triggers[0] > 0.05) {
	    		Robot.lift.setLiftPower(triggers[0]);
	    	}
	    	else if (triggers[1] > 0.05) {
	    		Robot.lift.setLiftPower(-triggers[1]);
	    	}
	    	else {
	    		Robot.lift.setLiftPower(0.35);
	    	}
    	}
    	else {
    		/*
    		 * runs prepositioned control using A and 
    		 * B buttons on the secondary controller
    		 */
    		if (autoMotion.equals("moving up")) {
    			Robot.lift.setLiftPower(0.7);
    			target = getNextTarget();
    		}
    		else {
    			Robot.lift.setLiftPower(-0.35);
    			target = getNextTarget();
    		}
    	}
    	/*
    	 * terminates prepositioned motion
    	 * if close enough to the target
    	 */
    	if (Math.abs(target - Robot.lift.getEncVal()) < TOLERANCE) {
    		autoMotion = "not moving";
    	}
    	
    }
    
    private double getNextTarget() {
    	
    	double encVal = Robot.lift.getEncVal();
    	
    	if (autoMotion.equals("moving up") ) {
    		/*
        	 * returns the next highest
        	 * target encoder count value
        	 */
    		for (int i = 0; i < targets.length; i++) {
    			if (encVal < targets[i]) {
    				return targets[i];
    			}
    		}
    	}
    	else {
    		/*
        	 * returns the next lowest
        	 * target encoder count value
        	 */
    		for (int i = targets.length - 1; i >= 0; i--) {
    			if (encVal > targets[i]) {
    				return targets[i];
    			}
    		}
    	}
    	/*
    	 * if no valid targets found
    	 * returns current position
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
