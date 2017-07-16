
SOURCES = morph.fst symbols.fst inflection.fst phon.fst

.PHONY: targets

targets: morph.a

morph.a: symbols.fst inflection.a phon.a morph.lex irregular.lex

%.a: %.fst
	fst-compiler $< $@

%.ca: %.a
	fst-compact $< $@

archive:
	gtar -zcf VERSION-`date '+%y%m%d'`.tar.gz Makefile lexicon *.fst

clean:
	-rm *.a *~ Makefile.bak 2>&- > /dev/null

Makefile: *.fst
	-makedepend -Y -o.a $(SOURCES) 2>/dev/null


# DO NOT DELETE

morph.a: symbols.fst
phon.a: symbols.fst
