$noun-reg-infl$ = <>:<N-reg> (\
                  {<NN>}:{} |\
                  {<NNS>}:{s})

$proper-noun-infl$ = <>:<PropN-reg> (\
                    {<NNP>}:{} |\
                    {<NNPS>}:{s})

$adj-reg-infl$ = <>:<A-reg> (\
                {<JJ>}:{} |\
                {<JJS>}:{er} |\
                {<JJR>}:{est})

$adv-reg-infl$ = <>:<Adv-reg> (\
                {<RB>}:{} |\
                {<RBR>}:{er} |\
                {<RBS>}:{est})

$verb-reg-infl$ = <>:<V-reg> (\
                {<VBZ>}:{s} |\
                {<VBD>}:{ed} |\
                {<VBN>}:{ed} |\
                {<VBG>}:{ing} |\
                {<VB>}:{}|\
                {<VBP>}:{})

$verb-dup-infl$ = <>:<V-dup> (\
                {<VBG>}:{s} |\
                {<VBD>}:{<dup>ed} |\
                {<VBN>}:{<dup>ed} |\
                {<VBG>}:{<dup>ing} |\
                {<VB>}:{}|\
                {<VBP>}:{})

$verb-pres-ger2-infl$ = <>:<V-pres-ger2> (\
                      {<VBZ>}:{s} |\
                      {<VBG>}:{<dup>ing} |\
                      {<VB>}:{}|\
                      {<VBP>}:{})

$verb-past-part-infl$ = <>:<V-past-part> (\
                      {<VBD>}:{} |\
                      {<VB>}:{}|\
                      {<VBP>}:{})

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
