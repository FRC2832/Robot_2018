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
import org.usfirst.frc.team2832.robot.commands.LowerIngestor;

public class Ingestor extends DiagnosticSubsystem<Ingestor.IngestorFlags> {
	
	final static int INGESTOR_L = 13; // fix these
	final static int INGESTOR_R = 14;
	final static int FORWARD_CHANNEL = 3; // assign these to pneumatics
	final static int REVERSE_CHANNEL = 2;
	final static int DIGITAL_PIN = 1; // get proper channel!
	final static int EXTEND_PINTCHER = 0;
	final static int RETRACT_PINTCHER = 1;
	
	private DigitalInput di;
	private TalonSRX talonL;
	private TalonSRX talonR;
	private DoubleSolenoid tilt;
	private DoubleSolenoid pintcher;
	
	public Ingestor() {
		super();
		di = new DigitalInput(DIGITAL_PIN);
		talonL = new TalonSRX(INGESTOR_L);
		talonR = new TalonSRX(INGESTOR_R);
		tilt = new DoubleSolenoid(FORWARD_CHANNEL, REVERSE_CHANNEL);
		pintcher = new DoubleSolenoid(EXTEND_PINTCHER, RETRACT_PINTCHER);
		setBrakeMode(true);
		// lowerTilt();
		talonR.setInverted(true);
	}
	
	@Override
	protected void initDefaultCommand() {
		
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

	public void lowerTilt() {
		tilt.set(Value.kReverse);
	}

	public void periodic () {
		double tLeft =  Math.abs(Robot.controls.getTrigger(Controls.Controllers.CONTROLLER_SECCONDARY, Hand.kLeft )); // intake
		double tRight = Math.abs(Robot.controls.getTrigger(Controls.Controllers.CONTROLLER_SECCONDARY, Hand.kRight)); // expel
		
		boolean sensorInIR = readDigital();
		SmartDashboard.putBoolean(Dashboard.PREFIX_PROG + "DigitalIntake Val", sensorInIR);
		
		if (Robot.controls.getButton(ButtonMapping.PINTCHER)) {
			if (pintcher.get() == Value.kForward) {
				pintcher.set(Value.kReverse);
			} else {
				pintcher.set(Value.kForward);
			}
		}
		
		if (Robot.controls.getButtonPressed(ButtonMapping.LOWER_TILT.getController(), ButtonMapping.LOWER_TILT.getButton())) {
			tilt.set(Value.kForward);
		} else if (Robot.controls.getButtonPressed(ButtonMapping.RAISE_TILT.getController(), ButtonMapping.RAISE_TILT.getButton())) {
			tilt.set(Value.kReverse);
		} else if (!(getCurrentCommand() instanceof LowerIngestor)){
			tilt.set(Value.kOff);
		}
		
		if (tLeft > 0.05) {
			if (sensorInIR) {
				setMotorSpeed(tLeft * -0.8); // max manual motor speed is 0.8
			} else {
				stopMotors();
			}
		} else if (tRight > 0.05) {
			setMotorSpeed(tRight * 0.8);
		} else {
			stopMotors();
		}
		/*
		if (!sensorInIR) {
			// code to "pinch" the cube
		} else {
			// code to "unpinch" the cube
		}
		*/
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Left Trigger Value",  tLeft);
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Right Trigger Value", tRight);
	}

	enum IngestorFlags {
		TEST
	}
}
