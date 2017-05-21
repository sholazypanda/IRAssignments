
How to compile and execute???

unzip the folder
cd into folder
execute the following commands
source /usr/local/corenlp341/classpath.sh
javac *.java
java Homework3 /people/cs/s/sanda/cs6322/Cranfield/ /people/cs/s/sanda/cs6322/resourcesIR/stopwords /people/cs/s/sanda/cs6322/hw3.queries


Description:

1. Output Attached as output.txt(vector representation)

2. Output attached as output.txt (rank,score,docid,headline,top5)

3. relevance and non relevance attached as relevantNonrelevant.txt

4. Some top ranked non relevant documents did not get a lower score, because their frequency was higher in most of the documents(both kinds- whether it be relevant or non -relevant. False positive rate was higher, because of term frequency. They ended up having larger score.

5.
w1 is formulated on tf ,maxtf whereas w2 is formulated on avgdoclength and doclen along with tf. 

Example1: Query 17, all the non relevant docs are from Weighing scheme 1, because the terms three dimensional and two dimensional have more frequency and their w1 score ended up being higher, but they were not relevant to the context of query.

Actual Query was:can the three-dimensional problem of a transverse potential flow about a body of revolution be reduced to a two-dimensional problem

Doc 1281 headline:turbulent heat transfer on blunt-nosed bodies in two-dimensional and general three-dimensional hypersonic flow .
which is actually not relevant, but because of the words two-dimensional and three dimensional it got scored higher and then ranked higher.


Example2: Query 19,
Actual Query: does there exist a good basic treatment of the dynamics of re-entry combining consideration of realistic effects with relative simplicity of results

Here Weight2 seems to be working well. Because most of the non relevant documents are from weighing scheme 1.

overall it is noticeable that Weighing scheme 2 works better than Weighing scheme 1. Relying solely on tf and maxtf doesn’t produce effective results , we should consider other parameters as well.

6.Design Decisions:
Query reading, generating tokens, removal of stop words, generating lemmas, building lemma index map with term features, building lemma tree map with doc features(docname, title, maxtf,doclen) , deriving avg doc len from there), populate weight1 and weight2 for each doc.
Build query feature, by reading the query, and populating doc feature objects for that particular query and along with that calc w1 and w2. Used cosine similarity using both doc  vector and query vector weights to calc scores s1 and s2 for each query. 
(Data structures: Hashmap and treemap)
DocFeatures(doclen,docname,doctit,w1,w1n,w2,w2n,max_tf,doctf,doctfw)
TermFeatures(df,tf,doctermfreq)
QueryFeature (s1, s2, docfeature obj)
QueryManager(Calc scores from doc vector n query vector)
