package org.usfirst.frc.team2832.widgets.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import edu.wpi.first.shuffleboard.api.data.NamedData;

import java.io.File;
import java.util.Map;

public class LogData extends NamedData<String[]>  {

    public LogData(String name, String[] file) {
        super(name, file);
    }

    public LogData(Map<String, Object> map) {
        this((String) map.getOrDefault("Name", "?"), (String[]) map.getOrDefault("Value", new String[]{}));
    }
}
