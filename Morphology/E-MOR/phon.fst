#include "symbols.fst"

ALPHABET = [#sym#] e:<>
$delete-e$ = e <=> <> (<V> [aei])

ALPHABET = [#sym#] y:i
$y-to-i$ = y <=> i ([<V><A>] [el])

ALPHABET = [#sym#] 
$y-to-ie$ = {y}:{ie} ^-> (__ [<V><N><A><Adv>] s)

ALPHABET = [#sym#]
$p-to-pp$ = {p}:{pp} ^-> (__ [<V><N><A><Adv>] [ing|ed]) % EXCEPTION JUST ADDED

ALPHABET = [#sym#]
$g-to-gg$ = {g}:{gg} ^-> (__ [<V><N><A><Adv>] [ing|ed]) % EXCEPTION JUST ADDED

% ADDITION: THE FOLLOWING TWO RULES ENABLE ACCEPTANCE OF WORDS IN THEIR UPPER/LOWER CASE
% VERSIONS FOR ALL WORDS EXCEPT PROPER NOUNS. SO WE DON'T HAVE TO INCLUDE BOTH 
% UPPERCASE AND LOWER CASE IN THE LEXICON WHICH WILL MAKE THE LEXICON UNNECESSARY BIG      
        
ALPHABET = [#Letter# <PropN>]
$normal$ = f:F ^-> (__[<PropN>]) % EXCEPTION JUST ADDED

ALPHABET = [#Letter# <N><V><A><Adv><DT>\
        <``><ADD><-LRB-><WRB><LS><PRP><DT><AFX><UH><MD> \
        <WP><NFP><CC><''><CD><PDT><$><IN><WDT><SYM><WP$><HYPH>\
        <,><.><GW><PRP$><EX><POS><-RRB-><:><TO><RP><FW>] a:A b:B c:C d:D e:E f:F g:G h:H \
        i:I j:J k:K l:L m:M n:N o:O p:P q:Q r:R s:S t:T u:U v:V w:W x:X y:Y z:Z

$upper-to-lower$ = (a<=>A b<=>B c<=>C d<=>D e<=>E f<=>F g<=>G h<=>H i<=>I \
  j<=>J k<=>K l<=>L m<=>M n<=>N o<=>O p<=>P q<=>Q r<=>R s<=>S t<=>T \
  u<=>U v<=>V w<=>W x<=>X z<=>Z y<=>Y) ^-> (__[<N><V><A><Adv><DT>\
        <``><ADD><-LRB-><WRB><LS><PRP><DT><AFX><UH><MD> \
        <WP><NFP><CC><''><CD><PDT><$><IN><WDT><SYM><WP$><HYPH>\
        <,><.><GW><PRP$><EX><POS><-RRB-><:><TO><RP><FW>]) 

#=D# = bdglmnpt
$T$ = {[#=D#]}:{[#=D#][#=D#]}

ALPHABET = [#sym#]
$duplicate$ = $T$ ^-> ([#cons#][#vowel#] __ (<A> e(r|st) | <V><dup>))

ALPHABET = [#Letter#] [#mcs#]:<>
$delete-POS$ = .*

$delete-e$ || $y-to-i$ || $y-to-ie$ || $p-to-pp$ || $g-to-gg$|| $duplicate$ \ 
|| ($upper-to-lower$|$normal$) || $delete-POS$