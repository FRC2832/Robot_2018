package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

/**
 * A container for mapping controls with the desired controller and button
 */
public class ButtonMapping {

	public static final ButtonMapping COMPRESSOR_TOGGLE = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.STICK_RIGHT);
	public static final ButtonMapping RAISE_TILT = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.X);
	public static final ButtonMapping LOWER_TILT = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.Y);
	//public static final ButtonMapping LEVEL_UP = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.BUMPER_RIGHT);
	//public static final ButtonMapping LOWER_TO_BOTTOM = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.BUMPER_LEFT);
	public static final ButtonMapping CLIMB_0 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.A);
	public static final ButtonMapping CLIMB_1 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.B);
	public static final ButtonMapping PACK_BUTTON = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.START);
	public static final ButtonMapping SHIFT_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.STICK_LEFT);
	public static final ButtonMapping PINTCHER_0 = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.BUMPER_LEFT);
	public static final ButtonMapping PINTCHER_1 = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.BUMPER_RIGHT);

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