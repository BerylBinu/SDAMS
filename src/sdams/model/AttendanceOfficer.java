package sdams.model;

/** An Attendance Officer. Basic version so login works; the Attendance member can add fields. */
public class AttendanceOfficer extends User {

    public AttendanceOfficer(String id, String name, String username, String password,
                             String email, String phone) {
        super(id, name, username, password, email, phone);
    }

    @Override
    public Role getRole() {
        return Role.ATTENDANCE_OFFICER;
    }
}
