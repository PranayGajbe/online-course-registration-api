package com.pranay.courses;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity @Table(name="students")
class Student {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @NotBlank String fullName;
 @NotBlank @Email @Column(unique=true) String email;
 protected Student(){} Student(String n,String e){fullName=n;email=e;}
}
@Entity @Table(name="courses")
class Course {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @NotBlank String title; @NotBlank String instructor; @Min(1) int capacity; @Min(0) int enrolledCount=0;
 protected Course(){} Course(String t,String i,int c){title=t;instructor=i;capacity=c;}
}
@Entity @Table(name="enrollments",uniqueConstraints=@UniqueConstraint(columnNames={"student_id","course_id"}))
class Enrollment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @ManyToOne(optional=false) Student student; @ManyToOne(optional=false) Course course;
 LocalDateTime enrolledAt=LocalDateTime.now();
 protected Enrollment(){} Enrollment(Student s,Course c){student=s;course=c;}
}
