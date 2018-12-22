package digitreco.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import digitreco.neural.Neural;
import digitreco.utils.CharacterCanvas;
import digitreco.utils.ImageUtils;
import neurolib.utils.MathHelper;

/**
 * @author Fabian
 */

@SuppressWarnings("serial")
public class Frame extends JPanel implements CharacterCanvas.DrawListener, ActionListener {

	public static ArrayList<Double> result = new ArrayList<>();

	JFrame frame;
	CharacterCanvas characterCanvas;

	public Frame() {
		frame = new JFrame("handwritten digit recognition - by Fabian");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 515);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(this);
		setLayout(null);
		init();
	}

	private void init() {

		characterCanvas = new CharacterCanvas();
		characterCanvas.setVisible(true);
		characterCanvas.setSize(new Dimension(400, 400));
		characterCanvas.setLocation(35, 35);
		characterCanvas.setDrawListener(this);
		add(characterCanvas);

		JButton clear = new JButton("Clear");
		clear.setVisible(true);
		clear.setSize(new Dimension(100, 40));
		clear.setLocation(characterCanvas.getX() + characterCanvas.getWidth() / 2 - clear.getWidth() / 2,
				characterCanvas.getY() + characterCanvas.getHeight() + 5);
		clear.addActionListener(this);
		add(clear);

		for (int i = 0; i < 10; i++) {
			result.add(0.0);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!Frame.result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				g.setFont(new Font("Courier", Font.BOLD, 18));
				Color c = new Color((int) ((1 - result.get(i)) * 255), (int) (result.get(i) * 255), 0);
				g.setColor(c);
				g.drawString(i + ": " + ((int) (result.get(i) * 1000) / (double) 10) + "%", 485, 40 * i + 60);
			}
		}
	}

	@Override
	public void onDrawEnd() {
		ArrayList<Double> input = new ArrayList<>();

		BufferedImage image = characterCanvas.getImg();

		image = ImageUtils.normalizedImg(image);

		int w = image.getWidth();
		int h = image.getHeight();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixelValue = new Color(image.getRGB(x, y)).getRed();
				input.add(MathHelper.round(pixelValue / 255.0D, 2));
			}
		}

		ArrayList<Double> result = Neural.run(input);

		if (result != null) {
			Frame.result.clear();
			Frame.result.addAll(result);

			int i = 0;
			for (Double res : Frame.result) {
				System.out.println(i + ": " + res);
				i++;
			}
			System.out.println();
		}
		repaint();
	}

	@Override
	public void onDrawStart() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		characterCanvas.clear();
	}
}
