package org.usfirst.frc.team2832.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**Unpacks the rails to their maximum height.*/
public class Unpacker extends Subsystem {

	boolean extended = false;
	
	DoubleSolenoid solenoid;
	
	final int UNPACK_FORWARD_CHANNEL = 3;
	final int UNPACK_REVERSE_CHANNEL = 4;
	
	public Unpacker() {
		super();
		solenoid = new DoubleSolenoid(UNPACK_FORWARD_CHANNEL, UNPACK_REVERSE_CHANNEL);
	}
	
	public void toggleExtend() {
		solenoid.set(extended ? Value.kReverse : Value.kForward);
		extended = !extended;
	}
	
	/**Turns off the solenoids. Fairly sure this should be called for when disabling the robot.*/
	public void setOff() {
		solenoid.set(Value.kOff);
		extended = false;
	}
	
	public boolean isExtended() {
		return extended;
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}