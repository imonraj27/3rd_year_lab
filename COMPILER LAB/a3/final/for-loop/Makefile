CC = gcc
CFLAGS = -g

LEX_PARSE = calc.tab.c lex.yy.c

main:$(LEX_PARSE)
	$(CC) $(CFLAGS) -o $@ $^

calc.tab.c:calc.y
	bison -d $<

lex.yy.c:lexer.l
	flex $<

clean:
	rm -f main lex.yy.c calc.tab.c calc.tab.h