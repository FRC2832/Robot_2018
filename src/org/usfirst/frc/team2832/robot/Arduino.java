package org.usfirst.frc.team2832.robot;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import edu.wpi.first.wpilibj.can.CANJNI;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A helper class to handle communications with the Arduino
 */
public class Arduino {
	
	private int messageID = -1;

	public Arduino(int ID) {
		messageID = ID;
	}

	/**
	 * Method to read RGB values from color sensors & pressure sensor value from CAN bus. 
	 * @return data received from CAN bus
	 * @throws CANMessageNotFoundException
	 */
	protected byte[] readBuffer() throws CANMessageNotFoundException {
		IntBuffer idBuffer = ByteBuffer.allocateDirect(4).asIntBuffer();
		idBuffer.clear();
		idBuffer.put(0, Integer.reverseBytes(messageID));
		ByteBuffer timestamp = ByteBuffer.allocate(4);
		try {
			return CANJNI.FRCNetCommCANSessionMuxReceiveMessage(idBuffer, 0x1fffffff, timestamp);
		} catch (CANMessageNotFoundException e) {
			throw new CANMessageNotFoundException("Unable to read CAN device with ID 0x" + Integer.toHexString(messageID));
		}
	}
}
