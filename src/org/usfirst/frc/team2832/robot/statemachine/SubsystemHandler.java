package org.usfirst.frc.team2832.robot.statemachine;

import org.usfirst.frc.team2832.robot.Robot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SubsystemHandler {

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
                Set<Subsystems> requirements = state.getRequirements();
                for(Subsystems subsystem: requirements) {
                    states.put(subsystem, null);
                }
                Robot.logger.log("SubsystemHandler", "Ending " + state.getName());
                state.end(false);
                for(Subsystems subsystem: requirements) {
                    if(subsystems.get(subsystem).getDefaultState() != null) {
                        Robot.logger.log("SubsystemHandler", "Starting " + subsystems.get(subsystem).getDefaultState().getName() + " as default command");
                        start(subsystems.get(subsystem).getDefaultState());
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
                states.get(subsystem).end(true);
                Robot.logger.log("SubsystemHandler", "Interrupting " + states.get(subsystem).getName());
            }
            states.put(subsystem, state);
        }
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
}