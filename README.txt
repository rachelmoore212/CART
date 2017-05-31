In order to run the code you should be able to execute the following command:

$ java -jar out/artifacts/CART_jar/CART.jar credit 10

This should tell you that the code is working, here is a breakdown of the parameters:

Parameter 1: dataset, there are 3 included datasets that we classified
-"credit" - classifes credit historys by whether they defaulted
-"mushroom" - classfies mushrooms by whether they are poisionsous or not
-"cander" - classifies benign and malignant tumors

Parameter 2: min_leaf_size, a parameter from CART, tells the algorithm when to stop classifying a node
(expects an integer >0)

Pruning: The next two optional parameters have to do with pruning
-CrossValidation: variables look like this

credit 10 crossValidation 0.3
(the 0.3 is a factor of how much of the training data is used for crossvalidatio )

-pessimistic: parameters look like this

credit 10 pessimistic 1.95
(the 1.95 corresponds to the z value for pessimistic pruning)

Optional Parameters 3: Random Forest
-not exclusive to pruning techniques, a simplified random forest approach was implemented

ex) credit 10 randomForest 100
(where 100 corresponds to the number of trees)


