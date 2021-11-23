import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;


public class GradeView {
    GradeController controller;
    GridPane StudentView;
    GridPane CourseView;
    private TabPane TabView;
    ArrayList<Integer> possibleGrades = new ArrayList<>();

    Button exitBtnStudent = new Button("Exit");
    Button exitBtnCourse = new Button("Exit");
    Button findStudentBtn = new Button("Find Grade");
    Button findCourseBtn = new Button("Find Grade");
    Button editGradeBtn = new Button("Edit Grade");
    Label studentLbl = new Label("Student");
    Label courseLbl = new Label("Course");

    ComboBox<String> studentComB = new ComboBox<>();
    ComboBox<String> courseComB = new ComboBox<>();
    ComboBox<Integer> editGradeComB = new ComboBox<>();
    TextArea studentGradeArea = new TextArea();
    TextArea courseGradeArea = new TextArea();

    public void editBtnVisible(){
        editGradeComB.setVisible(true);
        editGradeBtn.setVisible(true);
    }

    public void editBtnNotVisible(){
        editGradeComB.setVisible(false);
        editGradeBtn.setVisible(false);
    }

    //GradeView constructor
    public GradeView(GradeController controller){
        this.controller = controller;
        createAndConfigure();
    }

    //Here we create and configure all the nodes(buttons, labels, comboBoxes, TextArea)
    private void createAndConfigure(){
        StudentView = new GridPane();
        CourseView = new GridPane();
        TabView = new TabPane();
        Tab studentTab = new Tab("Students");
        Tab courseTab = new Tab("Courses");
        possibleGrades.add(-3);
        possibleGrades.add(0);
        possibleGrades.add(2);
        possibleGrades.add(4);
        possibleGrades.add(7);
        possibleGrades.add(10);
        possibleGrades.add(12);

        //Set the closing policy to false so the user can't close the tabs
        studentTab.setClosable(false);
        courseTab.setClosable(false);

        TabView.getTabs().add(studentTab);
        TabView.getTabs().add(courseTab);

        //Setting the size, padding and gap of the two GridPanes
        StudentView.setMinSize(300,200);
        StudentView.setPadding(new Insets(10,10,10,10));
        StudentView.setVgap(5);
        StudentView.setHgap(1);

        CourseView.setMinSize(300,200);
        CourseView.setPadding(new Insets(10,10,10,10));
        CourseView.setVgap(5);
        CourseView.setHgap(1);

        //assign each GridPane to it's Tabview
        studentTab.setContent(StudentView);
        courseTab.setContent(CourseView);

        //Adding the nodes to the StudentView GridPane
        StudentView.add(studentLbl, 1, 1);
        StudentView.add(studentComB, 14, 1);
        studentComB.setItems(controller.getStudents());
        studentComB.getSelectionModel().selectFirst();
        StudentView.add(findStudentBtn, 15, 1);
        StudentView.add(studentGradeArea, 2, 7, 15, 7);
        studentGradeArea.setEditable(false); //Ready-only TextArea
        StudentView.add(exitBtnStudent, 20, 15);

        editGradeComB.setItems(FXCollections.observableList(possibleGrades));
        StudentView.add(editGradeComB, 14, 15);
        editGradeComB.getSelectionModel().selectFirst();
        StudentView.add(editGradeBtn, 14, 16);
        editGradeComB.setVisible(false);
        editGradeBtn.setVisible(false);

        //Adding the nodes to the CourseView GridPane
        CourseView.add(courseLbl, 1, 1);
        CourseView.add(courseComB, 14, 1);
        courseComB.setItems(controller.getCourses());
        courseComB.getSelectionModel().selectFirst();
        CourseView.add(findCourseBtn, 15, 1);
        CourseView.add(courseGradeArea, 2, 7, 15, 7);
        courseGradeArea.setEditable(false); //Ready-only TextArea
        CourseView.add(exitBtnCourse, 20, 15);

    }
    public Parent asParent(){
        return TabView;
    }
}
