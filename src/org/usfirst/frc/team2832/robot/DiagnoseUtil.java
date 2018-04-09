package org.usfirst.frc.team2832.robot;

import java.util.*;

import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.DriveTrainFlags;
import org.usfirst.frc.team2832.robot.subsystems.Lift.LiftFlags;

public class DiagnoseUtil {

	public static List<SensorError> knownErrors = new ArrayList<SensorError>();
	
	public static void addKnownErrors(String s) {
		String[] strings = s.split(", ?");
		for(String str : strings) {
			if(str.matches(".*?[Ee]ncoder.*?")) {
				if(str.matches(".*?[Ll]eft.*?")){
					knownErrors.add(SensorError.ENCODER_L);
				}else if(str.matches(".*?[Rr]ight.*?")) {
					knownErrors.add(SensorError.ENCODER_R);
				}else if(str.matches(".*?[Ll]ift.*?")) {
					knownErrors.add(SensorError.ENCODER_LIFT);
				}
			}else if(str.matches(".*?[Pp]id?geon)).*?")){
				knownErrors.add(SensorError.PIGEON);
			}
		}
		applyKnownErrors();
	}
	
	/**Adds flags for errors given on the driver station.*/
	public static void applyKnownErrors() {
		for(SensorError s : knownErrors) {
			switch(s) {
				case ENCODER_L:
					Robot.driveTrain.addFlag(DriveTrainFlags.ENCODER_L);
					break;
				case ENCODER_R:
					Robot.driveTrain.addFlag(DriveTrainFlags.ENCODER_R);
					break;
				case ENCODER_LIFT:
					Robot.lift.addFlag(LiftFlags.ENCODER);
					break;
				case PIGEON:
					Robot.driveTrain.addFlag(DriveTrainFlags.PIGEON);
					break;
			}
		}
	}
	
	/**A enum which is essentially flags for all sensors*/
	public enum SensorError{
		ENCODER_L, ENCODER_R, ENCODER_LIFT, PIGEON;
	}
}
