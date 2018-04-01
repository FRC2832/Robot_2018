package org.usfirst.frc.team2832.robot.statemachine;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of {@link Subsystem} that stores a list of flags
 * @param <E> The enumeration type of flags being stored
 */
public abstract class WarriorSubsystem<E extends Enum> {

    private List<E> flags;

    public WarriorSubsystem() {
        flags = new ArrayList<>();
    }

    /**
     * Gets stored flags
     * @return stored flags
     */
    public List<E> getFlags() {
        return flags;
    }

    /**
     * Check if enum has been flagged
     * @param flag to check
     * @return whether flag is stored
     */
    public boolean hasFlag(E flag) {
        return this.flags.contains(flag);
    }

    /**
     * Check if all enums have been flagged
     * @param flags to check
     * @return whether all flags have been "flagged"
     */
    public boolean hasAll(E...flags) {
        return hasAll(Arrays.asList(flags));
    }

    /**
     * Check if all enums have been flagged
     * @param flags to check
     * @return whether all flags have been "flagged"
     */
    public boolean hasAll(List<E> flags) {
        return this.flags.containsAll(flags);
    }

    /**
     * Check if any of the provided enums have been flagged
     * @param flags to check
     * @return whether any flags have been "flagged"
     */
    public boolean hasAny(E...flags) {
        return hasAny(Arrays.asList(flags));
    }

    /**
     * Check if any of the provided enums have been flagged
     * @param flags to check
     * @return whether any flags have been "flagged"
     */
    public boolean hasAny(List<E> flags) {
        for(E flag: flags)
            if(this.flags.contains(flag))
                return true;
        return false;
    }

    /**
     * Clears stored flags
     */
    public void clearFlags() {
        this.flags.clear();
    }

    /**
     * Stores provided flags
     * @param flags to store
     */
    public void addFlags(E...flags) {
        addFlags(Arrays.asList(flags));
    }

    /**
     * Stores provided flags
     * @param flags to store
     */
    public void addFlags(List<E> flags) {
        this.flags.addAll(flags);
    }

    /**
     * Stores the provided flag
     * @param flag to store
     */
    public void addFlag(E flag) {
        this.flags.add(flag);
    }
}
