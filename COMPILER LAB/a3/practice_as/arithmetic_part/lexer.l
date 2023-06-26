%{
    #include<stdio.h>
    #include "calc.tab.h"
    void yyerror(char *);
%}

%%

[0-9]+  { yylval = atoi(yytext); return NUMBER; }
[-+*/)(&|\n]  return *yytext;
[ \t]   ; /* skip whitespace */
.       yyerror("invalid character");

%%

int yywrap() {
    return 1;
}