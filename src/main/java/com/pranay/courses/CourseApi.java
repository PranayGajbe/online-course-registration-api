package com.pranay.courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

record StudentInput(@NotBlank String fullName,@NotBlank @Email String email) {}
record CourseInput(@NotBlank String title,@NotBlank String instructor,@Min(1) int capacity) {}
record EnrollInput(@NotNull Long studentId,@NotNull Long courseId) {}
interface StudentRepo extends JpaRepository<Student,Long>{boolean existsByEmail(String email);}
interface CourseRepo extends JpaRepository<Course,Long>{}
interface EnrollmentRepo extends JpaRepository<Enrollment,Long>{
 boolean existsByStudentIdAndCourseId(Long studentId,Long courseId);
 List<Enrollment> findByStudentId(Long id); List<Enrollment> findByCourseId(Long id);
}
@RestController @RequestMapping("/api")
class CourseApi {
 private final StudentRepo students;private final CourseRepo courses;private final EnrollmentRepo enrollments;
 CourseApi(StudentRepo s,CourseRepo c,EnrollmentRepo e){students=s;courses=c;enrollments=e;}
 @PostMapping("/students") @ResponseStatus(HttpStatus.CREATED) Student createStudent(@Valid @RequestBody StudentInput r){
  if(students.existsByEmail(r.email()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
  return students.save(new Student(r.fullName(),r.email()));
 }
 @GetMapping("/students") List<Student> students(){return students.findAll();}
 @PostMapping("/courses") @ResponseStatus(HttpStatus.CREATED) Course createCourse(@Valid @RequestBody CourseInput r){return courses.save(new Course(r.title(),r.instructor(),r.capacity()));}
 @GetMapping("/courses") List<Course> courses(){return courses.findAll();}
 @PostMapping("/enrollments") @ResponseStatus(HttpStatus.CREATED) @Transactional
 Enrollment enroll(@Valid @RequestBody EnrollInput r){
  Student s=students.findById(r.studentId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));
  Course c=courses.findById(r.courseId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
  if(enrollments.existsByStudentIdAndCourseId(s.id,c.id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Already enrolled");
  if(c.enrolledCount>=c.capacity)throw new ResponseStatusException(HttpStatus.CONFLICT,"Course is full");
  c.enrolledCount++;courses.save(c);return enrollments.save(new Enrollment(s,c));
 }
 @GetMapping("/enrollments") List<Enrollment> enrollments(){return enrollments.findAll();}
 @GetMapping("/students/{id}/enrollments") List<Enrollment> studentEnrollments(@PathVariable Long id){if(!students.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found");return enrollments.findByStudentId(id);}
 @GetMapping("/courses/{id}/enrollments") List<Enrollment> courseEnrollments(@PathVariable Long id){if(!courses.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found");return enrollments.findByCourseId(id);}
 @DeleteMapping("/enrollments/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional
 void cancel(@PathVariable Long id){Enrollment e=enrollments.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Enrollment not found"));e.course.enrolledCount--;courses.save(e.course);enrollments.delete(e);}
}
