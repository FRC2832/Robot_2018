package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.ButtonMapping;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
		// set to break mode
	}
	
	@Override
	protected void initDefaultCommand() {
		stopMotors();
	}
	
	public void launch() {
		// FIGURE THIS OUT
		// via left trigger ???
	}
	
	public void setMotorSpeed(double sped) {
		talonL.set(ControlMode.PercentOutput, sped);
		talonR.set(ControlMode.PercentOutput, -sped);
	}
	
	public void stopMotors() {
		talonL.set(ControlMode.PercentOutput, 0.0);
		talonR.set(ControlMode.PercentOutput, -0.0);
	}
	
	public void setBreakMode() {
		// uses TalonSRX not WPI_TalonSRX
		// to be used during auton & teleop
	}
	
	public void setCoastMode() {
		// uses TalonSRX not WPI_TalonSRX
		// to be used towards the end of teleop
	}
	
	public void periodic () {
		
	}

}
