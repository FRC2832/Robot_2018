package org.usfirst.frc.team2832.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Controls_OldStyle {
	
	final int CONTROLLER_MAIN_PORT = 0;
	final int CONTROLLER_SECONDARY_PORT = 1;
	
	private Joystick controllerMain, controllerSecondary;
	
	public Controls_OldStyle() {
		controllerMain = new Joystick(CONTROLLER_MAIN_PORT);
		controllerSecondary = new Joystick(CONTROLLER_SECONDARY_PORT);
		
		JoystickButton mainA = new JoystickButton(controllerMain, Buttons.A.value);
		JoystickButton mainB = new JoystickButton(controllerMain, Buttons.B.value);
		JoystickButton mainX = new JoystickButton(controllerMain, Buttons.X.value);
		JoystickButton mainY = new JoystickButton(controllerMain, Buttons.Y.value);
		JoystickButton mainBumperLeft = new JoystickButton(controllerMain, Buttons.BUMPER_LEFT.value);
		JoystickButton mainBumperRight = new JoystickButton(controllerMain, Buttons.BUMPER_RIGHT.value);
		JoystickButton mainBack = new JoystickButton(controllerMain, Buttons.BACK.value);
		JoystickButton mainStart = new JoystickButton(controllerMain, Buttons.START.value);
		JoystickButton mainStickLeft = new JoystickButton(controllerMain, Buttons.STICK_LEFT.value);
		JoystickButton mainStickRight = new JoystickButton(controllerMain, Buttons.STICK_RIGHT.value);
		
		JoystickButton secondaryA = new JoystickButton(controllerSecondary, Buttons.A.value);
		JoystickButton secondaryB = new JoystickButton(controllerSecondary, Buttons.B.value);
		JoystickButton secondaryX = new JoystickButton(controllerSecondary, Buttons.X.value);
		JoystickButton secondaryY = new JoystickButton(controllerSecondary, Buttons.Y.value);
		JoystickButton secondaryBumperLeft = new JoystickButton(controllerSecondary, Buttons.BUMPER_LEFT.value);
		JoystickButton secondaryBumperRight = new JoystickButton(controllerSecondary, Buttons.BUMPER_RIGHT.value);
		JoystickButton secondaryBack = new JoystickButton(controllerSecondary, Buttons.BACK.value);
		JoystickButton secondaryStart = new JoystickButton(controllerSecondary, Buttons.START.value);
		JoystickButton secondaryStickLeft = new JoystickButton(controllerSecondary, Buttons.STICK_LEFT.value);
		JoystickButton secondaryStickRight = new JoystickButton(controllerSecondary, Buttons.STICK_RIGHT.value);
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
}
