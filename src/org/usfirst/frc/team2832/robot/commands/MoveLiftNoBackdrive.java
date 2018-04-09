package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Robot;

public class MoveLiftNoBackdrive extends Command {
	
	private static final double [] targets = {0, 32*20, 32*30, 32*84}; 	// fix this
	private static final int TOLERANCE = 32; 							// fix this
	
	private int autoMotion; // 0:not moving, 1:moving up, -1:moving down
	private double target;
    
    public MoveLiftNoBackdrive() {
        //requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        target = Robot.lift.getEncVal();
    	autoMotion = 0;
    }

    @Override
    protected void execute() {
    	// Receives controller inputs
    	if (Robot.controls.getButtonPressed(ButtonMapping.RAISE_LIFT)) {
    		autoMotion = 1;
    	}
    	else if (Robot.controls.getButtonPressed(ButtonMapping.LOWER_LIFT)) {
    		autoMotion = -1;
    	}
    	double [] triggers = Robot.lift.getTriggers();
		/* 
		 * Manual control takes priority over preposition control.
		 * 
		 * Raising the lift takes priority over lowering the lift
		 * in both circumstances.
		 */
		if (triggers[0] > 0.05) {
			Robot.lift.setLiftPower(triggers[0]);
			autoMotion = 0;
		}
		else if (triggers[1] > 0.05) {
			Robot.lift.setLiftPower(-triggers[1]);
			autoMotion = 0;
		}
		else if (autoMotion == 1) {
			Robot.lift.setLiftPower(0.7);
			target = getNextTarget();
		}
		else if (autoMotion == -1) {
			Robot.lift.setLiftPower(-0.35);
			target = targets[0];
		}
		else {
			Robot.lift.setLiftPower(0.1);
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
    			if (encVal + TOLERANCE < targets[i]) {
    				return targets[i];
    			}
    		}
    	}
    	else if (autoMotion == -1){
    		/*
        	 * Returns the next lowest
        	 * target encoder count value.
        	 */
    		for (int i = targets.length - 1; i >= 0; i--) {
    			if (encVal - TOLERANCE > targets[i]) {
    				return targets[i];
    			}
    		}
    	}
    	/*
    	 * If no valid targets found or if not
		 * intended to move, returns current position
    	 */
    	return encVal;
    }

    @Override
    protected void end() {Robot.lift.setLiftPower(0);}

    @Override
    protected void interrupted() {Robot.lift.setLiftPower(0);}
    
    @Override
    protected boolean isFinished() {return false;}
    
}
