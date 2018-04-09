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
import org.usfirst.frc.team2832.robot.statemachine.StandardState;
import org.usfirst.frc.team2832.robot.statemachine.WarriorSubsystem;

public class Ingestor extends WarriorSubsystem<Ingestor.IngestorFlags> {
	
	final static int INGESTOR_L = 13; // fix these
	final static int INGESTOR_R = 14;
	final static int FORWARD_CHANNEL = 2; // assign these to pneumatics
	final static int REVERSE_CHANNEL = 3;
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
		if(Robot.isReal())
			talonR.setInverted(true);
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
		//Robot.logger.log("Ingestor", "Brake mode " + (mode ? "enabled" : "disabled"));
		if(Robot.isSimulation())
			return;
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
		tilt.set(Value.kForward);
	}

	public void unpinch() {
		pintcher.set(Value.kReverse);
	}

	@Override
	public void update () {
		boolean sensorInIR = readDigital();
		SmartDashboard.putBoolean(Dashboard.PREFIX_PROG + "DigitalIntake Val", sensorInIR);
		
		if (Robot.controls.getButton(ButtonMapping.PINTCHER_0) || Robot.controls.getButton(ButtonMapping.PINTCHER_1)) {
			unpinch();
		}
		else {
			pintcher.set(Value.kForward);
		}
		
		if (Robot.controls.getButton(ButtonMapping.LOWER_TILT.getController(), ButtonMapping.LOWER_TILT.getButton())) {
			tilt.set(Value.kForward);
		} else if (Robot.controls.getButton(ButtonMapping.RAISE_TILT.getController(), ButtonMapping.RAISE_TILT.getButton())) {
			tilt.set(Value.kReverse);
		} else if (getState() instanceof StandardState && !(((StandardState)getState()).getModule() instanceof LowerIngestor)){
			tilt.set(Value.kOff);
		}
		talonL.set(ControlMode.PercentOutput, (Math.abs(Robot.controls.getJoystickY(Controls.Controllers.CONTROLLER_SECCONDARY, Hand.kLeft)) > .1)?-Robot.controls.getJoystickY(Controls.Controllers.CONTROLLER_SECCONDARY, Hand.kLeft):0);
		talonR.set(ControlMode.PercentOutput, (Math.abs(Robot.controls.getJoystickY(Controls.Controllers.CONTROLLER_SECCONDARY, Hand.kRight)) > .1)?-Robot.controls.getJoystickY(Controls.Controllers.CONTROLLER_SECCONDARY, Hand.kRight):0);
		
		
	}

	enum IngestorFlags {
		TEST
	}
}
