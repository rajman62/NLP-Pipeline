$noun-reg-infl$ = <>:<N-reg> <N> (\
                  {<3sg>}:{} |\
                  {<3pl>}:{s})

$proper-noun-infl$ = <>:<PropN-reg> <PropN> (\
                    {<3sg>}:{} |\
                    {<3pl>}:{s})

$adj-reg-infl$ = <>:<A-reg> <A> (\
                {<pos>}:{} |\
                {<comp>}:{er} |\
                {<sup>}:{est})

$adv-reg-infl$ = <>:<Adv-reg> <Adv> (\
                {<pos>}:{} |\
                {<comp>}:{er} |\
                {<sup>}:{est})

$verb-reg-infl$ = <>:<V-reg> <V> (\
                {<3sg>}:{s} |\
                {<past>}:{ed} |\
                {<part>}:{ed} |\
                {<gerund>}:{ing} |\
                {<n3s>}:{}|\
                {<INF>}:{})

$verb-dup-infl$ = <>:<V-dup> <V> (\
                {<3sg>}:{s} |\
                {<past>}:{<dup>ed} |\
                {<part>}:{<dup>ed} |\
                {<gerund>}:{<dup>ing} |\
                {<n3s>}:{}|\
                {<INF>}:{})

$verb-pres-ger2-infl$ = <>:<V-pres-ger2> <V>(\
                      {<3sg>}:{s} |\
                      {<gerund>}:{<dup>ing} |\
                      {<n3s>}:{}|\
                      {<INF>}:{})

$verb-past-part-infl$ = <>:<V-past-part> <V> (\
                      {<past>}:{} |\
                      {<part>}:{})

$other-no-infl$ = <>:<``-reg> <``>| <>:<ADD-reg> <ADD>| <>:<-LRB--reg> <-LRB->|\
                  <>:<WRB-reg> <WRB>| <>:<LS-reg> <LS>| <>:<PRP-reg> <PRP>|\
                  <>:<DT-reg> <DT>| <>:<AFX-reg> <AFX>| <>:<UH-reg> <UH>|\
                  <>:<MD-reg> <MD>| <>:<WP-reg> <WP>| <>:<NFP-reg> <NFP>|\
                  <>:<CC-reg> <CC>| <>:<''-reg> <''>| <>:<CD-reg> <CD>|\
                  <>:<PDT-reg> <PDT>| <>:<$-reg> <$>| <>:<IN-reg> <IN>|\
                  <>:<WDT-reg> <WDT>| <>:<SYM-reg> <SYM>| <>:<WP$-reg> <WP$>|\
                  <>:<HYPH-reg> <HYPH>| <>:<,-reg> <,>| <>:<.-reg> <.>|\
                  <>:<GW-reg> <GW>| <>:<PRP$-reg> <PRP$>| <>:<EX-reg> <EX>|\
                  <>:<POS-reg> <POS>| <>:<-RRB--reg> <-RRB->| <>:<:-reg> <:>|\
                  <>:<TO-reg> <TO>| <>:<RP-reg> <RP> | <>:<foreign> <FW>


$noun-reg-infl$ | $proper-noun-infl$ | $adj-reg-infl$ | $adv-reg-infl$ | $verb-reg-infl$ |\
$verb-dup-infl$ | $verb-pres-ger2-infl$ | $verb-past-part-infl$ | $other-no-infl$
