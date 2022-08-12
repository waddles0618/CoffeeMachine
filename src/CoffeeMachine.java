import java.util.Scanner;
import machine.Machine;

public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Machine coffeeMachine = new Machine(400, 540, 120, 9, 550);

        while (coffeeMachine.isRun()) {
            String action = scanner.next();
            coffeeMachine.actionHandler(action);
        }
    }
}