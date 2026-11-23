package sdams.model;

/** The Manager. Inherits every field from User. */
public class Manager extends User {

    public Manager(String id, String name, String username, String password,
                   String email, String phone) {
        super(id, name, username, password, email, phone);
    }

    @Override
    public Role getRole() {
        return Role.MANAGER;
    }
}
