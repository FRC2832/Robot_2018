package org.usfirst.frc.team2832.robot.statemachine;

import java.util.List;

public interface StateContainer extends StateComponent {

    List<State> getChildren();

    boolean hasChildren();
}
