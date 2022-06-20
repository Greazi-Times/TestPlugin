package com.greazi.plugin.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonReader {

	/**
	 * An util that allows you to read JSON Objects
	 *
	 * @param rd The reader
	 * @return String
	 * @throws IOException Possible IOException
	 */
	private static @NotNull String readAll(@NotNull final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * An util that allows you to read JSON Objects
	 *
	 * @param url The URL
	 * @return JSONObject
	 * @throws IOException   Possible IOException
	 * @throws JSONException Possible JSONException
	 */
	public static @NotNull JSONObject readJsonFromUrl(final String url) throws IOException, JSONException {
		final InputStream is = new URL(url).openStream();
		try {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			final String jsonText = readAll(rd);
			final JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
}
