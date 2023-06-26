%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int yylex();
void yyerror(const char *s);

%}

%union {
    char *name;
    int num;
}

%token <name> NAME
%token <num> NUMBER
%token IF ELSE 
%token EQ NEQ GT GTE LT LTE AND OR ASSIGN PLUS MULT SLASH MINUS DELIMITER
%token LPAREN RPAREN LBRACE RBRACE SEMICOLON

%left OR
%left AND
%left EQ NEQ
%left GT GTE LT LTE
%left PLUS MINUS
%left MULT SLASH
%left ASSIGN DELIMITER


%%

program:  statements DELIMITER { printf("\nA PROPER IF-ELSE SYNTAX IS MATCHED...."); }
        ;

statements: statements statement | ;

statement: expr SEMICOLON
         | NAME ASSIGN expr SEMICOLON
         | IF LPAREN expr RPAREN LBRACE  statements RBRACE 
         | IF LPAREN expr RPAREN LBRACE  statements RBRACE  ELSE LBRACE  statements RBRACE 
         ;


expr: NUMBER
    | NAME
    | expr PLUS expr
    | expr MINUS expr
    | expr MULT expr
    | expr SLASH expr
    | expr EQ expr
    | expr NEQ expr
    | expr GT expr
    | expr GTE expr
    | expr LT expr
    | expr LTE expr
    | expr AND expr
    | expr OR expr
    | LPAREN expr RPAREN
    ;

%%
void yyerror(const char *s) {
        fprintf(stderr, "%s - IT IS NOT A PROPER IF-ELSE CONSTRUCT\n", s);
}


int main() {
    yyparse();
    return 0;
}