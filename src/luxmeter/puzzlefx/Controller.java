package luxmeter.puzzlefx;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Controller implements Initializable {
    // Drawing Surface (Canvas)
    private GraphicsContext graphicsContext;
    private Canvas canvas;

    @FXML
    private StackPane rootPane;

    // first method to be called by the JavaFX framework
    // needs to set member class variables properly
    // otherwise methods depending on them might throw an NullPointerException
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // need to define an initial size for the canvas otherwise we won't see anything
        canvas = new Canvas(AppConstants.MAX_WINDOW_WIDTH, AppConstants.MAX_WINDOW_HEGHT);

        graphicsContext = canvas.getGraphicsContext2D();

        fillBackgroundWithColor(Color.BLACK);
        Image originalImage = getResizedImage();



        List<Piece> pieces = new ArrayList();
        for (int h = 0; h < AppConstants.NUM_HORIZONTAL_SLICES; h++) {
            int srcX = (int) ((originalImage.getWidth() / AppConstants.NUM_HORIZONTAL_SLICES) * h);
            int width = (int) (originalImage.getWidth() / AppConstants.NUM_HORIZONTAL_SLICES);

            for (int v = 0; v < AppConstants.NUM_VERTICAL_SLICES; v++) {
                int srcY = (int) ((originalImage.getHeight() / AppConstants.NUM_VERTICAL_SLICES) * v);
                int height = (int) (originalImage.getHeight() / AppConstants.NUM_VERTICAL_SLICES);
                pieces.add(new Piece(originalImage, srcX, srcY, width, height));
            }
        }

        for (Piece piece : pieces) {
            graphicsContext.getPixelWriter().setPixels(
                    piece.getSrcX(), piece.getSrcY(),
                    piece.getWidth(), piece.getHeight(),
                    originalImage.getPixelReader(),
                    piece.getSrcX(), piece.getSrcY());
        }

        addCanvasToRootPane(originalImage);
    }

    private Image getResizedImage() {
        boolean preserveRatio = true;
        boolean smooth = true;
        return new Image(getClass().getResourceAsStream(
                AppConstants.IMAGE_LOCATION),
                AppConstants.MAX_WINDOW_WIDTH,
                AppConstants.MAX_WINDOW_HEGHT,
                preserveRatio,
                smooth);
    }

    private void addCanvasToRootPane(Image image) {
        rootPane.setPrefWidth(image.getWidth());
        rootPane.setPrefHeight(image.getHeight());
        rootPane.getChildren().add(canvas);
        StackPane.setAlignment(canvas, Pos.TOP_CENTER);
    }

    private void fillBackgroundWithColor(Color color) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
