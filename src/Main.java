public class Main {

    public static void main(String[] args) {
        System.out.println("*".repeat(30));
        Manager manager = new Manager();

        manager.addTask("Написать код", "Описание 1");
        manager.addTask("Проверить код", "Описание 2");

        printTaskName(manager);
    }

    static void printTaskName(Manager manager)
    {
        for (Task task : manager.getListTasks()) {
            System.out.println(task);
        }
    }
}
