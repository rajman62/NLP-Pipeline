"""
Step 1: Gather Dependencies from Penn Treebank Sample and append them to a list of Dependency Structures
"""
from nltk.corpus import dependency_treebank
from nltk.parse import DependencyGraph, ProbabilisticProjectiveDependencyParser

t = dependency_treebank.parsed_sents()
dependency_structures = []
for i in range(0,len(t)):
    dependency_structures.append(dependency_treebank.parsed_sents()[i].to_conll(3))

"""
Step 2: Convert to Dependency Graphs and Append them to a list of Dependency Graphs
"""
dependency_trees = []
for i in range(0,len(dependencies_structures)):
    dependency_trees.append(DependencyGraph(dependency_structures[i]))

"""
Step 3: Train Probabilistic Dependency Parsing
"""
pbDp = ProbabilisticProjectiveDependencyParser()
ProbabilisticProjectiveDependencyParser.train(pbDp,dgs)


"""
Step 4: Return Dependency Graphs
"""

output_graphs = ProbabilisticProjectiveDependencyParser.parse(pbDp,['Pierre','Vinken',',','61','years','old',',','will','join','the','board','as','a','nonexecutive','director','Nov.','29','.'])
for g in sorted(output_graphs):
    print (g)

"""
Step 5: Get Dependency Structures in the form of triples
"""

for head, rel, dep in dg.triples():
    print('({h[0]}, {h[1]}), {r}, ({d[0]}, {d[1]})'.format(h=head, r=rel, d=dep))