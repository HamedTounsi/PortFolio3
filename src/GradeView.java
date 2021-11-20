import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;


public class GradeView {
    GradeController controller;
    GridPane StudentView;
    GridPane CourseView;
    private TabPane TabView;


    Button exitBtnStudent = new Button("Exit");
    Button exitBtnCourse = new Button("Exit");
    Button findStudentBtn = new Button("Find Grade");
    Button findCourseBtn = new Button("Find Grade");
    Label studentLbl = new Label("Student");
    Label courseLbl = new Label("Course");

    ComboBox<String> studentComB = new ComboBox<>();
    ComboBox<String> courseComB = new ComboBox<>();
    TextArea studentGradeArea = new TextArea();
    TextArea courseGradeArea = new TextArea();

    public GradeView(GradeController controller){
        this.controller = controller;
        createAndConfigure();
    }

    private void createAndConfigure(){
        StudentView = new GridPane();
        CourseView = new GridPane();
        TabView = new TabPane();
        Tab studentTab = new Tab("Students", new Label("Search by student"));
        Tab courseTab = new Tab("Courses", new Label("Search by course"));

        //Set the closing policy to false so the user can't close the tabs
        studentTab.setClosable(false);
        courseTab.setClosable(false);

        TabView.getTabs().add(studentTab);
        TabView.getTabs().add(courseTab);

        StudentView.setMinSize(300,200);
        StudentView.setPadding(new Insets(10,10,10,10));
        StudentView.setVgap(5);
        StudentView.setHgap(1);

        CourseView.setMinSize(300,200);
        CourseView.setPadding(new Insets(10,10,10,10));
        CourseView.setVgap(5);
        CourseView.setHgap(1);

        studentTab.setContent(StudentView);
        courseTab.setContent(CourseView);

        StudentView.add(studentLbl, 1, 1);
        StudentView.add(studentComB, 14, 1);
        ObservableList<String> studentList = controller.getStudents();
        studentComB.setItems(studentList);
        StudentView.add(findStudentBtn, 15, 1);
        StudentView.add(studentGradeArea, 2, 7, 15, 7);
        StudentView.add(exitBtnStudent, 20, 15);


        CourseView.add(courseLbl, 1, 1);
        CourseView.add(courseComB, 14, 1);
        ObservableList<String> courseList = controller.getCourses();
        courseComB.setItems(courseList);
        CourseView.add(findCourseBtn, 15, 1);
        CourseView.add(courseGradeArea, 2, 7, 15, 7);
        CourseView.add(exitBtnCourse, 20, 15);

    }

    public Parent asParent(){
        return TabView;
    }


}
