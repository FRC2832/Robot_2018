package org.usfirst.frc.team2832.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private final String USB_PATH = "/u/"; // Usb drive
    private final String SYSTEM_PATH = System.getProperty("user.home") + "/";
    private final String ENTRY_NAME = "Log";
    private final int MAX_LOGS = 200;

    private BufferedWriter writer;
    private File log;

    public Logger() {
        try {
            log = new File(USB_PATH + "Log-Overflow.txt");
            for (int i = 0; i <= MAX_LOGS; i++) {
                File file = new File(USB_PATH + "Log-" + i + ".txt");
                if (!file.exists()) {
                    log = file;
                    break;
                }
            }
            if (log.exists())
                log.delete();
            try {
                if (log.createNewFile())
                    System.out.println("Logging to '" + log.getPath() + "' on USB drive");
            } catch (IOException e) {
                System.out.println("Logging to '" + SYSTEM_PATH + "Log.txt'");
                log = new File(SYSTEM_PATH + "Log.txt");
                if (log.exists())
                    log.delete();
                log.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(log));
            writer.write("Hello\n");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String tag, Object object) {
        log(tag, object.toString());
    }

    public void log(String tag, String message) {
        System.out.println(tag + ": " + message);
        if (writer != null) {
            try {
                writer.write(tag + ": " + message + ": " + Timer.getFPGATimestamp());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void critical(String tag, String message) {
        System.out.println("-" + tag + ": " + message);
        if (writer != null) {
            try {
                writer.write("-" + tag + ": " + message + ": " + Timer.getFPGATimestamp());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void error(String tag, String message) {
        System.err.println(tag + ": " + message);
        if (writer != null) {
            try {
                writer.write("**");
                writer.newLine();
                writer.write(">>>" + tag + ": " + message + ": " + Timer.getFPGATimestamp());
                writer.newLine();
                writer.write("**");
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void header(String header) {
        System.out.println();
        System.out.println("@" + header);
        if (writer != null) {
            try {
                writer.newLine();
                writer.newLine();
                writer.write("@" + header + ": " + Timer.getFPGATimestamp());
                writer.newLine();
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        //NetworkTableInstance.getDefault().getEntry(ENTRY_NAME).setValue(log.list());
    }

    public void dispose() {
        System.out.println("Disposed logger");
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
