package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;

public class FileChooser {

    public static File loadImageFile(ImageView imageView){

        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        fileChooser.setTitle("Load image file");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File choosedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if(choosedFile != null){
            try {
                Image image = new Image(choosedFile.toURI().toString());
                imageView.setImage(image);

                imageView.setPreserveRatio(true);
                imageView.setImage(new Image(new FileInputStream(choosedFile)));
            }catch(Exception ignored){
            }
        }
        return choosedFile;
    }
}

