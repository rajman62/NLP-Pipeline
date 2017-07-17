%**************************************************************************
%  Content:  deletes tags on the surface string  
%**************************************************************************

% definition of the symbol classes
#include "symbols.fst"


% delete unwanted symbols on the "surface"
% and map the feature <Stems> to the more specific features
% <BaseStem> <DerivStem> and <CompStem>

ALPHABET = [#Letter# #WordClass# #StemType# #Origin# #Complex# #InflClass#] \
	<classic>:[#classic#]

[#Affix#] .* |\
<NoDef>? (<Stem>:<BaseStem>  .* <base>  |\
	  <Stem>:<DerivStem> .* <deriv> |\
	  <Stem>:<CompStem>  .* <comp>) .*
