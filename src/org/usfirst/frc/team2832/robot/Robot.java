
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2832.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.can.CANExceptionFactory;
import edu.wpi.first.wpilibj.can.CANJNI;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.commands.auton.DiagnoseSensors;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.Ingestor;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.subsystems.LiftSubsystemWithPID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Main robot class and location for static objects like subsystems and dashboard
 */

public class Robot extends TimedRobot {

	private final static int ROBOT_TYPE_PIN = 3;
	private final static int IMAGERY_OUTPUT_PIN = 3;
	
	//Subsystems
	public static DriveTrain driveTrain;
	public static Lift lift;
	public static Ingestor ingestor;

	//Other
	public static Controls controls;
	public static Dashboard dashboard;
	public static Logger logger;
	private final int PRESSURE_SENSOR_PIN = 0;
	PowerDistributionPanel pdp;
	AnalogInput pressureSensor;
	private static RobotType robotType;

	private AnalogInput robotTypeInput;
	private DigitalOutput doImagery;
	private DiagnoseSensors diagnostic;
	private boolean postDiagnosis;
	private Compressor compressor;
	private Arduino teensy;
	private double pressureVoltage;

	/**
	 * Gets which robot the code is running on
	 * @return type of robot
	 */
	public static RobotType getRobotType() {
		return robotType;
	}

	/**
	 * Called when the robot is initialized
	 */
	@Override
	public void robotInit() {
		logger = new Logger();
		logger.header("Robot Init");
		
		doImagery = new DigitalOutput(IMAGERY_OUTPUT_PIN);
        
		robotTypeInput = new AnalogInput(ROBOT_TYPE_PIN);
        robotType = RobotType.Competition; //Default to competition
        BufferedReader reader = null;
        try {
        	reader = new BufferedReader(new FileReader(new File("/home/lvuser/RobotType.txt")));
            String type = reader.readLine();
            if(!"".equals(type))
                robotType = RobotType.valueOf(type);
        } catch(Exception e) {
            logger.error("ReadTypeFile", e.toString());
            robotType = RobotType.Competition;
        }finally {//runs whether the try block finishes or not, after catch if it doesn't.
        	try {
        		if(reader != null)
        			reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

		controls = new Controls();

		driveTrain = new DriveTrain();
		lift = new Lift();
		Lift.setControls();
		ingestor = new Ingestor();
		compressor = new Compressor();
		pdp = new PowerDistributionPanel();
		pressureSensor = new AnalogInput(PRESSURE_SENSOR_PIN);

		dashboard = new Dashboard(); //Make sure that this is after all subsystems and controls

		compressor = new Compressor();

		// Create the command to be called before autonomous
		diagnostic = new DiagnoseSensors(
				new SensorTest(()-> Robot.driveTrain.getEncoderPosition(DriveTrain.Encoder.LEFT) == 0, Robot.driveTrain, DriveTrain.DriveTrainFlags.ENCODER_L),
				new SensorTest(()-> Robot.driveTrain.getEncoderPosition(DriveTrain.Encoder.RIGHT) == 0, Robot.driveTrain, DriveTrain.DriveTrainFlags.ENCODER_R),
				new SensorTest(()-> Robot.driveTrain.getPigeonYaw() == 0, Robot.driveTrain, DriveTrain.DriveTrainFlags.PIGEON),
				new SensorTest(()-> Robot.lift.getLiftEncoderPosition() == 0, Robot.lift, Lift.LiftFlags.ENCODER));

		
		// Create camera
		new Thread(() -> {
	    	UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
	    	camera.setResolution(640, 480);
	    	while(true) {
	    		try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					logger.error("Camera Thread Interrupted", e.toString());
				}
	    	}
	    }).start();
	    
		driveTrain.setPigeonYaw(0);
		teensy = new Arduino();

		logger.addLoggedValue(() -> robotTypeInput.getAverageVoltage());
		logger.addLoggedValue(() -> lift.getLiftEncoderPosition());
		logger.addLoggedValue(() -> driveTrain.getPigeonYaw());
		logger.addLoggedValue(() -> driveTrain.getPigeonPitch());
		logger.addLoggedValue(() -> driveTrain.getPigeonRoll());
		logger.addLoggedValue(() -> driveTrain.getEncoderPosition(DriveTrain.Encoder.LEFT));
		logger.addLoggedValue(() -> driveTrain.getEncoderVelocity(DriveTrain.Encoder.LEFT));
		logger.addLoggedValue(() -> driveTrain.getEncoderPosition(DriveTrain.Encoder.RIGHT));
		logger.addLoggedValue(() -> driveTrain.getEncoderVelocity(DriveTrain.Encoder.RIGHT));
	}

	@Override
	public void robotPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putString(Dashboard.PREFIX_DRIVER + "RobotType", robotType.name());
		SmartDashboard.putNumber("Pin Voltage", robotTypeInput.getAverageVoltage());
		logger.update();
		controls.update();
		pressureVoltage =  pressureSensor.getAverageVoltage();
		
		doImagery.set(Alliance.Blue.equals(DriverStation.getInstance().getAlliance()));
		
		if (Robot.controls.getButtonPressed(ButtonMapping.COMPRESSOR_TOGGLE)) {
			// System.out.println("Shift");
			// toggleShift();
			if(compressor.enabled())
				compressor.stop();
			else
				compressor.start();
			Robot.controls.setRumble(Controls.Controllers.CONTROLLER_MAIN, GenericHID.RumbleType.kLeftRumble, 0.5d, 1d);
			Robot.controls.setRumble(Controls.Controllers.CONTROLLER_MAIN, GenericHID.RumbleType.kRightRumble, 0.5d, 1d);
		}
		
		SmartDashboard.putBoolean(Dashboard.PREFIX_PROG + "Collapser Initialized", lift != null && lift.collapserer != null);
		SmartDashboard.putBoolean(Dashboard.PREFIX_PROG + "Collapser collapsed", lift == null ? false : lift.getPacked());
		SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "voltage", pdp.getVoltage());
		SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "pressure", 250*(pressureVoltage/5)-25);
		//SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "voltage", 10);
		//SmartDashboard.putNumber(Dashboard.PREFIX_DRIVER + "pressure", 40);
		//byte[] sensorVals = teensy.read();
		//for(byte temp:sensorVals)
		//	System.out.println(temp);
	}
	

	/**
	 * Called when when robot is disabled
	 */
	@Override
	public void disabledInit() {
	    driveTrain.clearFlags();
	    lift.clearFlags();
	    ingestor.clearFlags();
		logger.header("Disabled Init");
		Robot.driveTrain.setBrakeMode(false);
		controls.clearRumbles();
		logger.dispose();
	}

	/**
	 * Called periodically when robot is disabled
	 */
	@Override
	public void disabledPeriodic() {
		// Set the type of robot based on the average voltage of a pin

		/*double value = robotTypeInput.getAverageVoltage();
		if(3.75d <= value && value <= 5d) {
			robotType = RobotType.Programming;
		} else if(1.25d < value && value < 3.75d) {
			robotType = RobotType.Practice;
		} else {
			robotType = RobotType.Competition;
		}*/
	}

	/**
	 * Called when robot enters autonomous
	 */
	@Override
	public void autonomousInit() {
		//doImagery.set(false);
		
		if(logger.isOpen)
			logger = new Logger();
		logger.header("Autonomous Init");

		postDiagnosis = false;

        lift.resetLiftEncoder(0); // Talk about whether these should be used or just use limit switches

        lift.collapserer.set(Value.kReverse);
        Robot.driveTrain.setBrakeMode(true);
		Scheduler.getInstance().removeAll();
		
		Scheduler.getInstance().add(diagnostic);

		logger.log("Robot Type", robotType.name());
	}

	/**
	 * Called periodically when robot is in autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Robot.driveTrain.shift(DriveTrain.GEAR.LOW);
		// If diagnostic is done, start either selected command group or just drive forward to line.
		if(!postDiagnosis && diagnostic.doneDiagnosing()) {
			postDiagnosis = true;
			if(Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.PIGEON) || Robot.driveTrain.hasAll(DriveTrain.DriveTrainFlags.ENCODER_L, DriveTrain.DriveTrainFlags.ENCODER_R)) {
				logger.log("Auton", "Defaulting to driving past line due to critical system failure");
				Scheduler.getInstance().add(new DriveDistance(.5f, 120d, 10)); // TODO: 2/9/2018 Adjust this time in preparation for when both encoders fail
			} else {
				logger.log("Auton", "Starting " + dashboard.getSelectedCommand().getName() + " normally");
				Scheduler.getInstance().add(dashboard.getSelectedCommand());
			}
		}
	}

	/**
	 * Called when robot enters teleop
	 */
	@Override
	public void teleopInit() {
		if(logger.isOpen)
			logger = new Logger();
		logger.header("Teleop Init");
        lift.resetLiftEncoder(0); // Talk about whether these should be used or just use limit switches
        //lift.unpack();
		Robot.driveTrain.setBrakeMode(true);
		Scheduler.getInstance().removeAll();
	}

	/**
	 * Called periodically when robot is in teleop
	 */
	@Override
	public void teleopPeriodic() {

	}

	/**
	 * Called when test mode begins
	 */
	@Override
	public void testInit() {
		if(logger.isOpen)
			logger = new Logger();
		logger.header("Test Init");
	}

	/**
	 * Called periodically while in test mode
	 */
	@Override
	public void testPeriodic() {
		
	}

	public enum RobotType {
		Programming, Practice, Competition;
	}
}
