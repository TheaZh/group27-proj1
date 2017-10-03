### Query modification method

1. Use the Rocchio algorithm
 to compute new ___q<sub>i</sub>___ vector. The formula is slightly different from class lecture but the same as sikit-learn implementation in order to get better accuracy.
2. Sort all the terms by their new weights. Choose the heaviest two terms that are not in the old query ___q<sub>i-1</sub>___. Then use the new query to search on Google CSE.
3. Note that during each iteration, the order of original terms from previous iteration may vary, and here we use the new order to generate the new query.
