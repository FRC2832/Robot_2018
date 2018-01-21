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

	/**
	 * Called periodically to handle rumble events
	 */
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

	/**
	 * Adds a new {@link Rumble} instance to the list
	 * 
	 * @param controller to be rumbled
	 * @param type as in left or right
	 * @param duration in seconds
	 * @param intensity between 0 and 1
	 */
	public void setRumble(Controllers controller, RumbleType type, double duration, double intensity) {
		rumbles.add(new Rumble(controller, type, duration));
		getController(controller).setRumble(type, intensity);
	}

	/**
	 * Get joystick x value from controller
	 * 
	 * @param controller to retrieve from
	 * @param hand, as in which joystick
	 * @return x value between -1 and 1
	 */
	public double getJoystickX(Controllers controller, Hand hand) {
		return getController(controller).getX(hand);
	}

	/**
	 * Get joystick y value from controller
	 * 
	 * @param controller to retrieve from
	 * @param hand, as in which joystick
	 * @return y value between -1 and 1
	 */
	public double getJoystickY(Controllers controller, Hand hand) {
		return getController(controller).getY(hand);
	}

	/**
	 * Get D-pad from controller
	 * 
	 * @param controller to retrieve d-pad from
	 * @return angle, I think, going clockwise
	 */
	public int getPOV(Controllers controller) {
		return getController(controller).getPOV();
	}

	/**
	 * Checks whether or not the button is currently pressed
	 * 
	 * @param mapping to retrieve value from
	 * @return if the button is pressed
	 */
	public boolean getButton(ButtonMapping mapping) {
		return getButton(mapping.getController(), mapping.getButton());
	}
	
	/**
	 * Checks whether or not the button is currently pressed
	 * 
	 * @param controller to retrieve values from
	 * @param button to check
	 * @return if the button is pressed
	 */
	public boolean getButton(Controllers controller, Buttons button) {
		return getController(controller).getButton(button);
	}

	/**
	 * Checks whether or not the button was just pressed
	 * 
	 * @param mapping to retrieve value from
	 * @return if the button was just pressed
	 */
	public boolean getButtonPressed(ButtonMapping mapping) {
		return getButtonPressed(mapping.getController(), mapping.getButton());
	}
	
	/**
	 * Checks whether or not the button was just pressed
	 * 
	 * @param controller to retrieve values from
	 * @param button to check
	 * @return if the button was just pressed
	 */
	public boolean getButtonPressed(Controllers controller, Buttons button) {
		return getController(controller).getRawButtonPressed(button.value);
	}

	/**
	 * Checks whether or not the button was just released
	 * 
	 * @param mapping to retrieve value from
	 * @return if the button was just released
	 */
	public boolean getButtonReleased(ButtonMapping mapping) {
		return getButtonReleased(mapping.getController(), mapping.getButton());
	}
	
	/**
	 * Checks whether or not the button was just released
	 * 
	 * @param controller to retrieve values from
	 * @param button to check
	 * @return if the button button was just released
	 */
	public boolean getButtonReleased(Controllers controller, Buttons button) {
		return getController(controller).getRawButtonReleased(button.value);
	}

	/**
	 * Registers a command to be run while a button is pressed
	 * 
	 * @param mapping from which to listen
	 * @param command to run
	 * @return a reference to the trigger to remove listener
	 */
	public Trigger whilePressed(ButtonMapping mapping, Command command) {
		return whilePressed(mapping.getController(), mapping.getButton(), command);
	}
	
	/**
	 * Registers a command to be run while a button is pressed
	 * 
	 * @param controller to listen for
	 * @param button to listen for
	 * @param command to run
	 * @return a reference to the trigger to remove listener
	 */
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

	/**
	 * Registers a command to be run when a button is pressed
	 * 
	 * @param mapping from which to listen
	 * @param command to run
	 * @return a reference to the trigger to remove listener
	 */
	public Trigger whenPressed(ButtonMapping mapping, Command command) {
		return whenPressed(mapping.getController(), mapping.getButton(), command);
	}
	
	/**
	 * Registers a command to be run when a button is pressed
	 * 
	 * @param controller to listen for
	 * @param button to listen for
	 * @param command to run
	 * @return a reference to the trigger to remove listener
	 */
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

	/**
	 * Registers a command to be run when a button is released
	 * 
	 * @param mapping from which to listen
	 * @param command to run
	 * @return a reference to the trigger to remove listener
	 */
	public Trigger whenReleased(ButtonMapping mapping, Command command) {
		return whenReleased(mapping.getController(), mapping.getButton(), command);
	}
	
	/**
	 * Registers a command to be run when a button is released
	 * 
	 * @param controller to listen for
	 * @param button to listen for
	 * @param command to run
	 * @return a reference to the trigger to remove listener
	 */
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

	/**
	 * Removes a button listener
	 * 
	 * @param trigger to remove
	 */
	public void removeTrigger(Trigger trigger) {
		triggers.remove(trigger);
	}

	/**
	 * Returns an instance of the desired controller from the {@link Controllers} enumeration
	 * 
	 * @param controller enumeration
	 * @return relevant instance of {@link Controller}
	 */
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
