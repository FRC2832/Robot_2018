package org.usfirst.frc.team2832.robot.statemachine;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.*;

public class CompoundState extends State<CompoundState> {

    private List<Module> modules = new ArrayList<>();

    public CompoundState(Module...modules) {
        this(Arrays.asList(modules));
    }

    public CompoundState(List<Module> modules) {
        if(modules.size() == 0)
            throw new IllegalArgumentException("Module list must not be empty");
        this.modules.addAll(modules);
        List<Subsystems> requirements = new ArrayList<>();
        for(Module module: modules)
            for(Subsystems subsystem: ((SubsystemModule) module).getRequired()) {
                if (requirements.contains(subsystem))
                    throw new RuntimeException("Modules must not be on separate Subsystems");
                requirements.add(subsystem);
            }
    }

    @Override
    public Module getRunningModuleOfType(Class clazz) {
        for(Module module: modules)
            if(module.getClass().isAssignableFrom(clazz))
                return module;
        return null;
    }

    @Override
    public Set<Subsystems> getRequirements() {
        Set<Subsystems> set = new HashSet<>(super.getRequirements());
        for(Module module: modules)
            if(module instanceof SubsystemModule && ((SubsystemModule)module).getRequired().size() > 0)
                set.addAll(((SubsystemModule)module).getRequired());
        return set;
    }

    @Override
    void end(boolean interrupted) {
        for(Module module: modules) {
            if(interrupted)
                module.interrupted();
            else
                module.end();
        }
    }

    @Override
    void start() {
        for(Module module: modules)
            module.start();
    }

    @Override
    void execute() {
        for(Module module: modules)
            module.execute();
    }

    @Override
    void initialize() {
        for(Module module: modules)
            module.initialize();
    }

    @Override
    protected boolean isFinished() {
        for(Module module: modules)
            if(module.isFinished())
                return true;
        return super.isFinished();
    }
}
