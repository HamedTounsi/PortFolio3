import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

import java.awt.*;

public class GradeView {
    private GridPane StartView;
    Button exitBtn = new Button("Exit");

    public GradeView(){
        createAndConfigure();
    }

    private void createAndConfigure(){
        StartView = new GridPane();
        StartView.setMinSize(300,200);
        StartView.setPadding(new Insets(10,10,10,10));
        StartView.setVgap(5);
        StartView.setHgap(1);


        StartView.add(exitBtn, 20, 15);
    }

    public Parent asParent(){
        return StartView;
    }


}
