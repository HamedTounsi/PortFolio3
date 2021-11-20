import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String url = "jdbc:sqlite:C:/Users/Hamed Tounsi/IdeaProjects/PortFolio 3/UniDB.sqlite";
        GradesModel model = new GradesModel(url);
        GradeController controller = new GradeController(model);
        GradeView view = new GradeView(controller);
        controller.setView(view);

        primaryStage.setTitle("University Grades");
        primaryStage.setScene(new Scene(view.asParent(), 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
