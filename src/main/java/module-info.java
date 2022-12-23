module com.visualizer.visualizerinjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.visualizer.visualizerinjava to javafx.fxml;
    exports com.visualizer.visualizerinjava;
}