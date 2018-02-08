package org.usfirst.frc.team2832.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Dashboard;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.commands.Climb;
import org.usfirst.frc.team2832.robot.commands.MoveLift;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The lift subsystem which handles an encoder, commanding the lift motor, and
 * folding with a pneumatic cylinder
 */
public class Lift extends DiagnosticSubsystem<Lift.LiftFlags> {

	final static private int COLLAPSE_FORWARD_CHANNEL = 2;
	final static private int COLLAPSE_REVERSE_CHANNEL = 3;
	final static private int LIFT_MOTOR = 15;
	final static private int WINCH_MOTOR = 9;
	final static private int LIFT_LIMIT_SWITCH_PIN = 1;

	private static final double ENCODER_COUNT_TO_INCH = 96 / Math.PI;
	
	public static final double RAIL_HEIGHT = 84;

	private DoubleSolenoid collapserer;
	private WPI_TalonSRX talonLift;
	private TalonSRX talonPhoenixLift, winchMotor;
	private AnalogInput limitSwitch;
	
	public Lift() {
		super();
		limitSwitch = new AnalogInput(LIFT_LIMIT_SWITCH_PIN);
		winchMotor = new TalonSRX(WINCH_MOTOR);
		talonLift = new WPI_TalonSRX(LIFT_MOTOR);
		talonPhoenixLift = new TalonSRX(LIFT_MOTOR);
		collapserer = new DoubleSolenoid(COLLAPSE_FORWARD_CHANNEL, COLLAPSE_REVERSE_CHANNEL);
		collapserer.set(Value.kForward);
	}

	//the pistons are retracted when the climber is extended and extended when the climber is retracted
	public void pack() {
		collapserer.set(Value.kForward);
	}
	public void unpack() {
		collapserer.set(Value.kReverse);
	}
	
	//Adjust for lift motors (see google drive folder)
	public double getLiftPosition() {
		return talonPhoenixLift.getSensorCollection().getQuadraturePosition() * ENCODER_COUNT_TO_INCH;
	}

	public void setLiftPower(double power) {
		talonLift.set(power);
	}

	public void setWinchBrakeMode(boolean value) {
		if(value)
			winchMotor.setNeutralMode(NeutralMode.Brake);
		else
			winchMotor.setNeutralMode(NeutralMode.Coast);
	}

	public void setWinchPower(double power) {
		winchMotor.set(ControlMode.PercentOutput, power);
	}

	public boolean getLiftLimitSwitch() {
		return Math.abs(limitSwitch.getAverageVoltage() - 5) < 2;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new MoveLift());
	}

	@Override
    public void periodic() {
		if(Robot.controls.getButtonPressed(ButtonMapping.CLIMB_0) || Robot.controls.getButtonPressed(ButtonMapping.CLIMB_1)) {
			if(getCurrentCommand() instanceof Climb)
				getCurrentCommand().cancel();
			else
				Scheduler.getInstance().add(new Climb());
		}
		SmartDashboard.putString(Dashboard.PREFIX_PROG + "current command", getCurrentCommandName());
		/*if(Robot.controls.getButtonPressed(LOWER_LIFT)) {
			for(int i = Position.values().length - 1; i >= 0; i--) {
				if(Position.values()[i].height < getLiftPosition()) {
					Scheduler.getInstance().add(new MoveLiftPID(Position.values()[i]));
					break;
				}
			}
			
		}
		if(Robot.controls.getButtonPressed(RAISE_LIFT)) {
			for(int i = 0; i < Position.values().length; i++) {
				if(Position.values()[i].height > getLiftPosition()) {
					Scheduler.getInstance().add(new MoveLiftPID(Position.values()[i]));
					break;
				}
			}
		} */
		
		//dpad controls
		int pov = Robot.controls.getPOV(Controllers.CONTROLLER_MAIN);
		if(pov != -1) {
//			if(getCurrentCommand() != null && getCurrentCommand() instanceof MoveLiftPID)
//				getCurrentCommand().cancel();
			if(pov > 90 && pov < 270)
				talonLift.set(-0.4d);
			else
				talonLift.set(0.4d);
		}
		else {
			talonLift.set(0.0);
		}
	}
	public enum Position {
		INGESTOR(0), SWITCH(50), HEIGHT(70), SCALE(84);

		public double height;

		private Position(int height) {
			this.height = height;
		}
	}

	public enum LiftFlags {
	    ENCODER
    }
}
