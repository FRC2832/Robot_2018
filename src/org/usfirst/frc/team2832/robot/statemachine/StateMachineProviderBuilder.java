package org.usfirst.frc.team2832.robot.statemachine;

import java.util.ArrayList;
import java.util.List;

public class StateMachineProviderBuilder implements StateMachineProvider {

    private List<StateMachineValue> states = new ArrayList<>();
    private List<Module> buffer = new ArrayList<>();
    private List<State> processes = new ArrayList<>();

    @Override
    public List<StateMachineValue> getValues() {
        return states;
    }

    public State addSequential(State state) {
        states.add(new StateMachineValue(state, processes));
        processes.clear();
        return state;
    }

    /**
     * Combines added parallel modules and processes into a state machine state
     * @param module to create state with if no parallel modules are present or
     *               added to parallel modules
     * @return the created state
     */
    public State addSequential(Module module) {
        State state;
        if(buffer.size() > 0) {
            buffer.add(module);
            state = new CompoundState(buffer);
            buffer.clear();
        } else {
            state = new StandardState(module);
        }
        states.add(new StateMachineValue(state, processes));
        processes.clear();
        return state;
    }

    //This breaks the chaining style, but is nice for convenience...
    /*public State addProcess(Module module) {
        State state = new StandardState(module);
        addExternal(state);
        return state;
    }*/

    /**
     * Adds a state to be started outside of the state machine when
     * the state set by the next addSequential call
     *
     * @param state to start
     * @return builder for chaining
     */
    public StateMachineProviderBuilder addExternal(State state) {
        processes.add(state);
        return this;
    }

    /**
     * Adds to a buffer of modules to be combined into a {@link CompoundState}. This
     * buffer combined the next time addSequential is called, using the provided module.
     *
     * @param module to add to buffer
     * @return builder so as to chain methods prior to addSequential call.
     */
    public StateMachineProviderBuilder addParallel(Module module) {
        buffer.add(module);
        return this;
    }
}
