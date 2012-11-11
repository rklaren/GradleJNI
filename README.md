GradleJNI
=========

Test project to investigate building a java project with native JNI code.

Compiler settings are MinGW windows centric.

Requirements:
- 32 bit java JDK (Java 7)
- somewhat recent version of mingw on the path

1. Clone
2. Set JAVA_HOME to point at a 32 bit JDK (Java 7)
3. Add the mingw bin directory to your path (set PATH=%PATH%;C:\MinGW\bin)
4. gradlew build
5. %JAVA_HOME%\bin\java -jar build\libs\GradleJNI-1.0.jar

Note: a trick is used to store the DLL inside the jar. Before the native library is loaded
it is extracted from the jar to a temporary location then loaded from there. With OSGi you'd
get that for free.

Note2: This is not intended to be nice java/C++ code. Just enough to investigate the in's and out's
of building this with gradle.

TODO: change to multi project build:

:java 
   builds jar & C++ artifact with JNIFoo.h generated with javah
:native
   builds DLL/so for an architecture, has dependency on artifact with JNIFoo.h
   creates native artifact for the DLL/so
:release-jar
   grabs available DLL/so's and jar and bundles it to one release-jar that
   can be deployed and run on multiple architectures

On a local machine this should depend on each other in the right way and build 
just for the native architecture. 

In CI one would
- trigger the java build & block native builds
- run native builds in parallel
- after the above run release-jar gathering all bits

TODO investigate cross compiling with mingw32/mingw64 compiler on linux
