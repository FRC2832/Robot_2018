package edu.wpi.first.shuffleboard.plugin.base.data.types;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.plugin.base.data.ThreeAxisAccelerometerData;

import java.util.Map;
import java.util.function.Function;

public final class ThreeAxisAccelerometerType extends ComplexDataType<ThreeAxisAccelerometerData> {

  public static final edu.wpi.first.shuffleboard.plugin.base.data.types.ThreeAxisAccelerometerType Instance = new edu.wpi.first.shuffleboard.plugin.base.data.types.ThreeAxisAccelerometerType();

  private ThreeAxisAccelerometerType() {
    super("3AxisAccelerometer", ThreeAxisAccelerometerData.class);
  }

  @Override
  public Function<Map<String, Object>, ThreeAxisAccelerometerData> fromMap() {
    return ThreeAxisAccelerometerData::new;
  }

  @Override
  public ThreeAxisAccelerometerData getDefaultValue() {
    return new ThreeAxisAccelerometerData(0, 0, 0);
  }
}
