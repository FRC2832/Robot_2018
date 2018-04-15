package org.usfirst.frc.team2832.robot.statemachine;

import edu.wpi.first.wpilibj.DriverStation;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.DriveDistance;
import org.usfirst.frc.team2832.robot.commands.auton.drivetrain.TurnPID;
import org.usfirst.frc.team2832.robot.commands.auton.lift.ExpelCube;
import org.usfirst.frc.team2832.robot.commands.auton.lift.MoveLiftTime;

public class CenterAuton extends StateMachineProviderBuilder {

    public CenterAuton() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        addSequential(new DriveDistance(0.7d, -20d, 10d)).setTimeout(5);
        addParallel(new MoveLiftTime(1.2, 1, 1.5));

        if (gameData.charAt(0) == 'R') {
            addSequential(new TurnPID(45)).setTimeout(5);
            addSequential(new DriveDistance(0.8d, -62d, 10d)).setTimeout(5);
            addSequential(new TurnPID(-45)).setTimeout(5);
            //addParallel(new LowerIngestor(.5));
            addSequential(new DriveDistance(0.7d, -30d, 2d)).setTimeout(2);
            addSequential(new ExpelCube());
        } else {
            addSequential(new TurnPID(-45)).setTimeout(5);
            addSequential(new DriveDistance(0.8d, -90d, 10d)).setTimeout(5);
            addSequential(new TurnPID(45)).setTimeout(5);
            //addParallel(new LowerIngestor(.5));
            addSequential(new DriveDistance(0.7d, -30d, 2d)).setTimeout(2);
            addSequential(new ExpelCube()).setTimeout(5);
        }
    }
}
