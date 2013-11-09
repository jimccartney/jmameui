JFLAGS = -cp .:src/:lib/ -d bin
JC = javac
.SUFFIXES: .java .class
.java.class:
				$(JC) $(JFLAGS) $*.java

CLASSES = $(shell find . -name "*.java")

default: classes

classes: $(CLASSES:.java=.class)