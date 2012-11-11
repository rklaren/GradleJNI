package eu.knightswhosay.demo.gradlejni;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/* originally from: https://blogs.oracle.com/moonocean/entry/a_simple_example_of_jni */
public class JNIFoo {
	public native String nativeFoo();

	public static final boolean NATIVE_INITIALIZED = loadEmbedded("foo");

	public static void closeQuietly(InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
			// don't care
		}
	}

	public static boolean loadEmbedded(String library) {
		try {
			String osName = System.getProperty("os.name");
			if (osName.toLowerCase().contains("windows")) {
				osName = "Windows";
			}
			String osArch = System.getProperty("os.arch");
			String path = String.format("/lib/%s-%s/foo.dll", osName, osArch);

			InputStream src = JNIFoo.class.getResourceAsStream(path);
			if (src == null) {
				throw new RuntimeException(String.format("No native support for %s %s", osName, osArch));
			}

			final Path dstFile = Files.createTempFile("foolib-", ".tmp");
			Files.copy(src, dstFile, StandardCopyOption.REPLACE_EXISTING);
			closeQuietly(src);

			System.load(dstFile.toString());

			// NOTE: not attempting to delete tmp file since it will fail anyway on windows
			return true;
		} catch (Exception e) {
			System.out.println("Error loading library " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public void print() {
		String str = nativeFoo();
		System.out.println(str);
	}

	public static void main(String[] args) {
		try {
			if (!NATIVE_INITIALIZED) {
				System.out.println("Failed to initialize native library exiting.");
				return;
			}

			(new JNIFoo()).print();
		} catch (Error e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
		return;
	}
}
