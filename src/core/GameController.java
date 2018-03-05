package core;

import Data.daos.CategoryDAOImpl;
import Data.daos.PlayerDAOImpl;
import Data.daos.StatisticDAOImpl;
import Data.daos.WordDAOImpl;
import Data.model.Category;
import Data.model.Player;
import Data.model.Word;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameController {

    @FXML
    public Pane mainPane;
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
    private String currentCategory;
    private String revealedPassword;
    private int noOfMisses;

    private boolean gameInProgress = false;
    private ArrayList<Character> usedLetters = new ArrayList<>();

    private WordDAOImpl wordDAO = new WordDAOImpl();
    private PlayerDAOImpl playerDAO = new PlayerDAOImpl();
    private StatisticDAOImpl statisticDAO = new StatisticDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    //EVENT HANDLING
    public void createNewPlayer(ActionEvent actionEvent) {
        //setting up dialog
        TextInputDialog dialog = new TextInputDialog("nick");
        dialog.setTitle("New Player");
        dialog.setHeaderText("New Player Creation");
        dialog.setContentText("Please enter your name:");
        Optional<String> result = dialog.showAndWait();

        //Retriving info from dialog
        //TODO add validation to duplicates
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

    public void addCategory(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Category");
        dialog.setHeaderText("New Category Creation");
        dialog.setContentText("Please enter new Category name:");
        Optional<String> result = dialog.showAndWait();

        //TODO add validation to duplicates
        if (result.isPresent()) {
            result.ifPresent(name -> categoryDAO.addCategory(new Category(name.toUpperCase())));
        }
    }

    public void addWord(ActionEvent actionEvent) {

        List<Category> categories = categoryDAO.getAllCategories();
        List<String> names = new ArrayList<>();
        for (Category x : categories)
            names.add(x.getName());
        ObservableList<String> choices = FXCollections.observableArrayList(names);

        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("New Word");
        dialog.setHeaderText("New Word Creation");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField newWord = new TextField();
        newWord.setPromptText("word");
        ComboBox<String> categoriesCB = new ComboBox<>(choices);

        grid.add(new Label("new Word:"), 0, 0);
        grid.add(newWord, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoriesCB, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        newWord.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> newWord.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                //TODO
                Category tempCategory = categoryDAO.getCategoryByName(categoriesCB.getSelectionModel().getSelectedItem());
                return new Pair<String, Integer>(newWord.getText(), tempCategory.getCategoryId());
            }
            return null;
        });
        Optional<Pair<String, Integer>> result = dialog.showAndWait();

        //TODO add validation to duplicates
        result.ifPresent(pair -> wordDAO.addWord(new Word(pair.getKey().toUpperCase(), pair.getValue())));
    }

    public void beginGame(ActionEvent actionEvent) {
        if (presentPlayer != null) {
            repeatGameButton.setVisible(false);
            repeatGameButton.setText("One more game?");
            noOfMisses = 0;
            imageView.setImage(new Image("./img/" + noOfMisses + ".png"));

            acquirePassword();

            winLabel.setText("");
            categoryLabel.setText("Category: " + currentCategory);
            passwordLabel.setText(currentPassword);

            mainPane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (gameInProgress) {
                        char sign = Character.toUpperCase(event.getText().charAt(0));
                        missCounter.setText(String.valueOf((noOfMisses)));
                        if (isCharacterPresentInPassword(sign) && !(isLetterAlreadyUsed(sign)))
                            revealLetter(sign);
                        else
                            missLetter();

                        if (isPasswordRevealed(currentPassword)) {
                            repeatGameButton.setVisible(true);
                            gameInProgress = false;
                            winLabel.setText("YOU WON!");
                        } else if (isMaxNumberOfMissesReached()) {
                            repeatGameButton.setVisible(true);
                            gameInProgress = false;
                            winLabel.setText("YOU LOST!");
                        }
                    }
                }
            });
            gameInProgress = true;
        } else {
            winLabel.setText("Choose player first!");
        }
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    //SUPPORT METHODS
    private void missLetter() {
        noOfMisses++;
        if (isMaxNumberOfMissesReached())
            winLabel.setText("YOU LOST");
        else
            imageView.setImage(new Image("./img/" + noOfMisses + ".png"));
    }

    private void revealLetter(char letter) {
        //getCharPositions
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < revealedPassword.length() - 1; i++)
            if (revealedPassword.charAt(i) == letter)
                positions.add(i);

        StringBuilder sb = new StringBuilder(currentPassword);

        //revealLetter
        for (Integer x : positions)
            sb.setCharAt(x, revealedPassword.charAt(x));
        currentPassword = sb.toString();
        passwordLabel.setText(currentPassword);
    }

    private void acquirePassword() {
        int minWordId = wordDAO.getMinId(), maxWordId = wordDAO.getMaxId();
        int randomId = new Random().nextInt(maxWordId + 1 - minWordId) + minWordId;

        Word word = wordDAO.getWord(randomId);
        Category category = categoryDAO.getCategory(word.getCategoryId());

        currentPassword = preparePassword(word.getContent());
        currentCategory = category.getName();
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

    //FLAGS
    private boolean isPasswordRevealed(String hiddenPassword) {
        return revealedPassword.equals(hiddenPassword) ? true : false;
    }

    private boolean isCharacterPresentInPassword(char sign) {
        return revealedPassword.indexOf(sign) == -1 ? false : true;
    }

    private boolean isMaxNumberOfMissesReached() {
        return noOfMisses == 11 ? true : false;
    }

    private boolean isLetterAlreadyUsed(char sign) {
        return usedLetters.contains(sign);
    }

}

