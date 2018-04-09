package org.usfirst.frc.team2832.robot.statemachine;

import com.ctre.phoenix.Logger;
import org.usfirst.frc.team2832.robot.Robot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class StateMachine extends State<StateMachine> {

    private List<State> states;
    private int index;

    private StateMachine(List<State> states) {
        this.states = states;
    }

    @Override
    void start() {
        super.start();
        index = 0;
        Robot.logger.log(getName(), "Starting state " + index + ": " + states.get(index).getName());
        states.get(index).start();
    }

    @Override
    public Set<Subsystems> getRequirements() {
        Set<Subsystems> set = new HashSet<>(super.getRequirements());
        for(State state: states) {
            set.addAll(state.getRequirements());
        }
        return set;
    }

    @Override
    public Module getRunningModuleOfType(Class clazz) {
        return null;
    }

    @Override
    void initialize() {
        StringBuilder builder = new StringBuilder("Composed of ");
        for(int i = 0; i < states.size(); i++) {
            if(i < states.size() - 1) {
                builder.append(states.get(i).getName() + ", ");
            } else {
                builder.append("and " + states.get(i).getName());
            }
        }
        Robot.logger.log("StateMachine", builder);
    }

    @Override
    void execute() {
        if(index < states.size())
            if(states.get(index).run()) {
                Robot.logger.log(getName(), "Ending state " + index + ": " + states.get(index).getName());
                states.get(index).end(false);
                index++;
                if(index < states.size()) {
                    Robot.logger.log(getName(), "Starting state " + index + ": " + states.get(index).getName());
                    states.get(index).start();
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

        public Builder addState(Module module) {
            return addState(new StandardState(module));
        }

        public Builder addStates(StateProvider states) {
            this.states.addAll(states.getStates());
            return this;
        }

        public Builder addState(State state) {
            states.add(state);
            return this;
        }

        public StateMachine build() {
            return new StateMachine(states);
        }
    }
}
