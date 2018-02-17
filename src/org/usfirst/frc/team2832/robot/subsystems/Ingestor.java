package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Ingestor extends DiagnosticSubsystem<Ingestor.IngestorFlags> {
	
	final static int INGESTOR_L = 13; // fix these
	final static int INGESTOR_R = 14;
	final static int FORWARD_CHANNEL = 3; // assign these to pneumatics
	final static int REVERSE_CHANNEL = 2;
	final static int DIGITAL_PIN = 1; // get proper channel!
	
	private DigitalInput di;
	private TalonSRX talonL;
	private TalonSRX talonR;
	private DoubleSolenoid tilt;

	public Ingestor() {
		super();
		di = new DigitalInput(DIGITAL_PIN);
		talonL = new TalonSRX(INGESTOR_L);
		talonR = new TalonSRX(INGESTOR_R);
		tilt = new DoubleSolenoid(FORWARD_CHANNEL, REVERSE_CHANNEL);
		setBrakeMode(true);
		lowerTilt();
		talonR.setInverted(true);
	}
	
	@Override
	protected void initDefaultCommand() {
		stopMotors();
	}
	
	public void toggleTilt() {
		if (Value.kForward == tilt.get()) {
			tilt.set(Value.kReverse);
			Robot.logger.log("Ingestor", "Tilt reverse");
		} else {
			tilt.set(Value.kForward);
			Robot.logger.log("Ingestor", "Tilt forward");
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
		talonR.set(ControlMode.PercentOutput, sped);
	}
	
	public void stopMotors() {
		talonL.set(ControlMode.PercentOutput, 0.0);
		talonR.set(ControlMode.PercentOutput, 0.0);
	}
	
	public void setBrakeMode(boolean mode) {
		Robot.logger.log("Ingestor", "Brake mode " + (mode ? "enabled" : "disabled"));
    	NeutralMode brakeMode;
    	if (mode) {
    		brakeMode = NeutralMode.Brake;
    	} else {
    		brakeMode = NeutralMode.Coast;
    	}
       	talonL.setNeutralMode(brakeMode);
    	talonR.setNeutralMode(brakeMode);
    }
	/**
	 * Reads in the value from the IR Proximity sensor.
	 * Returns false if obstruction is detected.
	 */
	public boolean readDigital() {
		return di.get();
	}

	public void periodic () {
		double tLeft =  Math.abs(Robot.controls.getTrigger(Controls.Controllers.CONTROLLER_MAIN, Hand.kLeft )); // intake
		double tRight = Math.abs(Robot.controls.getTrigger(Controls.Controllers.CONTROLLER_MAIN, Hand.kRight)); // expel
		
		boolean digitalVal = readDigital();
		SmartDashboard.putBoolean(Dashboard.PREFIX_PROG + "DigitalIntake Val", digitalVal);

		if (  Robot.controls.getButtonPressed(ButtonMapping.TOGGLE_TILT_0.getController(),
											  ButtonMapping.TOGGLE_TILT_0.getButton()) 
		   || Robot.controls.getButtonPressed(ButtonMapping.TOGGLE_TILT_1.getController(), 
				   							  ButtonMapping.TOGGLE_TILT_1.getButton()) 
		   )
		{
			Robot.ingestor.toggleTilt();
		} else if (tLeft > 0.05) {
			if (digitalVal) {
				setMotorSpeed(tLeft * -0.8); // max manual motor speed is 0.8
			} else {
				stopMotors();
			}
		} else if (tRight > 0.05) {
			setMotorSpeed(tRight * 0.8);
		} else {
			stopMotors();
		}
		
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Left Trigger Value",  tLeft);
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Right Trigger Value", tRight);
	}

	enum IngestorFlags {
		TEST
	}
}
