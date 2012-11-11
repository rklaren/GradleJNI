package eu.knightswhosay.demo.gradlejni;

/* originally from: https://blogs.oracle.com/moonocean/entry/a_simple_example_of_jni */
public class JNIFoo {
	public native String nativeFoo();

	public static final boolean NATIVE_INITIALIZED = EmbeddedLoader.load("foo");

	public void print() {
		String str = nativeFoo();
		System.out.println(str);
	}

	public static void main(String[] args) {
		try {
			if (!NATIVE_INITIALIZED) {
				System.err.println("Failed to initialize native library exiting.");
				return;
			}

			(new JNIFoo()).print();
		} catch (Error e) {
			System.err.println("Error " + e.getMessage());
			e.printStackTrace();
		}
		return;
	}
}
