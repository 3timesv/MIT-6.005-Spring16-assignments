## Commands to run tests

#### Assumptions

* ```junit4-4.12.jar``` is stored in the directory ```/usr/share/java/```

#### Problem 1/ 2/ 3/ 4

* **Step 1** Move to _test_ directory: ```cd test```
* **Step 2** compile: ```javac -cp ".:/usr/share/java/junit4-4.12.jar:../lib/antlr.jar:"  ../src/expressivo/*.java ../src/expressivo/parser/*.java expressivo/*.java```
* **Step 3** run _Expression.test_: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/antlr.jar:../src/:../src/parser/:" org.junit.runner.JUnitCore expressivo.ExpressionTest```
* **Step 4** run _Commands.test_: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/antlr.jar:../src/:../src/parser/:" org.junit.runner.JUnitCore expressivo.CommandsTest```


#### Console view [Optional]
* **Step 1** Move to _src_ directory: ```cd src``` 
* **Step 2** compile: ```javac -cp ".:/usr/share/java/junit4-4.12.jar:../lib/antlr.jar:" expressivo/parser/*.java expressivo/*.java```
* **Step 3** run Main: ```java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/antlr.jar:parser/:" expressivo.Main```
