RE: We tried to improve the coding part of the project 

Question 2 Test the correctness of decision tree implemementation:

Testing the correctness of our dataset was done by using the accuracy method in the DataWrapperClass.
If the predictions on the arraylists are closer to the data provided we will have good accuracy.
This can be tuned to our need by changing either the data set or applying prunning techniques to the tree.

Question 4 How to improve Efficiency:

 If a tree is unpruned we can apply tree prunning to recursively remove splits
with the least improvement, while still keeping it a complete binary tree.
This method will also reduce the time complexity of calculating where to split each time
and thus will also improve on the accuracy of the predictions.Sometimes decision trees
have issues with overfitting(the problem of memorising the data they are given to
the point where the tree can't make "unbiased" predictions anymore, often called noise in data).
Prunning helps reduce that noise by cutting out leaves with small significance and reducing the tree
size overall. The total error will be smaller in a tree with smaller splits between nodes.
 Another way to help improve the tree efficiency overall is with a technique called bootstrapping.
In this method we create many random sub-samples, then "teach" the tree on each sample. After that we
calculate the prediction for each sub-sample and finally call on the average of all the predictions.
Again in this method we can reduce the data "noise". The key is that each new sub sample of the dataset is
created with replacement. This keeps the set uniform and random like the original. The machine will suffer
less from overfitting.
 Another way to improve the efficiency of the decision tree is by measuring the quality of the split by calculating the Entropy.
Entropy is the measure of uncertainty or randomness, the more random the dataset, the higher the entropy. 
We need to pick features/nodes that have less entropy to them. 
To check if our feature should be used as our decision node we can check the entropy before and after the split.
This is known as information gain.

Platforms:
 Code was compiled and executed in Windows O.S and Mac O.S.

Resources that helped us:
Data Structured and Algorithms in Java, M.T.Goodrich,R.Tamassia
case_study_decision_tree.pdf
https://www.geeksforgeeks.org/priority-queue-class-in-java-2/
https://www.homeandlearn.co.uk/java/read_a_textfile_in_java.html
https://www.baeldung.com/java-file-to-arraylist
https://medium.com/greyatom/decision-trees-a-simple-way-to-visualize-a-decision-dc506a403aeb
https://towardsdatascience.com/boosting-the-accuracy-of-your-machine-learning-models-f878d6a2d185
https://www.javagists.com/java-tree-data-structure
http://www.jmlr.org/papers/volume8/hickey07a/hickey07a.pdf
https://www.displayr.com/machine-learning-pruning-decision-trees/
https://towardsdatascience.com/decision-tree-and-random-forest-explained-8d20ddabc9dd
