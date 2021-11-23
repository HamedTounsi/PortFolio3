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

    //GradeController constructor
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

    //This holds all the events
    public void setView(GradeView view){
        this.view = view;
        //Exit Button Event
        view.exitBtnStudent.setOnAction(e-> Platform.exit());
        view.exitBtnCourse.setOnAction(e-> Platform.exit());

        //Event for the findCourse Button
        EventHandler<ActionEvent> printCourseAvr = e -> printCourseGradeAvr(view.courseComB.getValue(), view.courseGradeArea);
        view.findCourseBtn.setOnAction(printCourseAvr);

        //Event for the findStudent Button
        EventHandler<ActionEvent> printStudentAvr = e -> printStudentGradeAvr(view.studentComB.getValue(), view.studentGradeArea);
        view.findStudentBtn.setOnAction(printStudentAvr);

        //Event for the editGrade Button
        EventHandler<ActionEvent> updateGrade =
                e -> updateIfGradeNull(view.studentComB.getValue(), view.editGradeComB.getValue(), view.studentGradeArea);
        view.editGradeBtn.setOnAction(updateGrade);
    }

    //Take the arrayList returned by SQLQueryStudentNames() and returns it as a ObservableList
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

    //Take the arrayList returned by SQLQueryCourseNames() and returns it as a ObservableList
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

    //Takes the results from findCourseGradeAvr() and findTeacherName() and prints it out in the textarea
    public void printCourseGradeAvr(String courseName, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("The average grade for the course is: \n");
        String roundedAvr = String.format("%.2g%n", model.findCourseGradeAvr(courseName));
        String teacherName = model.findTeacherName(courseName);
        txtArea.appendText(String.valueOf(roundedAvr))
        ;
        txtArea.appendText("\nTeacher of this course is: "+teacherName);
    }

    //Takes the results from findStudentGradeAvr() and findStudentGrade() and prints it out
    public void printStudentGradeAvr(String studentName, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("This students average grade is: \n");
        Double avr = model.findStudentGradeAvr(studentName);
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

    //Updates and refresh the grade
    public void updateIfGradeNull(String studentName, Integer newGrade, TextArea txtArea){
        model.updateGrade(studentName, newGrade);
        printStudentGradeAvr(studentName, txtArea);
    }
}
