package org.usfirst.frc.team2832.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Dashboard;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.commands.Climb;

public class LiftSubsystemWithPID extends Lift implements PIDSource, PIDOutput {

    private PIDController controller;

    private final double P = 0.1;
    private final double I = 0.00;
    private final double D = 0.15;
    private final double F = 0.00;
    private final double TOLERANCE_INCHES = 1.5f;

    public LiftSubsystemWithPID() {
        super();
        controller = new PIDController(P, I, D, F,this, this);
        controller.setInputRange(0, RAIL_HEIGHT);
        controller.setOutputRange(-1, 1);
        controller.setAbsoluteTolerance(TOLERANCE_INCHES);
        controller.setSetpoint(0);
        controller.setContinuous();
        controller.enable();
    }

    @Override
    public void periodic() {
        if (Robot.controls.getButtonPressed(ButtonMapping.CLIMB_0) || Robot.controls.getButtonPressed(ButtonMapping.CLIMB_1)) {
            if (getCurrentCommand() instanceof Climb)
                getCurrentCommand().cancel();
            else
                Scheduler.getInstance().add(new Climb());
        }

        if (Robot.controls.getButtonPressed(ButtonMapping.PACK_BUTTON)) {
            if (collapserer.get() == DoubleSolenoid.Value.kForward)
                collapserer.set(DoubleSolenoid.Value.kReverse);
            else
                collapserer.set(DoubleSolenoid.Value.kForward);
        }

        if(talonPhoenixLift.getSensorCollection().isRevLimitSwitchClosed())
            resetLiftEncoder(0);
        else if(talonPhoenixLift.getSensorCollection().isFwdLimitSwitchClosed())
            ;//resetLiftEncoder(0); Todo: Potentially set calculated postion when at top of lift for better precision

        SmartDashboard.putString(Dashboard.PREFIX_PROG + "current command", getCurrentCommandName());

        if(Robot.controls.getButtonPressed(ButtonMapping.LEVEL_UP)) {
            for(int i = 0; i < Lift.Position.values().length; i++)
                if(Lift.Position.values()[i].height > getLiftEncoderPosition()) {
                    setPosition(Lift.Position.values()[i].height);
                    break;
                }
        }
        if(Robot.controls.getButtonPressed(ButtonMapping.LOWER_TO_BOTTOM)) {
            setPosition(0);
        }

        int pov = Robot.controls.getPOV(Controls.Controllers.CONTROLLER_MAIN);
        if (pov != -1) {
            if (pov > 90 && pov < 270)
                controller.setSetpoint(Math.max(getLiftEncoderPosition() - 2, 0));
            else
                controller.setSetpoint(Math.min(getLiftEncoderPosition() + 2, RAIL_HEIGHT));
        }
    }

    public void setPosition(Position position) {
        setPosition(position.height);
    }

    public void setPosition(double height) {
        controller.setSetpoint(height);
    }

    /**
     * Changes the target position by the input delta height
     * @param delta height
     */
    public void changeLiftPositionRelative(double delta) {
        controller.setSetpoint(Math.max(Math.min(controller.getSetpoint() + delta, RAIL_HEIGHT), 0));
    }

    public void enableController() {
        controller.enable();
    }

    public void disableController() {
        controller.disable();
    }

    @Override
    public void initDefaultCommand() {

    }

    @Override
    public void pidWrite(double output) {
        setLiftPower(output);
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return getLiftEncoderPosition();
    }
}
