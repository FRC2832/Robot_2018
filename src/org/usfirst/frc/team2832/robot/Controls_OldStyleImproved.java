package org.usfirst.frc.team2832.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.usfirst.frc.team2832.robot.Controls.Buttons;
import org.usfirst.frc.team2832.robot.Controls.Controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class Controls_OldStyleImproved {
	
	final int CONTROLLER_MAIN_PORT = 0;
	final int CONTROLLER_SECONDARY_PORT = 1;
	
	private Joystick controllerMain, controllerSecondary;
	private Map<Buttons, JoystickButton> buttonsMain, buttonsSecondary;
	private List<Rumble> rumbles;
	
	public Controls_OldStyleImproved() {
		controllerMain = new Joystick(CONTROLLER_MAIN_PORT);
		controllerSecondary = new Joystick(CONTROLLER_SECONDARY_PORT);
		
		buttonsMain = new HashMap<>();
		buttonsSecondary = new HashMap<>();
		
		for(Buttons button: Buttons.values()) {
			buttonsMain.put(button, new JoystickButton(controllerMain, button.value));
		}
		
		for(Buttons button: Buttons.values()) {
			buttonsSecondary.put(button, new JoystickButton(controllerSecondary, button.value));
		}
		rumbles = new ArrayList<Rumble>();
	}
	
	public void update() {
		for(Rumble rumble: rumbles) 
			if(rumble.start + rumble.duration < System.currentTimeMillis()) {
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
		return getController(controller).getRawButton(button.value);
	}
	
	public boolean getButtonPressed(Controllers controller, Buttons button) {
		return getController(controller).getRawButtonPressed(button.value);
	}
	  
	public boolean getButtonReleased(Controllers controller, Buttons button) {
		return getController(controller).getRawButtonReleased(button.value);
	}
	
	public void whilePressed(Controllers controller, Buttons button, Command command) {
		getControllerButtons(controller).get(button).whileHeld(command);
	}
	
	public void whenPressed(Controllers controller, Buttons button, Command command) {
		getControllerButtons(controller).get(button).whenPressed(command);
	}
	
	public void whenReleased(Controllers controller, Buttons button, Command command) {
		getControllerButtons(controller).get(button).whenReleased(command);
	}
	
	private Map<Buttons, JoystickButton> getControllerButtons(Controllers controller) {
		return controller.equals(Controllers.CONTROLLER_MAIN)?buttonsMain: buttonsSecondary;
	}
	
	public Joystick getController(Controllers controller) {
		return controller.equals(Controllers.CONTROLLER_MAIN)?controllerMain: controllerSecondary;
	}
	
	public enum Buttons {
		A(1),
	    B(2),
	    X(3),
	    Y(4),
	    BUMPER_LEFT(5),
	    BUMPER_RIGHT(6),
	    BACK(7),
	    START(8),
	    STICK_LEFT(9),
	    STICK_RIGHT(10);

	    private int value;

	    Buttons(int value) {
	    	this.value = value;
	    }
	}
	
	public enum Controllers {
		CONTROLLER_MAIN,
		CONTROLLER_SECCONDARY;
	}
	
private class Rumble {
		
		public Controllers controller;
		public RumbleType type;
		public double duration;
		public long start;
		
		public Rumble(Controllers controller, RumbleType type, double duration) {
			this.controller = controller;
			this.type = type;
			this.duration = duration;
			start = System.currentTimeMillis();
		}
	}
}
