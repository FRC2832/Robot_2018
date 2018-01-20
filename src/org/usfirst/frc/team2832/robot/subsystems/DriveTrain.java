package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.Button;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.commands.ArcadeDrive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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

	final static int SOLENOID_FORWARD_CHANNEL = 0;
	final static int SOLENOID_REVERSE_CHANNEL = 1;
	
	final static int DRIVE_MOTER_FL = 10;
	final static int DRIVE_MOTER_FR = 8;
	final static int DRIVE_MOTER_BL = 17;
	final static int DRIVE_MOTER_BR = 2;
	
	private static final double ENCODER_COUNT_TO_INCH = 0;
	
	final Button SHIFT_BUTTON = new Button(Controllers.CONTROLLER_MAIN, Buttons.Y);
	
	private DoubleSolenoid transmission;
	private DifferentialDrive drive;
	private SpeedControllerGroup leftMotors, rightMotors;	
	private WPI_TalonSRX talonFL, talonFR, talonBL, talonBR;
	private TalonSRX talonPhoenixFL, talonPhoenixFR;
	private Encoder encoderLeft, encoderRight;
	
	private Controls controls;
	
	public DriveTrain(Controls controls) {
		super();
		transmission = new DoubleSolenoid(SOLENOID_FORWARD_CHANNEL, SOLENOID_REVERSE_CHANNEL);
		talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
		talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
		talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
		talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
		talonPhoenixFL = new TalonSRX(DRIVE_MOTER_FL);
		talonPhoenixFR = new TalonSRX(DRIVE_MOTER_FR);
		leftMotors = new SpeedControllerGroup(talonFL, talonBL);
		rightMotors = new SpeedControllerGroup(talonFR, talonBR);
		drive = new DifferentialDrive(leftMotors, rightMotors);
		this.controls = controls;
	}
	
    public void initDefaultCommand() {
        setDefaultCommand(new ArcadeDrive(this, controls));
    }
    
    @Override
    public void periodic() {
        if(controls.getButtonPressed(SHIFT_BUTTON.getController(), SHIFT_BUTTON.getButton())) {
        	System.out.println("Shift");
        	toggleShift();
        	controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kLeftRumble, 1d, 200d);
        	controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kRightRumble, 1d, 200d);
        }
    }
    
    public int getEncoderVelocity(ENCODER side) {
    	return side.equals(ENCODER.LEFT) ? talonPhoenixFL.getSensorCollection().getQuadratureVelocity() : talonPhoenixFR.getSensorCollection().getQuadratureVelocity();
    }
    
    public int getEncoderPosition(ENCODER side) {
    	return side.equals(ENCODER.LEFT) ? talonPhoenixFL.getSensorCollection().getQuadraturePosition() : talonPhoenixFR.getSensorCollection().getQuadraturePosition();
    }
    
    public void shift(GEAR gear) {
    	transmission.set(gear.getValue());
    }
    
    public void toggleShift() {
    	transmission.set(transmission.get().equals(GEAR.HIGH.getValue()) ? GEAR.LOW.getValue(): GEAR.HIGH.getValue());
    }
    
    public void arcadeDrive(double speed, double direction) {
    	drive.arcadeDrive(speed, direction);
    }
    
    public enum ENCODER {
    	LEFT, RIGHT;
    }
    
    public enum GEAR {
    	HIGH, LOW;
    	
    	public Value getValue() {
    		return this == HIGH ? Value.kReverse : Value.kForward;
    	}
    }
}

