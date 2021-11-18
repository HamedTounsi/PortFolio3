import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLException;
import java.util.ArrayList;

public class GradeController {
    GradesModel model;
    GradeView view;

    public GradeController(GradesModel model){
        this.model = model;
        try {
            model.connectToUniDB();
            model.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    ArrayList<Integer> grades = new ArrayList<>();


}
