package sdams.model;

/**
 * An Administrator. The Manager creates, updates and deletes these.
 * (Your Administrator teammate builds the Administrator's own menu.)
 */
public class Administrator extends User {

    public Administrator(String id, String name, String username, String password,
                         String email, String phone) {
        super(id, name, username, password, email, phone);
    }

    @Override
    public Role getRole() {
        return Role.ADMINISTRATOR;
    }
}
