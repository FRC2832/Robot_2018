package org.usfirst.frc.team2832.robot.statemachine;

import java.util.ArrayList;
import java.util.List;

public class StateProviderBuilder implements StateProvider {

    private List<State> states = new ArrayList<>();
    private List<Module> buffer = new ArrayList<>();

    @Override
    public List<State> getStates() {
        return states;
    }

    public State addSequential(Module module) {
        State state;
        if(buffer.size() > 0) {
            buffer.add(module);
            state = new CompoundState(buffer);
            buffer.clear();
        } else {
            state = new StandardState(module);
        }
        states.add(state);
        return state;
    }

    public StateProviderBuilder addParallel(Module module) {
        buffer.add(module);
        return this;
    }
}
