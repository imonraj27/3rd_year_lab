%{

/*
 Write a lex program to recognize a string that
 starts with a capital letter which is followed
 by any small letter or decimal digits and
 ends with a special character of your choice.
*/
#include<stdio.h>
#include<string.h>

// FUNCTION TO PRINT THE CURRENT TOKEN THAT
// MATCHES ANY REGEX
void printString(){
	printf("(");
	for(int i=0; i<yyleng; i++){
		printf("%c", yytext[i]);
	}
	printf(") ");
}
%}


capital [A-Z]
alphanumeric [a-z0-9]
special [#]
delim [ \t\n]
  

%%
({capital}{alphanumeric}+{special}) { printf("\nVALID -> "); printString(); }
([^ \t\n])+ { printf("\nINVALID -> "); printString(); }
%%
  
int yywrap(void){}

int main()
{   
    yylex();
    return 0;
}