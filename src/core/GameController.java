package core;

import Data.daos.CategoryDAOImpl;
import Data.daos.StatisticDAOImpl;
import Data.daos.WordDAOImpl;
import Data.model.Player;
import Data.daos.PlayerDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController {

    @FXML
    public MenuBar newGame;
    @FXML
    public ImageView imageView;
    @FXML
    public Label currentPlayerLabel;
    @FXML
    public Label categoryLabel;
    @FXML
    public Button repeatGameButton;
    @FXML
    public Label winLabel;
    @FXML
    public Label missCounter;
    @FXML
    public Label passwordLabel;

    private Player presentPlayer;
    private String currentPassword;

    private WordDAOImpl wordDAO = new WordDAOImpl();
    private PlayerDAOImpl playerDAO = new PlayerDAOImpl();
    private StatisticDAOImpl statisticDAO = new StatisticDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    private int noOfMisses;


    public void createNewPlayer(ActionEvent actionEvent) {
        //setting up dialog
        TextInputDialog dialog = new TextInputDialog("nick");
        dialog.setTitle("New Player");
        dialog.setHeaderText("New Player Creation");
        dialog.setContentText("Please enter your name:");
        Optional<String> result = dialog.showAndWait();

        //Retriving info from dialog
        if (result.isPresent()) {
            result.ifPresent(name -> playerDAO.addPlayer(new Player(name)));
        }
    }

    public void changePlayer(ActionEvent actionEvent) {
        //Getting players from DB
        List<Player> players = playerDAO.getAllPlayers();
        //Populating the List
        List<String> choices = new ArrayList<>();
        for(Player player : players) {

            String name = player.getName();
            int id = player.getplayerId();

            System.out.println(id + " " + name);
            choices.add(name);
            }

        //Setting up dialog
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Choice Player");
        dialog.setHeaderText("Choice the current Player");
        dialog.setContentText("Choose Player:");
        Optional<String> result = dialog.showAndWait();

        //Retriving info from dialog
        if (result.isPresent()) {
            result.ifPresent(name -> {

                currentPlayerLabel.setText("Current Player: " + name);
            });
        }
    }

    public void missLetter() {
        noOfMisses++;
        if (noOfMisses < 11)
            imageView.setImage(new Image("./img/" + noOfMisses + ".png"));
        else
            winLabel.setText("YOU LOST");
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void acquirePassword() {}

    public void play(ActionEvent actionEvent) {
        noOfMisses = 0;
        imageView.setImage(new Image("./img/" + noOfMisses + ".png"));

        //Load random password from DB


        String category = "building";
        String password = "house";
        String hiddenPassword = "";

        categoryLabel.setText(category);

        for (int i = 0; i < password.length() - 1; i++)
            hiddenPassword += "_";

        passwordLabel.setText(hiddenPassword);

    }


}

