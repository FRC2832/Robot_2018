package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {
	//get the actual values for these
	final static int EXTENDER_FORWARD = 6;
	final static int EXTENDER_REVERSE = 7;
	final static int WINCH_MOTOR = 9;
	final static int HOOK_MOTOR = 13;
	
	private DoubleSolenoid extender;
	private TalonSRX winchMotor;
	private TalonSRX hook;
	
	
	public Climber() {
		super();
		extender = new DoubleSolenoid(EXTENDER_FORWARD, EXTENDER_REVERSE);
		winchMotor = new TalonSRX(WINCH_MOTOR);
	}
	
	@Override
	protected void initDefaultCommand() {
		setWinchMotorSpeed(0);
		setWinchMotorSpeed(0);
		
	}
	
	public void setWinchMotorSpeed(double speed) {
		winchMotor.set(ControlMode.PercentOutput, speed);
	}
	
	public void setHookMotorSpeed(double speed) {
		winchMotor.set(ControlMode.PercentOutput, speed);
	}
	
	public void extendPiston() {
		extender.set(Value.kForward);
	}
	
	public void retractPiston() {
		extender.set(Value.kReverse);
	}
	
}
