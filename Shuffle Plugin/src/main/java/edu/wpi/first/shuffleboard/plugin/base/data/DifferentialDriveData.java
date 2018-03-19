package edu.wpi.first.shuffleboard.plugin.base.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import edu.wpi.first.shuffleboard.api.util.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * Represents data from a differential drive base. All motor speeds are in the range <tt>(-1, 1)</tt>.
 */
public final class DifferentialDriveData extends ComplexData<edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData> {

  private final double leftSpeed;
  private final double rightSpeed;

  /**
   * Creates a new differential drive data object.
   *
   * @param leftSpeed  the speed of the left motor
   * @param rightSpeed the speed of the right motor
   */
  public DifferentialDriveData(double leftSpeed, double rightSpeed) {
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
  }

  /**
   * Creates a new differential drive data object from a map.
   *
   * @param map the map to create a data object from
   *
   * @throws java.util.NoSuchElementException if the map is missing any motor speed entry
   */
  public static edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData fromMap(Map<String, Object> map) {
    return new edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData(
        Maps.get(map, "Left Motor Speed"),
        Maps.get(map, "Right Motor Speed")
    );
  }

  @Override
  public Map<String, Object> asMap() {
    return Maps.<String, Object>builder()
        .put("Left Motor Speed", leftSpeed)
        .put("Right Motor Speed", rightSpeed)
        .build();
  }

  public double getLeftSpeed() {
    return leftSpeed;
  }

  public double getRightSpeed() {
    return rightSpeed;
  }

  public edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData withLeftSpeed(double leftSpeed) {
    return new edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData(leftSpeed, rightSpeed);
  }

  public edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData withRightSpeed(double rightSpeed) {
    return new edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData(leftSpeed, rightSpeed);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData that = (edu.wpi.first.shuffleboard.plugin.base.data.DifferentialDriveData) o;
    return this.leftSpeed == that.leftSpeed
        && this.rightSpeed == that.rightSpeed;
  }

  @Override
  public int hashCode() {
    return Objects.hash(leftSpeed, rightSpeed);
  }

  @Override
  public String toString() {
    return String.format("DifferentialDriveData(leftSpeed=%s, rightSpeed=%s)", leftSpeed, rightSpeed);
  }

}
