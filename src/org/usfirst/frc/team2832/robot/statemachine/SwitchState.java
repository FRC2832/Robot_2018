package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SwitchState extends StateSelector {

    private Map<Object, State> entries;
    private Supplier supplier;
    private State defaultState;

    private SwitchState(Map<Object, State> entries, Supplier supplier, State defaultState) {
        super(entries.values());
        this.entries = entries;
        this.supplier = supplier;
        this.defaultState = defaultState;
    }

    @Override
    State getSelected() {
        Object key = supplier.get();
        if(!entries.containsKey(key)) {
            if(defaultState == null)
                throw new RuntimeException("Key not found and default state is null");
            return defaultState;
        }
        return entries.get(key);
    }

    @Override
    public Set<Subsystems> getRequirements() {
        Set<Subsystems> set = new HashSet<>(super.getRequirements());
        for(State state: entries.values())
            set.addAll(state.getRequirements());
        return set;
    }

    public static class SwitchStateBuilder {
        private Map<Object, State> entries = new HashMap<>();

        public SwitchStateBuilder add(Object key, State state) {
            entries.put(key, state);
            return this;
        }

        public SwitchState build(State defaultState, Supplier supplier) {
            return new SwitchState(entries, supplier, defaultState);
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
        builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append("switch(key) {\n");
        for(Object object: entries.keySet()) {
            builder.append(IntStream.range(0, depth + 3).mapToObj(w -> " ").collect(Collectors.joining("")));
            builder.append("case ").append(object).append(":\n");
            entries.get(object).buildHierarchy(builder, depth + 6);
        }
        builder.append(IntStream.range(0, depth + 3).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append("default:\n");
        defaultState.buildHierarchy(builder, depth + 6);
        builder.append(IntStream.range(0, depth).mapToObj(w -> " ").collect(Collectors.joining("")));
        builder.append("}\n");
        return builder;
    }
}
