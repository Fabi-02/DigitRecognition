package digitreco.utils;

import java.util.ArrayList;

import neurolib.utils.MathHelper;

/**
 * @author flood2d
 */

public class CharacterImage {
    public final char label;
    public final int[][] pixels;

    public CharacterImage(char label, int[][] pixels) {
        this.label = label;
        this.pixels = pixels;
    }

    public ArrayList<Double> toArrayList() {
    	ArrayList<Double> out = new ArrayList<>();
    	int width = pixels.length;
		int height = pixels[0].length;
    	for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out.add(MathHelper.round(pixels[y][x] / (double) 255, 2));
			}
		}
    	return out;
    }
}
