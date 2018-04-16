package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;
import java.util.function.Supplier;

public class SwitchState extends StateSelector {

    private Map<Object, State> entries;
    private Supplier supplier;

    private SwitchState(Map<Object, State> entries, Supplier supplier) {
        super(entries.values());
        this.entries = entries;
        this.supplier = supplier;
    }

    @Override
    State getSelected() {
        Object key = supplier.get();
        if(!entries.containsKey(key))
            throw new RuntimeException("'" + key + "' was not found in entry list");
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

        public SwitchState build(Supplier supplier) {
            return new SwitchState(entries, supplier);
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
