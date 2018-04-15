package org.usfirst.frc.team2832.robot.statemachine;

import java.util.List;

public class StateMachineValue {

    private State state;
    private List<State> processes;

    public StateMachineValue(State state, List<State> processes) {
        this.state = state;
        this.processes = processes;
    }

    public State getState() {
        return state;
    }

    public List<State> getProcesses() {
        return processes;
    }
}
