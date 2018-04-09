package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.statemachine.WarriorSubsystem;
import org.usfirst.frc.team2832.robot.subsystems.DiagnosticSubsystem;

import java.util.function.Supplier;

/**
 * A wrapper for a sensor test
 */
public class SensorTest {

    private Supplier<Boolean> sensor;
    private WarriorSubsystem subsystem;
    private Enum flag;

    /**
     * @param sensor is not working when true
     * @param subsystem to flag
     * @param flag to add
     */
    public SensorTest(Supplier<Boolean> sensor, WarriorSubsystem subsystem, Enum flag) {
        this.sensor = sensor;
        this.subsystem = subsystem;
        this.flag = flag;
    }

    public boolean getSensor() {
        return sensor.get();
    }

    public WarriorSubsystem getSubsystem() {
        return subsystem;
    }

    public Enum getFlag() {
        return flag;
    }
}
