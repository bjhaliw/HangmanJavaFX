package view;

import javafx.application.*;
import javafx.stage.Stage;
import model.Hangman;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.scene.text.*;

@SuppressWarnings("restriction")
public class GUI extends Application {

	private Hangman hangman;

	public GUI() {
		this.hangman = new Hangman();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		VBox mainMenu = new VBox(10);
		mainMenu.setAlignment(Pos.CENTER);
		mainMenu.setPadding(new Insets(10, 10, 10, 10));

		Button onePlayer = new Button("One Player");
		onePlayer.setOnAction(e -> {
			pane.setCenter(createHangman(this.hangman.pickRandomWord().toLowerCase()));
			primaryStage.sizeToScene();
			primaryStage.centerOnScreen();
		});

		Button twoPlayers = new Button("Two Players");
		twoPlayers.setOnAction(e -> {
			pane.setCenter(enterWordScreen(pane, primaryStage));
			primaryStage.sizeToScene();
			primaryStage.centerOnScreen();
		});

		mainMenu.getChildren().addAll(onePlayer, twoPlayers);

		pane.setTop(createMenuBar(pane, mainMenu));
		pane.setCenter(mainMenu);

		primaryStage.setScene(new Scene(pane));
		primaryStage.setTitle("Hangman");
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> System.exit(0));
	}

	private VBox enterWordScreen(BorderPane pane, Stage primaryStage) {
		VBox box = new VBox(10);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10, 10, 10, 10));

		Label label = new Label("Enter your word(s)");
		label.setFont(Font.font(20));
		TextField entry = new TextField();
		entry.setFont(Font.font(20));
		entry.setAlignment(Pos.CENTER);

		entry.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				if (!entry.getText().equals("")) {
					pane.setCenter(createHangman(entry.getText().toLowerCase()));
					primaryStage.sizeToScene();
					primaryStage.centerOnScreen();
				}
			}
		});

		box.getChildren().addAll(label, entry);
		return box;
	}

	private MenuBar createMenuBar(BorderPane pane, VBox mainMenu) {
		MenuBar bar = new MenuBar();

		Menu file = new Menu("File");

		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(e -> pane.setCenter(mainMenu));

		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> System.exit(0));

		file.getItems().addAll(newGame, exit);
		bar.getMenus().addAll(file);

		return bar;
	}

	private VBox createHangman(String word) {
		VBox box = new VBox(10);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10, 10, 10, 10));

		ImageView view = new ImageView(new Image("/start.png"));

		Label hiddenWord = new Label();
		hiddenWord.setFont(Font.font(20));
		hiddenWord.setText(this.hangman.createHiddenWord(word));

		Label usedLetters = new Label();
		usedLetters.setFont(Font.font(20));

		Label wordLength = new Label("Word has " + word.length() + " letters");
		wordLength.setFont(Font.font(20));

		TextField entry = new TextField();
		entry.setFont(Font.font(20));
		entry.setAlignment(Pos.CENTER);

		Text strikes = new Text("0");

		entry.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				evaluateEntry(entry, word, hiddenWord, usedLetters, strikes, view);
				entry.setText("");
			}
		});

		box.getChildren().addAll(view, wordLength, usedLetters, hiddenWord, entry);
		return box;
	}

	private void evaluateEntry(TextField entry, String word, Label hiddenWord, Label usedLetters, Text strikes,
			ImageView view) {

		System.out.println("Entry: " + entry.getText());

		if (!this.hangman.evaluateEntry(entry.getText(), word, hiddenWord.getText(), usedLetters.getText())) {
			return;
		}

		if (this.hangman.checkIfStrike(word, entry.getText())) {
			int strikeNum = hangman.getNumStrikes();

			switch (strikeNum) {
			case 1:
				view.setImage(new Image("/first.png"));
				break;
			case 2:
				view.setImage(new Image("/second.png"));
				break;
			case 3:
				view.setImage(new Image("/third.png"));
				break;
			case 4:
				view.setImage(new Image("/fourth.png"));
				break;
			case 5:
				view.setImage(new Image("/fifth.png"));
				break;
			case 6:
				view.setImage(new Image("/sixth.png"));
				break;
			case 7:
				view.setImage(new Image("/end.png"));
				entry.setEditable(false);
				hiddenWord.setText(word);
				break;
			default:
				System.out.println("In the default case for some reason");
				break;
			}

		} else {
			String newWord = this.hangman.revealHiddenLetters(word, hiddenWord.getText(), entry.getText());

			hiddenWord.setText(newWord);

			if (this.hangman.checkIfWin(hiddenWord.getText(), word)) {
				System.out.println("Player Wins!");
				view.setImage(new Image("/win.png"));
				entry.setEditable(false);
			}
		}

		usedLetters.setText(usedLetters.getText() + " " + entry.getText());
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
