package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

/**
 * A container for mapping controls with the desired controller and button
 */
public class ButtonMapping {

	final static boolean DEFAULT_CONTROLLER_DUAL = true;

	public static ButtonMapping COMPRESSOR_TOGGLE;
	public static ButtonMapping RAISE_TILT;
	public static ButtonMapping LOWER_TILT;
	public static ButtonMapping LEVEL_UP;
	public static ButtonMapping LOWER_TO_BOTTOM;
	public static ButtonMapping CLIMB_0;
	public static ButtonMapping CLIMB_1;
	public static ButtonMapping PACK_BUTTON;
	public static ButtonMapping PINTCH_EXTEND;
	public static ButtonMapping PINTCH_RETRACT;

	static {
		enableDualControllers(DEFAULT_CONTROLLER_DUAL);
	}

	public static void enableDualControllers(boolean enabled) {
		if(enabled) {
			COMPRESSOR_TOGGLE = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.STICK_LEFT);
			RAISE_TILT = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.X);
			LOWER_TILT = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.A);
			LEVEL_UP = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_RIGHT);
			LOWER_TO_BOTTOM = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_LEFT);
			CLIMB_0 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.Y);
			CLIMB_1 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.B);
			PACK_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.START);
			PINTCH_EXTEND = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.A);
			PINTCH_RETRACT = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.B);
		} else {
			COMPRESSOR_TOGGLE = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.STICK_LEFT);
			RAISE_TILT = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.X);
			LOWER_TILT = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.A);
			LEVEL_UP = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_RIGHT);
			LOWER_TO_BOTTOM = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.BUMPER_LEFT);
			CLIMB_0 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.Y);
			CLIMB_1 = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.B);
			PACK_BUTTON = new ButtonMapping(Controllers.CONTROLLER_MAIN, Buttons.START);
			PINTCH_EXTEND = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.STICK_RIGHT);
			PINTCH_RETRACT = new ButtonMapping(Controllers.CONTROLLER_SECCONDARY, Buttons.BACK);
		}
	}

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