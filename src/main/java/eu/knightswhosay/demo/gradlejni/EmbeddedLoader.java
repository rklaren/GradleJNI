package eu.knightswhosay.demo.gradlejni;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class EmbeddedLoader {

	private EmbeddedLoader() {
	}

	public static void closeQuietly(InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
			// don't care
		}
	}

	public static boolean load(String library) {
		try {
			String osName = System.getProperty("os.name");
			String extension = "so";
			if (osName.toLowerCase().contains("windows")) {
				osName = "Windows";
				extension = "dll";
			}
			String osArch = System.getProperty("os.arch");
			String path = String.format("/lib/%s-%s/%s.%s", osName, osArch, library, extension);

			InputStream src = JNIFoo.class.getResourceAsStream(path);
			if (src == null) {
				System.err.println(String.format("Error: No native support for library %s on %s %s", library, osName, osArch));
				return false;
			}

			Path dstFile = Files.createTempFile("foolib-", ".tmp");
			Files.copy(src, dstFile, StandardCopyOption.REPLACE_EXISTING);
			closeQuietly(src);

			System.load(dstFile.toString());

			// NOTE: not attempting to delete tmp file since it will fail anyway on
			// windows (due to the file being locked)
			return true;
		} catch (Exception e) {
			System.err.println(String.format("Error loading library %s: %s", library, e.getMessage()));
			e.printStackTrace();
		}
		return false;
	}
}
