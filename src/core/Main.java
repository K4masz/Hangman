package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));

        primaryStage.setTitle("Hangman");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {

//        ResultSet result = exec.executeQuery("SELECT * FROM player");
/*
        try {
            while (result.next()) {
                String surname = result.getString("surname");
                String firstname = result.getString("firstname");
                Long phone_number = result.getLong("phone_number");
                int id = result.getInt("id");
                System.out.println(surname + " " + firstname + " " + phone_number + " " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
        launch(args);
    }
}
