package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.ButtonMapping;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Ingestor extends Subsystem {
	
	final static int INGESTOR_L = 9000; // Fix these
	final static int INGESTOR_R = 9001;
	
	private TalonSRX talonL;
	private TalonSRX talonR;
	
	public final ButtonMapping EXPEL_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_LEFT);
	public final ButtonMapping INTAKE_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_RIGHT);
	
	public Ingestor() {
		super();
		talonL = new TalonSRX(INGESTOR_L);
		talonR = new TalonSRX(INGESTOR_R);
		setBrakeMode(true);
	}
	
	@Override
	protected void initDefaultCommand() {
		stopMotors();
	}
	
	public void launch() {
		// FIGURE THIS OUT
		// via left trigger ???
	}
	
	public void setMotorSpeed(double speed) {
		talonL.set(ControlMode.PercentOutput, speed);
		talonR.set(ControlMode.PercentOutput, -speed);
	}
	
	public void stopMotors() {
		talonL.set(ControlMode.PercentOutput, 0.0);
		talonR.set(ControlMode.PercentOutput, -0.0);
	}
	
	public void setBrakeMode(boolean mode) {
    	NeutralMode brakeMode;
    	if(mode) 
    		brakeMode = NeutralMode.Brake;
    	else
    		brakeMode = NeutralMode.Coast;
       	talonL.setNeutralMode(brakeMode);
    	talonR.setNeutralMode(brakeMode);
    }
	
	public void periodic () {
		
	}

}
