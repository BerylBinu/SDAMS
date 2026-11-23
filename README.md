SDAMS – Self Defence Academy Management System
CT038-3-2-OODJ group assignment. Java Swing desktop app, NetBeans (Ant), text files for storage. Deadline: Sunday 06/12/2026, 11:59 PM (Week 12)

1. Who does what
Role	Member	Status
Manager	Beryl	Foundation done, features in progress
Administrator	Krishna	
Instructor  Lanim	
Student Manuel		
Facilities Officer  Omar		
Attendance Officer  Attendence		
Login screen Beryl		
5 members, 6 roles → one person takes two roles. Fill this in together.

2. Getting started
Get the project (clone the repo or unzip the shared folder).
NetBeans → File → Open Project → select the SDAMS folder.
Run sdams.Step1Test, Step2Test and Step3Test (right-click → Run File) to check everything works.
The data folder must stay in the project root (next to src), not inside src.

3. Project structure
SDAMS/
├── data/                     ← all .txt data files
│   └── users.txt
└── src/sdams/
    ├── model/                ← data classes (User and every role, Booking, Payment...)
    ├── util/                 ← shared helpers (FileManager, Validator)
    ├── service/              ← logic for each role (e.g. AdminService)
    └── gui/                  ← Swing JFrame forms, one sub-package per role
        ├── manager/
        ├── admin/
        ├── instructor/
        ├── student/
        ├── facilities/
        └── attendance/
Put your screens in your gui sub-package. This avoids two people editing the same file.

4. Shared foundation — use these, don't rewrite them
Class	Package	What it's for
User	sdams.model	Abstract parent of every role. Holds id, name, username, password, email, phone.
Role	sdams.model	Enum of the 6 roles, with ID prefix and display name.
Manager, Administrator	sdams.model	Example subclasses of User.
FileManager	sdams.util	Read, write, append lines and generate IDs for any .txt file.
Validator	sdams.util	Input checks (name, username, password, email, phone, date, amount).
Do not change a shared class without telling the group first. Everyone depends on them.

Adding your role's class
Copy Administrator.java, rename it, and return your role:

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
You can add extra fields (e.g. discipline for Instructor), but keep the first 7 fields in users.txt the same for everyone.

Using FileManager
// Read
ArrayList<String> lines = FileManager.readLines("classes.txt");
for (String line : lines) {
    String[] f = line.split("\\|");   // f[0] = id, f[1] = ..., etc.
}

// Add
String id = FileManager.generateId("classes.txt", "C");
FileManager.appendLine("classes.txt", id + "|Karate Basics|KARATE|...");

// Update / delete: read all → change the ArrayList → write all back
FileManager.writeLines("classes.txt", lines);
Using Validator in a form
String error = Validator.validateUser(name, username, password, email, phone);
if (error != null) {
    JOptionPane.showMessageDialog(this, error);
    return;
}
if (Validator.containsDelimiter(descriptionField.getText())) {
    JOptionPane.showMessageDialog(this, "The | character is not allowed.");
    return;
}
5. Text file rules (everyone must follow)
One record per line, fields in a fixed order.
| is the delimiter. Block users from typing | (Validator.containsDelimiter).
Dates are yyyy-MM-dd, times are HH:mm.
Link records by ID, never by name (store studentId, not "Ali").
Each record type has its own ID prefix. New ID = highest number + 1 (FileManager.generateId).
Status values are UPPERCASE (OPEN, CANCELLED, PRESENT).
6. Data files
users.txt (agreed)
All users in one file, so login and the staff report can read one place.

id|name|username|password|email|phone|role
A0001|Ali bin Abu|ali01|pass123|ali@sda.com|0123456789|ADMINISTRATOR
Role	ID prefix	role value
Manager	M	MANAGER
Administrator	A	ADMINISTRATOR
Instructor	I	INSTRUCTOR
Student	S	STUDENT
Facilities Officer	F	FACILITIES_OFFICER
Attendance Officer	T	ATTENDANCE_OFFICER
Other files (PROPOSED – the owner confirms or changes before coding)
File	Owner	Prefix	Fields
classes.txt	Instructor	C	classId|className|discipline|instructorId|date|startTime|endTime|capacity|fee|status (OPEN / CANCELLED / COMPLETED)
bookings.txt	Student	B	bookingId|studentId|classId|bookingDate|status (BOOKED / CANCELLED)
payments.txt	Student	P	paymentId|bookingId|studentId|amount|paymentDate|status (PAID / REFUNDED)
ratings.txt	Student	R	ratingId|studentId|instructorId|classId|score(1-5)|comment|ratingDate
attendance.txt	Attendance Officer	AT	attendanceId|classId|studentId|status|recordedBy|recordDate (PRESENT / ABSENT / LATE)
equipment.txt	Facilities Officer	E	equipmentId|name|category|quantity|condition|location
problems.txt	Instructor creates, Facilities updates	FP	problemId|instructorId|title|description|location|reportDate|status|remarks (OPEN / IN_PROGRESS / RESOLVED)
Who reads whose files:

Manager revenue report reads payments.txt and bookings.txt (needs amount, paymentDate, status).
Manager staff report reads users.txt.
Student booking reads classes.txt.
Attendance Officer reads classes.txt and bookings.txt.
Student rating should only be allowed after attendance.txt has a record for that class.
If you change a field order, update this table and tell the group. Other people's code splits your lines by position.

7. The two workflows (from the brief)
Class: Instructor creates class → Student books and pays → Manager sees payment in revenue report → Attendance Officer takes attendance → Student rates Instructor.
Facility: Instructor reports problem → Facilities Officer updates it until RESOLVED.
Test these end to end before submission.

8. Team rules
Each role must only see its own menu after login.
Every input must be validated (requirement 6.0).
Java + Swing only, text files only, no database or other libraries (requirement 9.0).
Delete the StepXTest files before submission.
Write down any assumptions you make. They go in the report's "Introduction and assumptions" section.
