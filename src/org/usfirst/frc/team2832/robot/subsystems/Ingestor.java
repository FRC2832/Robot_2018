package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Ingestor extends Subsystem {
	
	final static int INGESTOR_L = 9000; // fix these
	final static int INGESTOR_R = 9001;
	final static int FORWARD_CHANNEL = 4; // assign these to pneumatics
	final static int REVERSE_CHANNEL = 5;
	
	private TalonSRX talonL;
	private TalonSRX talonR;
	private DoubleSolenoid tilt;
	
	public final ButtonMapping TOGGLE_TILT_0 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.A);
	public final ButtonMapping TOGGLE_TILT_1 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.X);
	
	public Ingestor() {
		super();
		talonL = new TalonSRX(INGESTOR_L);
		talonR = new TalonSRX(INGESTOR_R);
		tilt = new DoubleSolenoid(FORWARD_CHANNEL, REVERSE_CHANNEL);
		setBrakeMode(true);
		lowerTilt();
	}
	
	@Override
	protected void initDefaultCommand() {
		stopMotors();
	}
	
	public void toggleTilt() {
		if (Value.kForward == tilt.get()) {
			tilt.set(Value.kReverse);
		} else {
			tilt.set(Value.kForward);
		}
	}
	
	public void lowerTilt() {
		tilt.set(Value.kReverse);
	}
	
	public void raiseTilt() {
		tilt.set(Value.kForward);
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
		talonR.set(ControlMode.PercentOutput, 0.0);
	}
	
	public void setBrakeMode(boolean mode) {
    	NeutralMode brakeMode;
    	if (mode) {
    		brakeMode = NeutralMode.Brake;
    	} else {
    		brakeMode = NeutralMode.Coast;
    	}
       	talonL.setNeutralMode(brakeMode);
    	talonR.setNeutralMode(brakeMode);
    }
	
	public void periodic () {
		double tLeft = Robot.controls.getTrigger(Controls.Controllers.CONTROLLER_MAIN, Hand.kLeft); // intake
		double tRight = Robot.controls.getTrigger(Controls.Controllers.CONTROLLER_MAIN, Hand.kRight); // expel
		
		if (  Robot.controls.getButtonPressed(Robot.ingestor.TOGGLE_TILT_0.getController(), 
											  Robot.ingestor.TOGGLE_TILT_0.getButton()) 
		   || Robot.controls.getButtonPressed(Robot.ingestor.TOGGLE_TILT_1.getController(), 
											  Robot.ingestor.TOGGLE_TILT_1.getButton()) 
		   )
		{
			System.out.println("toggle tilt");
			Robot.ingestor.toggleTilt();
		} else if (tLeft > 0) { // fix this [-1,1]
			setMotorSpeed(tLeft); // check sign and include motor speed conversion
		} else if (tRight > 0) { // fix this [-1,1]
			setMotorSpeed(tRight); // check sign and include motor speed conversion
		} else {
			stopMotors();
		}
	}

}
