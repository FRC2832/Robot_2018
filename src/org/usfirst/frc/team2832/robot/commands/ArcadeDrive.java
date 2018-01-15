package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArcadeDrive extends Command {

	private DriveTrain driveTrain;
	private Controls controls;
	
    public ArcadeDrive(DriveTrain driveTrain, Controls controls) {
        requires(driveTrain);
        this.driveTrain = driveTrain;
        this.controls = controls;
    }

    protected void initialize() {
    }

    protected void execute() {
    	driveTrain.getDrive().arcadeDrive(controls.getJoystickY(Controllers.CONTROLLER_MAIN, Hand.kLeft), controls.getJoystickX(Controllers.CONTROLLER_MAIN, Hand.kRight));
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
