package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;

import org.usfirst.frc.team2832.robot.SensorTest;
import org.usfirst.frc.team2832.robot.statemachine.SubsystemModule;
import org.usfirst.frc.team2832.robot.statemachine.Subsystems;

/**
 * A command to test if sensorTests are working and driveforward to line if not
 */
public class DiagnoseSensors extends SubsystemModule {

    private final double TEST_DURATION = 0.3d;
    private final double SPEEEED = 0.4;

    private SensorTest[] sensorTests;

    public DiagnoseSensors(SensorTest... sensorTests) {
        requires(Subsystems.DriveTrain);
        requires(Subsystems.Lift);
        this.sensorTests = sensorTests;
    }

    public void initialize() {

    }

    @Override
    public void start() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sensorTests.length; i++) {
            if (i < sensorTests.length - 1)
                builder.append(sensorTests[i].getFlag().name() + (i == sensorTests.length - 2 ? ", and " : ", "));
            else
                builder.append(sensorTests[i].getFlag().name());
        }
        Robot.logger.log("Diagnose Sensors", "Testing " + builder);
    }

    /**
     * Move motors at set speed
     */
    public void execute() {
        Robot.driveTrain.arcadeDrive(SPEEEED, 0);
        Robot.lift.setLiftPower(SPEEEED);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private void testSensors() {
        for (SensorTest test : sensorTests)
            if (test.getSensor()) {
                test.getSubsystem().addFlag(test.getFlag());
                Robot.logger.critical("Sensor Failure", test.getFlag().name() + " has failed on " + test.getSubsystem().getName());
            }
    }

    /**
     * I have forgotten this one too many times
     */
    public void end() {
        Robot.logger.log("Diagnose Sensors", "Ended");
        Robot.driveTrain.arcadeDrive(0, 0);
        Robot.lift.setLiftPower(0);
        testSensors();
    }

    /**
     * Never again
     */
    public void interrupted() {
        Robot.logger.log("Diagnose Sensors", "Interrupted");
        Robot.driveTrain.arcadeDrive(0, 0);
        Robot.lift.setLiftPower(0);
        testSensors();
    }
}
