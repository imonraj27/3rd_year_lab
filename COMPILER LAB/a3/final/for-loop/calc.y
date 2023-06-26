%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern int yylex();
extern int yylineno;
extern char* yytext;
void yyerror(const char*);

%}

%token NAME NUMBER DELIM FOR OPEN_PAREN CLOSE_PAREN SEMICOLON PLUS ASSIGN EQ NEQ GT GTE LT LTE AND OR MINUS TIMES DIVIDE OPEN_BRACE CLOSE_BRACE 
%left NAME NUMBER DELIM FOR OPEN_PAREN CLOSE_PAREN SEMICOLON PLUS ASSIGN EQ NEQ GT GTE LT LTE AND OR MINUS TIMES DIVIDE OPEN_BRACE CLOSE_BRACE 

%%


for_loop:
    FOR OPEN_PAREN start SEMICOLON condition SEMICOLON update CLOSE_PAREN  OPEN_BRACE 
    statements CLOSE_BRACE DELIM { printf("\n....PROPER FOR-LOOP SYNTAX IS MATCHED.."); }
    ;

statements: statements statement | ;

statement: expr SEMICOLON
         | NAME ASSIGN expr SEMICOLON
         ;


expr: NUMBER
    | NAME
    | expr PLUS expr
    | expr MINUS expr
    | expr TIMES expr
    | expr DIVIDE expr
    | OPEN_PAREN expr CLOSE_PAREN
    ;

start:
    NAME ASSIGN NUMBER | 
    ;


condition:
    boolexpr
    |
    ;

boolexpr: expr EQ expr
    | expr NEQ expr
    | expr GT expr
    | expr GTE expr
    | expr LT expr
    | expr LTE expr
    | expr AND expr
    | expr OR expr
    ;

update:
    NAME PLUS PLUS
    | NAME MINUS MINUS
    |
    ;

%%

void yyerror(const char* s) {
    fprintf(stderr, "Line %d: %s near token %s\n", yylineno, s, yytext);
}

int main() {
    yyparse();
    return 0;
}
