package SpriteAnimator.Database;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.json.JSONObject;

public class DatabaseJSON {
	public static final JSONObject ALL_DATA;

	private static final String DATA_PATH = "AnimationData.json";

	static {
		URL url = Resources.getResource(DATA_PATH);

		String text = null;

		try {
			text = Resources.toString(url, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		ALL_DATA = new JSONObject(text);
	}
}