package Minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Minesweeper extends Application {

	private static final int size = 40;
	private static final int w = 800;
	private static final int h = 600;
	private static final int xTiles = w / size;
	private static final int yTiles = h / size;
	private Tile[][] grid = new Tile[xTiles][yTiles];
	public Scene scene;

	private Parent createContent() {
		Pane root = new Pane();		//Erstellt ein neues Panes
		root.setPrefSize(w, h);		//Setzt die grösse des Panes
		for (int y = 0; y < yTiles; y++) {
			for (int x = 0; x < xTiles; x++) {		//Verschachtelter for Loop
				Random test = new Random();			//Generiert eine neue Random Zahl
				int ran = test.nextInt(8) + 1;		//Übernimmt die Zufallszahl
			Tile tile = new Tile(x, y, ran > 7);	//Bestimmt das jedes 8. Tile eine Bombe hat
				grid[x][y] = tile;					
				root.getChildren().add(tile);		//Bestimmt in der Gruppe Root das Tile ein Parent ist
			}
		}

		for (int y = 0; y < yTiles; y++) {
			for (int x = 0; x < xTiles; x++) {
				Tile tile = grid[x][y];
				long bombs = getNeighbors(tile).stream().filter(t -> t.boom).count();	//Setzt die Anzahl Bomben in der Umgebung fest
				tile.text.setText(String.valueOf(bombs));		//Schreibt die Zahl in das Tile
			}
		}
		return root;
	}

	private List<Tile> getNeighbors(Tile tile) {	//Bestimmt die Nachbaren
		List<Tile> neighbors = new ArrayList<>();	//Erstellt eine Liste
		int[] points = new int[] { -1, -1, -1, 0, -1, -1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1 };
		for (int i = 0; i < points.length; i++) {
			int dx = points[i];
			int dy = points[++i];
			int newX = tile.x + dx;
			int newY = tile.y + dy;
			if (newX >= 0 && newX < xTiles && newY >= 0 && newY < yTiles) {
				neighbors.add(grid[newX][newY]);
			}
		}
		return neighbors;
	}

	private class Tile extends StackPane {
		private int x, y;
		private boolean boom;
		private boolean open = false;
		private Rectangle field = new Rectangle(size - 2, size - 2);
		private Text text = new Text();

		public Tile(int x, int y, boolean boom) {
			this.x = x;
			this.y = y;
			this.boom = boom;
			field.setFill(Color.GRAY);
			field.setStroke(Color.WHITE);
			text.setFont(Font.font(18));
			text.setVisible(false);
			text.setText(boom ? "X" : "");
			getChildren().addAll(field, text);
			setTranslateX(x * size);
			setTranslateY(y * size);
			setOnMouseClicked(e -> open());
		}

		public void open() {	//Die Funktion die das Tile aufdeckt
			if (open)
				return;
			if (boom) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null, "Game Over", "FAIL", JOptionPane.ERROR_MESSAGE);
				scene.setRoot(createContent());
				return;
			}
			open = true;
			text.setVisible(true);
			field.setFill(null);
			if(text.getText().equals("0")){
				text.setText("");
			}
			if (text.getText().isEmpty()) {
				getNeighbors(this).forEach(Tile::open);
			}
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		scene = new Scene(createContent());
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		
		launch(args);

	}
}