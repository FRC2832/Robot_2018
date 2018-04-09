package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;

public abstract class SubsystemModule implements Module {

    private Set<Subsystems> requirements = new HashSet<>();

    protected final void requires(Subsystems...subsystems) {
        requirements.addAll(Arrays.asList(subsystems));
    }

    protected final Set<Subsystems> getRequired() {
        return requirements;
    }
}
