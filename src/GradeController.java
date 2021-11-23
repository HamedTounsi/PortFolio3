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

        EventHandler<ActionEvent> updateGrade =
                e -> checkIfGradeNull(view.studentComB.getValue(), view.editGradeComB.getValue(), view.studentGradeArea);
        view.editGradeBtn.setOnAction(updateGrade);
    }

    public ObservableList<String> getStudents(){
        ArrayList<String> names = null;
        try {
            names = model.SQLQueryStudentNames();
        } catch (SQLException e){
            e.printStackTrace();
        }
        assert names != null;
        return FXCollections.observableList(names);
    }

    public ObservableList<String> getCourses(){
        ArrayList<String> names = null;
        try {
            names = model.SQLQueryCourseName();
        } catch (SQLException e){
            e.printStackTrace();
        }
        assert names != null;
        return FXCollections.observableList(names);
    }

    public void printCourseGradeAvr(String courseName, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("The average grade for the course is: \n");
        String roundedAvr = String.format("%.2g%n", model.preparedStmtCourseGradeAvr(courseName));
        String teacherName = model.findTeacherName(courseName);
        txtArea.appendText(String.valueOf(roundedAvr))
        ;
        txtArea.appendText("\nTeacher of this course is: "+teacherName);
    }

    public void printStudentGradeAvr(String studentName, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("This students average grade is: \n");
        Double avr = model.preparedStmtStudentGradeAvr(studentName);
        ArrayList<gradesAndCourse> Grade = model.findStudentGrade(studentName);
        txtArea.appendText(String.valueOf(avr));
        txtArea.appendText("\nStudents grades: \n");
        for (gradesAndCourse result : Grade) {
            if (result.studentGrade == 400) {
                txtArea.appendText(result.CourseID + ": No grade have been given yet.\n");
                view.editBtnVisible();
            } else {
                txtArea.appendText(result.CourseID + ": " + result.studentGrade + "\n");
                view.editBtnNotVisible();
            }
        }
    }

    public void checkIfGradeNull(String studentName, Integer newGrade, TextArea txtArea){
        model.updateGrade(studentName, newGrade);
        printStudentGradeAvr(studentName, txtArea);
    }
}
