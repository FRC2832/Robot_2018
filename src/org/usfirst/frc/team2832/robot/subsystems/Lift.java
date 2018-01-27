package org.usfirst.frc.team2832.robot.subsystems;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.commands.MoveLift;
import org.usfirst.frc.team2832.robot.commands.MoveLiftPID;
import org.usfirst.frc.team2832.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The lift subsystem which handles an encoder, commanding the lift motor, and
 * folding with a pneumatic cylinder
 */
public class Lift extends Subsystem {

	final static int COLLAPSE_FORWARD_CHANNEL = 2;
	final static int COLLAPSE_REVERSE_CHANNEL = 3;
	final static int LIFT_MOTOR = 0;
	private static final double ENCODER_COUNT_TO_INCH = 1000;
	
	public static final double RAIL_HEIGHT = 84;
	
	private final ButtonMapping RAISE_LIFT = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_RIGHT);
	private final ButtonMapping LOWER_LIFT = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_LEFT);

	private DoubleSolenoid collapse;
	private WPI_TalonSRX talonLift;
	private TalonSRX talonPhoenixLift;
	
	public Lift() {
		super();
		talonLift = new WPI_TalonSRX(LIFT_MOTOR);
		talonPhoenixLift = new TalonSRX(LIFT_MOTOR);
		collapse.set(Value.kForward);
		collapse = new DoubleSolenoid(COLLAPSE_FORWARD_CHANNEL, COLLAPSE_REVERSE_CHANNEL);
	}

	public void pack() {
		collapse.set(Value.kForward);
	}
	
	public void unpack() {
		collapse.set(Value.kReverse);
	}
	
	public double getLiftPosition() {
		return talonPhoenixLift.getSensorCollection().getQuadraturePosition() * ENCODER_COUNT_TO_INCH;
	}

	public void setLiftPower(double speed) {
		talonLift.set(speed);
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new MoveLift());
	}

	@Override
    public void periodic() {
		/*if(Robot.controls.getButtonPressed(LOWER_LIFT)) {
			for(int i = POSITION.values().length - 1; i >= 0; i--) {
				if(POSITION.values()[i].height < getLiftPosition()) {
					Scheduler.getInstance().add(new MoveLiftPID(POSITION.values()[i]));
					break;
				}
			}
			
		}
		if(Robot.controls.getButtonPressed(RAISE_LIFT)) {
			for(int i = 0; i < POSITION.values().length; i++) {
				if(POSITION.values()[i].height > getLiftPosition()) {
					Scheduler.getInstance().add(new MoveLiftPID(POSITION.values()[i]));
					break;
				}
			}
		}
		int pov = Robot.controls.getPOV(Controllers.CONTROLLER_MAIN);
		if(pov != -1) {
			if(getCurrentCommand() != null && getCurrentCommand() instanceof MoveLiftPID)
				getCurrentCommand().cancel();
			if(pov > 90 && pov < 270)
				talonLift.set(-0.4d);
			else
				talonLift.set(0.4d);
		}*/
		if(Robot.controls.getButton(LOWER_LIFT)) {
			talonLift.set(-0.4d);
		} else if(Robot.controls.getButton(RAISE_LIFT)) {
			talonLift.set(0.4d);
		} else {
			talonLift.set(0.0d);
		}
    }

	/**
	 * An enumeration for lift height positions
	 */
	public enum POSITION {
		INGESTOR(0), SWITCH(50), HEIGHT(70), SCALE(84);

		public double height;

		private POSITION(int height) {
			this.height = height;
		}
	}
}
