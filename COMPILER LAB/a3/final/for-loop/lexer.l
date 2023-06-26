%{
    #include<stdio.h>
    #include "calc.tab.h"
%}

%%
"for"       { return FOR; }
"("         { return OPEN_PAREN; }
";"         { return SEMICOLON; }
")"         { return CLOSE_PAREN; }
"+"         { return PLUS; }
"-"         { return MINUS; }
"*"         { return TIMES; }
"/"         { return DIVIDE; }
"{"         { return OPEN_BRACE; }
"}"         { return CLOSE_BRACE; }
"="         { return ASSIGN; }
"=="        { return EQ; }
"$"         { return DELIM; }
"!="        { return NEQ; }
">"         { return GT; }
">="        { return GTE; }
"<"         { return LT; }
"<="        { return LTE; }
"&&"        { return AND; }
"||"        { return OR; }
[0-9]+             {  return NUMBER; }
[a-zA-Z][a-zA-Z0-9]* { return NAME; }
[ \t\n]            { /* Ignore whitespace */ }
.                  { /* Ignore anything else */ }

%%

int yywrap() {
    return 1;
}
