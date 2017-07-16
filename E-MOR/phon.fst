#include "symbols.fst"

ALPHABET = [#sym#] e:<>
$delete-e$ = e <=> <> (<V> [aei])

ALPHABET = [#sym#] y:i
$y-to-i$ = y <=> i ([<V><A>] [el])

ALPHABET = [#sym#]
$y-to-ie$ = {y}:{ie} ^-> (__ [<V><N>] s)

ALPHABET = [#sym#]

$s-to-es$ = {s}:{es} ^-> ([ch|th] <N><PropN> __) % EXCEPTION JUST ADDED

ALPHABET = [#sym#]

$g-to-gg$ = {g}:{gg} ^-> (__ [<V><N><PropN>] [ing|ed]) % EXCEPTION JUST ADDED

ALPHABET = [#sym#]

$r-to-rr$ = {r}:{rr} ^-> (__ [<V><N><A><PropN>] [ing|ed]) % EXCEPTION JUST ADDED

ALPHABET = [#sym#]

$normal$ = f:F ^-> (__[<PropN>]) % EXCEPTION JUST ADDED

ALPHABET = [#Letter# <N><A><Adv><V><DT><``><ADD><-LRB-><WRB><LS><PRP><DT><AFX>\
        <UH><MD><WP><NFP><CC><''><CD><PDT><$><IN><WDT><SYM><WP$><HYPH><,><.><GW>\
        <PRP$><EX><POS><-RRB-><:><TO><RP><FW>] a:A b:B c:C d:D e:E f:F g:G h:H i:I \
        j:J k:K l:L m:M n:N o:O p:P q:Q r:R s:S t:T u:U v:V w:W x:X y:Y z:Z

$upper-to-lower$ = (a<=>A b<=>B c<=>C d<=>D e<=>E f<=>F g<=>G h<=>H i<=>I \
  j<=>J k<=>K l<=>L m<=>M n<=>N o<=>O p<=>P q<=>Q r<=>R s<=>S t<=>T \
  u<=>U v<=>V w<=>W x<=>X y<=>Y z<=>Z) ^-> (__[<N><A><Adv><V><DT><``><ADD>\
  <-LRB-><WRB><LS><PRP><DT><AFX><UH><MD><WP><NFP><CC><''><CD><PDT><$><IN><WDT>\
  <SYM><WP$><HYPH><,><.><GW><PRP$><EX><POS><-RRB-><:><TO><RP><FW>]) % EXCEPTION JUST ADDED

#=D# = bdglmnpt
$T$ = {[#=D#]}:{[#=D#][#=D#]}

ALPHABET = [#sym#]
$duplicate$ = $T$ ^-> ([#cons#][#vowel#] __ (<A> e(r|st) | <V><dup>))

ALPHABET = [#Letter#] [#mcs#]:<>
$delete-POS$ = .*

$delete-e$ || $y-to-i$ || $y-to-ie$ || $s-to-es$ || $g-to-gg$ || $r-to-rr$ || $duplicate$ \
|| ($upper-to-lower$|$normal$) || $delete-POS$
