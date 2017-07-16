#include "symbols.fst"

% Read the lexicon and delete the inflection class on the analysis layer

$lex$ = ($Letter$* <>:[#infl#]) || "morph.lex"

%#include "derivation.fst"
%#include "compounding.fst"

% Concatenate stems with the inflectional endings

$morph$ = $lex$ "<inflection.a>"

% Eliminate incorrect combinations with a filter transducer
$=C$ = [#infl#]:<>
$inflection-filter$ = $Letter$+ $=C$ $=C$ $sym$*

$morph$ = $morph$ || $inflection-filter$

%ALPHABET = $Letter$* [#infl##origin#]:<>
%$withoutPOS$ = "morph.lex" || .*
$withoutPOS$ = ($Letter$* <>:[#infl##origin#]) || "morph.lex"
$example$ =  $withoutPOS$

%$example$ "<inflection.a>" || $inflection-filter$

($morph$ || "<phon.a>") | "irregular.lex"

%$lex$
