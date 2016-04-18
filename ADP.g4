grammar ADP;

context
: globalDeclarationList LINEBREAK functionDeclarationList LINEBREAK mainFunction
;

globalDeclarationList
: GLOBAL ':' LINEBREAK declarationList LINEBREAK ENDGLOBAL
;

declarationList
: typeSpecifier VARIABLENAME ';'
| typeSpecifier VARIABLENAME ';' LINEBREAK declarationList 
;

functionDeclarationList
: functions
;

functions
: function LINEBREAK functions
| function
;

function
: FUN functionName LP paramList RP ':' LINEBREAK statements LINEBREAK ENDFUN
| FUN functionName LP RP ':' LINEBREAK statements LINEBREAK ENDFUN
;

paramList
: parameter
| parameter ',' paramList
;

parameter
: typeSpecifier VARIABLENAME
;

typeSpecifier
: INT
| BOOL
;

mainFunction
: MAINFUN LP RP ':' LINEBREAK statements LINEBREAK ENDFUN  
;

statements
: declarationList 
| assignment 
| callfun  
;

expressions
: expr
| expr LINEBREAK expressions
;

expr
: expr '-' NUMBER
| expr '+' NUMBER
| expr '*' NUMBER
| expr '\\' NUMBER
| VARIABLENAME
| NUMBER
;

assignment
: VARIABLENAME ASSIGN value ';'
;

value
: expr
| NUMBER
| VARIABLENAME
;


callfun
: functionName LP VARIABLENAME RP ';'
;


functionName: VARIABLENAME ;

MAINFUN: 'main' ;
LP: '(';
RP: ')';
FUN: 'fun';
ENDFUN: 'endfunction' ;

GLOBAL: 'global' ;
ENDGLOBAL: 'endglobal' ;

LINEBREAK: [ \t\r\n]+ -> skip ;
VARIABLENAME: [a-z_][a-zA-Z0-9]* ;
NUMBER: [0-9]+;
INT: 'int' ;
BOOL: 'bool' ;
ASSIGN: '=';
