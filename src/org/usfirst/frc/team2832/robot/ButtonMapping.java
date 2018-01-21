package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

/**
 * A container for mapping controls with the desired controller and button
 */
public class ButtonMapping {

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