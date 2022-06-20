package com.greazi.plugin.util;

import org.json.JSONObject;

import java.io.IOException;

public class Human {

	public static JSONObject getHumanObject() {
		final JSONObject json;

		final String url = "https://randomuser.me/api/";

		try {
			json = JsonReader.readJsonFromUrl(url);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		return json;
	}

	public static String getName(final JSONObject human) {
		final String firstName = human.getJSONArray("results").getJSONObject(0).getJSONObject("name").getString("first");
		final String lastName = human.getJSONArray("results").getJSONObject(0).getJSONObject("name").getString("last");
		return firstName + " " + lastName;
	}

	public static String getAge(final JSONObject human) {
		return String.valueOf(human.getJSONArray("results").getJSONObject(0).getJSONObject("dob").getInt("age")) + " years old";
	}

	public static String getNationality(final JSONObject human) {
		return human.getJSONArray("results").getJSONObject(0).getString("nat");
	}

	public static String getTimeZone(final JSONObject human) {
		final JSONObject base = human.getJSONArray("results").getJSONObject(0).getJSONObject("location").getJSONObject("timezone");
		return base.getString("offset") + " " + base.getString("description");
	}

	public static String getCity(final JSONObject human) {
		return human.getJSONArray("results").getJSONObject(0).getJSONObject("location").getString("city");
	}

	public static String getState(final JSONObject human) {
		return human.getJSONArray("results").getJSONObject(0).getJSONObject("location").getString("state");
	}

	public static String getCountry(final JSONObject human) {
		return human.getJSONArray("results").getJSONObject(0).getJSONObject("location").getString("country");
	}

	public static String getPostalCode(final JSONObject human) {
		return human.getJSONArray("results").getJSONObject(0).getJSONObject("location").getString("postcode");
	}
}
