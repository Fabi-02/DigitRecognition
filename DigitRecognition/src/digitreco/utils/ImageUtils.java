package digitreco.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author flood2d
 */

public class ImageUtils {

	public static BufferedImage normalizedImg(BufferedImage image) {
		BufferedImage normalizedImg = ImageUtils.deepCopy(image);
		normalizedImg = ImageUtils.cropWhiteBorder(normalizedImg);
		normalizedImg = ImageUtils.invertColor(normalizedImg);
		normalizedImg = ImageUtils.fitInto(normalizedImg, 20, 20, Color.BLACK.getRGB());
		normalizedImg = ImageUtils.centerWithMassInto(normalizedImg, 28, 28, Color.BLACK.getRGB());
		return normalizedImg;
	}

	private static BufferedImage deepCopy(BufferedImage image) {
		return new BufferedImage(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);
	}

	private static BufferedImage fitInto(BufferedImage source, int width, int height, int backgroundColor) {
		float sourceAspectRatio = source.getWidth() / source.getHeight();
		float resultAspectRatio = (float) width / height;
		float newSourceWidth, newSourceHeight;

		if (resultAspectRatio > sourceAspectRatio) {
			newSourceHeight = height;
			newSourceWidth = (newSourceHeight / source.getHeight()) * source.getWidth();
		} else {
			newSourceWidth = width;
			newSourceHeight = (newSourceWidth / source.getWidth()) * source.getHeight();
		}

		Image scaledSrc = source.getScaledInstance((int) newSourceWidth, (int) newSourceHeight, Image.SCALE_SMOOTH);

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = result.createGraphics();
		g2.setColor(new Color(backgroundColor));
		g2.fillRect(0, 0, result.getWidth(), result.getHeight());
		g2.drawImage(scaledSrc, (int) (width / 2 - newSourceWidth / 2), (int) (height / 2 - newSourceHeight / 2), null);
		g2.dispose();
		return result;
	}

	private static BufferedImage invertColor(BufferedImage source) {
		for (int x = 0; x < source.getWidth(); x++) {
			for (int y = 0; y < source.getHeight(); y++) {
				Color pixelColor = new Color(source.getRGB(x, y), true);
				pixelColor = new Color(255 - pixelColor.getRed(), 255 - pixelColor.getGreen(),
						255 - pixelColor.getBlue());
				source.setRGB(x, y, pixelColor.getRGB());
			}
		}
		return source;
	}

	private static BufferedImage centerWithMassInto(BufferedImage source, int width, int height, int backgroundColor) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = result.createGraphics();
		g2.setColor(new Color(backgroundColor));
		g2.fillRect(0, 0, result.getWidth(), result.getHeight());
		int[] centerOfMass = computeCenterOfMass(source);
		g2.drawImage(source, width / 2 - centerOfMass[0], height / 2 - centerOfMass[1], null);
		g2.dispose();
		return result;
	}

	private static BufferedImage cropWhiteBorder(BufferedImage source) {
		int upperBound = -1, lowerBound = -1, leftBound = -1, rightBound = -1;

		upper: for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				if (source.getRGB(x, y) != Color.white.getRGB()) {
					upperBound = y;
					break upper;
				}
			}
		}

		lower: for (int y = source.getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < source.getWidth(); x++) {
				if (source.getRGB(x, y) != Color.white.getRGB()) {
					lowerBound = y;
					break lower;
				}
			}
		}

		left: for (int x = 0; x < source.getWidth(); x++) {
			for (int y = 0; y < source.getHeight(); y++) {
				if (source.getRGB(x, y) != Color.white.getRGB()) {
					leftBound = x;
					break left;
				}
			}
		}

		right: for (int x = source.getWidth() - 1; x >= 0; x--) {
			for (int y = 0; y < source.getHeight(); y++) {
				if (source.getRGB(x, y) != Color.white.getRGB()) {
					rightBound = x;
					break right;
				}
			}
		}

		return source.getSubimage(leftBound, upperBound, rightBound - leftBound + 1, lowerBound - upperBound + 1);
	}

	private static int[] computeCenterOfMass(BufferedImage img) {
		long xSum = 0;
		long ySum = 0;
		long num = 0;

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int weight = new Color(img.getRGB(x, y)).getRed();
				xSum += x * weight;
				ySum += y * weight;
				num += weight;
			}
		}
		return new int[] { (int) ((double) xSum / num), (int) ((double) ySum / num) };
	}
}
