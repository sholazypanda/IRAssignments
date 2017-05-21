
How to compile and execute???

unzip the folder
cd into folder
execute the following commands
source /usr/local/corenlp341/classpath.sh
javac *.java
java Homework2 /people/cs/s/sanda/cs6322/Cranfield/ /people/cs/s/sanda/cs6322/resourcesIR/stopwords

Description:

Used separate dictionaries for lemma and stem.
IndexReader - reads all tokens
Indexing uses the token objects read by reader to build indexes using treemaps.
Term features class contains(df,tf(total freq),docFreq(Map) which is doc term frequency)
Doc Features class contains(doclen,max_tf,mostFrequentTerm)
Build index using these features, basically if the treemap doesnt already contain the term, then put a new one otherwise update the existing ones.
create tree map of doc id and doc features.
create index map with term features.
Once I calculate all statistics and store the features, I output them using WriteOutput helper class.


I first make a pass through the collection assembling all term-docID pairs. I then sorted the pairs with the term as the dominant key and docID as the secondary key.(Basically used a treemap) Finally, I organized the docIDs for each term into a postings list and compute statistics like term and document frequency. This is basically BSBI using in memory techniques.

Compression technique::

Compression class :: constructs the compression list.

Used front encoding for the lemmas
Used Blocked coding for the stems
Front Encoding:
A sequence of terms with identical prefix(created a method for the same), is enclosed by marking the end of prefix by *, and replacing it with # with subsequent terms.
Blocked Coding:
Store pointers to every kth term string.Need to store term lengths.

Compression Codes:
Used Gamma for lemma: Used the algorithm , get binary representation, calc offset, calc unary(offset.length), concat uv with offset
,Used Delta Codes for stems:getGamma(binrepresentation.length),offset of bin representation,concat gc with offset and convert it to byte array

