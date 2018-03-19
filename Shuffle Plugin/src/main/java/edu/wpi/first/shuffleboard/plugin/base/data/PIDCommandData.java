package edu.wpi.first.shuffleboard.plugin.base.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import edu.wpi.first.shuffleboard.api.util.Maps;
import edu.wpi.first.shuffleboard.plugin.base.data.CommandData;
import edu.wpi.first.shuffleboard.plugin.base.data.PIDControllerData;

import java.util.Map;

public final class PIDCommandData extends ComplexData<edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData> {

  private final CommandData commandData;
  private final PIDControllerData pidControllerData;

  public PIDCommandData(String name,
                        boolean running,
                        boolean isParented,
                        double p,
                        double i,
                        double d,
                        double f,
                        double setpoint,
                        boolean enabled) {
    commandData = new CommandData(name, running, isParented);
    pidControllerData = new PIDControllerData(p, i, d, f, setpoint, enabled);
  }

  public PIDCommandData(Map<String, Object> map) {
    commandData = new CommandData(map);
    pidControllerData = new PIDControllerData(map);
  }

  public PIDCommandData(CommandData commandData, PIDControllerData pidControllerData) {
    this.commandData = commandData;
    this.pidControllerData = pidControllerData;
  }

  @Override
  public Map<String, Object> asMap() {
    return Maps.<String, Object>builder()
        .putAll(commandData.asMap())
        .putAll(pidControllerData.asMap())
        .build();
  }

  public CommandData getCommandData() {
    return commandData;
  }

  public PIDControllerData getPidControllerData() {
    return pidControllerData;
  }

  public String getName() {
    return commandData.getName();
  }

  public boolean isRunning() {
    return commandData.isRunning();
  }

  public boolean isParented() {
    return commandData.isParented();
  }

  public double getP() {
    return pidControllerData.getP();
  }

  public double getI() {
    return pidControllerData.getI();
  }

  public double getD() {
    return pidControllerData.getD();
  }

  public double getF() {
    return pidControllerData.getF();
  }

  public double getSetpoint() {
    return pidControllerData.getSetpoint();
  }

  public boolean isEnabled() {
    return pidControllerData.isEnabled();
  }

  public edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData withP(double p) {
    return new edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData(commandData, pidControllerData.withP(p));
  }

  public edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData withEnabled(boolean enabled) {
    return new edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData(commandData, pidControllerData.withEnabled(enabled));
  }

  public edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData withRunning(boolean running) {
    return new edu.wpi.first.shuffleboard.plugin.base.data.PIDCommandData(commandData.withRunning(running), pidControllerData);
  }

}
