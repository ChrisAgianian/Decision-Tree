import java.util.*;

public class DecisionTreeClass {
	private class DecisionTreeNode implements Comparable<DecisionTreeNode> {
		public ArrayList<Integer> data_list; // list of data IDs
		public int opt_fea_type = -1; // 0 if continuous, 1 if categorical
		public int opt_fea_id = -1; // the index of the optimal feature
		public double opt_fea_thd = Double.NEGATIVE_INFINITY; // the optimal splitting threshold
																// for continuous feature
		public int opt_improvement = Integer.MIN_VALUE; // the improvement if split based on the optimal feature
		public boolean is_leaf = true; // is it a leaf
		public int majority_class = -1; // class prediction based on majority vote
		public int num_accurate = -1; // number of accurate data using majority_class
		public DecisionTreeNode parent = null; // parent node
		public ArrayList<DecisionTreeNode> children = null; // list of children when split

		public DecisionTreeNode(ArrayList<Integer> d_list, int m_class, int n_acc) {
			data_list = new ArrayList<Integer>(d_list);
			majority_class = m_class;
			num_accurate = n_acc;
		}

		// compare improvements of Nodes
		public int compareTo(DecisionTreeNode node) {
			if (this.equals(node)) {
				return 0;
			} else if (this.opt_improvement > node.opt_improvement) {
				return -1;
			} else
				return 1;
		}
	}

	public DataWrapperClass train_data;
	public int max_height;
	public int max_num_leaves;
	public int height;
	public int num_leaves;
	public DecisionTreeNode root;

	// constructor, build the decision tree using train_data, max_height and
	// max_num_leaves
	public DecisionTreeClass(DataWrapperClass t_d, int m_h, int m_n_l) {
		train_data = t_d;
		max_height = m_h;
		max_num_leaves = m_n_l;
		// FILL IN
		//
		// create the root node, use all data_ids [0..N-1]
		// find the majority class, also how many accurate using the majority class
		// if the majority class is correct for all data,
		// no improvement possible
		// the optimal accuracy improvement = 0
		// else
		// find the optimal feature to split
		// for each feature
		// if categorical
		// split the data_list into sub-lists using different values of the feature
		// for each sub-list
		// find the majority class
		// compute the number of accurate prediction using this majority class
		// sum up number of accurate predictions for all sub-lists as the score
		// if continuous
		// sort the data based on the continuous feature
		// find the optimal threshold to split the data_list into two sub-lists
		// for each of sub-list
		// find the majority class
		// compute the number of accurate prediction using this majority class
		// sum up number of accurate predictions for all sub-lists as the score
		// find the feature with the largest score (best total num of accurate
		// prediction after splitting)
		// optimal accuracy improvement = the difference between the best total num of
		// accurate prediction after splitting
		// and the number of accurate prediction using the majority class of the current
		// node
		// put the root node and the optimal accuracy improvement into a max-heap

		// while the heap is not empty
		// extract the maximum entry (the leaf node with the maximal optimal accuracy
		// improvement) from the heap
		// if the optimal accuracy improvement is zero (no improvement possible)
		// break;
		// else
		// split the node
		// create children based on the optimal feature and split (each sub-list creates
		// one child)
		// for each child node
		// find its optimal accuracy improvement (and the optimal feature) (as you do
		// for the root)
		// put the node into the max-heap
		// if the number of leaves > max_num_leaves
		// break;
		// if the height > max_height
		// break;

	ArrayList<Integer> list = new ArrayList<>();
	for(int i = 0;i<train_data.labels.size();i++)
	{
		list.add(i);
	}root=new DecisionTreeNode(list,-1,-1);System.out.println("\n"+"Processing Data");

	setNode(root);

		// Tree using a maxHeap PQ to arrange the structure
		PriorityQueue<DecisionTreeNode> maxHeap = new PriorityQueue<>();
		maxHeap.add(root);
		System.out.println("Decision Tree Root Is maxHeap Top");

		while (!maxHeap.isEmpty()) {
			DecisionTreeNode currentNode = maxHeap.poll();
			currentNode.children = new ArrayList<DecisionTreeNode>();
			if (currentNode.opt_improvement == 0) {
				break;
			} else {
				if (currentNode.opt_fea_type == 1) {
					// split node data based on optimal feature type
					ArrayList<Integer> feature = train_data.categorical_features.get(currentNode.opt_fea_id);
					ArrayList<Integer> classType = featureTypes(feature);
					ArrayList<ArrayList<Integer>> subLists = subLists_catfea(feature, currentNode.data_list,
							classType);
					// for each sublist make the child nodes
					for (ArrayList<Integer> subList : subLists) {
						currentNode.is_leaf = false;
						DecisionTreeNode child = new DecisionTreeNode(subList, -1, -1);
						currentNode.children.add(child);
						child.parent = currentNode;
						setNode(child);
						maxHeap.add(child);
					}
				} else {
					// split noda data based on the threshold
					ArrayList<Double> feature = train_data.continuous_features.get(currentNode.opt_fea_id);
					double threshold = currentNode.opt_fea_thd;
					ArrayList<ArrayList<Integer>> subLists = subLists_confea(threshold, currentNode.data_list,
							feature);
					for (ArrayList<Integer> subList : subLists) {
						currentNode.is_leaf = false;
						DecisionTreeNode child = new DecisionTreeNode(subList, -1, -1);
						currentNode.children.add(child);
						child.parent = currentNode;
						setNode(child);
						maxHeap.add(child);
					}
				}
			}
			// if the number of leaves > max_num_leaves
			// break;
			if (maxHeap.size() > max_num_leaves)
				break;
			// if the height > max_height
			// break;
			if (getHeight(currentNode) > max_height)
				break;

		}
		System.out.println("Decision Tree Built.....");
	}

	public ArrayList<Integer> predict(DataWrapperClass test_data) {
		// for each data in the test_data
		// starting from the root,
		// at each node, go to the right child based on the splitting feature
		// continue until a leaf is reached
		// assign the label to the data based on the majority-class of the leaf node
		// return the list of predicted label
		ArrayList<Integer> labels = new ArrayList<>();
		int size_data = test_data.num_data;
		DecisionTreeNode node;
		DecisionTreeNode child;
		int index = 0;
		while (index < size_data) {
			node = root;
			while (node.children != null) {
				if (node.opt_fea_type == 1) {
					// go to child based on split feature
					int value = test_data.categorical_features.get(node.opt_fea_id).get(index);
					// until a leaf is reached
					child = node.children.get(value);
					if (child.is_leaf) {
						labels.add(child.majority_class);
						break;
					} else {
						node = child;
					}
				} else {
					double threshold = node.opt_fea_thd;
					// go to child based on split with thd continuous
					double value = test_data.continuous_features.get(node.opt_fea_id).get(index);
					// choose a child
					if (value < threshold)
						child = node.children.get(0);
					else
						child = node.children.get(1);
					//until a leaf is reached
					if (child.is_leaf) {
						labels.add(node.majority_class);
						break;
					} else
						node = child;
				}
			}
			index++;
		}
		return labels;
	}
	
	//method for height of tree
		private int getHeight(DecisionTreeNode node) {
			DecisionTreeNode current = node;
			int counter = 0;
			while (current.parent != null) {
				counter++;
				current = current.parent;
			}
			return counter;
		}

	//method to form the Decision Tree Nodes
	private void setNode(DecisionTreeNode node) {
		// majority class and the accuracy of node
		setMajority(node);
		
		ArrayList<Integer> majClass = featureTypes(train_data.labels);
		if (node.num_accurate == node.data_list.size()) {
			node.opt_improvement = 0;
		} else {
			int accuracy = Integer.MIN_VALUE;
			if (train_data.categorical_features != null) {
				int sum;
				for (ArrayList<Integer> feature : train_data.categorical_features) {
					node.opt_fea_type = 1;
					// split based on feature type
					ArrayList<Integer> feaTypes = featureTypes(feature);
					ArrayList<ArrayList<Integer>> idList = subLists_catfea(feature, node.data_list, feaTypes);
					// sum up the # of accurate predictions
					sum = majorityOf(idList, train_data.labels, majClass);
					//feature with largest score is the max of acc and sum
					accuracy = Math.max(accuracy, sum);
					// get best feature to split
					if (accuracy == sum) {
						node.opt_fea_id = train_data.categorical_features.indexOf(feature);
					}
				}
			} else {
				node.opt_fea_type = 0;
				double bestThreshold;
				for (ArrayList<Double> feature : train_data.continuous_features) {
					// follow pattern for continuous types
					ArrayList<Double> newFeature = newList(node.data_list, feature);
					// then sorting feature and find out threshold
					ArrayList<Double> sortedfeaTypes = new ArrayList<>(newFeature);
					sort(sortedfeaTypes);
					int majorityVote = 0;
					int bestVote = Integer.MIN_VALUE;
					bestThreshold = Double.MIN_VALUE;
					for (int i = 0; i < sortedfeaTypes.size() - 1; i++) {
						// after sort, check every threshold and find the best
						ArrayList<ArrayList<Integer>> idList = labelID(sortedfeaTypes, feature, node.data_list, i);
						majorityVote = majorityOf(idList, train_data.labels, majClass);
						// get the best vote over all thresholds in one feature
						bestVote = Math.max(bestVote, majorityVote);
						if (bestVote == majorityVote) {
							// set the best feature based on continuous
							bestThreshold = sortedfeaTypes.get(i) + 0.000001;
						}
					}
					// get the best Accuracy over all features
					accuracy = Math.max(accuracy, bestVote);
					if (accuracy == bestVote) {
						node.opt_fea_id = train_data.continuous_features.indexOf(feature);
						node.opt_fea_thd = bestThreshold;
					}
				}
			}
			//optimal accuracy improvement = the difference between the best total num of accurate prediction after splitting
			//								and the number of accurate prediction using the majority class of the current node
			node.opt_improvement = accuracy - node.num_accurate;
		}
	}

	// get type of feature CAT or Continuous
	private ArrayList<Integer> featureTypes(ArrayList<Integer> list) {
		ArrayList<Integer> featype = new ArrayList<>();
		for (int element : list) {
			if (!featype.contains(element)) {
				featype.add(element);
			}
		}
		// sort the data based on feature
		Collections.sort(featype);
		return featype;
	}

	// set the majority class and the accuracy from label
	private void setMajority(DecisionTreeNode node) {
		ArrayList<Integer> listInlabel = new ArrayList<>();
		for (Integer i : node.data_list) {
			listInlabel.add(train_data.labels.get(i));
		}
		ArrayList<Integer> majClass = featureTypes(listInlabel);
		int count, maxClass = Integer.MIN_VALUE;
		for (Integer type : majClass) {
			count = 0;
			for (Integer e : listInlabel) {
				if (e == type) {
					count++;
				}
			}
			maxClass = Math.max(maxClass, count);
			if (maxClass == count) {
				node.majority_class = type;
				node.num_accurate = count;
			}
		}
	}

	// compute majority of lists with features split
	private int majorityOf(ArrayList<ArrayList<Integer>> idLists, ArrayList<Integer> label,
			ArrayList<Integer> classType) {
		int sum = 0;
		int count;
		int max;
		for (ArrayList<Integer> list : idLists) {
			max = Integer.MIN_VALUE;
			for (int type : classType) {
				count = 0;
				for (int id : list) {
					if (label.get(id) == type)
						count++;
				}
				max = Math.max(max, count);
			}
			sum += max;
		}
		return sum;
	}

	//get label id for continuous
	private ArrayList<ArrayList<Integer>> labelID(ArrayList<Double> sortedFeature, ArrayList<Double> feature,
			ArrayList<Integer> dataList, int pThr) {
		ArrayList<ArrayList<Double>> subList = new ArrayList<>();
		ArrayList<Double> dlist1 = new ArrayList<>();
		ArrayList<Double> dlist2 = new ArrayList<>();
		double breakPoint = sortedFeature.get(pThr) + 0.000001;
		// split sorted feature to two sublists by threshold
		for (double i : sortedFeature) {
			if (i < breakPoint) {
				dlist1.add(i);
			} else {
				dlist2.add(i);
			}
		}
		subList.add(dlist1);
		subList.add(dlist2);
		ArrayList<ArrayList<Integer>> idLists = new ArrayList<>();
		for (ArrayList<Double> list : subList) {
			int i = 0, j = 0;
			ArrayList<Integer> idList = new ArrayList<>();
			while (i < list.size()) {
				// take one value from sorted sublist and check the value through original
				
				if (list.get(i).equals(feature.get(dataList.get(j)))) {
					idList.add(dataList.get(j));
					i++;
				}
				if (j == dataList.size() - 1) {
					j = -1;
				}
				j++;
			}
			idLists.add(idList);
		}
		return idLists;
	}

	// sort for continuous feature using PQ
	private void sort(ArrayList<Double> feature) {
		PriorityQueue<Double> temp = new PriorityQueue<>();
		for (Double value : feature) {
			temp.add(value);
		}
		feature.clear();
		while (!temp.isEmpty()) {
			feature.add(temp.poll());
		}
	}

	// get values from feature by using id
	private ArrayList<Double> newList(ArrayList<Integer> idList, ArrayList<Double> feature) {
		ArrayList<Double> newList = new ArrayList<>();
		for (int i : idList) {
			newList.add(feature.get(i));
		}
		return newList;
	}

	// split continuous feature on threshold
	private ArrayList<ArrayList<Integer>> subLists_confea(double pThre, ArrayList<Integer> dataList,
			ArrayList<Double> unsortFeature) {
		ArrayList<ArrayList<Integer>> idList = new ArrayList<>();
		ArrayList<Integer> list1 = new ArrayList<>();
		ArrayList<Integer> list2 = new ArrayList<>();
		for (int i = 0; i < dataList.size(); i++) {
			if (unsortFeature.get(i) <= pThre) {
				list1.add(i);
			} else {
				list2.add(i);
			}
		}
		idList.add(list1);
		idList.add(list2);
		return idList;
	}

	// cat feature
	// split categorical feature
	private ArrayList<ArrayList<Integer>> subLists_catfea(ArrayList<Integer> feature, ArrayList<Integer> dataList,
			ArrayList<Integer> typesInFeature) {
		ArrayList<ArrayList<Integer>> idLists = new ArrayList<>();
		for (int type : typesInFeature) {
			ArrayList<Integer> idList = new ArrayList<>();
			for (int id : dataList) {
				if (feature.get(id) == type) {
					idList.add(id);
				}
			}
			idLists.add(idList);
		}
		return idLists;
	}

}