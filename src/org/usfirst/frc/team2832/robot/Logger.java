package org.usfirst.frc.team2832.robot;

import edu.wpi.first.wpilibj.Timer;

import java.io.*;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Logger {

    private final String USB_PATH = "U"; // Usb drive
    private final String SYSTEM_PATH = System.getProperty("user.home") + File.separator;
    private final int MAX_LOGS = 200;

    private BufferedWriter logWriter, csvWriter;
    private File log, csv;

    private List<String> singletons;
    private List<Supplier<Double>> suppliers;

    public Logger() {
        suppliers = new ArrayList<>();
        singletons = new ArrayList<>();

        try {
            createFiles((Robot.isReal() ? "/" : "") + USB_PATH + (Robot.isReal() ? "" : ":") + File.separator);
        } catch (Exception e0) {
            try {
                createFiles(SYSTEM_PATH);
            } catch (Exception e1) {
                System.err.println(new Exception("Failed to create logger", e1).toString());
            }
        }

        if(log != null) {
            System.out.println("Logging to " + log.getPath());
            try {
                logWriter = new BufferedWriter(new FileWriter(log), 4096);
                logWriter.write("Hello\n");
                logWriter.newLine();
                logWriter.flush();
                csvWriter = new BufferedWriter(new FileWriter(csv), 4096);
                csvWriter.write("\"Time\",\"Type voltz\",\"Used Memory\",\"Voltage\",\"FL Ampz\",\"FR Ampz\",\"BL Ampz\",\"BR Ampz\",\"Winch Ampz\",\"Lift Ampz\",\"InL Ampz\",\"InR Ampz\",\"Comp Cur\",\"Lift pos\",\"Yaw\",\"Pitch\",\"Roll\",\"Left pos\",\"Left vel\",\"Right pos\",\"Right vel\"");
                csvWriter.newLine();
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createFiles(String path) throws IOException, NullPointerException {
        File dir = new File(path + File.separator + "LOGS" + File.separator);
        dir.mkdir();
        dir = new File(path + File.separator + "CSV" + File.separator);
        dir.mkdir();
        File log = null, csv = null;
        for (int i = 0; i <= MAX_LOGS; i++) {
            log = new File(path + File.separator + "LOGS" + File.separator + "EventLog-" + i + ".txt");
            if (!log.exists()) {
                csv = new File(path + File.separator + "CSV" + File.separator + "DataLog-" + i + ".csv");
                break;
            }
        }
        log.createNewFile();
        csv.createNewFile();
        this.log = log;
        this.csv = csv;
    }

    public void clearSingletons() {
        singletons.clear();
    }

    public void logOnce(Object object) {
        logOnce(object.toString());
    }

    public void logOnce(String message) {
        if(singletons.contains(message))
            return;
        singletons.add(message);
        log(message);
    }

    public void log(String tag, Object object) {
        log(tag, object.toString());
    }

    public void log(String tag, String message) {
        log(tag + ": " + message);
    }

    public void log(String message) {
        System.out.println(message);
        if (logWriter != null) {
            try {
                logWriter.write(message + ": " + Timer.getFPGATimestamp());
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
            } catch (Exception e) {
                builder.append("NULL");
                logOnce(e);
            }
        }
        try {
            csvWriter.write(builder.toString());
            csvWriter.newLine();
            csvWriter.flush();
        } catch (IOException e) {
            logOnce(e);
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

    public void flush() {
        if(csvWriter != null) {
            try {
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
