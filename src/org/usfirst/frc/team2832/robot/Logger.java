package org.usfirst.frc.team2832.robot;

import edu.wpi.first.wpilibj.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Logger {

    private final String USB_PATH = "/U/"; // Usb drive
    private final String SYSTEM_PATH = System.getProperty("user.home") + "/";
    private final String ENTRY_NAME = "Log";
    private final int MAX_LOGS = 200;

    private BufferedWriter logWriter, csvWriter;
    private File log, csv;

    private List<Supplier<Double>> suppliers;

    public Logger() {
        suppliers = new ArrayList<>();
        try {
            log = new File(USB_PATH + "Log-Overflow.txt");
            if (log.exists())
                log.delete();
            for (int i = 0; i <= MAX_LOGS; i++) {
                File file = new File(USB_PATH + "EventLog-" + i + ".txt");
                if (!file.exists()) {
                    log = file;
                    csv = new File(USB_PATH + "DataLog-" + i + ".csv");
                    break;
                }
            }
            try {
                if (log.createNewFile() && csv.createNewFile())
                    System.out.println("Logging to '" + log.getPath() + "' on USB drive");
            } catch (IOException e) {
                System.out.println("Logging to '" + SYSTEM_PATH + "Log.txt'");
                log = new File(SYSTEM_PATH + "Log.txt");
                csv = new File(SYSTEM_PATH + "CSV.csv");
                if (log.exists())
                    log.delete();
                if (csv.exists())
                    csv.delete();
                csv.createNewFile();
                log.createNewFile();
            }
            logWriter = new BufferedWriter(new FileWriter(log));
            logWriter.write("Hello\n");
            logWriter.newLine();
            logWriter.flush();
            csvWriter = new BufferedWriter(new FileWriter(csv));
            csvWriter.write("\"Time\",\"Type voltz\",\"Lift pos\",\"Yaw\",\"Pitch\",\"Roll\",\"Left pos\",\"Left vel\",\"Right pos\",\"Right vel\"");
            csvWriter.newLine();
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String tag, Object object) {
        log(tag, object.toString());
    }

    public void log(String tag, String message) {
        System.out.println(tag + ": " + message);
        if (logWriter != null) {
            try {
                logWriter.write(tag + ": " + message + ": " + Timer.getFPGATimestamp());
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void critical(String tag, String message) {
        System.out.println("-" + tag + ": " + message);
        if (logWriter != null) {
            try {
                logWriter.write("-" + tag + ": " + message + ": " + Timer.getFPGATimestamp());
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void error(String tag, String message) {
        System.err.println(tag + ": " + message);
        if (logWriter != null) {
            try {
                logWriter.write("**");
                logWriter.newLine();
                logWriter.write(">>>" + tag + ": " + message + ": " + Timer.getFPGATimestamp());
                logWriter.newLine();
                logWriter.write("**");
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void header(String header) {
        System.out.println();
        System.out.println("@" + header);
        if (logWriter != null) {
            try {
                csvWriter.write("\"" + header + "\"");
                csvWriter.newLine();
                csvWriter.flush();
                logWriter.newLine();
                logWriter.newLine();
                logWriter.write("@" + header + ": " + Timer.getFPGATimestamp());
                logWriter.newLine();
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addLoggedValue(Supplier<Double> supplier) {
        suppliers.add(supplier);
    }

    public void update() {
        StringBuilder builder = new StringBuilder(Double.toString(Timer.getFPGATimestamp()));
        for(Supplier<Double> value: suppliers) {
            try {
                builder.append(",").append(Double.toString(value.get()));
            } catch (Exception err) {
                builder.append(",").append("0");
            }
        }
        try {
            csvWriter.write(builder.toString());
            csvWriter.newLine();
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        if(NetworkTableInstance.getDefault().getEntry("logRequest").getBoolean(false)) {
            NetworkTableInstance.getDefault().getEntry("logRequest").setBoolean(false);
            List<String> strings = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(log.getPath()));
                String line;
                while ((line = reader.readLine()) != null)
                    strings.add(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            NetworkTableInstance.getDefault().getEntry(ENTRY_NAME).setStringArray(strings.toArray(new String[]{}));
            NetworkTableInstance.getDefault().flush();
        }*/
    }

    public void dispose() {
        System.out.println("Disposed logger");
        try {
            logWriter.close();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
