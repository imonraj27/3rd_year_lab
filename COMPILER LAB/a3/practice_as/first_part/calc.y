%{
/* Definition section */
    #include<stdio.h>
    int yylex(void);
    void yyerror(char *);
    int flag=0;
    extern FILE *yyin;
	extern FILE *yyout;
%}


%token A B NL


/* Rule Section */
%%

Expression: S NL { return 0; } ;

S : A S B | ;
%%

//driver code
int main() {
    printf("\nENTER STRING:\n");

    yyparse();
    if(flag!=0)
        printf("\nSTRING IS NOT VALID..\n\n");
    else
        printf("\nSTRING IS VALID...\n\n");
}

void yyerror(char *s) {
    flag=1;
}
