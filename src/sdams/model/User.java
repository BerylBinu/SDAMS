package sdams.model;

/**
 * Parent class of every role (Manager, Administrator, Instructor, ...).
 *
 * OOP concepts shown here:
 *  - Abstraction:   abstract class, you can never do "new User(...)".
 *  - Encapsulation: all fields private, accessed through getters/setters.
 *  - Inheritance:   every role extends User and reuses these fields.
 *  - Polymorphism:  getRole() is abstract, each subclass answers differently.
 */
public abstract class User {

    public static final String DELIMITER = "|";

    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;

    public User(String id, String name, String username, String password,
                String email, String phone) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    /** Each subclass must say which role it is. */
    public abstract Role getRole();

    /** Converts this object into one line of users.txt. */
    public String toFileLine() {
        return String.join(DELIMITER,
                id, name, username, password, email, phone, getRole().name());
    }

    // ---------- getters and setters ----------
    // There is no setId(): an ID never changes once created (rule 4).

    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + getRole() + ")";
    }
}
