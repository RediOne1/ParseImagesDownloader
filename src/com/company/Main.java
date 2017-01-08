package com.company;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.URL;
import java.util.List;

public class Main {

	private static OkHttpClient client = new OkHttpClient();

	public static void main(String[] args) {
		InsertImage.insertImagesFromDir(new File("images2"));
		//fetchImages();
	}

	private static void fetchImages(){
		String json = null;
		try {
			HttpUrl url = getUrl();
			System.out.println(url.toString());
			json = run(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Results results = deserializeJSON(json);
		saveImages(results.results);
	}

	private static void saveImages(List<Waypoint> waypointList) {
		Progress.setMax(waypointList.size());
		for (int i = 0; i < waypointList.size(); i++) {
			Progress.setProgress(i + 1);
			Progress.writeProgress();
			Waypoint waypoint = waypointList.get(i);
			try {
				if (waypoint.imageUrl != null || waypoint.image != null) {
					String imageUrl = waypoint.imageUrl != null ? waypoint.imageUrl : waypoint.image.url;
					String imageUrlTab[] = imageUrl.split("\\.");
					if(imageUrlTab.length == 0)
						continue;
					String imageFormat = imageUrlTab[imageUrlTab.length - 1];
					String fileName = "images2" + File.separator + waypoint.objectId + "." + imageFormat;
					System.out.print("  "+fileName);
					File file = new File(fileName);
					file.getParentFile().mkdirs();
					file.createNewFile();
					saveImage(imageUrl, file);
				}
			} catch (Exception ignored) {
			}
		}
	}

	private static HttpUrl getUrl() {

		return new HttpUrl.Builder()
				.scheme("http")
				.host("vps302004.ovh.net")
				.port(1337)
				.addPathSegment("parse")
				.addPathSegment("footsteps")
				.addPathSegment("classes")
				.addPathSegment("Waypoint")
				//.addQueryParameter("skip", "1000")
				.addQueryParameter("limit", "1000")
				.build();
	}

	private static HttpUrl getFilesUrl() {
		return new HttpUrl.Builder()
				.scheme("http")
				.host("vps302004.ovh.net")
				.port(1337)
				.addPathSegment("parse")
				.addPathSegment("footsteps")
				.addPathSegment("classes")
				.addPathSegment("Waypoint")
				.addQueryParameter("limit", "1000")
				.addQueryParameter("where", "{\"image\":{\"$exists\":true}}")
				.build();
	}

	private static String run(HttpUrl url) throws IOException {
		Request request = new Request.Builder()
				.addHeader("X-Parse-Application-Id", "M1FV3fnghimM90vQptlb76H9K7woxlyya3HEgs6O")
				.addHeader("Content-Type", "application/json")
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	private static Results deserializeJSON(String json) {
		Gson gson = new Gson();
		Results waypointResults;
		waypointResults = gson.fromJson(json, Results.class);
		return waypointResults;
	}

	public static void saveImage(String imageUrl, File destinationFile) throws Exception {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile, false);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
