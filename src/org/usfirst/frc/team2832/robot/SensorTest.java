package org.usfirst.frc.team2832.robot;

import java.util.function.Supplier;

/**
 * A wrapper for a sensor test
 */
public class SensorTest {

    private Supplier<Double> sensor;
    private DiagnosticSubsystem subsystem;
    private Enum flag;

    public SensorTest(Supplier<Double> sensor, DiagnosticSubsystem subsystem, Enum flag) {
        this.sensor = sensor;
        this.subsystem = subsystem;
        this.flag = flag;
    }

    public double getSensor() {
        return sensor.get();
    }

    public DiagnosticSubsystem getSubsystem() {
        return subsystem;
    }

    public Enum getFlag() {
        return flag;
    }
}
