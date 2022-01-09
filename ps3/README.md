## Commands to run tests

#### Assumptions

* ```junit4-4.12.jar``` is stored in the directory ```/usr/share/java/```

#### Problem 3.1

* **Step 1** Move to test directory: ```cd test```
* **Step 2** compile:  ```javac -cp ".:/usr/share/java/junit4-4.12.jar:" ../src/expressivo/*.java expressivo/*.java```
* **Step 3** run: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../src/:" org.junit.runner.JUnitCore expressivo.ExpressionTest```