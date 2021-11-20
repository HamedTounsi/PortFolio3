import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;

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

    public void setView(GradeView view){
        this.view = view;
        view.exitBtnStudent.setOnAction(e-> Platform.exit());
        view.exitBtnCourse.setOnAction(e-> Platform.exit());

        EventHandler<ActionEvent> printCourseAvr = e -> printCourseGradeAvr(view.courseComB.getValue(), view.courseGradeArea);
        view.findCourseBtn.setOnAction(printCourseAvr);

        EventHandler<ActionEvent> printStudentAvr = e -> printStudentGradeAvr(view.studentComB.getValue(), view.studentGradeArea);
        view.findStudentBtn.setOnAction(printStudentAvr);
    }

    public ObservableList<String> getStudents(){
        ArrayList<String> names = null;
        try {
            names = model.SQLQueryStudentNames();
        } catch (SQLException e){
            e.printStackTrace();
        }
        ObservableList<String> studentNames = FXCollections.observableList(names);
        return studentNames;
    }

    public ObservableList<String> getCourses(){
        ArrayList<String> names = null;
        try {
            names = model.SQLQueryCourseName();
        } catch (SQLException e){
            e.printStackTrace();
        }
        ObservableList<String> courseNames = FXCollections.observableList(names);
        return courseNames;
    }

    public void printCourseGradeAvr(String courseID, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("The average grade for the course is: \n");
        Double avr = model.preparedStmtCourseGradeAvr(courseID);
        txtArea.appendText(String.valueOf(avr));
    }

    public void printStudentGradeAvr(String studentID, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("This students average grade is: \n");
        Double avr = model.preparedStmtStudentGradeAvr(studentID);
        txtArea.appendText(String.valueOf(avr));
    }
}
