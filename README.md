# COMS6111 Project 1
COMS6111 Project1

Group Name
--------
Project 1 Group 27

Group Member
--------
   Qianwen Zheng (qz2271)

   Jiajun Zhang (jz2793)

Files
--------
	└── IR-system
	    ├── libs
	    │   ├── google-api-client-1.22.0.jar
	    │   ├── google-api-client-jackson2-1.22.0.jar
	    │   ├── google-api-services-customsearch-v1-rev56-1.22.0.jar
	    │   ├── google-http-client-1.22.0.jar
	    │   ├── google-http-client-jackson2-1.22.0.jar
	    │   ├── httpclient-4.0.1.jar
	    │   └── jackson-core-2.1.3.jar
	    ├── sup
	    │   ├── proj1-stopword.txt
	    │   └── formula.png
	    ├── Makefile
	    ├── Doc.java
	    ├── Filter.java
	    ├── GoogleSearch
	    ├── GoogleSearch.java
	    ├── Query.java
	    ├── Rocchio.java
	    ├── README.md
	    └── transcript.txt


Run
--------
1. Clone project

        $ git clone https://github.com/petercanmakit/group27-proj1.git
	
2. Navigate to folder

		$ cd ./group27-proj1
	
3. Install dependencies

        $ make

4. Run

        $ ./GoogleSearch <google api key> <google engine id> <precision> <query>

   \<google api key> -- your Google Custom Search API Key

   \<google engine id> -- your Google Custom Search Engine ID

   \<precision> -- the target value for precision@10, a real number between 0 and 1

   \<query> -- your query, separated by space

Keys
--------
1. Google Custom Search API Key

         AIzaSyARFSgO3Kiuu3IOtEL8UwdIbrS7SiB43qo

2. Google Custom Search Engine ID

         018258045116810257593:z1fmkqqt_di

Internal Design
---------
1. We get the original query from users input. Then, we stores query as a customized Query type. In this class, we compute the initial __q<sub>i</sub>__ vector (tf-idf weight).

2. After searching the query in Google CSE, user can get 10 results and each result contains URL, title and its summary. The result will display one by one, and at the end of each result, user is requested to determine whether it is relevant to what he wants to search by inputing “Y” as yes and “N” as no.

3. We combine each result’s title and summary as a String, and then use a filter method (which is defined in Filter class) to eliminate stopwords. Note that instead strictly eliminating all words that show in "proj1-stopword.txt" file, we skip those words which are contained in the original query. Then store the filtered string as a Doc type (Doc is a customized class). For each Doc, we compute its term frequency and tf-idf weight, which are stored in two HashMap.

4. Based on user’s feedback, we have two ArrayLists. One list stores relevant Docs, and the other store non-relevant Docs. According to these two list, we compute two HashMap. One stores word-weight pairs in relevant docs, and the other stores pairs in non-relevant docs.

5. We put the main computing part in a while loop, and the loop will only be broken when the desired precision is reached or the precision is 0. Therefore, if the precision is still below the desired precision, given the __q__ vector and two word-weight HashMap, we implement Rocchio’s Algorithm to get new query. The expanded query will be automatically searched in Google CSE and a new round starts.

Query modification method
--------
1. Given the original query, firstly compute the __q<sub>i</sub>__ vector, in which each element is the tf-idf weight of each word.

2. Based on Google Search results and user’s feedback, two vectors are computed. One stores tf-idf weight of words in relevant results and the other stores tf-idf weight of words in non-relevant results. Then normalized them separately.

   The formula to compute tf-idf weight is :

         tf-idf = tf * (1 + idf) = tf * (1 + log(N/df))

  **Reference:** 
  
  Tobias Liland Bjormyr, [Deep Learning with emphasis on extracting information from text data](https://daim.idi.ntnu.no/masteroppgaver/013/13216/masteroppgave.pdf), Section 3.2.2.2

3. Implement Rocchio’s Algorithm to compute new weight of words ( vector __q<sub>i\+1</sub>__). 

	<img src="https://github.com/petercanmakit/IR-system/blob/master/sup/formula.png" width="600">
	
   That is,
	
         new weight of a word =  Alpha * (original weight in vector qi) 
                                 + Beta * (tf-idf weight in relevant results) 
				 	  - Gamma * (tf-idf weight in non-relevant results)
				 
   In our project, we implement Alpha = 1.0, Beta = 0.75, Gamma = 0.15.
   
4. According to the new vector, choose two terms that have the heavies weight and are not in the old query. Sort two new terms and old query according to their weight. The order of keywords may vary during each iteration.

5. Start a new round until the desired precision is reached.
