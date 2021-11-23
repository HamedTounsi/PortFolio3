import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Main class extends Application (Inherit from Application class)
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        String url = "jdbc:sqlite:C:/Users/Hamed Tounsi/IdeaProjects/PortFolio 3/UniDB.sqlite"; //Path to our database (Edit this path to run program)
        GradesModel model = new GradesModel(url); //Creating a GradesModel object
        GradeController controller = new GradeController(model); //Creating a GradeController object
        GradeView view = new GradeView(controller); //Creating a GradeView object
        controller.setView(view);

        primaryStage.setTitle("University Grades");
        primaryStage.setScene(new Scene(view.asParent(), 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
