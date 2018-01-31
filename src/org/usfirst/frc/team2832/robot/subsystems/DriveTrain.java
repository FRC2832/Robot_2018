package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.commands.ArcadeDrive;
import org.usfirst.frc.team2832.robot.Dashboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

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
	final static int DRIVE_MOTER_FR = 24;
	final static int DRIVE_MOTER_BL = 11;
	final static int DRIVE_MOTER_BR = 23;
	
	private static final double ENCODER_COUNT_TO_INCH = 6d * Math.PI / 1440d; //Circumference divided by pulses/revolution
	private static final double ENCODER_ERROR_PERCENTAGE = 68d / 66.62d; //Actual/desired distance
	
	final ButtonMapping SHIFT_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.Y);
	
	private DoubleSolenoid transmission;
	private DifferentialDrive drive;
	private SpeedControllerGroup leftMotors, rightMotors;	
	private WPI_TalonSRX talonFL, talonFR, talonBL, talonBR;
	private TalonSRX talonPhoenixLeft, talonPhoenixRight;
	private PigeonIMU pigeon;
		
	public DriveTrain() {
		super();
		//transmission = new DoubleSolenoid(TRANSMISSION_FORWARD_CHANNEL, TRANSMISSION_REVERSE_CHANNEL);
		talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
		talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
		talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
		talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
		talonPhoenixLeft = new TalonSRX(DRIVE_MOTER_FL);
		talonPhoenixRight = new TalonSRX(DRIVE_MOTER_FR);
		leftMotors = new SpeedControllerGroup(talonFL, talonBL);
		rightMotors = new SpeedControllerGroup(talonFR, talonBR);
		drive = new DifferentialDrive(leftMotors, rightMotors);
		pigeon = new PigeonIMU(0);
		talonPhoenixLeft.getSensorCollection().setQuadraturePosition(0, 100);
		talonPhoenixRight.getSensorCollection().setQuadraturePosition(0, 100);
	}
	
	/**
	 * Runs the {@link ArcadeDrive} command when no other command is running on this subsystem
	 */
    public void initDefaultCommand() {
        setDefaultCommand(new ArcadeDrive());
    }
    
    /**
     * Puts values to dashboard and listens for pressing of the Y button for rumbling
     */
    @Override
    public void periodic() {
    	SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Encoder Left Position", getEncoderPosition(Encoder.LEFT));
    	SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Encoder Right Position", getEncoderPosition(Encoder.RIGHT));
    	SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Pigeon Yaw Value", getPigeonYaw());

    	//Toggles which gear it is in and makes controller rumble
        if(Robot.controls.getButtonPressed(SHIFT_BUTTON.getController(), SHIFT_BUTTON.getButton())) {
        	System.out.println("Shift");
        	toggleShift();
        	Robot.controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kLeftRumble, 0.5d, 1d);
        	Robot.controls.setRumble(Controllers.CONTROLLER_MAIN, RumbleType.kRightRumble, 0.5d, 1d);
        }
    }
    
    /**\
     * Gets velocity from drive motor encoders
     * @param side of robot to get encoder
     * @return velocity of the selected encoder
     */
    public int getEncoderVelocity(Encoder side) {
    	return side.equals(Encoder.LEFT) ? talonPhoenixLeft.getSensorCollection().getQuadratureVelocity() : talonPhoenixRight.getSensorCollection().getQuadratureVelocity();
    }
    
    /**\
     * Gets encoder position from drive motor encoders
     * @param side of robot to get encoder
     * @return position of the selected encoder(inches)
     */
    public double getEncoderPosition(Encoder side) {
    	return (side.equals(Encoder.LEFT) ? talonPhoenixLeft.getSensorCollection().getQuadraturePosition(): 
    		(side.equals(Encoder.RIGHT)? -talonPhoenixRight.getSensorCollection().getQuadraturePosition(): 
    			(talonPhoenixLeft.getSensorCollection().getQuadraturePosition() + 
    					talonPhoenixRight.getSensorCollection().getQuadraturePosition()) / 2d)) * 
    			ENCODER_COUNT_TO_INCH * ENCODER_ERROR_PERCENTAGE * 
    			(Robot.RobotType.Programming.equals(Robot.getRobotType())? 1d: 1d/3d);
    }
    
    /**
     * Turns brake mode on or off for the moters
     */
    public void setBrakeMode(boolean mode) {
    	NeutralMode brakeMode;
    	if(mode) 
    		brakeMode = NeutralMode.Brake;
    	else
    		brakeMode = NeutralMode.Coast;
       	talonFL.setNeutralMode(brakeMode);
    	talonFR.setNeutralMode(brakeMode);
    	talonBL.setNeutralMode(brakeMode);
    	talonBR.setNeutralMode(brakeMode);
    }
    
    /**
     * Shifts gears
     * @param gear to shift to
     */
    public void shift(GEAR gear) {
    	//transmission.set(gear.getValue());
    }
    
    /**
     * Toggles selected gear
     */
    public void toggleShift() {
    	//transmission.set(transmission.get().equals(GEAR.HIGH.getValue()) ? GEAR.LOW.getValue(): GEAR.HIGH.getValue());
    }
    
    /**
     * Commands the drive motors using arcade drive
     * 
     * @param speed to drive at between 0 and 1
     * @param direction to drive in between -1 and 1
     */
    public void arcadeDrive(double speed, double direction) {
    	drive.arcadeDrive(speed, direction);
    }
    
    /**
     * Commands the drive motors using tank drive
     * 
     * @param leftSpeed between 0 and 1
     * @param rightSpeed between 0 and 1
     */
    public void tankDrive(double leftSpeed, double rightSpeed) {
    	drive.tankDrive(leftSpeed, rightSpeed);
    }
    
    /**
     *Enumeration for drive motor encoders
     */
    public enum Encoder {
    	LEFT, RIGHT;
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
    
    /**
     * Set pigeon yaw
     * @param angle to set
     */
    public void setPigeonYaw(double angle) {
		pigeon.setYaw(angle, 100);
    }
    
    /**
     * Retrieves the yaw value from the pigeon IMU
     * 
     * @return current yaw angle
     */
    public double getPigeonYaw() {
    	double[] rotations = new double[3];
    	this.pigeon.getYawPitchRoll(rotations);
    	return rotations[0];
    }
    
    /**
     * Retrieves the pitch value from the pigeon IMU
     * 
     * @return current pitch angle
     */
    public double getPigeonPitch() {
    	double[] rotations = new double[3];
    	this.pigeon.getYawPitchRoll(rotations);
    	return rotations[1];
    }
    
    public PigeonIMU getPigeon() {
    	return pigeon;
    }
}

