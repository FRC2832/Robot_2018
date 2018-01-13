package org.usfirst.frc.team2832.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;;

/**
 *
 */
public class DriveTrain extends Subsystem {

	final int SOLENOID_FORWARD_CHANNEL = 9001; //It's over 9000!
	final int SOLENOID_REVERSE_CHANNEL = 9002;
	
	final int DRIVE_MOTER_FL = 9003;
	final int DRIVE_MOTER_FR = 9004;
	final int DRIVE_MOTER_BL = 9005;
	final int DRIVE_MOTER_BR = 9006;
	
	private DoubleSolenoid transmission;
	
	private DifferentialDrive drive;
	
	private SpeedControllerGroup leftMoters;
	private SpeedControllerGroup rightMoters;
	
	private WPI_TalonSRX talonFL;
	private WPI_TalonSRX talonFR;
	private WPI_TalonSRX talonBL;
	private WPI_TalonSRX talonBR;
	
	public DriveTrain() {
		super();
		transmission = new DoubleSolenoid(SOLENOID_FORWARD_CHANNEL, SOLENOID_REVERSE_CHANNEL);
		talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
		talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
		talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
		talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
		leftMoters = new SpeedControllerGroup(talonFL, talonBL);
		rightMoters = new SpeedControllerGroup(talonFR, talonBR);
		drive = new DifferentialDrive(leftMoters, rightMoters);
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

