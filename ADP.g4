grammar ADP;

program
: (vardeclaration)*(fundeclaration)* mainblock
;

mainblock
: 'main''('')' mainbody 'endmain'
;

mainbody
: statementlist
;

statementlist
: statements+
;

vardeclaration
: typespecifier varname Assign literal LINEBREAK
| typespecifier varname LINEBREAK
| typespecifier varname
;

varname
: String
;

fundeclaration
: returntypespecifier 'fun' functionname '(' paramlist ')' 'startfun' statements+ 'endfun'
;

paramlist
: typespecifier argumentlist
;

returntypespecifier
: typespecifier
;

typespecifier
: 'integer'
| 'boolean'
;

statements
: booleanstatement
| assignmentstatement
| conditionalstatement
| declarationstatement
| loopingstatement 
| arithmeticstatement 
| printstatement
| functioncall
;

booleanstatement
: 'boolean' varname Assign booleanliteral LINEBREAK
| 'boolean' varname LINEBREAK
;

declarationstatement
: 'integer' varname Assign expr LINEBREAK
| 'boolean' varname Assign expr LINEBREAK
| 'integer' varname LINEBREAK
| 'boolean' varname LINEBREAK
;

conditionalstatement
: 'if' '('conditionalexpression')' 'startif' statements+ 'endif'
| 'else''startels' statements+ 'endels'
;

loopingstatement
:'while' '('conditionalexpression')' 'startwhile' statements+ 'endwhile'
;

conditionalexpression
: BooleanLiteral
|expr conditionaloperator expr 
;

conditionaloperator
: '>'| '>='| '<'| '<='| '=='| '!=' | booleanliteral ; 

arithmeticstatement
: expr
;

printstatement
: 'print' (varname|literallist) LINEBREAK
| 'print' '\''(String)+ '\'' LINEBREAK
;

functioncall
: functionname '('argumentlist')' LINEBREAK;

argumentlist
: literallist
| varname
| varname ','
;

assignmentstatement
: varname Assign expr LINEBREAK
;

expr
: expr '-' expr
| expr '+' expr
| expr '*' expr
| expr '/' expr
| expr '%' expr
| '(' expr ')'
| '{'expr'}'
| booleanliteral
| varname
| literal
;

literallist
:literal
| literal ','
| booleanliteral
;

functionname
: String
;

String
: [a-zA-Z_]+[a-zA-Z_.0-9:]*
;
literal
: numericLiteral
| booleanliteral
| String
;
numericLiteral
: NumericLiteral;

NumericLiteral
: [0-9]+
;
LINEBREAK
: ';'
;

booleanliteral
: BooleanLiteral
;

BooleanLiteral
: 'true'
| 'false'
;

Assign
: '='
;

WS: [ \n\t\r]+ -> skip;
