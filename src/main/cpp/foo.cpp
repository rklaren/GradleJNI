
#include "JNIFoo.h"

JNIEXPORT jstring JNICALL Java_eu_knightswhosay_demo_gradlejni_JNIFoo_nativeFoo(JNIEnv *env, jobject) {
  return env->NewStringUTF("Hello from the native code.");
}
