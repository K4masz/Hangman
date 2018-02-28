package core;

import Data.daos.CategoryDAOImpl;
import Data.daos.StatisticDAOImpl;
import Data.daos.WordDAOImpl;
import Data.model.Player;
import Data.daos.PlayerDAOImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController {

    @FXML
    public Pane mainPane;
    @FXML
    public MenuBar newGame;

    //TODO Change picture on the begging to 0.png
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

    private Scene presentScene;
    private Player presentPlayer;
    private String currentPassword;
    private String currentCategory;
    private String revealedPassword;
    private int noOfMisses = 0;
    private boolean gameInProgress = false;

    private WordDAOImpl wordDAO = new WordDAOImpl();
    private PlayerDAOImpl playerDAO = new PlayerDAOImpl();
    private StatisticDAOImpl statisticDAO = new StatisticDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

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
        for (Player player : players) {

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
                presentPlayer = playerDAO.getPlayerByName(name);
                currentPlayerLabel.setText("Current Player: " + presentPlayer.getName());
            });
        }
    }

    private void missLetter() {
        noOfMisses++;
        if (noOfMisses < 11)
            imageView.setImage(new Image("./img/" + noOfMisses + ".png"));
        else
            winLabel.setText("YOU LOST");
    }

    private void acquirePassword() {

    }

    private String preparePassword(String word) {
        String spacedPassword = "";
        for (int i = 0; i < word.length(); i++) {
            spacedPassword += word.charAt(i) + " ";
        }
        revealedPassword = spacedPassword;
        String hiddenPassword = "";
        for (int i = 0; i < word.length(); i++) {
            if (spacedPassword.charAt(i) != ' ')
                hiddenPassword += '_';
            else
                hiddenPassword += " ";
        }
        return hiddenPassword;
    }

    public void prepareGame(ActionEvent actionEvent) {
        imageView.setImage(new Image("./img/" + noOfMisses + ".png"));

        //Load random password from DB
        //assign retrived pass to variable
        //currentPassword = var
        //assign retrived category to variable
        //currentCategory = var

        //retrived pass goes there V
        //String password = preparePassword(var)

        //temporary section
        String category = "building";
        String password = "h o u s e";
        String hiddenPassword = "_ _ _ _ _";
        //

        categoryLabel.setText("Category: " + category);
        passwordLabel.setText(hiddenPassword);

        presentScene = mainPane.getScene();
        presentScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (gameInProgress)
                    isCharacterPresentInPassword(event.getText());
            }
        });
        game();
    }


    private void game() {

        gameInProgress = true;
        do {

            //Game logic
            //Wait for key to be pressed

            //key is present in password ? reveal keys in pass : missClick();


            //DO while is passReav == false or isMaxNumberOfMissesReached == false
        } while (isPasswordRevealed(currentPassword) || isMaxNumberOfMissesReached());
        gameInProgress = false;
        if (isPasswordRevealed(currentPassword))
            winLabel.setText("YOU WON!");
        else
            winLabel.setText("YOU LOST!");
    }

    private boolean isPasswordRevealed(String hiddenPassword) {
        return revealedPassword.equals(hiddenPassword) ? true : false;
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    private boolean isCharacterPresentInPassword(String sign) {
        return revealedPassword.contains(sign);
    }

    public boolean isMaxNumberOfMissesReached() {
        return noOfMisses == 10 ? true : false;
    }
}

