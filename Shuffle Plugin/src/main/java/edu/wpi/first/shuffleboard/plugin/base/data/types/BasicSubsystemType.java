package edu.wpi.first.shuffleboard.plugin.base.data.types;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.plugin.base.data.BasicSubsystemData;

import java.util.Map;
import java.util.function.Function;

/**
 * Type for basic subsystems. These really only have information about a command for the subsystem
 */
public final class BasicSubsystemType extends ComplexDataType<BasicSubsystemData> {

  public static final edu.wpi.first.shuffleboard.plugin.base.data.types.BasicSubsystemType Instance = new edu.wpi.first.shuffleboard.plugin.base.data.types.BasicSubsystemType();

  private BasicSubsystemType() {
    super("Subsystem", BasicSubsystemData.class);
  }

  @Override
  public Function<Map<String, Object>, BasicSubsystemData> fromMap() {
    return BasicSubsystemData::new;
  }

  @Override
  public BasicSubsystemData getDefaultValue() {
    return new BasicSubsystemData(false, "", false, "");
  }

}
