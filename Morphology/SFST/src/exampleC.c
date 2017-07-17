# include <stdio.h>
# include "fst.h"

int main(int argc, char **argv) {
	FILE *file1=fopen("file1","rb");
	FILE *file2=fopen("file2","rb");
	char *s="Hello";
	Automaton a1(file1);
	Automaton a2(file2);
	Automaton a(s);
	(a1 || a2 || a).minimise().lower_level().print_strings(stdout);
}