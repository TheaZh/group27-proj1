JFLAGS = -cp
JC = javac
CLASSPATH = libs/google-api-client-1.22.0.jar:libs/google-api-client-jackson2-1.22.0.jar:libs/google-api-services-customsearch-v1-rev56-1.22.0.jar:libs/google-http-client-1.22.0.jar:libs/google-http-client-jackson2-1.22.0.jar:libs/httpclient-4.0.1.jar:libs/jackson-core-2.1.3.jar:

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(CLASSPATH) $*.java

CLASSES = \
	GoogleSearch.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
