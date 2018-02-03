package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

/**
 * A container for mapping controls with the desired controller and button
 */
public class ButtonMapping {

	public static final ButtonMapping SHIFT_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.STICK_LEFT);
	public static final ButtonMapping TOGGLE_TILT_0 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.A);
	public static final ButtonMapping TOGGLE_TILT_1 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.X);
	public static final ButtonMapping LEVEL_UP = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_RIGHT);
	public static final ButtonMapping LOWER_TO_BOTTOM = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_LEFT);
	public static final ButtonMapping raiseButton = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.Y);
	public static final ButtonMapping lowerButton = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.B);
	public static final ButtonMapping toggleUnpack = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.START);
	
	private Controllers controller;
	private Buttons button;
	
	public ButtonMapping(Controllers controller, Buttons button) {
		this.button = button;
		this.controller = controller;
	}

	/**
	 * A getter for the wrapped controller
	 * 
	 * @return mapped controller
	 */
	public Controllers getController() {
		return controller;
	}

	/**
	 * A getter for the wrapped button
	 * 
	 * @return mapped button
	 */
	public Buttons getButton() {
		return button;
	}
}