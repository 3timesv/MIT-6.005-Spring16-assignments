### Commands to test problems

#### Problem 1

* Move to test directory : mkdir test
* _compile_ : javac -cp ".:/usr/share/java/junit4-4.12.jar:../lib/javax.json-1.0.jar:" ../src/twitter/*.java twitter/*.java
* _execute_ : java -ea -cp ".:/usr/share/java/junit4-4.12.jar:../lib/javax.json-1.0.jar:../src/:" org.junit.runner.JUnitCore twitter.ExtractTest


#### Problem 2

Follow the instructions above for problem 1, but replace 'ExtractTest' with 'FilterTest'
