package digitreco.neural;

import java.io.IOException;
import java.util.ArrayList;

import neurolib.neural.Network;

/**
 * @author Fabian
 */

public class Neural {
	public static ArrayList<Double> run(ArrayList<Double> test) {
		Network testNet = null;
		try {
			testNet = Network.load("network", "digitreco");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (testNet == null) {
			return null;
		}

		ArrayList<Double> results = testNet.run(test);

		return results;
	}

	public static ArrayList<Double> ArrayToArrayList(double[] array) {
		ArrayList<Double> out = new ArrayList<>();
		for (double d : array) {
			out.add(d);
		}
		return out;
	}
}
