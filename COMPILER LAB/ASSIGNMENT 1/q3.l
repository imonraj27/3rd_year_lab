%{

/*
 Write a lex program that will function
 as a calculator and perform the
 operations like addition, subtraction,
 multiplication, division, modulo and power.
*/
#include<stdio.h>
#include<string.h>

#define ADD 0
#define SUB 1
#define MULT 2
#define EXP 3
#define DIV 4
#define MOD 5

int flag = 0, invalid = 0;
int firstnum, secondnum, op=-1;


// TO FIND THE EXPONENTIATION OF A NUMBER
int power(int a, int b){
    if(a==1 || b==0) return 1;
    int ans = 1; 
    for(int i=0; i<b; i++){
        ans *= a;
    }
    return ans;
}

// FUNCTION TO PRINT THE CURRENT TOKEN THAT
// MATCHES ANY REGEX
int stringToInt(){
    int ans = 0;
	for(int i=0; i<yyleng; i++){
		ans = ans*10 + (yytext[i]-'0');
	}
    return ans;
}

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

digit [0-9]
nonzerodigit [1-9]
number {digit}+
ws [ \t\n]
add [\+]
sub [\-]
mult [\*]
exp [\*][\*]
div [/]
mod [%]
  

%%
{number} { 
    if(flag==0){
        /* ITS THE FIRST NUMBER */

        flag++;
        firstnum = stringToInt();
    }else if(op<0){
        /* SECOND NUMBER BUT NO OPERATOR IN MIDDLE */

        invalid = 1;
        op = -1;
        flag=0;
    }else{
        /* TAKES 2ND NUMBER AND DOES THE CALCULAITON */
        
        secondnum = stringToInt();
        int final;
        if(op==ADD){
            final = firstnum+secondnum;
        }else if(op==SUB){
            final = firstnum-secondnum;
        }else if(op==MULT){
            final = firstnum*secondnum;
        }else if(op==EXP){
            final = power(firstnum,secondnum);
        }else if(op==DIV){
            final = firstnum/secondnum;
        }else if(op==MOD){
            final = firstnum%secondnum;
        }

        op = -1;
        flag=0;
        invalid=0;
        printf("ANS: %d", final);
    }
}

{add} {
    if(op>=0) { invalid = 1; }
    op = ADD;
}

{sub} {
    if(op>=0) { invalid = 1; }
    op = SUB;
}

{mult} {
    if(op>=0) { invalid = 1; }
    op = MULT;
}

{exp} {
    if(op>=0) { invalid = 1; }
    op = EXP;
}

{div} {
    if(op>=0) { invalid = 1; }
    op = DIV;
}

{mod} {
    if(op>=0) { invalid = 1; }
    op = MOD;
}

{ws}+ {}

([^ +\-*/%0-9])+ { printf("\nINVALID -> "); printString(); }
%%
  
int yywrap(void){}

int main()
{   
    yylex();
    return 0;
}