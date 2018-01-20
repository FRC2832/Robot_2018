package org.usfirst.frc.team2832.robot.subsystems;


import org.usfirst.frc.team2832.robot.Controls;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * The drive train subsystem which handles encoders, the transmission, and driving
 */
public class Lift extends Subsystem {

	final static int COLLAPSE_FORWARD_CHANNEL = 2;
	final static int COLLAPSE_REVERSE_CHANNEL = 3;
	
	final static int LIFT_MOTOR = 0;
	
	private static final double ENCODER_COUNT_TO_INCH = 0;
		
	private DoubleSolenoid collapse;
	private WPI_TalonSRX talonLift;
	private TalonSRX talonPhoenixLift;
		
	public Lift() {
		super();
		talonLift = new WPI_TalonSRX(LIFT_MOTOR);
		talonPhoenixLift = new TalonSRX(LIFT_MOTOR);
		//collapse = new DoubleSolenoid(COLLAPSE_FORWARD_CHANNEL, COLLAPSE_REVERSE_CHANNEL);
	}
	
	public void setLiftPositon(POSITION position) {
		
	}
	
    public void initDefaultCommand() {
        //setDefaultCommand(new ArcadeDrive(this, controls));
    }
    
    @Override
    public void periodic() {
    	
    }
    
    public enum POSITION {
    	SWITCH(0),
    	SCALE(1);
    	
    	public double height;
    	
    	private POSITION(int height) {
    		this.height = height;
    	}
    }
}

