package org.usfirst.frc.team2832.robot.statemachine;

import java.util.*;

public class CompoundState extends State<CompoundState> {

    private List<Module> modules = new ArrayList<>();

    public CompoundState(Module...modules) {
        this(Arrays.asList(modules));
    }

    public CompoundState(List<Module> modules) {
        this.modules.addAll(modules);
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
