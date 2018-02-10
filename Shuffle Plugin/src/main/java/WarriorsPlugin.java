import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.data.DataTypes;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import widget.LogWidget;

import java.util.List;
import java.util.Map;

@Description(
        group = "",
        name = "Warriors Plugin",
        version = "1.0.0",
        summary = "Provides widgets"
)
public class WarriorsPlugin extends Plugin {

    public WarriorsPlugin() {
        super();
    }

    @Override
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
                WidgetType.forAnnotatedWidget(LogWidget.class)
        );
    }

    @Override
    public Map<DataType, ComponentType> getDefaultComponents() {
        return ImmutableMap.<DataType, ComponentType>builder()
                .put(DataTypes.StringArray, WidgetType.forAnnotatedWidget(LogWidget.class))
                .build();
    }
}
