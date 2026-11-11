# API endpoints (base URL: http://localhost:8080)

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/students` | Register student |
| GET | `/api/students` | List students |
| POST | `/api/courses` | Create course |
| GET | `/api/courses` | List courses |
| POST | `/api/enrollments` | Enroll student |
| GET | `/api/enrollments` | List enrollments |
| GET | `/api/students/{id}/enrollments` | Student enrollment history |
| GET | `/api/courses/{id}/enrollments` | Course enrollment list |
| DELETE | `/api/enrollments/{id}` | Cancel enrollment |

Student JSON: `{"fullName":"Rohan Patil","email":"rohan@example.com"}`
Course JSON: `{"title":"Java and Spring Boot","instructor":"A. Sharma","capacity":30}`
Enrollment JSON: `{"studentId":1,"courseId":1}`
