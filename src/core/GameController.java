package core;

import Data.daos.CategoryDAOImpl;
import Data.daos.PlayerDAOImpl;
import Data.daos.StatisticDAOImpl;
import Data.daos.WordDAOImpl;
import Data.model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

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


    private Player presentPlayer;
    private String currentPassword;
    private String currentCategory;
    private String revealedPassword;
    private int noOfMisses;

    private boolean gameInProgress = false;
    private ArrayList<Character> usedLetters = new ArrayList<>();

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
        if (isMaxNumberOfMissesReached())
            winLabel.setText("YOU LOST");
        else
            imageView.setImage(new Image("./img/" + noOfMisses + ".png"));

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
        for (int i = 0; i < spacedPassword.length(); i++) {
            if (spacedPassword.charAt(i) != ' ')
                hiddenPassword += '_';
            else
                hiddenPassword += " ";
        }
        return hiddenPassword;
    }

    public void prepareGame(ActionEvent actionEvent) {
        noOfMisses = 0;
        imageView.setImage(new Image("./img/" + noOfMisses + ".png"));

        //Load random password from DB

        currentPassword = wordDAO.getWord(31).getContent();
        //assign retrived category to variable
        //currentCategory = var

        String password = preparePassword(currentPassword);

        //temporary section
        String category = "building";
        currentCategory = category;

        winLabel.setText("");
        categoryLabel.setText("Category: " + currentCategory);
        passwordLabel.setText(password);


        mainPane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (gameInProgress)
                    missCounter.setText(event.getText());
                if (isCharacterPresentInPassword(event.getText()) && !(isLetterAlreadyUsed(event.getText()))) {
                    //RevealaLetter

                } else {
                    missLetter();
                }


                if (isPasswordRevealed(currentPassword)) {
                    gameInProgress = false;
                    winLabel.setText("YOU WON!");

                } else if (isMaxNumberOfMissesReached()) {
                    gameInProgress = false;
                    winLabel.setText("YOU LOST!");

                }
            }
        });
        game();
    }

    private void game() {
        gameInProgress = true;


        //Game logic
        //Wait for key to be pressed

        //key is present in password ? reveal keys in pass : missClick();
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
        return noOfMisses == 11 ? true : false;
    }

    private boolean isLetterAlreadyUsed(String sign) {
        return usedLetters.contains(sign.charAt(0));
    }

}

