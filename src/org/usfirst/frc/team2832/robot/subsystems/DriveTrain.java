package org.usfirst.frc.team2832.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 *
 */
public class DriveTrain extends Subsystem {

	final int SOLENOID_FORWARD_CHANNEL = 9001; //It's over 9000!
	final int SOLENOID_REVERSE_CHANNEL = 9002;
	
	private DoubleSolenoid transmission;
	
	public DriveTrain() {
		super();
		transmission = new DoubleSolenoid(SOLENOID_FORWARD_CHANNEL, SOLENOID_REVERSE_CHANNEL);
	}

    public void initDefaultCommand() {
        
    }
    
    @Override
    public void periodic() {
        
    }
    
    public void shift(GEAR gear) {
    	transmission.set(gear.getValue());
    }
    
    public enum GEAR {
    	HIGH(Value.kReverse), 
    	LOW(Value.kForward);
    	
    	private Value value;
    	GEAR(Value value) {
    		this.value = value;
    	}
    	public Value getValue() {
    		return value;
    	}
    }
}

