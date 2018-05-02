package org.usfirst.frc.team2832.robot.statemachine;

import org.usfirst.frc.team2832.robot.Robot;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StateMachine extends State<StateMachine> implements StateContainer {

    private List<State> states;
    private int index;
    private List<List<State>> processes;

    private StateMachine(List<State> states, List<List<State>> processes) {
        this.states = states;
        this.processes = processes;
        for(State state: states)
            state.setParent(this);
    }

    /**
     *
     * @Todo The next thing to add is a wrapper for existing commands so as to not have to convert them all in
     * the statemachine builder class
     * also create a profile system for robots with json reading
     *
     */

    

    @Override
    void start() {
        super.start();
        index = 0;
        startState();
    }

    private void startState() {
        if (processes.size() > 0 && processes.get(index) != null)
            for (State state : processes.get(index)) {
                Robot.logger.log(getName(), "Starting external state " + states.get(index).getName());
                Set<Subsystems> requirements = state.getRequirements();
                for (Subsystems subsystem : requirements)
                    if (getRequirements().contains(subsystem))
                        Robot.logger.critical(getName(), "External states should probably not replace state machines: " + state.getName());
                Robot.subsystemHandler.start(state);
            }
        Robot.logger.log(getName(), "Starting state " + index + ": " + states.get(index).getName());
        states.get(index).start();
    }

    @Override
    public Set<Subsystems> getRequirements() {
        Set<Subsystems> set = new HashSet<>(super.getRequirements());
        for (State state : states) {
            set.addAll(state.getRequirements());
        }
        return set;
    }

    @Override
    public Module getRunningModuleOfType(Class clazz) {
        return states.get(index).getRunningModuleOfType(clazz);
    }

    @Override
    void initialize() {
        StringBuilder builder = new StringBuilder("Composed of ");
        for (int i = 0; i < states.size(); i++) {
            if (i < states.size() - 1) {
                builder.append(states.get(i).getName() + ", ");
            } else {
                builder.append("and " + states.get(i).getName());
            }
        }
        Robot.logger.log("StateMachine", builder);
    }

    @Override
    void execute() {
        if (index < states.size())
            if (states.get(index).run()) {
                Robot.logger.log(getName(), "Ending state " + index + ": " + states.get(index).getName());
                states.get(index).end(false);
                index++;
                if (index < states.size()) {
                    startState();
                }
            }
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() || index >= states.size();
    }

    public State getState() {
        return states.get(index);
    }

    public static class Builder {

        private List<State> states = new ArrayList<>();
        private List<List<State>> processes = new ArrayList<>();

        public Builder addState(Module module) {
            return addState(new StandardState(module));
        }

        public Builder addStates(StateMachineProvider values) {
            while (processes.size() < values.getValues().size())
                processes.add(null);
            for (StateMachineValue value : values.getValues()) {
                states.add(value.getState());
                processes.add(states.size() - 1, value.getProcesses());
            }
            return this;
        }

        public Builder addState(State state) {
            states.add(state);
            return this;
        }

        public StateMachine build() {
            while (processes.size() < states.size())
                processes.add(null);
            return new StateMachine(states, processes);
        }
    }

    @Override
    public List<State> getChildren() {
        return states;
    }

    @Override
    public boolean hasChildren() {
        return states.size() > 0;
    }

    @Override
    public StringBuilder buildHierarchy(StringBuilder builder, int depth) {
        builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append(getName()).append(":\n");
        for(int i = 0; i < states.size(); i++) {
            builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
            builder.append("State ").append(i).append(":\n");
            states.get(i).buildHierarchy(builder, depth + 3);
            if(processes.size() > 0 && processes.get(i) != null)
                for(State process: processes.get(i)) {
                    process.buildHierarchy(builder, depth + 3);
                }
        }
        return builder;
    }
}
