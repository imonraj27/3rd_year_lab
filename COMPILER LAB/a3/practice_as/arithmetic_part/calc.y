%{
/* Definition section */
    #include<stdio.h>
    int yylex(void);
    void yyerror(char *);
    int flag=0;
    extern FILE *yyin;
	extern FILE *yyout;
%}

%token NUMBER

%left '+' '-'

%left '*' '/' '%'

%left '(' ')'
%left '&' '|'

/* Rule Section */
%%

ArithmeticExpression: E { printf("\nResult=%d\n", $$); return 0; } ;

/* E: E'+'E {$$=$1+$3;} | E'-'E {$$=$1-$3;} | E'*'E {$$=$1*$3;} | E '/'E {$$=$1/$3;} | E'%'E {$$=$1%$3;} | '('E')' {$$=$2;} | NUMBER {$$=$1;} ; */


E: E'+'T {$$=$1+$3;} | E'-'T {$$=$1-$3;} | T {$$=$1;}
T: T'*'F {$$=$1*$3;} | T'/'F {$$=$1/$3;} | F {$$=$1;}
F: F'&'G {$$=$1&$3;} | F'|'G {$$=$1|$3;} | G {$$=$1;}
G: NUMBER {$$=$1;} | '('E')' {$$=$2;}
%%

//driver code
int main() {
    printf("\nENTER ARITHMETIC EXPRESSION:\n");

    yyparse();
    if(flag!=0)
        printf("\nEXPRESSION IS NOT VALID..\n\n");
}

void yyerror(char *s) {
    fprintf(stderr, "%s\n", s);
    flag=1;
}
