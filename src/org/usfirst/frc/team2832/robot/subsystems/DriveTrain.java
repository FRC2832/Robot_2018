package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.Button;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.commands.ArcadeDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;;

/**
 *
 */
public class DriveTrain extends Subsystem {

	//Should these constants be static? Don't know of an example use, but could exist.
	final int SOLENOID_FORWARD_CHANNEL = 0;
	final int SOLENOID_REVERSE_CHANNEL = 1;
	
	final int DRIVE_MOTER_FL = 10;
	final int DRIVE_MOTER_FR = 8;
	final int DRIVE_MOTER_BL = 17;
	final int DRIVE_MOTER_BR = 2;
	
	final Button SHIFT_BUTTON = new Button(Controllers.CONTROLLER_MAIN, Buttons.Y);
	
	private DoubleSolenoid transmission;
	private DifferentialDrive drive;
	private SpeedControllerGroup leftMoters, rightMoters;	
	private WPI_TalonSRX talonFL, talonFR, talonBL, talonBR;
	private Encoder encoderLeft, encoderRight;
	
	private Controls controls;
	
	public DriveTrain(Controls controls) {
		super();
		transmission = new DoubleSolenoid(SOLENOID_FORWARD_CHANNEL, SOLENOID_REVERSE_CHANNEL);
		talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
		talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
		talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
		talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
		leftMoters = new SpeedControllerGroup(talonFL, talonBL);
		leftMoters.setInverted(true);
		rightMoters = new SpeedControllerGroup(talonFR, talonBR);
		drive = new DifferentialDrive(leftMoters, rightMoters);
		//encoderLeft = new Encoder();
	}

    public void initDefaultCommand() {
        setDefaultCommand(new ArcadeDrive(this, controls));
    }
    
    @Override
    public void periodic() {
    	if(controls != null)
        if(controls.getButtonPressed(
        		SHIFT_BUTTON.getController(), 
        		SHIFT_BUTTON.getButton())) {
        	toggleShift();
        	controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kLeftRumble, 0.5d, 0.5d);
        	controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kRightRumble, 0.5d, 0.5d);
        }
    }
    
    public void shift(GEAR gear) {
    	transmission.set(gear.getValue());
    }
    
    public void toggleShift() {
    	transmission.set(transmission.get().equals(GEAR.HIGH.getValue()) ? GEAR.LOW.getValue(): GEAR.HIGH.getValue());
    }
    
    public DifferentialDrive getDrive() {
    	return drive;
    }
    
    public enum GEAR {
    	HIGH, LOW;
    	
    	public Value getValue() {
    		return this == HIGH ? Value.kReverse : Value.kForward;
    	}
    }
}

