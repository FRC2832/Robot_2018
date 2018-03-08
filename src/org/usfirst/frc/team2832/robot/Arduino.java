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
	
	private int messageID = 0x612;

	public Arduino() {
		
	}

	/**
	 * Method to read RGB values from color sensors & pressure sensor value from CAN bus. 
	 * @return data received from CAN bus
	 * @throws CANMessageNotFoundException
	 */
	protected byte[] readBuffer() throws CANMessageNotFoundException {
		IntBuffer idBuffer = ByteBuffer.allocateDirect(8).asIntBuffer();
		idBuffer.clear();
		idBuffer.put(0, Integer.reverseBytes(messageID));
		ByteBuffer timestamp = ByteBuffer.allocate(4);
		System.out.println("Reached try block.");
		try {
			System.out.println("In try block.");
			Robot.logger.log("CAN Reciever","successful");
			return CANJNI.FRCNetCommCANSessionMuxReceiveMessage(idBuffer, 0x1fffffff, timestamp);
		} catch (CANMessageNotFoundException e) {
			System.out.println("Exception reached. :(");
			Robot.logger.log("CAN Reciever","unsuccessful");
			throw new CANMessageNotFoundException("Unable to read CAN device with ID 0x" + Integer.toHexString(messageID));
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws CANMessageUnavailableException
	 */
	public byte[] read() throws CANMessageNotFoundException {
		ByteBuffer dataBuffer = ByteBuffer.wrap(readBuffer());
		if (dataBuffer == null) {
			System.out.println("Null.");
			return null;
		}
		if (dataBuffer.remaining() <= 0) {
			System.out.println("Empty.");
			return null;
		}
		dataBuffer.rewind();
		byte[] data = new byte[dataBuffer.remaining()];
		for (int i = 0; i < dataBuffer.remaining(); i++) {
			data[i] = dataBuffer.get(i);
		}
		System.out.println("There's stuff in there. :)");
		Robot.logger.log("CAN Reciever","data stored");
		return data;
	}
}
