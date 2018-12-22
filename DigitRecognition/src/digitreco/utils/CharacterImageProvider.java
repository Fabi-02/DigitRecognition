package digitreco.utils;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author flood2d
 */

public class CharacterImageProvider implements Closeable {
    private DataInputStream labelDis;
    private DataInputStream imageDis;
    private File imageFile;
    private File labelFile;
    private int numberOfImages;
    private int rows;
    private int cols;
    private int i = 0;

    public CharacterImageProvider(File imageFile, File labelFile) throws IOException {
       this.imageFile = imageFile;
       this.labelFile = labelFile;
       reset();
    }

    private DataInputStream createDataInputStream(File file) throws FileNotFoundException {
        return new DataInputStream(new FileInputStream(file));
    }

    public boolean hasNext() {
        return i < numberOfImages;
    }

    public CharacterImage next() throws IOException {
        if(!hasNext()) {
            return null;
        }

        i++;
        byte label = labelDis.readByte();
        int[][] pixels = new int[rows][cols];
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                byte b = imageDis.readByte();
                pixels[r][c] = b & 0xff;
            }
        }
        return new CharacterImage(String.valueOf(label).charAt(0), pixels);
    }

    public void reset() throws IOException {
        if(labelDis != null) {
            try {
                labelDis.close();
            } catch(Exception e) {}
        }
        if(imageDis != null) {
            try {
                imageDis.close();
            } catch(Exception e) {}
        }
        labelDis = createDataInputStream(labelFile);
        imageDis = createDataInputStream(imageFile);

        labelDis.readInt();
        imageDis.readInt();

        numberOfImages = labelDis.readInt();
        imageDis.readInt();

        rows = imageDis.readInt();
        cols = imageDis.readInt();
        i = 0;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    @Override
    public void close() throws IOException {
        imageDis.close();
        labelDis.close();
    }
}
