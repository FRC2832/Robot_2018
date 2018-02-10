package widget;

import data.LogData;
import edu.wpi.first.shuffleboard.api.components.ShuffleboardDialog;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.event.ActionEvent;
import java.io.*;

@Description(name = "Log", dataTypes = String[].class)
@ParametrizedController("LogWidget.fxml")
public class LogWidget extends SimpleAnnotatedWidget<LogData> {

    @FXML
    private Pane root;
    @FXML
    private Button button;

    private final Property<Color> hasColor = new SimpleObjectProperty<>(this, "colorWhenHasLog", Color.LAWNGREEN);
    private final Property<Color> noHasColor = new SimpleObjectProperty<>(this, "colorWhenNoHasLog", Color.DARKRED);

    @FXML
    private void initialize() {
        root.backgroundProperty().bind(
                Bindings.createObjectBinding(() -> createSolidColorBackground(getColor()), dataProperty(), hasColor, noHasColor));
        exportProperties(hasColor, noHasColor);
        button.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("S:\\Programming\\FRC\\Robot_2018_Intellij\\Robot_2018\\log.txt"));
                    for (String string : getData().getValue()) {
                        writer.write(string);
                        writer.newLine();
                    }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Background createSolidColorBackground(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    private Color getColor() {
        final LogData data = getData();
        if (data == null)
            return noHasColor.getValue();
        else
            return hasColor.getValue();
    }

    @Override
    public Pane getView() {
        return root;
    }
}
