#cons# = bcdfghjklmnprstvwxz
#vowel# = aeiou
#letter# = a-z
#LETTER# = A-Z
#Number# = 0-9
#Punct# = \.:,;\-
#Letter# = #LETTER# #letter# #Number# #Punct#
#pos# = <N><V><A><PropN><Adv><DT>\
        <``><ADD><-LRB-><WRB><LS><PRP><DT><AFX><UH><MD> \
        <WP><NFP><CC><''><CD><PDT><$><IN><WDT><SYM><WP$><HYPH>\
        <,><.><GW><PRP$><EX><POS><-RRB-><:><TO><RP><FW>
#origin# = <native><classic>
#infl# = <N-reg><PropN-reg><A-reg><Adv-reg><V-reg><V-irreg><V-dup><V-pres-ger2><V-past-part><DT-reg>\
        <``-reg><ADD-reg><-LRB--reg><WRB-reg><LS-reg><PRP-reg><DT-reg><AFX-reg><UH-reg><MD-reg>\
        <WP-reg><NFP-reg><CC-reg><''-reg><CD-reg><PDT-reg><$-reg><IN-reg><WDT-reg><SYM-reg><WP$-reg>\
        <HYPH-reg><,-reg><.-reg><GW-reg><PRP$-reg><EX-reg><POS-reg><-RRB--reg><:-reg><TO-reg><RP-reg><foreign>

#trigger# = <dup>
#mcs# = #pos# #trigger#
#sym# = #Letter# #mcs# #infl#

$cons$ = [#cons#]
$vowel$ = [#vowel#]
$letter$ = [#letter#]
$LETTER$ = [#LETTER#]
$Letter$ = [#Letter#]
$pos$ = [#pos#]
$infl$ = [#infl#]
$trigger$ = [#trigger#]
$mcs$ = [#mcs#]
$sym$ = [#sym#]
