package sdams.model;

/** A Facilities Officer. Basic version so login works; the Facilities member can add fields. */
public class FacilitiesOfficer extends User {

    public FacilitiesOfficer(String id, String name, String username, String password,
                             String email, String phone) {
        super(id, name, username, password, email, phone);
    }

    @Override
    public Role getRole() {
        return Role.FACILITIES_OFFICER;
    }
}
