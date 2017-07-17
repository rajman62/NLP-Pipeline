#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct x{
    string[] tokens;
};

int count(const char *s){
    if(s == NULL ||  *s != '[')
        return 0;//bad
    string[] tokens;
    int n, count = 0;
    for(;;){
        n = -1;
        if(2!=sscanf(++s, "[%s]%n", &tokens, &n) || n < 0){
            return 0;
        } else {
            s += n;
            ++count;
            if(*s == ',')
                continue;
            else if(*s == ']')
                return count;
            else
                return 0;
        }
    }
}
void set(struct x *a, const char *s){
    int n, c = 0;
    while(2==sscanf(++s, "[%s]%n", &a[c].tokens, &n)){
        s += n;
        ++c;
    }
}

int main(void) {
    char *input = "[[';', 'eat', ';', '<rea>', 'd', '.'], ['','','','','',''], ['eat','','','','',''], ['','','','','',''], ['','','','','',''], ['','','','','','']]";
    int i, n = count(input);
    if(n == 0){
        printf("invalid format!\n");
        exit(EXIT_FAILURE);
    }
    struct x arr[n];
    set(arr, input);
    for(i = 0; i < n; ++i){
        printf("[%s]\n", arr[i].tokens);
    }

    return 0;;
}