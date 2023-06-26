%{

/*
 Write a lex file to check the validity of 
 a binary message that starts with 1 
 and ends with 101 and contains any number
 of dots in between the parts of the messages.
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

  

%%
(1[\.]?([01]([\.])?)*101) { printf("\nVALID -> "); printString(); }
([^ \t\n])+ { printf("\nINVALID -> "); printString(); }
%%
  

int yywrap(void){}


  
int main()
{   
    yylex();
    return 0;
}