grammar ADP;

program
: globalDeclarationList LINEBREAK functionDeclarationList LINEBREAK mainFunction
;

globalDeclarationList
: global':' LINEBREAK declarationList LINEBREAK end global
;

functionDeclarationList
: functions
;

functions
: function
| functions LINEBREAK function
;

function
: fun functionName (paramList): LINEBREAK statements LINEBREAK endOfFunction
| fun functionName (): LINEBREAK statements LINEBREAK endOfFunction
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
: main()
;

statements
: expressions
;

expressions
: expression
| expressions LINEBREAK expression
;

expression
: expr ASSIGN expr
| expr -' expr
| expr '+' expr
| expr '*' expr
| expr '\' expr
| '('expr')'
|'{'expr'}'
;


endOfFunction
: end fun
;

global: GLOBAL;
end global: END GLOBAL;
LINEBREAK: '\n';
VARIABLENAME: [a-z_][a-zA-Z0-9]*;
INT: 'int';
BOOL: 'bool';
ASSIGN: '=';
