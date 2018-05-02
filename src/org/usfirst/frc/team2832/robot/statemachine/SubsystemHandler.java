package org.usfirst.frc.team2832.robot.statemachine;

import org.usfirst.frc.team2832.robot.Robot;

import java.util.*;

public class SubsystemHandler implements StateContainer {

    private Map<Subsystems, WarriorSubsystem> subsystems = new HashMap<>();
    private Map<Subsystems, State> states = new HashMap<>();
    private boolean initialized;

    public void update() {
        if(!initialized) {
            Robot.logger.log("SubsystemHandler", "Initializing subsystems");
            for(WarriorSubsystem subsystem: subsystems.values()) {
                Robot.logger.log("SubsystemHandler", "Initializing " + subsystem.getName());
                subsystem.initialize();
            }
            initialized = true;
        }
        for(WarriorSubsystem subsystem: subsystems.values())
            subsystem.update();
        for(State state: states.values()) {
            if(state != null && state.run()) {
                Robot.logger.log("SubsystemHandler", "Ending " + state.getName());
                state.end(false);
                state.setParent(null);
                Set<Subsystems> requirements = state.getRequirements();
                for(Subsystems subsystem: requirements) {
                    states.put(subsystem, null);
                }
                for(Subsystems subsystem: requirements) {
                    if(subsystems.get(subsystem).getDefaultState() != null) {
                        Robot.logger.log("SubsystemHandler", "Starting " + subsystems.get(subsystem).getDefaultState().getName() + " as default command");
                        start(subsystems.get(subsystem).getDefaultState());
                    } else {
                        states.put(subsystem, null);
                    }
                }
            }
        }
    }

    public void start(Module module) {
        start(new StandardState(module));
    }

    public void start(State state) {
        Set<Subsystems> requirements = state.getRequirements();
        if(requirements.size() <= 0) {
            Robot.logger.critical("SubsystemHandler", "Failed to start " + state.getName() + ", it needs to require a subsystem.");
            return;
        }
        for(Subsystems subsystem: requirements) {
            if(!subsystems.containsKey(subsystem))
                throw new RuntimeException((subsystem.name() + " is not registered"));
            if(states.get(subsystem) != null && !states.get(subsystem).isInterruptable()) {
                Robot.logger.critical("SubsystemHandler", "Failed to start " + state.getName() + ", " + states.get(subsystem).getName() + " is not interruptable.");
                return;
            }
        }
        for(Subsystems subsystem: requirements) {
            if(states.get(subsystem) != null) {
                Robot.logger.log("SubsystemHandler", "Interrupting " + states.get(subsystem).getName());
                states.get(subsystem).end(true);
                states.get(subsystem).setParent(null);
            }
            states.put(subsystem, state);
        }
        state.setParent(this);
        Robot.logger.log("SubsystemHandler", "Starting " + state.getName() + " on " + requirements.size() + " subsystems.");
        state.start();
    }

    public void clearStates() {
        for(State state: states.values()) {
            if (state != null) {
                Robot.logger.log("SubsystemHandler", "Interrupting " + state.getName());
                state.end(true);
            }
        }
        states.clear();
    }

    public void register(WarriorSubsystem subsystem, Subsystems value) {
        Robot.logger.log("SubsystemHandler", "registered " + value.name());
        subsystems.put(value, subsystem);
        if(initialized) {
            subsystem.initialize();
            Robot.logger.log("SubsystemHandler", "Initializing " + subsystem.getName());
        }
    }

    public WarriorSubsystem get(Subsystems subsystem) {
        return subsystems.get(subsystem);
    }

    public List<State> getActiveStates() {
        ArrayList<State> active = new ArrayList<>();
        for(State state: states.values())
            if(state != null)
                active.add(state);
        return active;
    }

    @Override
    public List<State> getChildren() {
        return getActiveStates();
    }

    @Override
    public boolean hasChildren() {
        return !states.isEmpty();
    }

    @Override
    public StateComponent getParent() {
        return null;
    }

    @Override
    public boolean hasParent() {
        return false;
    }

    @Override
    public void setParent(StateContainer component) {
        Robot.logger.critical("SubsystemHandler", "Setting the parent is not supported, as this isn't a state");
    }

    @Override
    public String getHierarchy() {
        return buildHierarchy(new StringBuilder(), 0).toString();
    }

    @Override
    public StringBuilder buildHierarchy(StringBuilder builder, int depth) {
        List<State> used = new ArrayList<>();
        for(Subsystems subsystem: states.keySet())
            if(states.get(subsystem) != null) {
                builder.append("-------------").append(subsystem.name()).append("-------------").append("\n");
                if(!used.contains(states.get(subsystem))) {
                    states.get(subsystem).buildHierarchy(builder, depth + 3);
                    used.add(states.get(subsystem));
                } else {
                    builder.append("Already enumerated").append("\n");
                }
            }
        return builder;
    }
}