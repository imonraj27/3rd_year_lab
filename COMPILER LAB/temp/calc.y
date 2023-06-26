%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern int yylex();
extern int yylineno;
extern char* yytext;
void yyerror(const char*);
int ans=0;

%}

%token bindigit octdigit B O DELIM PLUS
%left O B 
%%

E : E S { printf("Decimal Value: %d ",$2); ans=0; } DELIM | ;
S : Sb B { $$ = ans; ans=0; } | So O { $$ = ans; ans=0; } | S PLUS S { $$ = $1 + $3; };
Sb : Sb bindigit {  ans = ans*2 + $2;} | bindigit {  ans = ans*2 + $1;};
So : So digit {  ans = ans*8 + $2;} | digit {  ans = ans*8 + $1;};
digit : bindigit | octdigit;
%%

void yyerror(const char* s) {
    fprintf(stderr, "Line %d: %s near token %s\n", yylineno, s, yytext);
}

int main() {
    yyparse();
    return 0;
}
