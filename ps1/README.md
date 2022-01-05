## Commands to run tests

#### Assumptions

* ```junit4-4.12.jar``` is stored in the directory ```/usr/share/java/```

#### Problem 1

* **Step 1** Move to test directory : ```cd test```
* **Step 2** compile : ```javac -cp ".:/usr/share/java/junit4-4.12.jar:../lib/javax.json-1.0.jar:" ../src/twitter/*.java twitter/*.java```
* **Step 3** run : ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/javax.json-1.0.jar:../src/:" org.junit.runner.JUnitCore twitter.ExtractTest```


#### Problem 2

Repeat first two steps of problem 1
* **Step 3** run : ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/javax.json-1.0.jar:../src/:" org.junit.runner.JUnitCore twitter.FilterTest```


#### Problem 3

Repeat first two steps of problem 1
* **Step 3** run : ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/javax.json-1.0.jar:../src/:" org.junit.runner.JUnitCore twitter.SocialNetworkTest```

