## Commands to run tests

### Assumptions

* _junit4-4.12.jar_ is stored in the directory _/usr/share/java/_

### Problem 2.1

* **Step 1** Move to test directory: ```cd test```
* **Step 2** compile: ```javac -cp ".:/usr/share/java/junit4-4.12.jar:" ../src/graph/*.java graph/*.java```
* **Step 3** run: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore graph.ConcreteEdgesGraphTest```

### Problem 2.2

Repeat first two steps of problem 2.1
* **Step 3** run: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore graph.ConcreteVerticesGraphTest```


### Problem 3.1
Repeat first two steps of problem 2.1
* **Step 3** run:
    * ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore graph.ConcreteEdgesGraphTest```
    * ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore graph.ConcreteVerticesGraphTest```

### Problem 3.2
Repeat first two steps of problem 2.1
* **Step 3** run: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore graph.GraphStaticTest```

### Problem 4

Repeat first step of problem 2.1
* **Step 2** compile: ```javac -cp ".:/usr/share/java/junit4-4.12.jar:" ../src/graph/*.java ../src/poet/*.java graph/*.java poet/*.java```
* **Step 3** run: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore poet.GraphPoetTest
