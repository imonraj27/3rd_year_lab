%{
    #include<stdio.h>
    #include "calc.tab.h"
    void yyerror(char *);
%}

%%

a { return A; }
b { return B; }
\n { return NL; }
.       yyerror("invalid character");

%%

int yywrap() {
    return 1;
}