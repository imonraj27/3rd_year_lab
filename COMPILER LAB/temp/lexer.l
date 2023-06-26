%{
    #include<stdio.h>
    #include "calc.tab.h"
%}

%%
[2-7] {
    yylval = atoi(yytext);
    return octdigit;
}
[0-1] {
    yylval = atoi(yytext);
    return bindigit;
}

"b" {
    return B;
}

"o" {
    return O;
}

"$" {
    return DELIM;
}

"+" {
    return PLUS;
}
[ \t\n] {}


%%

int yywrap() {
    return 1;
}
