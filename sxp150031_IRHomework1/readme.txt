
scp the folder into csgrads1
Use following commands to run the program;

cd into the folder

Command:

javac *.java

After all class files are generated successfully, then use the following command, Homework1 is the entry point.

Command:
java Homework1 /people/cs/s/sanda/cs6322/Cranfield/

Approach::

1.Tokenization

Homework1 is the entry point class.
Dictionary class contains(term, no of docs, total frequency, posting List hash map)
Word class contains(token, document id)
Tokenize class will initialize  all required variables and initiate the process of filling up dict and counting frequency of each term.
First of all I get document names by going through the list of files.
For each document name, I get the list of tokens from the doc and then append all of those tokens in a string buffer.
I read only those lines which do not contain end and beginning tag.
I replace all hyphens by space
I replace all those ’s by empty string
I also replace all punctuations which are [^a-zA-Z] with empty string. I also ignored numbers.If I do [^a-zA-Z0-9], then no of tokens increase.
At first the dictionary is empty, so create new dictionary, add word, number of documents =1 and frequency = 1, in the posting file, I have the word with its own frequency.
When the dictionary already contains the term, then update the no of docs, frequency in posting list, and increase total frequency by one.
Then show results using wordList size and other methods like occur once and most frequent.

2. Stemming

I used the Stemmer class provided in the assignment. 
Used it to create a object and call stem method. and My code for show results of stemming is same as tokenization with a little bit of tweaking.
I created the stemming dictionary to add stems and its frequencies. Counted the number of stems to use it in showResults.
While counting for frequency = 1, if we store the frequency as key in treeMap and list of words as value and then store them in a newMap which is a collections.reverseOrder treeMap, then we can get the top most 30 frequent stems, similar code is written for tokenization.