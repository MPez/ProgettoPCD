# Makefile che consente di eseguire il progetto: Sistema Trasporti Artici
# Realizzato da Pezzutti Marco 1008804

JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
	ditta/DittaStarter.java \
	base/BaseStarter.java \
	autotreno/AutotrenoStarter.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) ditta/*.class
	$(RM) base/*.class
	$(RM) autotreno/*.class

start:
	rmiregistry &
	sleep 2
	java ditta.DittaStarter &
	sleep 2
	java base.BaseStarter Nord &
	java base.BaseStarter Sud &
	java base.BaseStarter Ovest &
	java base.BaseStarter Est &
	java autotreno.AutotrenoStarter Blue Nord &
	java autotreno.AutotrenoStarter Green Sud &
	java autotreno.AutotrenoStarter Red Ovest &
	java autotreno.AutotrenoStarter White Est