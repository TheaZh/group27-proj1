<<<<<<< HEAD
### Query modification method

1. Use the Rocchio algorithm
 to compute new ___q<sub>i</sub>___ vector. The formula is slightly different from class lecture but the same as sikit-learn implementation in order to get better accuracy.
2. Sort all the terms by their new weights. Choose the heaviest two terms that are not in the old query ___q<sub>i-1</sub>___. Then use the new query to search on Google CSE.
3. Note that during each iteration, the order of original terms from previous iteration may vary, and here we use the new order to generate the new query.
=======
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
      ├── Doc.java
      ├── Filter.java
      ├── GoogleSearch
      ├── GoogleSearch.java
      ├── Makefile
      ├── Query.java
      ├── README.md
      ├── Rocchio.java
      ├── file.txt
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
      └── transcript.txt

Run
--------
1. Install dependencies
   
        $ make 

2. Run 

        $ ./GoogleSearch <google api key> <google engine id> <precision> <query>
         
   \<google api key> -- your Google Custom Search API Key
   
   \<google engine id> -- your Google Custom Search Engine ID
   
   \<precision> -- the target value for precision@10, a real number between 0 and 1
   
   \<query> -- your query
   
Keys
--------
1. Google Custom Search API Key

         AIzaSyARFSgO3Kiuu3IOtEL8UwdIbrS7SiB43qo
         
2. Google Custom Search Engine ID

         018258045116810257593:z1fmkqqt_di
   
>>>>>>> 58654b6ee5057fe4d8eaa49c7a4700a5d7d204ac
