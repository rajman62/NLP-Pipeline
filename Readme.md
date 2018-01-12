Word embeddings algorithms such as word2vec and
fastText   gained   massive   popularity,   as   they   can   be   applied
to  huge  unannotated  corpuses  in  an  efficient  way.  A  possible
amelioration  of  those  algorithms  is  exploiting  more  linguistic
features.  Work  has  already  been  done  in  this  domain,  with
very  encouraging  results.  In  this  work,  we  build  a  full  NLP-
pipeline  that  implements  dependency  based  word  embeddings.
The  main  goal  is  to  reach  enterprise  level  code  quality  of  an
NLP-pipeline that can easily be used and extended to experiment
new  techniques.

The getting started section below can be used to execute the example code. The detailed concepts of the pipeline is
described in the `concepts.pdf` file. To extend the code, by far the easiest is to import it in a IntelliJ. Otherwise,
this is just a standard maven project and can be compiled with the mvn command. Javadocs are used in the code,
and the JUnit tests can also be useful as documentation for the modules.


# Getting started

Java 8 is required to run the code. Example configuration files are given in the `example/` folder. To try them out, 
simply double click on launch.cmd if you are on windows, if you are on linux or mac you can run the code from the
command line, as described below. A command prompt should open, 
type any english sentence and the parsed sentence should appear in a chart.

The program can be run by navigating to the project root, and running the `.jar` file in the `bin/` folder
```
java -jar bin/PipelineNLP.jar -c example/conf-linux.yml -syntax
```
The `-c` option specifies which configuration file to load, and the `-syntax` option specifies we want fully parsed charts
on the output. Be careful of which directory you run the program from, as all the paths specified in the configuration file
are relative to where the program is executed. The `conf-linux.yml` is written for linux and mac, and the `conf-win.yml`
 file is for windows. A full list of parameters can be printed with the `-h` option:
```
java -jar bin/PipelineNLP.jar -h
```

The [Foma](https://fomafst.github.io/) transducer is also needed. The code was tested with the version 0.9.18.
An executable for windows is provided in the `bin/` folder, it has been tested on Windows 10.
There is also a linux executable, compiled and tested on debian8. 
If there is any problems with executing the code, there might be a problem with the Foma executable, 
downloading and replacing the executable with the correct one for your system might fix it.


# The Example folder

The main configuration files are `conf-linux.yml` and `conf-win.yml`. One for linux and mac, the other for windows.
These two files are exactly identical, paths to executable just needed an extra `.exe` extension on windows.
The entire behavior of the program is dictated by these .yml configuration files. We will describe the composition
of these files and show how they affected the parsing of sentences. Every example file contains comments to explain 
the syntax.

For the moment there are two parts in the configuration files: one for the lexical module, and the other for the 
syntactic module.

The transducer and the grammar rules were extracted from the english conllu file of 
[Universal Dependencies](http://universaldependencies.org/).

## Lexical configuration
Configuration is made up of 5 parameters:
- wordFSAPath: the path to the Finite State Automaton that can parse full words
- separatorFSAPath: the path to the Finite State Automaton that can parse any separator. A Separator is
a sequence of character that separates two words.
- eosSeparatorRegex: a regex that match separators meant to indicate the End Of Sentence (eos)
- invisibleCharacterRegex: a regex that match any sequence of characters that should be removed from the output charts
- FomaBinPath: path to the foma binary
- FomaConfPath: path to the file to be loaded in foma

The paths are relative to where the program is called.

## Lexical execution example
Start the program, by navigating in a terminal to the root folder of the project and 
running:
```
java -jar bin/PipelineNLP.jar -c example/conf-linux.yml -lexical
```

Typing `Hello world! My name is Bob.` should get you to:
```
>>> Hello world! My name is Bob.

           | 
INTJ       | PROPN NOUN | PUNCT
Hello      | world      | !


                  | 
                  |                   | 
                  |                   |                   | 
PRON PROPN        | VERB NOUN         | ADV PRON AUX VERB | PROPN             | PUNCT
My                | name              | is                | Bob               | .

Total execution time: 129.141756ms


>>>
```

What we see here are two charts that have just been initialized by the lexical analyzer, one chart for each sentence.
Step by step, what happened is:

1. wordFSA and separator FSA have been called alternatively to find all the tokens of the sentences. WordFSA tried all match
from the start of the sentence and found Hello. Then separatorFSA was called and tried a longest match after Hello
and matched just a space. wordFSA was called again and matched word and so on.
2. eosSeparatorRegex is applied on every separator token. Every separator tokens matching eosSeparatorRegex that
is not an ambiguity is marked as a valid end of sentence. Ambiguity here could be `M. Smith` for example. `M` and `.`
are recognized as a word and a separator, but `M.` is also a possible token.
3. every token matching invisibleCharacterRegex is removed
4. the foma transducer is called with every remaining token. The tokens are passed as is to the transducer. For example
`"! "` is passed to the transducer, not `"!"`.
5. every match of invisibleCharacterRegex inside the tokens is removed.


## Transducer
`foma` and `lexc` files can be used. Their [website](https://fomafst.github.io/) is the best place to find documentation
on how to write them.


## Syntactic configuration and execution example
The syntactic configuration only has one option for the moment: `grammarPath`. To run the program in syntax mode, the
launch scripts can be used or the `.jar` can be called:
```
java -jar bin/PipelineNLP.jar -c example/conf-linux.yml -lexical
```

The previous sentence should give:

```
>>> Hello world! My name is Bob.
S PROPN INTJ NOUN
S PROPN INTJ NOUN | S PROPN NOUN
INTJ              | PROPN NOUN        | PUNCT
Hello             | world             | !

6 full charts.

S ADV PRON PROPN VERB NOUN
S ADV PRON PROPN VERB NOUN     | S ADV PRON PROPN VERB NOUN
S ADV PRON AUX PROPN VERB NOUN | S ADV PRON PROPN VERB NOUN     | S ADV PRON PROPN VERB
S PRON PROPN VERB NOUN         | ADV S PRON VERB NOUN           | S ADV PRON PROPN VERB          | S PROPN
PRON PROPN                     | VERB NOUN                      | ADV PRON AUX VERB              | PROPN                          | PUNCT
My                             | name                           | is                             | Bob                            | .

982 full charts.

Total execution time: 653.041521ms


>>>
```
The `S` rule is built in as the Start rule. As we can see, the grammar is very ambiguous. The grammar can be changed
to a less ambiguous grammar, and the semantic layer can solve the remaining ambiguities.
