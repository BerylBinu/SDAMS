package sdams.model;

/** A Student. Basic version so login works; the Student/Admin member can add fields. */
public class Student extends User {

    public Student(String id, String name, String username, String password,
                   String email, String phone) {
        super(id, name, username, password, email, phone);
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }
}
