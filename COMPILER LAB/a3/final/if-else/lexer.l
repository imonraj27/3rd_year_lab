%{
    #include<stdio.h>
    #include "calc.tab.h"
%}


%%
"if"        { return IF; }
"else"      { return ELSE; }
"("         { return LPAREN; }
")"         { return RPAREN; }
"{"         { return LBRACE; }
"}"         { return RBRACE; }
";"         { return SEMICOLON; }
[0-9]+      { yylval.num = atoi(yytext); return NUMBER; }
[a-zA-Z]+   { yylval.name = strdup(yytext); return NAME; }
"=="        { return EQ; }
"="        { return ASSIGN; }
"*"        { return MULT; }
"/"        { return SLASH; }
"+"        { return PLUS; }
"-"        { return MINUS; }
"$"        { return DELIMITER; }
"!="        { return NEQ; }
">"         { return GT; }
">="        { return GTE; }
"<"         { return LT; }
"<="        { return LTE; }
"&&"        { return AND; }
"||"        { return OR; }
[ \t\n]     { /* ignore whitespace */ }
.           { }
%%

int yywrap() {
    return 1;
}
