package SpriteAnimator.Database;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONObject;

public class DatabaseJSON {
	public static final JSONObject ALL_DATA;

	private static final String DATA_PATH =
			"C:\\Users\\CR\\eclipse-workspace\\ALttPNG\\src\\SpriteAnimator\\Database\\AnimationData.json";
	static {
		StringBuilder ret = new StringBuilder();
		FileReader fr;
		try {
			fr = new FileReader(DATA_PATH);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				ret.append(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		ALL_DATA = new JSONObject(ret.toString());
	}
}