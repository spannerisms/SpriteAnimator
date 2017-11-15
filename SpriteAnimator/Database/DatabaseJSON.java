package SpriteAnimator.Database;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class DatabaseJSON {
	public static final JSONObject ALL_DATA;

	private static final String DATA_PATH =
			"AnimationData.json";
	static {
		StringBuilder ret = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							DatabaseJSON.class.getResourceAsStream(DATA_PATH),
							"UTF-8")
					);
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