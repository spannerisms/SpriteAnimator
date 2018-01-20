package animator.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.*;

public class VTGrabber {
	private static final String VT_PATH = "http://vt.alttp.run/sprites";

	public static void run() {
		final URL vtURL;

		URL tempURL = null;
		try {
			 tempURL = new URL(VT_PATH);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		vtURL = tempURL;

		StringBuilder ret = new StringBuilder();
		try (
			BufferedReader br =
				new BufferedReader(
						new InputStreamReader(vtURL.openStream()));
				)
		{
			String line;
			while ((line = br.readLine()) != null) {
				ret.append(line.replaceAll("\\\\/","/")); // unescape
			}
			br.close();
		} catch (Exception e) {
			ret.append("OOPS");
		}

		JSONArray sprJSON = new JSONArray(ret.toString());

		AnimatorGUI.VT_DIRECTORY.delete(); // purge old files
		AnimatorGUI.VT_DIRECTORY.mkdirs();

		for (int i = 0; i < sprJSON.length(); i++) {
			JSONObject sprObj = sprJSON.getJSONObject(i);
			String sprPath = sprObj.getString("file");
			URL sprURL;
			String[] sprPathSplit = sprPath.split("/");
			String sprName = sprPathSplit[sprPathSplit.length-1];

			try {
				sprURL = new URL(sprPath);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				continue;
			}
			String path = AnimatorGUI.VT_DIRECTORY.getAbsolutePath() + "/" + sprName;
			path = path.replace("/", System.getProperty("file.separator"));
			File curSpr = new File(path);

			try(
				InputStream s = sprURL.openStream();
				FileOutputStream output = new FileOutputStream(curSpr);
			) {
				curSpr.createNewFile();
				int r = 0;
				do {
					r = s.read();
					if (r == -1) { break; }
					output.write(r);
				} while (r != -1);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}