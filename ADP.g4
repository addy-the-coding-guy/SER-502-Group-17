grammar ADP;

program
: globalDeclarationList LINEBREAK functionDeclarationList LINEBREAK mainFunction
;

globalDeclarationList
: global':' LINEBREAK declarationList LINEBREAK endglobal
;
declarationList: typeSpecifier VARIABLENAME ';'
;

functionDeclarationList
: functions
;

functions
: function
| functions LINEBREAK function
;

function
: fun functionName '('paramList')'':' LINEBREAK statements LINEBREAK endOfFunction
| fun functionName '(' ')' ':' LINEBREAK statements LINEBREAK endOfFunction
;

paramList
: parameter
| paramList ',' parameter
;

parameter
: typeSpecifier VARIABLENAME
;

typeSpecifier
: 'int'
| 'bool'
;

mainFunction
: MAIN
;

statements
: expressions
;

expressions
: expr
| expressions LINEBREAK expr
;

expr
: expr ASSIGN expr
| expr '-' expr
| expr '+' expr
| expr '*' expr
| expr '\\' expr
| VARIABLENAME
| NUMBER
;

fun: FUN;
functionName:VARIABLENAME;
MAIN: 'main' '(' ')';
endOfFunction
: ENDFUN
;
global: GLOBAL;
endglobal: ENDGLOBAL;

LINEBREAK: [\n]+ ->skip;
VARIABLENAME: [a-z_][a-zA-Z0-9]*;
NUMBER: [0-9]+;

ASSIGN: '=';
