package org.usfirst.frc.team2832.robot.statemachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StandardState extends State<StandardState> {

    private Module module;

    public StandardState(Module module) {
        super(module.getClass().getSimpleName());
        this.module = module;
    }

    @Override
    public Module getRunningModuleOfType(Class type) {
        if (module.getClass().isAssignableFrom(type))
            return module;
        return null;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public Set<Subsystems> getRequirements() {
        Set<Subsystems> set = new HashSet<>(super.getRequirements());
        if (module instanceof SubsystemModule && ((SubsystemModule) module).getRequired().size() > 0)
            set.addAll(((SubsystemModule) module).getRequired());
        return set;
    }

    @Override
    void end(boolean interrupted) {
        super.end(interrupted);
        if(interrupted)
            module.interrupted();
        else
            module.end();
    }

    @Override
    void execute() {
        module.execute();
    }

    @Override
    void start() {
        super.start();
        module.start();
    }

    @Override
    void initialize() {
        module.initialize();
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() || module.isFinished();
    }
}
