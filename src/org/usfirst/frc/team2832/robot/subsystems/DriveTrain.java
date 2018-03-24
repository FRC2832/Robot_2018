package org.usfirst.frc.team2832.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.Dashboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.commands.ArcadeDrive;;

/**
 * The drive train subsystem which handles encoders, the transmission, and
 * driving
 */
public class DriveTrain extends DiagnosticSubsystem<DriveTrain.DriveTrainFlags> {

	final static int TRANSMISSION_FIRST_GEAR_CHANNEL = 5;
	final static int TRANSMISSION_SECOND_GEAR_CHANNEL = 6;

	final static int DRIVE_MOTER_FL = 10;
	final static int DRIVE_MOTER_FR = 24;
	final static int DRIVE_MOTER_BL = 11;
	final static int DRIVE_MOTER_BR = 23;

	final static double VIBRATE_THRESHOLD = 0.4d;

	private static final double ENCODER_COUNT_TO_INCH = 6d * Math.PI / 1440d; // Circumference divided by
																				// pulses/revolution
	private static final double ENCODER_ERROR_PERCENTAGE_LEFT = 68d / 6.62d; // Actual/desired distance
	private static final double ENCODER_ERROR_PERCENTAGE_RIGHT = 68d / 6.62d; // Actual/desired distance

	private DoubleSolenoid transmission;
	private DifferentialDrive drive;
	private WPI_TalonSRX talonFL, talonFR, talonBL, talonBR;
	private TalonSRX talonPhoenixLeft, talonPhoenixRight;
	private PigeonIMU pigeon;

	private boolean isTipping = false;
	
	public DriveTrain() {
		super();
		transmission = new DoubleSolenoid(TRANSMISSION_FIRST_GEAR_CHANNEL,TRANSMISSION_SECOND_GEAR_CHANNEL);
		talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
		talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
		talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
		talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
		talonPhoenixLeft = new TalonSRX(DRIVE_MOTER_FL);
		talonPhoenixRight = new TalonSRX(DRIVE_MOTER_FR);
		talonBL.follow(talonFL);
		talonBR.follow(talonFR);
		drive = new DifferentialDrive(talonFL, talonFR);
		pigeon = new PigeonIMU(0);
		talonFL.configOpenloopRamp(0.2, 0);
		talonFR.configOpenloopRamp(0.2, 0);
		if(Robot.isReal()) {
			talonPhoenixLeft.setSensorPhase(true);
			talonPhoenixRight.setSensorPhase(true);
			talonPhoenixLeft.getSensorCollection().setQuadraturePosition(0, 100);
			talonPhoenixRight.getSensorCollection().setQuadraturePosition(0, 100);
		}
	}
	
	/**
	 * Runs the {@link ArcadeDrive} command when no other command is running on this
	 * subsystem
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDrive());
	}

	/**
	 * Puts values to dashboard and listens for pressing of the Y button for
	 * rumbling
	 */
	@Override
	public void periodic() {
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Encoder Left Position", getEncoderPosition(Encoder.LEFT));
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Encoder Right Position", getEncoderPosition(Encoder.RIGHT));
		SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Pigeon Yaw Value", getPigeonYaw());
        SmartDashboard.putNumber(Dashboard.PREFIX_PROG + "Pigeon Pitch Value", getPigeonPitch());
        SmartDashboard.putString(Dashboard.PREFIX_PROG + "Current Gear", transmission.get().equals(Value.kForward) ? "first" : "second");

		// Toggles which gear it is in and makes controller rumble


		// Vibrate controllers if greater than threshold in "Gs"
		/*double[] accelerometer = new double[3];
		pigeon.getAccelerometerAngles(accelerometer);
		if (accelerometer[0] >= VIBRATE_THRESHOLD || accelerometer[2] >= VIBRATE_THRESHOLD) {
			Robot.controls.setRumble(Controllers.CONTROLLER_MAIN,       RumbleType.kLeftRumble,  0.7d, Math.max(accelerometer[0], accelerometer[2]));
			Robot.controls.setRumble(Controllers.CONTROLLER_MAIN,       RumbleType.kRightRumble, 0.7d, Math.max(accelerometer[0], accelerometer[2]));
			Robot.controls.setRumble(Controllers.CONTROLLER_SECCONDARY, RumbleType.kLeftRumble,  0.7d, Math.max(accelerometer[0], accelerometer[2]));
			Robot.controls.setRumble(Controllers.CONTROLLER_SECCONDARY, RumbleType.kRightRumble, 0.7d, Math.max(accelerometer[0], accelerometer[2]));
		}*/
		/*
		if (getPigeonRoll() > 20) {
			isTipping = true;
			drive.tankDrive(1, 1);
			System.out.println("Correcting a forward fall");
		} else if (getPigeonRoll() < -20) {
			isTipping = true;
			drive.tankDrive(-1, -1);
			System.out.println("Correcting a backward fall");
		} else if(isTipping){
			isTipping = false;
			drive.tankDrive(0, 0);
		}
		//*/
	}

	/**
	 * Gets velocity from drive motor encoders
	 * 
	 * @param side
	 *            of robot to get encoder
	 * @return velocity of the selected encoder in rotations/second
	 */
	public double getEncoderVelocity(Encoder side) {
		if(Robot.isSimulation())
			return 0;
		double pulsesPer100Mili;
		if(side.equals(Encoder.LEFT)) //Left
			pulsesPer100Mili = talonPhoenixLeft.getSensorCollection().getQuadratureVelocity();
		else if(side.equals(Encoder.RIGHT)) // Right
			pulsesPer100Mili = -talonPhoenixRight.getSensorCollection().getQuadratureVelocity();
		else if(hasAll(DriveTrainFlags.ENCODER_R, DriveTrainFlags.ENCODER_L)) // Average with both encoders flagged
			pulsesPer100Mili = 0;
		else if(hasFlag(DriveTrainFlags.ENCODER_R)) // Average with just right flag
			pulsesPer100Mili = talonPhoenixLeft.getSensorCollection().getQuadratureVelocity();
		else if(hasFlag(DriveTrainFlags.ENCODER_L)) // Average with just left flagged
			pulsesPer100Mili = -talonPhoenixRight.getSensorCollection().getQuadratureVelocity();
		else // Average with no encoder flags
			pulsesPer100Mili = (talonPhoenixLeft.getSensorCollection().getQuadratureVelocity() - talonPhoenixRight.getSensorCollection().getQuadratureVelocity()) / 2d;
		return pulsesPer100Mili / 144d * (Robot.RobotType.Programming.equals(Robot.getRobotType()) ? 1d : 1d / 3d);
	}

	/**
	 * Gets encoder position from drive motor encoders
	 * 
	 * @param side
	 *            of robot to get encoder
	 * @return position of the selected encoder(inches)
	 */
	public double getEncoderPosition(Encoder side) {
		if(Robot.isSimulation())
			return 0;
		double pulses;
		if(side.equals(Encoder.LEFT)) //Left
			pulses = talonPhoenixLeft.getSensorCollection().getQuadraturePosition() * ENCODER_ERROR_PERCENTAGE_LEFT;
		else if(side.equals(Encoder.RIGHT)) // Right
			pulses = -talonPhoenixRight.getSensorCollection().getQuadraturePosition() * ENCODER_ERROR_PERCENTAGE_RIGHT;
		else
			pulses = 0; // Don't use average, it won't work
		return pulses * ENCODER_COUNT_TO_INCH * (Robot.RobotType.Programming.equals(Robot.getRobotType()) ? 1d : 1d / 3d);
	}

	/**
	 * Turns brake mode on or off for the moters
	 */
	public void setBrakeMode(boolean mode) {
		Robot.logger.log("Drive Train", "Brake mode " + (mode ? "enabled" : "disabled"));
		if(Robot.isSimulation())
			return;
		NeutralMode brakeMode;
		if (mode)
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
	 * 
	 * @param gear
	 *            to shift to
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
	 * Commands the drive motors using arcade drive
	 * 
	 * @param speed
	 *            to drive at between 0 and 1
	 * @param direction
	 *            to drive in between -1 and 1
	 */
	public void arcadeDrive(double speed, double direction) {
		if(/*!isTipping && */Robot.isReal()) {
			drive.arcadeDrive(speed, direction);
		}	
	}

	/**
	 * Commands the drive motors using tank drive
	 * 
	 * @param leftSpeed
	 *            between 0 and 1
	 * @param rightSpeed
	 *            between 0 and 1
	 */
	public void tankDrive(double leftSpeed, double rightSpeed) {
		if(/*!isTipping &&*/ Robot.isReal()) {
			drive.tankDrive(leftSpeed, rightSpeed);
		}
	}

	/**
	 * Enumeration for drive motor encoders
	 */
	public enum Encoder {
		LEFT, RIGHT, AVERAGE;
	}

	/**
	 * Enumeration for gears of robot
	 */
	public enum GEAR {
		HIGH, LOW;

		public Value getValue() {
			return this == HIGH ? Value.kReverse : Value.kForward;
		}
	}

	/**
	 * Set pigeon yaw
	 * 
	 * @param angle
	 *            to set
	 */
	public void setPigeonYaw(double angle) {
		if(Robot.isReal())
			pigeon.setYaw(angle, 100);
	}

	/**
	 * Retrieves the yaw value from the pigeon IMU
	 * 
	 * @return current yaw angle
	 */
	public double getPigeonYaw() {
		double[] rotations = new double[3];
			if(Robot.isReal())
		this.pigeon.getYawPitchRoll(rotations);
		return -rotations[0];
	}

	/**
	 * Retrieves the pitch value from the pigeon IMU
	 *
	 * @return current pitch angle
	 */
	public double getPigeonPitch() {
		double[] rotations = new double[3];
		if(Robot.isReal())
			this.pigeon.getYawPitchRoll(rotations);
		return rotations[1];
	}

    public double getPigeonRoll() {
        double[] rotations = new double[3];
        if(Robot.isReal())
        	this.pigeon.getYawPitchRoll(rotations);
        return rotations[2];
    }

	public PigeonIMU getPigeon() {
		return pigeon;
	}

	public enum DriveTrainFlags {
		PIGEON,
		ENCODER_L,
		ENCODER_R;
	}
}
