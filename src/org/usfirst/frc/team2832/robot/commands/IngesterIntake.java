package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class IngesterIntake extends Command {
	
	public IngesterIntake() {
		
	}

	public IngesterIntake(String direction) {
		requires(Robot.ingestor);
	}
	
	protected void execute() {
		if (Robot.controls.getButtonPressed(Robot.ingestor.INTAKE_BUTTON.getController(), 
											Robot.ingestor.INTAKE_BUTTON.getButton())) {
			System.out.println("intake");
			Robot.ingestor.setMotorSpeed(0.3); // May need to be inverted & speed adjusted
		} else if (Robot.controls.getButtonPressed(Robot.ingestor.EXPEL_BUTTON.getController(), 
												   Robot.ingestor.EXPEL_BUTTON.getButton())) {
			System.out.println("expel");
			Robot.ingestor.setMotorSpeed(-0.3); // May need to be inverted & speed adjusted
		} else {
			Robot.ingestor.stopMotors();
		}
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
		
	}

}
