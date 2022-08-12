package machine;

public class Machine {
    private boolean isRun;
    private ListMenu currentMenu;
    private FillMenuAction currentFill;
    private String mainMenuText;
    private String buyMenuText;
    private int water;
    private int milk;
    private int coffeeBeans;
    private int disposableCups;
    private int money;

    enum ListMenu {
        MAIN_MENU, BUY, FILL
    }

    enum MainMenuAction {
        BUY, FILL, TAKE, REMAINING, EXIT;

        public String nameToLowerCase() {
            return this.name().toLowerCase();
        }
    }

    enum BuyMenuAction {
        ESPRESSO("1", "espresso"), LATTE("2", "latte"),
        CAPPUCCINO("3", "cappuccino"), BACK("back", "to main menu");
        final String value;
        final String describeForCoffeeMenu;

        BuyMenuAction(String value, String describe) {
            this.value = value;
            this.describeForCoffeeMenu = describe;
        }

        public String getValue() {
            return this.value;
        }

        public String getDescribeForCoffeeMenu() {
            return this.describeForCoffeeMenu;
        }

        public static BuyMenuAction searchActionByValue(String action) throws IllegalArgumentException {
            for (BuyMenuAction item : BuyMenuAction.values()) {
                if (item.getValue().equals(action)) {
                    return item;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    enum FillMenuAction {
        WATTER, MILK, COFFEE_BEANS, DISPOSABLE_CUPS
    }

    public Machine(int water, int milk, int coffeeBeans, int disposableCups, int money) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.disposableCups = disposableCups;
        this.money = money;

        this.createMainMenu();
        this.createBuyMenu();

        this.isRun = true;
        this.currentFill = FillMenuAction.WATTER;
        this.resetMenuToMain(false);
    }

    public boolean isRun() {
        return this.isRun;
    }

    public void actionHandler(String action) {
        try {
            switch (this.currentMenu) {
                case MAIN_MENU -> {
                    MainMenuAction actionMainMenu = MainMenuAction.valueOf(action.toUpperCase());
                    this.actionMainMenu(actionMainMenu);
                }
                case BUY -> {
                    BuyMenuAction actionBuyMenu = BuyMenuAction.searchActionByValue(action);
                    this.actionBuyMenu(actionBuyMenu);
                }
                case FILL -> {
                    int batchResource = Integer.parseInt(action);
                    this.actionFillMenu(batchResource);
                }
            }
        } catch (IllegalArgumentException e) {
            this.resetMenuToMain(true);
        }
    }

    private void actionMainMenu(MainMenuAction action) {
        this.putIndent();

        switch (action) {
            case BUY -> {
                this.currentMenu = ListMenu.BUY;
                showBuyMenu();
            }
            case FILL -> {
                this.currentMenu = ListMenu.FILL;
                this.showFillMenu();
            }
            case TAKE -> {
                this.takeMoney();
                this.resetMenuToMain(true);
            }
            case REMAINING -> {
                this.showRemaining();
                this.resetMenuToMain(true);
            }
            case EXIT -> this.isRun = false;
        }
    }

    private void actionBuyMenu(BuyMenuAction action) {
        boolean isEnoughResourcesForCoffee;
        boolean isMakeCoffee = true;

        int needWater = 0;
        int needMilk = 0;
        int needCoffeeBeans = 0;
        int cost = 0;

        switch (action) {
            case ESPRESSO -> {
                needWater = 250;
                needCoffeeBeans = 16;
                cost = 4;
            }
            case LATTE -> {
                needWater = 350;
                needMilk = 75;
                needCoffeeBeans = 20;
                cost = 7;
            }
            case CAPPUCCINO -> {
                needWater = 200;
                needMilk = 100;
                needCoffeeBeans = 12;
                cost = 6;
            }
            case BACK -> isMakeCoffee = false;
        }

        if (isMakeCoffee) {
            isEnoughResourcesForCoffee = checkEnoughResourcesForCoffee(needWater, needMilk, needCoffeeBeans);

            if (isEnoughResourcesForCoffee) {
                makeCoffee(needWater, needMilk, needCoffeeBeans, cost);
            }
        }

        this.resetMenuToMain(true);
    }

    private void actionFillMenu(int batchResource) {
        this.fillResource(batchResource);
        this.nextFillResource();

        if (this.currentFill.equals(FillMenuAction.WATTER)) {
            this.resetMenuToMain(true);
        } else {
            this.showFillMenu();
        }
    }

    private void resetMenuToMain(boolean isTopIndent) {
        if (isTopIndent) {
            this.putIndent();
        }
        this.currentMenu = ListMenu.MAIN_MENU;
        showMainMenu();
    }

    private void makeCoffee(int water, int milk, int coffeeBeans, int cost) {
        this.money += cost;
        this.disposableCups--;
        this.water -= water;
        this.milk -= milk;
        this.coffeeBeans -= coffeeBeans;
    }

    private boolean checkEnoughResourcesForCoffee(int water, int milk, int coffeeBeans) {
        boolean result = false;
        String nameResourceNotEnough = null;
        if (this.water < water) {
            nameResourceNotEnough = "water";
        } else if (this.milk < milk) {
            nameResourceNotEnough = "milk";
        } else if (this.coffeeBeans < coffeeBeans) {
            nameResourceNotEnough = "coffee beans";
        } else if (this.disposableCups == 0) {
            nameResourceNotEnough = "disposable cups";
        } else {
            result = true;
        }

        if (result) {
            System.out.printf("I have enough resources, making you a coffee!%n");
        } else {
            System.out.printf("Sorry, not enough %s!%n", nameResourceNotEnough);
        }

        return result;
    }

    private void createMainMenu() {
        StringBuilder listMenu = new StringBuilder();

        for (MainMenuAction action : MainMenuAction.values()) {
            listMenu.append(action.nameToLowerCase()).append(", ");
        }
        listMenu.setLength(listMenu.length() - 2);

        this.mainMenuText = ("Write action (" + listMenu + "):%n");
    }

    private void createBuyMenu() {
        StringBuilder listMenu = new StringBuilder();

        for (BuyMenuAction action : BuyMenuAction.values()) {
            listMenu.append(action.getValue())
                    .append(" - ")
                    .append(action.getDescribeForCoffeeMenu())
                    .append(", ");
        }
        listMenu.setLength(listMenu.length() - 2);

        this.buyMenuText = ("What do you want to buy? " + listMenu + ":%n");
    }

    private void showMainMenu() {
        System.out.printf(this.mainMenuText);
    }
    private void showBuyMenu() {
        System.out.printf(this.buyMenuText);
    }
    private void showFillMenu() {
        switch (this.currentFill) {
            case WATTER -> System.out.println("Write how many ml of water you want to add:");
            case MILK -> System.out.println("Write how many ml of milk you want to add:");
            case COFFEE_BEANS -> System.out.println("Write how many grams of coffee beans you want to add:");
            case DISPOSABLE_CUPS -> System.out.println("Write how many disposable cups of coffee you want to add:");
        }
    }
    private void showRemaining() {
        System.out.printf("The coffee machine has:%n");
        System.out.printf("%d ml of water%n", this.water);
        System.out.printf("%d ml of milk%n", this.milk);
        System.out.printf("%d g of coffee beans%n", this.coffeeBeans);
        System.out.printf("%d disposable cups%n", this.disposableCups);
        System.out.printf("$%d of money%n", this.money);
    }
    private void putIndent() {
        System.out.printf("%n");
    }
    private void fillResource(int batchResource) {
        switch (this.currentFill) {
            case WATTER -> this.water += batchResource;
            case MILK -> this.milk += batchResource;
            case COFFEE_BEANS -> this.coffeeBeans += batchResource;
            case DISPOSABLE_CUPS -> this.disposableCups += batchResource;
        }
    }

    private void nextFillResource() {
        switch (this.currentFill) {
            case WATTER -> this.currentFill = FillMenuAction.MILK;
            case MILK -> this.currentFill = FillMenuAction.COFFEE_BEANS;
            case COFFEE_BEANS -> this.currentFill = FillMenuAction.DISPOSABLE_CUPS;
            case DISPOSABLE_CUPS -> this.currentFill = FillMenuAction.WATTER;
        }
    }

    private void takeMoney() {
        System.out.printf("I gave you $%d%n", this.money);
        this.money = 0;
    }

}