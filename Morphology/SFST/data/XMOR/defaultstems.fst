%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%  
%  Content:  generation of derivational and compounding stems
%            from base stems by means of default rules
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% Default derivation and compounding stems are not generated for
% lexicon entries which start with <NoDef>


#include "symbols.fst"

% Rule which turns a adjectival base stem into a compounding stems
% The inflection feature is deleted; the morpheme itself is unchanged.

$DefCompAdj$ = $LEX$ ||\
 <BaseStem>:<CompStem> .* <ADJ><base>:<comp> [#Origin#] [#InflClass#]:<>


$DefDerivVerb$ = $LEX$ ||\
 <BaseStem>:<DerivStem> .* <V><base>:<deriv> [#Origin#] [#InflClass#]:<>



% Add the new stems to the set of stems
$BDKStem$ = $BDKStem$ | $DefCompAdj$ | $DefDerivVerb$
