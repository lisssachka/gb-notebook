package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

import java.util.*;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {

        this.userController = userController;
    }

    public void run() {
        Commands com;

        while (true) {
            String command = prompt("Ââåäèòå êîìàíäó: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE:
                    User userCreate = createUser();
                    userController.saveUser(userCreate);
                    break;
                case READ:
                    String id = prompt("Èäåíòèôèêàòîð ïîëüçîâàòåëÿ: ");
                    try {
                        User userRead = userController.readUser(Long.parseLong(id));
                        System.out.println(userRead);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UPDATE:
                    String userId = prompt("Ââåäèòå id ïîëüçîâàòåëÿ: ");
                    userController.updateUser(userId, createUser());
                    break;
                case DELETE:
                    String userDeleteId = prompt("Ââåäèòå id ïîëüçîâàòåëÿ: ");
                    userController.deleteUser(userDeleteId);
                    break;

            }

        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }

    private User createUser() {
        String firstName = prompt("Имяe: ");
        String lastName = prompt("Фамилия: ");
        String phone = prompt("Номер телефона: ");
        return new User(firstName, lastName, phone);
    }
}
