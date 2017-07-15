from nltk import *
def main():
	corpus.treebank
	S = "(ROOT (S (NP (DT the) (NNS kids)) (VP (VBD opened) (NP (DT the) (NN box)) (PP (IN on) (NP (DT the) (NN floor))))))";
	t = Tree.fromstring(S)
	t.chomsky_normal_form()
	for p in t.productions():
		print (p)
if __name__ == "__main__":
    main()


