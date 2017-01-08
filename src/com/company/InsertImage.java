package com.company;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.istack.internal.NotNull;
import okhttp3.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * author:  RediOne1
 * date:    08.03.2016
 */
public class InsertImage {
	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");
	private static OkHttpClient client = new OkHttpClient();


	public static void insertImagesFromDir(@NotNull File file) {
		File[] files = file.listFiles();
		Progress.setMax(files.length);
		for (int i = 0; i < files.length; i++) {
			Progress.setProgress(i);
			Progress.writeProgress();
			System.out.print("   " + files[i].getName());

			insertImage(files[i]);
		}
	}

	private static void insertImage(File file) {
		HttpUrl url = getUrl(FilenameUtils.removeExtension(file.getName()));
		try {
			String json = run(url, file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static RequestBody getPutRequest(String imageUrl) {
		return RequestBody.create(JSON, "{\"optimizedImage\":\"" + imageUrl + "\"}");
	}

	private static HttpUrl getUrl(String fileName) {

		return new HttpUrl.Builder()
				.scheme("http")
				.host("vps302004.ovh.net")
				.port(1337)
				.addPathSegment("parse")
				.addPathSegment("footsteps")
				.addPathSegment("classes")
				.addPathSegment("Waypoint")
				.addPathSegment(fileName)
				.build();
	}

	private static String run(HttpUrl url, String fileName) throws IOException {
		Request request = new Request.Builder()
				.addHeader("X-Parse-Application-Id", "M1FV3fnghimM90vQptlb76H9K7woxlyya3HEgs6O")
				.addHeader("Content-Type", "application/json")
				.url(url)
				.put(getPutRequest("http://vps302004.ovh.net:1337/public/footsteps_images/" + fileName))
				.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}
}
