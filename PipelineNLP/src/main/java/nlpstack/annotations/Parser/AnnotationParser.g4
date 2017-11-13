grammar AnnotationParser;

init : toplevel EOF;

toplevel:
    inputString                         # inputStringThenEOF
    | annotation                        # annotationThenEOF
    | annotation toplevel               # annotationThenToplevel
    | inputString annotation            # inputStringThenAnnotation
    | inputString annotation toplevel   # inputStringThenAnnotationThenToplevel
    ;

inputString :
    simpleString                                # simpleStringThenEnd
    | wordAnnotation                            # wordAnnotationThenEnd
    | wordAnnotation inputString                # wordAnnotationThenInputString
    | simpleString wordAnnotation               # simpleStringThenWordAnnotation
    | simpleString wordAnnotation inputString   # simpleStringThenWordAnnotationThenInputString
    ;

annotation :
    lexicalAnnotation       # lexicalAnnotationCase
    | syntacticAnnotation   # syntacticAnnotationCase
    ;

lexicalAnnotation : ANNOTATIONCHARACTER ANNOTATIONOPEN 'lex:' chart ANNOTATIONCLOSE;
syntacticAnnotation : ANNOTATIONCHARACTER ANNOTATIONOPEN 'syn:' chart ANNOTATIONCLOSE;

wordAnnotation :
    ANNOTATIONCHARACTER ANNOTATIONOPEN
        SPACE* TAGDELIMITER word TAGDELIMITER SPACE* COMMA
        SPACE* TAGDELIMITER tag TAGDELIMITER SPACE*
    ANNOTATIONCLOSE;

word :
    (
        (ESCAPECHARACTER TAGDELIMITER) |
        ANNOTATIONCHARACTER |
        ANNOTATIONOPEN |
        ANNOTATIONCLOSE |
        (ESCAPECHARACTER ESCAPECHARACTER) |
        COLON |
        COMMA |
        OPENBRACKET |
        CLOSEBRACKET |
        SPACE |
        DIGIT |
        CHARACTER
    )+;

simpleString :
    (
        TAGDELIMITER |
        (ESCAPECHARACTER ANNOTATIONCHARACTER) |
        ANNOTATIONOPEN |
        ANNOTATIONCLOSE |
        (ESCAPECHARACTER ESCAPECHARACTER) |
        COLON |
        COMMA |
        OPENBRACKET |
        CLOSEBRACKET |
        SPACE |
        DIGIT |
        CHARACTER
    )+;

chart : SPACE* (chartToken SPACE*)+ COLON SPACE* (chartTag SPACE*)+;

chartToken : OPENBRACKET token CLOSEBRACKET;
chartTag :  OPENBRACKET SPACE* number SPACE* COMMA
            SPACE* number SPACE* COMMA
            SPACE* TAGDELIMITER tag TAGDELIMITER SPACE* CLOSEBRACKET;

token :
    (
        TAGDELIMITER |
        ANNOTATIONCHARACTER |
        ANNOTATIONOPEN |
        ANNOTATIONCLOSE |
        (ESCAPECHARACTER ESCAPECHARACTER) |
        COLON |
        COMMA |
        OPENBRACKET |
        (ESCAPECHARACTER CLOSEBRACKET) |
        SPACE |
        DIGIT |
        CHARACTER
    )+;

tag :
    (
        (ESCAPECHARACTER TAGDELIMITER) |
        ANNOTATIONCHARACTER |
        ANNOTATIONOPEN |
        ANNOTATIONCLOSE |
        (ESCAPECHARACTER ESCAPECHARACTER) |
        COLON |
        COMMA |
        OPENBRACKET |
        CLOSEBRACKET |
        SPACE |
        DIGIT |
        CHARACTER
    )+;

number : DIGIT+;

TAGDELIMITER : '"';
ANNOTATIONCHARACTER : '#';
ANNOTATIONOPEN : '[';
ANNOTATIONCLOSE : ']';
ESCAPECHARACTER : '\\';
COLON : ':';
COMMA : ',';
OPENBRACKET : '(';
CLOSEBRACKET : ')';
SPACE : [ \r\t\n];
DIGIT : [0-9];
CHARACTER : ~["#[\]\\: \r\t\n0-9];