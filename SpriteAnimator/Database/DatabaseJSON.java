package SpriteAnimator.Database;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONObject;

import SpriteAnimator.OldDatabase;

public class DatabaseJSON {
	public static final JSONObject ALL_DATA = new JSONObject(getDataString());

	private static final String DATA_PATH = "AnimationData.json";
	private static final String getDataString() {
		StringBuilder ret = new StringBuilder();
		String path = OldDatabase.class.getResource(DATA_PATH).getPath();
		FileReader fr;
		try {
			fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				ret.append(line);
			}
			br.close();
		} catch (Exception e) {
			return "";
		}

		return ret.toString();
	}
}