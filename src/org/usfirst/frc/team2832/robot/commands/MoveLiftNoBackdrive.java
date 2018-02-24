package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

public class MoveLiftNoBackdrive extends Command {

    private static final int TOLERANCE = 12;

    private double target;
    private boolean moving;

    public MoveLiftNoBackdrive() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        target = Robot.lift.getLiftEncoderPosition();
        moving = false;
    }

    @Override
    protected void execute() {
    	if (Robot.controls.getButtonPressed(ButtonMapping.LEVEL_UP)) {
    		for (int i = 0; i < Lift.liftPositions.length; i++) {
    			if (Lift.liftPositions[i] - Robot.lift.getEncVal() > TOLERANCE) {
    				target = Lift.liftPositions[i];
    				moving = true;
    				break;
    			}
    		}
    	} else if (Robot.controls.getButtonPressed(ButtonMapping.LOWER_TO_BOTTOM)) {
    		target = Lift.liftPositions[0];
    		moving = true;
    	}
    	
    	int manualInput = Robot.controls.getPOV(Controls.Controllers.CONTROLLER_SECCONDARY);
    	if (manualInput != -1) {
    		moving = false;
    		if (manualInput > 90 && manualInput < 270) {
    			Robot.lift.setLiftPower(0.35);
    		} else {
    			Robot.lift.setLiftPower(-0.7); // moves up
    		}
    	} else {
    		if (moving) {
    			if (target == Lift.liftPositions[0]) {
    				Robot.lift.setLiftPower(0.0);
    			} else {
    				Robot.lift.setLiftPower(0.7);
    			}
    		} else {
    			if (Math.abs(Robot.lift.getEncVal() - Lift.liftPositions[0]) <= TOLERANCE) {
    				Robot.lift.setLiftPower(0.0);
    			} else {
    				Robot.lift.setLiftPower(0.35);
    			}
    		}
    	}
    	
    	if (Math.abs(Robot.lift.getEncVal() - target) <= TOLERANCE) {
    		moving = false;
    	}
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
