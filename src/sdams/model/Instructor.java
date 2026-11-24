package sdams.model;

/** An Instructor. Basic version so login works; the Instructor/Admin member can add fields. */
public class Instructor extends User {

    public Instructor(String id, String name, String username, String password,
                      String email, String phone) {
        super(id, name, username, password, email, phone);
    }

    @Override
    public Role getRole() {
        return Role.INSTRUCTOR;
    }
}
