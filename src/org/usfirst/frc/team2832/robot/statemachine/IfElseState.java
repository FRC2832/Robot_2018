package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<State> children = new ArrayList<>(entries.values());
        children.add(defaultState);
        return children;
    }

    @Override
    public boolean hasChildren() {
        return entries.size() > 0;
    }

    @Override
    public StringBuilder buildHierarchy(StringBuilder builder, int depth) {
        builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append(getName()).append(":\n");
        List<State> states = new ArrayList<>(entries.values());
        for(int i = 0; i < states.size(); i++) {
            builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
            if(i == 0) {
                builder.append("if(i == ").append(i).append(") {\n");
            } else {
                builder.append("} else if(i == ").append(i).append(") {\n");
            }
            states.get(i).buildHierarchy(builder, depth + 3);
        }
        builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append("} else {\n");
        defaultState.buildHierarchy(builder, depth + 3);
        builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append("}\n");
        return builder;
    }
}
