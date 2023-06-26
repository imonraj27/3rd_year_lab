%{
    #include<stdio.h>
    void yyerror(char *);
%}

%%

([a]+[b]+) { printf("Accepted: format a^m-b^n"); }
[ \t\n] ;


%%

int yywrap() {
    return 1;
}