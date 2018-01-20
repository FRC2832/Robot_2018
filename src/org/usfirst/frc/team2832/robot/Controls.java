package org.usfirst.frc.team2832.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

/*
 * A class for handling controller inputs and mapping them to commands or getting raw values
 */
public class Controls {

	final static int CONTROLLER_MAIN_PORT = 0;
	final static int CONTROLLER_SECONDARY_PORT = 1;

	private Controller controllerMain, controllerSecondary;
	private List<Trigger> triggers;
	private List<Rumble> rumbles;

	public Controls() {
		controllerMain = new Controller(CONTROLLER_MAIN_PORT);
		controllerSecondary = new Controller(CONTROLLER_SECONDARY_PORT);
		triggers = new ArrayList<Trigger>();
		rumbles = new CopyOnWriteArrayList<Rumble>();
	}

	public void update() {
		// System.out.println("Controllers: " + controllerMain + ": " +
		// controllerSecondary);
		for (Rumble rumble : rumbles)
			if (rumble.start + rumble.duration < Timer.getFPGATimestamp()) {
				System.out.println("Rumble '" + rumble + "' killed");
				getController(rumble.controller).setRumble(rumble.type, 0d);
				rumbles.remove(rumble);
			}
	}

	public void setRumble(Controllers controller, RumbleType type, double duration, double intensity) {
		rumbles.add(new Rumble(controller, type, duration));
		getController(controller).setRumble(type, intensity);
	}

	public double getJoystickX(Controllers controller, Hand hand) {
		return getController(controller).getX(hand);
	}

	public double getJoystickY(Controllers controller, Hand hand) {
		return getController(controller).getY(hand);
	}

	public int getPOV(Controllers controller) {
		return getController(controller).getPOV();
	}

	public boolean getButton(Controllers controller, Buttons button) {
		return getController(controller).getButton(button);
	}

	public boolean getButtonPressed(Controllers controller, Buttons button) {
		return getController(controller).getRawButtonPressed(button.value);
	}

	public boolean getButtonReleased(Controllers controller, Buttons button) {
		return getController(controller).getRawButtonReleased(button.value);
	}

	public Trigger whilePressed(Button button, Command command) {
		return whilePressed(button.getController(), button.getButton(), command);
	}
	
	public Trigger whilePressed(Controllers controller, Buttons button, Command command) {
		Trigger trigger = new Trigger() {
			@Override
			public boolean get() {
				return getController(controller).getButton(button);
			}
		};
		trigger.whileActive(command);
		triggers.add(trigger);
		return trigger;
	}

	public Trigger whenPressed(Button button, Command command) {
		return whenPressed(button.getController(), button.getButton(), command);
	}
	
	public Trigger whenPressed(Controllers controller, Buttons button, Command command) {
		Trigger trigger = new Trigger() {
			@Override
			public boolean get() {
				return getController(controller).getButton(button);
			}
		};
		trigger.whenActive(command);
		triggers.add(trigger);
		return trigger;
	}

	public Trigger whenReleased(Button button, Command command) {
		return whenReleased(button.getController(), button.getButton(), command);
	}
	
	public Trigger whenReleased(Controllers controller, Buttons button, Command command) {
		Trigger trigger = new Trigger() {
			@Override
			public boolean get() {
				return getController(controller).getButton(button);
			}
		};
		trigger.whenInactive(command);
		triggers.add(trigger);
		return trigger;
	}

	public void removeTrigger(Trigger trigger) {
		triggers.remove(trigger);
	}

	private Controller getController(Controllers controller) {
		return controller.equals(Controllers.CONTROLLER_MAIN) ? controllerMain : controllerSecondary;
	}

	/**
	 * A single rumble event
	 */
	private class Rumble {

		public Controllers controller;
		public RumbleType type;
		public double duration, start;

		public Rumble(Controllers controller, RumbleType type, double duration) {
			this.controller = controller;
			this.type = type;
			this.duration = duration;
			start = Timer.getFPGATimestamp();
		}
	}

	/**
	 * Custom implementation of GenericHID for xbox controllers
	 */
	private class Controller extends GenericHID {

		public Controller(int port) {
			super(port);
		}

		@Override
		public double getX(Hand hand) {
			if (hand.equals(Hand.kLeft)) {
				return getRawAxis(0);
			} else {
				return getRawAxis(4);
			}
		}

		@Override
		public double getY(Hand hand) {
			if (hand.equals(Hand.kLeft)) {
				return getRawAxis(1);
			} else {
				return getRawAxis(5);
			}
		}

		public boolean getButton(Buttons button) {
			return getRawButton(button.value);
		}
	}

	/**
	 * Enumeration for differentiating between controllers
	 */
	public enum Controllers {
		CONTROLLER_MAIN, CONTROLLER_SECCONDARY;
	}

	/**
	 * Enumeration for mapping for xbox controller
	 */
	public enum Buttons {
		A(1), B(2), X(3), Y(4), BUMPER_LEFT(5), BUMPER_RIGHT(6), BACK(7), START(8), STICK_LEFT(9), STICK_RIGHT(10);

		private int value;

		Buttons(int value) {
			this.value = value;
		}
	}
}
