package com.example.Baja.Entity;



import jakarta.persistence.*;



import java.util.ArrayList;

import java.util.List;



import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity

@Table(name = "user_details")

public class UserDetailsEntity {



@Id

@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;



private String name;

private String email;

private String rollNumber;

private String officialEmail;

private String department;

private String parentsName;

private String bloodGroup;

private String studentMobile;

private String parentMobile;


@Column(length = 1000)

private String address;



// Store subsystems as comma separated string

private String subsystems;



private String role;



// Store photos as byte arrays

@Lob

private byte[] photo;



@Lob

private byte[] idCardPhoto;

@OneToOne
@JoinColumn(name = "signup_id", unique = true)
private SignupEntity signup;

// ✅ Getters and Setters
public SignupEntity getSignup() {
    return signup;
}

public void setSignup(SignupEntity signup) {
    this.signup = signup;
}

public Long getId() {

return id;

}



public void setId(Long id) {

this.id = id;

}



public String getName() {

return name;

}



public void setName(String name) {

this.name = name;

}



public String getEmail() {

return email;

}



public void setEmail(String email) {

this.email = email;

}



public String getRollNumber() {

return rollNumber;

}



public void setRollNumber(String rollNumber) {

this.rollNumber = rollNumber;

}



public String getOfficialEmail() {

return officialEmail;

}



public void setOfficialEmail(String officialEmail) {

this.officialEmail = officialEmail;

}



public String getDepartment() {

return department;

}



public void setDepartment(String department) {

this.department = department;

}



public String getParentsName() {

return parentsName;

}



public void setParentsName(String parentsName) {

this.parentsName = parentsName;

}



public String getBloodGroup() {

return bloodGroup;

}



public void setBloodGroup(String bloodGroup) {

this.bloodGroup = bloodGroup;

}



public String getStudentMobile() {

return studentMobile;

}



public void setStudentMobile(String studentMobile) {

this.studentMobile = studentMobile;

}



public String getParentMobile() {

return parentMobile;

}



public void setParentMobile(String parentMobile) {

this.parentMobile = parentMobile;

}



public String getAddress() {

return address;

}



public void setAddress(String address) {

this.address = address;

}



public String getSubsystems() {

return subsystems;

}



public void setSubsystems(String subsystems) {

this.subsystems = subsystems;

}



public String getRole() {

return role;

}



public void setRole(String role) {

this.role = role;

}



public byte[] getPhoto() {

return photo;

}



public void setPhoto(byte[] photo) {

this.photo = photo;

}



public byte[] getIdCardPhoto() {

return idCardPhoto;

}



public void setIdCardPhoto(byte[] idCardPhoto) {

this.idCardPhoto = idCardPhoto;

}



// --- Ensure @JsonManagedReference is on all @OneToMany collections ---

@JsonManagedReference

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

private List<WorkEntity> works = new ArrayList<>();


@Column(length = 1000) // adjust length as needed

private String work;



public String getWork() {

return work;

}



public void setWork(String work) {

this.work = work;

}



@JsonManagedReference

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

private List<CommonWorkEntity> commonWorks = new ArrayList<>();



// Getters and Setters for commonWorks

public List<CommonWorkEntity> getCommonWorks() {

return commonWorks;

}



public void setCommonWorks(List<CommonWorkEntity> commonWorks) {

this.commonWorks = commonWorks;

}



// Convenience methods for managing commonWorks (optional but recommended)

public void addCommonWork(CommonWorkEntity commonWork) {

commonWorks.add(commonWork);

commonWork.setUser(this); // Ensure bidirectional link

}



public void removeCommonWork(CommonWorkEntity commonWork) {

commonWorks.remove(commonWork);

commonWork.setUser(null); // Break bidirectional link

}



@JsonManagedReference

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

private List<AttendanceEntity> attendances = new ArrayList<>();



// Getters and Setters for attendances

public List<AttendanceEntity> getAttendances() {

return attendances;

}



public void setAttendances(List<AttendanceEntity> attendances) {

this.attendances = attendances;

}



// Optional: Convenience methods to manage the bidirectional relationship

public void addAttendance(AttendanceEntity attendance) {

attendances.add(attendance);

attendance.setUser(this); // Ensure bidirectional link

}



public void removeAttendance(AttendanceEntity attendance) {

attendances.remove(attendance);

attendance.setUser(null); // Break bidirectional link

}

}