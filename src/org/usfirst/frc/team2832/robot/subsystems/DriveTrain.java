package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.Button;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
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
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

/**
 * The drive train subsystem which handles encoders, the transmission, and driving
 */
public class DriveTrain extends Subsystem {

	final static int TRANSMISSION_FORWARD_CHANNEL = 0;
	final static int TRANSMISSION_REVERSE_CHANNEL = 1;
	
	final static int DRIVE_MOTER_FL = 10;
	final static int DRIVE_MOTER_FR = 8;
	final static int DRIVE_MOTER_BL = 17;
	final static int DRIVE_MOTER_BR = 2;
	
	private static final double ENCODER_COUNT_TO_INCH = 1000d;
	
	final Button SHIFT_BUTTON = new Button(Controllers.CONTROLLER_MAIN, Buttons.Y);
	
	private DoubleSolenoid transmission;
	private DifferentialDrive drive;
	private SpeedControllerGroup leftMotors, rightMotors;	
	private WPI_TalonSRX talonFL, talonFR, talonBL, talonBR;
	private TalonSRX talonPhoenixFL, talonPhoenixFR;
		
	public DriveTrain() {
		super();
		transmission = new DoubleSolenoid(TRANSMISSION_FORWARD_CHANNEL, TRANSMISSION_REVERSE_CHANNEL);
		talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
		talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
		talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
		talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
		talonPhoenixFL = new TalonSRX(DRIVE_MOTER_FL);
		talonPhoenixFR = new TalonSRX(DRIVE_MOTER_FR);
		leftMotors = new SpeedControllerGroup(talonFL, talonBL);
		rightMotors = new SpeedControllerGroup(talonFR, talonBR);
		drive = new DifferentialDrive(leftMotors, rightMotors);
	}
	
    public void initDefaultCommand() {
        setDefaultCommand(new ArcadeDrive(this));
    }
    
    @Override
    public void periodic() {
    	SmartDashboard.putNumber("Encoder Left Position", getEncoderPosition(ENCODER.LEFT));
    	SmartDashboard.putNumber("Encoder Right Position", getEncoderPosition(ENCODER.RIGHT));

    	//Toggles which gear it is in and makes controller rumble
        if(Robot.controls.getButtonPressed(SHIFT_BUTTON.getController(), SHIFT_BUTTON.getButton())) {
        	System.out.println("Shift");
        	toggleShift();
        	Robot.controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kLeftRumble, 1d, 2.0d);
        	Robot.controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kRightRumble, 1d, 2.0d);
        }
    }
    
    /**\
     * Gets velocity from drive motor encoders
     * @param side of robot to get encoder
     * @return velocity of the selected encoder
     */
    public int getEncoderVelocity(ENCODER side) {
    	return side.equals(ENCODER.LEFT) ? talonPhoenixFL.getSensorCollection().getQuadratureVelocity() : talonPhoenixFR.getSensorCollection().getQuadratureVelocity();
    }
    
    /**\
     * Gets encoder position from drive motor encoders
     * @param side of robot to get encoder
     * @return position of the selected encoder
     */
    public double getEncoderPosition(ENCODER side) {
    	return side.equals(ENCODER.LEFT) ? talonPhoenixFL.getSensorCollection().getQuadraturePosition() : (side.equals(ENCODER.RIGHT)? talonPhoenixFR.getSensorCollection().getQuadraturePosition(): (talonPhoenixFL.getSensorCollection().getQuadraturePosition() + talonPhoenixFR.getSensorCollection().getQuadraturePosition()) / 2d);
    }
    
    /**
     * Shifts gears
     * @param gear to shift to
     */
    public void shift(GEAR gear) {
    	transmission.set(gear.getValue());
    }
    
    /**
     * Toggles selected gear
     */
    public void toggleShift() {
    	transmission.set(transmission.get().equals(GEAR.HIGH.getValue()) ? GEAR.LOW.getValue(): GEAR.HIGH.getValue());
    }
    
    /**
     * @param speed
     * @param direction
     */
    public void arcadeDrive(double speed, double direction) {
    	drive.arcadeDrive(speed, direction);
    }
    
    /**
     *Enumeration for drive motor encoders
     */
    public enum ENCODER {
    	LEFT, RIGHT, BOTH;
    }
    
    /**
     *Enumeration for gears of robot
     */
    public enum GEAR {
    	HIGH, LOW;
    	
    	public Value getValue() {
    		return this == HIGH ? Value.kReverse : Value.kForward;
    	}
    }
}

