%{

/*
 Write a lexer for simple C program
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

DIGIT [0-9]
LETTER [a-zA-Z]
NUMBER {DIGIT}+
ALPHANUMERIC ({DIGIT}|{LETTER})+
IF "if"
ELSE "else"
LBRACE "{"
RBRACE "}"
DELIM [ \t\n]
LPAREN [(]
RPAREN [)]
ID ({LETTER}{ALPHANUMERIC}*)
ASSIGNOP [=]
COMPOP ("<="|">="|"<"|">"|"==")
COMPARABLE ({NUMBER}|{ID})
COMPARISON ({COMPARABLE}{COMPOP}{COMPARABLE})
EXPR ({LPAREN}{COMPARISON}{RPAREN})



%%
({DELIM}+) {}
[;] {
    printf("<SEMICOLON>\n");
}

({ASSIGNOP}) {
    printf("<ASSIGNOP>\n");
}

({IF}) {
    printf("<IF>\n");
}

({ELSE}) {
    printf("<ELSE>\n");
}

("int") {
    printf("<INT>\n");
}

("float") {
    printf("<FLOAT>\n");
}

("bool") {
    printf("<BOOL>\n");
}

({LBRACE}) {
    printf("<LBRACE>\n");
}

({RBRACE}) {
    printf("<RBRACE>\n");
}

({ID}) {
    printf("<ID, ");
    printString();
    printf(">\n");
}



({NUMBER}) {
    printf("<NUMBER, ");
    printString();
    printf(">\n");
}
({EXPR}) {
    printf("<EXPR, ");
    printString();
    printf(">\n");
}
([^ \t\n])+ { printf("<INVALID>\n"); }
%%
  

int yywrap(void){}


  
int main()
{   
    yylex();
    return 0;
}