package com.company;

/**
 * author:  RediOne1
 * date:    08.03.2016
 */
public class Progress {

	private static int max = 100;
	private static int progress = 0;
	private static int progressBarLength = 50;
	private static int progressInPercent;

	public static void setMax(int max) {
		Progress.max = max;
	}

	public static void setProgress(int progress) {
		Progress.progress = progress;
	}

	public static void setProgressBarLength(int progressBarLength) {
		Progress.progressBarLength = progressBarLength;
	}

	public static void writeProgress() {
		progressInPercent = (int) ((float) progress / (float) max * 100);

		System.out.print("\r[");

		writeProgressBar();

		System.out.print("] ");
		System.out.print(progressInPercent + "% ");
		System.out.print("progress: " + progress);
		System.out.print(" max: " + max);
	}

	private static void writeProgressBar() {
		int progressBarPercent = (int) ((float) progressInPercent / 100f * (float) progressBarLength);
		for (int i = 0; i < progressBarLength; i++) {
			if (i < progressBarPercent && progressBarPercent != 100)
				System.out.print("=");
			else if (i == progressBarPercent) {
				System.out.print(">");
			} else
				System.out.print(" ");
		}
	}
}
