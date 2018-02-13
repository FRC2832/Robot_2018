package org.usfirst.frc.team2832.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Controls;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

public class MoveLiftUltimateWithoutPidButBetterThanTheRest extends Command {

    private static final double TOLERANCE_INCHES = 1;

    private double target;
    private boolean moving;

    public MoveLiftUltimateWithoutPidButBetterThanTheRest() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        target = Robot.lift.getLiftEncoderPosition();
        moving = false;
    }

    @Override
    protected void execute() {
        /*if(Robot.controls.getButtonPressed(ButtonMapping.LEVEL_UP)) {
            for(int i = 0; i < Lift.Position.values().length; i++)
                if(Lift.Position.values()[i].height > Robot.lift.getLiftEncoderPosition()) {
                    target = Lift.Position.values()[i].height;
                    break;
                }
            moving = true;
        }
        if(Robot.controls.getButtonPressed(ButtonMapping.LOWER_TO_BOTTOM)) {
            target = 0;
            moving = true;
        }*/
        Robot.lift.setLiftPower(0);
        int pov = Robot.controls.getPOV(Controls.Controllers.CONTROLLER_MAIN);
        if (pov != -1) {
            moving = false;
            if (pov > 90 && pov < 270)
                Robot.lift.setLiftPower(0.7);
            else
                Robot.lift.setLiftPower(-0.7);
        } /*else if(moving){
            Robot.lift.setLiftPower(Math.signum(target - Robot.lift.getLiftEncoderPosition()) * Math.max(0.3, Math.min(1, Math.abs(target - Robot.lift.getLiftEncoderPosition()) / 10d)));
        } else {
            Robot.lift.setLiftPower(0);
        }

        if(Math.abs(target - Robot.lift.getLiftEncoderPosition()) <= 1)
            moving = false;*/
    }

    @Override
    protected void end() {
        Robot.lift.setLiftPower(0);
    }

    @Override
    protected void interrupted() {
        Robot.lift.setLiftPower(0);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
