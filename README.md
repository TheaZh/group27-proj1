# IR System
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
      ├── libs
      │   ├── google-api-client-1.22.0.jar
      │   ├── google-api-client-jackson2-1.22.0.jar
      │   ├── google-api-services-customsearch-v1-rev56-1.22.0.jar
      │   ├── google-http-client-1.22.0.jar
      │   ├── google-http-client-jackson2-1.22.0.jar
      │   ├── httpclient-4.0.1.jar
      │   └── jackson-core-2.1.3.jar
      ├── sup
      │   └── proj1-stopword.txt
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

        $ git clone https://github.com/petercanmakit/IR-system.git
        
2. Install dependencies

        $ cd ./IR-system
        $ make

3. Run

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

3. We combine each result’s title and summary as a String, and then store it as a Doc type which is a customized class. For each Doc, we compute its term frequency and tf-idf weight, which are stored in two HashMap.

4. Based on user’s feedback, we have two ArrayLists. One list stores relevant Docs, and the other store non-relevant Docs. According to these two list, we compute two HashMap. One stores word-weight pairs in relevant docs, and the other stores pairs in non-relevant docs.

5. We put the main computing part in a while loop, and the loop will only be broken when the desired precision is reached or the precision is 0. Therefore, if the precision is still below the desired precision, given the __q__ vector and two word-weight HashMap, we implement Rocchio’s Algorithm to get new query. The expanded query will be automatically searched in Google CSE and a new round starts.

Query modification method
--------
1. Use the Rocchio algorithm
 to compute new __q<sub>i</sub>__ vector. The formula is slightly different from class lecture but the same as sikit-learn implementation in order to get better accuracy.
2. Sort all the terms by their new weights. Choose the heaviest two terms that are not in the old query __q<sub>i\-1</sub>__. Then use the new query to search on Google CSE.
3. Note that during each iteration, the order of original terms from previous iteration may vary, and here we use the new order to generate the new query.
