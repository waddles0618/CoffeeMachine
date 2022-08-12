![java version](https://img.shields.io/badge/java-18-brightgreen)
# CoffeeMachine
Little project with console menu

### Example
![Example](Example.png)


### Build source
```shell
javac --source-path src/ -d bin src/CoffeeMachine.java
```

### Run
```shell
java --class-path ./bin CoffeeMachine
```

### Create jar
```shell
javac --source-path src/ -d bin src/CoffeeMachine.java
jar cefv CoffeeMachine  coffeeMachine.jar -C ./bin .
```

### Run jar
```shell
java -jar ./coffeMachine.jar
```