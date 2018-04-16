package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;
import java.util.function.Supplier;

public class IfElseState extends StateSelector {

    private Map<Supplier<Boolean>, State> entries;
    private State defaultState;

    private IfElseState(Map<Supplier<Boolean>, State> entries, State defaultState) {
        super(entries.values());
        this.entries = entries;
        this.defaultState = defaultState;
    }

    @Override
    State getSelected() {
       for(Supplier<Boolean> condition: entries.keySet())
           if(condition.get())
               return entries.get(condition);
       return defaultState;
    }

    @Override
    public Set<Subsystems> getRequirements() {
        Set<Subsystems> set = new HashSet<>(super.getRequirements());
        for(State state: entries.values())
            set.addAll(state.getRequirements());
        return set;
    }

    public static class IfElseStateBuilder {
        private Map<Supplier<Boolean>, State> entries = new LinkedHashMap<>();

        public IfElseStateBuilder add(Supplier<Boolean> condition, State state) {
            entries.put(condition, state);
            return this;
        }

        public IfElseState build(State defaultState) {
            return new IfElseState(entries, defaultState);
        }
    }

    @Override
    public List<State> getChildren() {
        return new ArrayList<>(entries.values());
    }

    @Override
    public boolean hasChildren() {
        return entries.size() > 0;
    }
}
