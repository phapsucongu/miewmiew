module snowflake.ooproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens snowflake.ooproject to javafx.fxml;
    exports snowflake.ooproject.commandline;
}