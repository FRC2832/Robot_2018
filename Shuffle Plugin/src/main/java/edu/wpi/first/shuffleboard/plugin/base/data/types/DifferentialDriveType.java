package edu.wpi.first.shuffleboard.plugin.base.data.types;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData;

import java.util.Map;
import java.util.function.Function;

public final class DifferentialDriveType extends ComplexDataType<DifferentialDriveData> {

  public static final edu.wpi.first.shuffleboard.plugin.base.data.types.DifferentialDriveType Instance = new edu.wpi.first.shuffleboard.plugin.base.data.types.DifferentialDriveType();

  private DifferentialDriveType() {
    super("DifferentialDrive", DifferentialDriveData.class);
  }

  @Override
  public Function<Map<String, Object>, DifferentialDriveData> fromMap() {
    return DifferentialDriveData::fromMap;
  }

  @Override
  public DifferentialDriveData getDefaultValue() {
    return new DifferentialDriveData(0, 0);
  }

}
