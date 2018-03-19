package edu.wpi.first.shuffleboard.plugin.base.data;

import edu.wpi.first.shuffleboard.api.data.MapData;

import java.util.HashMap;
import java.util.Map;

public class RobotPreferencesData extends MapData {

  public RobotPreferencesData(Map<String, Object> map) {
    super(map);
  }

  @Override
  public edu.wpi.first.shuffleboard.plugin.base.data.RobotPreferencesData put(String key, Object value) {
    HashMap<String, Object> map = new HashMap<>(asMap());
    map.put(key, value);
    return new edu.wpi.first.shuffleboard.plugin.base.data.RobotPreferencesData(map);
  }

}
