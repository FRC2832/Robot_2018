package org.usfirst.frc.team2832.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public abstract class DiagnosticSubsystem<E extends Enum> extends Subsystem {

    private List<E> flags;

    public DiagnosticSubsystem() {
        flags = new ArrayList<>();
    }

    public List<E> getFlags;

    public boolean hasFlag(E flag) {
        return flags.contains(flag);
    }

    public void clearFlags() {
        flags.clear();
    }

    public void addFlags(List<E> flags) {
        this.flags.addAll(flags);
    }

    public void addFlag(E flag) {
        this.flags.add(flag);
    }
}
