package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

public class Button {

	private Controllers controller;
	private Buttons button;
	
	public Button(Controllers controller, Buttons button) {
		this.button = button;
		this.controller = controller;
	}
	
	public Controllers getController() {
		return controller;
	}
	
	public Buttons getButton() {
		return button;
	}
}
