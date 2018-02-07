package org.usfirst.frc.team2832.robot;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public abstract class DiagnosticSubsystem<E extends Enumeration> extends Subsystem {

    private List<E> flags;

    public DiagnosticSubsystem() {
        flags = new ArrayList<>();
    }

    public List<E> getFlags;

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
