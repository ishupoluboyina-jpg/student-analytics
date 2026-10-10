package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        StudentProfileEntity::class,
        AcademicRecordEntity::class,
        AttendanceRecordEntity::class,
        AssignmentEntity::class,
        AssignmentSubmissionEntity::class,
        StudyMaterialEntity::class,
        PlacementDriveEntity::class,
        PlacementApplicationEntity::class,
        GroupMessageEntity::class,
        NotificationEntity::class,
        FeedbackEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun studentDao(): StudentDao
    abstract fun academicDao(): AcademicDao
    abstract fun placementDao(): PlacementDao
    abstract fun communicationDao(): CommunicationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "careerbridge_db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed database asynchronously
                        CoroutineScope(Dispatchers.IO).launch {
                            getDatabase(context).seedInitialData()
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun seedInitialData() {
        val userDao = userDao()
        val studentDao = studentDao()
        val academicDao = academicDao()
        val placementDao = placementDao()
        val commDao = communicationDao()

        // 1. Users for all 7 roles
        val users = listOf(
            UserEntity(
                id = "usr_student_1",
                name = "Arjun Verma",
                email = "student@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.STUDENT.name,
                phone = "+91 98765 43210"
            ),
            UserEntity(
                id = "usr_student_2",
                name = "Priya Nair",
                email = "priya@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.STUDENT.name,
                phone = "+91 98765 43211"
            ),
            UserEntity(
                id = "usr_teacher_1",
                name = "Prof. Rajesh Sharma",
                email = "teacher@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.TEACHER.name,
                phone = "+91 98765 43212"
            ),
            UserEntity(
                id = "usr_parent_1",
                name = "Mr. Suresh Verma",
                email = "parent@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.PARENT.name,
                phone = "+91 98765 43213"
            ),
            UserEntity(
                id = "usr_principal_1",
                name = "Dr. Meenakshi Sundaram",
                email = "principal@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.PRINCIPAL.name,
                phone = "+91 98765 43214"
            ),
            UserEntity(
                id = "usr_mgmt_1",
                name = "Trustee Vikram Malhotra",
                email = "management@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.MANAGEMENT.name,
                phone = "+91 98765 43215"
            ),
            UserEntity(
                id = "usr_place_1",
                name = "Dr. Ananya Roy",
                email = "placement@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.PLACEMENT_OFFICER.name,
                phone = "+91 98765 43216"
            ),
            UserEntity(
                id = "usr_recruiter_1",
                name = "Kavita Rao (Google Hiring)",
                email = "recruiter@careerbridge.edu",
                passwordHash = "password123",
                role = UserRole.RECRUITER.name,
                phone = "+91 98765 43217"
            )
        )
        users.forEach { userDao.insertUser(it) }

        // 2. Student Profiles
        val studentArjun = StudentProfileEntity(
            studentId = "CB-2023-CS101",
            userId = "usr_student_1",
            fullName = "Arjun Verma",
            rollNumber = "2023BCSE042",
            department = "Computer Science & Engineering",
            semester = 6,
            academicYear = "3rd Year (2023-2027)",
            section = "Section A",
            cgpa = 8.74,
            attendancePercentage = 88.5,
            backlogsCount = 0,
            skills = "Kotlin, Jetpack Compose, Python, Spring Boot, Docker, PostgreSQL, Data Structures",
            certifications = "AWS Certified Cloud Practitioner, Google Android Developer NanoDegree",
            projects = "CareerBridge Mobile Portal, Distributed Task Queue in Go, Campus Lost & Found App",
            internships = "Software Engineering Intern at CloudScale Technologies (Summer 2025)",
            resumeSummary = "Passionate 3rd year CSE student with solid problem-solving foundations (LeetCode 350+ solved) and full-stack mobile development expertise.",
            preferredJobRoles = "Software Development Engineer, Mobile App Engineer, Backend Specialist",
            parentUserId = "usr_parent_1"
        )
        val studentPriya = StudentProfileEntity(
            studentId = "CB-2023-CS102",
            userId = "usr_student_2",
            fullName = "Priya Nair",
            rollNumber = "2023BCSE088",
            department = "Computer Science & Engineering",
            semester = 6,
            academicYear = "3rd Year (2023-2027)",
            section = "Section A",
            cgpa = 9.20,
            attendancePercentage = 94.0,
            backlogsCount = 0,
            skills = "Python, PyTorch, SQL, Scikit-Learn, React, FastApi",
            certifications = "TensorFlow Developer Certificate, DeepLearning.AI Specialization",
            projects = "Medical Image Segmentation with U-Net, Multilingual Chatbot for Farmers",
            internships = "Data Science Intern at HealthAI Labs (Winter 2025)",
            resumeSummary = "AI/ML enthusiast focused on applied deep learning and predictive modeling.",
            preferredJobRoles = "Data Scientist, Machine Learning Engineer, AI Research Fellow",
            parentUserId = null
        )
        studentDao.insertStudent(studentArjun)
        studentDao.insertStudent(studentPriya)

        // 3. Academic Records for Arjun
        val records = listOf(
            AcademicRecordEntity(
                studentId = "CB-2023-CS101",
                semester = 5,
                subjectCode = "CS501",
                subjectName = "Design & Analysis of Algorithms",
                credits = 4,
                internalMarks = 28.5,
                externalMarks = 64.0,
                totalMarks = 92.5,
                grade = "O (Outstanding)"
            ),
            AcademicRecordEntity(
                studentId = "CB-2023-CS101",
                semester = 5,
                subjectCode = "CS502",
                subjectName = "Database Management Systems",
                credits = 4,
                internalMarks = 26.0,
                externalMarks = 61.5,
                totalMarks = 87.5,
                grade = "A+ (Excellent)"
            ),
            AcademicRecordEntity(
                studentId = "CB-2023-CS101",
                semester = 5,
                subjectCode = "CS503",
                subjectName = "Operating Systems & Kernels",
                credits = 3,
                internalMarks = 27.0,
                externalMarks = 59.0,
                totalMarks = 86.0,
                grade = "A+ (Excellent)"
            ),
            AcademicRecordEntity(
                studentId = "CB-2023-CS101",
                semester = 6,
                subjectCode = "CS601",
                subjectName = "Machine Learning & AI",
                credits = 4,
                internalMarks = 29.0,
                externalMarks = 63.0,
                totalMarks = 92.0,
                grade = "O (Outstanding)"
            ),
            AcademicRecordEntity(
                studentId = "CB-2023-CS101",
                semester = 6,
                subjectCode = "CS602",
                subjectName = "Cloud Architecture & DevOps",
                credits = 3,
                internalMarks = 25.5,
                externalMarks = 58.0,
                totalMarks = 83.5,
                grade = "A (Very Good)"
            ),
            AcademicRecordEntity(
                studentId = "CB-2023-CS101",
                semester = 6,
                subjectCode = "CS603",
                subjectName = "Full Stack Web & Mobile Engineering",
                credits = 4,
                internalMarks = 29.5,
                externalMarks = 65.5,
                totalMarks = 95.0,
                grade = "O (Outstanding)"
            )
        )
        records.forEach { academicDao.insertRecord(it) }

        // 4. Attendance Records
        val attendances = listOf(
            AttendanceRecordEntity(studentId = "CB-2023-CS101", date = "2026-10-09", subjectName = "Machine Learning & AI", status = "PRESENT"),
            AttendanceRecordEntity(studentId = "CB-2023-CS101", date = "2026-10-09", subjectName = "Cloud Architecture", status = "PRESENT"),
            AttendanceRecordEntity(studentId = "CB-2023-CS101", date = "2026-10-08", subjectName = "Full Stack Engineering", status = "PRESENT"),
            AttendanceRecordEntity(studentId = "CB-2023-CS101", date = "2026-10-07", subjectName = "Machine Learning & AI", status = "PRESENT"),
            AttendanceRecordEntity(studentId = "CB-2023-CS101", date = "2026-10-06", subjectName = "Cloud Architecture", status = "ABSENT"),
            AttendanceRecordEntity(studentId = "CB-2023-CS101", date = "2026-10-05", subjectName = "Algorithms Lab", status = "PRESENT")
        )
        attendances.forEach { academicDao.insertAttendance(it) }

        // 5. Study Materials
        val materials = listOf(
            StudyMaterialEntity(
                subjectName = "Machine Learning & AI",
                title = "Neural Networks, Backpropagation & Transformers Architecture",
                description = "Complete lecture presentation and handwritten derivation notes for Unit 4.",
                fileType = "PDF",
                fileSize = "4.8 MB",
                uploadedBy = "Prof. Rajesh Sharma",
                uploadDate = "Oct 05, 2026"
            ),
            StudyMaterialEntity(
                subjectName = "Cloud Architecture & DevOps",
                title = "Docker Containers, Kubernetes Pods & CI/CD Pipelines Lab Manual",
                description = "Step-by-step terminal commands and deployment configurations.",
                fileType = "DOC",
                fileSize = "2.1 MB",
                uploadedBy = "Prof. Rajesh Sharma",
                uploadDate = "Oct 02, 2026"
            ),
            StudyMaterialEntity(
                subjectName = "Design & Analysis of Algorithms",
                title = "Graph Algorithms (Dijkstra, Bellman-Ford, Kruskal) Master Cheat Sheet",
                description = "Complexities, edge cases, and code templates in Python/Kotlin/C++.",
                fileType = "PDF",
                fileSize = "1.9 MB",
                uploadedBy = "Prof. Rajesh Sharma",
                uploadDate = "Sep 28, 2026"
            )
        )
        materials.forEach { academicDao.insertStudyMaterial(it) }

        // 6. Assignments
        val assignments = listOf(
            AssignmentEntity(
                classId = "CLASS_CS3A",
                subjectName = "Machine Learning & AI",
                title = "Assignment 3: Fine-tuning MobileBERT on Sentiment Classification",
                description = "Implement tokenization, training loop, evaluate F1 score and submit a Jupyter notebook + PDF report.",
                dueDate = "Oct 20, 2026",
                maxMarks = 25,
                createdByTeacher = "Prof. Rajesh Sharma"
            ),
            AssignmentEntity(
                classId = "CLASS_CS3A",
                subjectName = "Cloud Architecture & DevOps",
                title = "Assignment 2: Deploy Multi-tier App with Docker Compose & Nginx Proxy",
                description = "Containerize React frontend + Spring Boot backend + Postgres DB. Provide docker-compose.yml and screenshot proof.",
                dueDate = "Oct 25, 2026",
                maxMarks = 20,
                createdByTeacher = "Prof. Rajesh Sharma"
            )
        )
        assignments.forEach { academicDao.insertAssignment(it) }

        // Submission for Arjun
        academicDao.insertSubmission(
            AssignmentSubmissionEntity(
                assignmentId = 1,
                studentId = "CB-2023-CS101",
                studentName = "Arjun Verma",
                submissionDate = "Oct 08, 2026",
                fileAttachmentName = "Arjun_Verma_ML_Assignment3.ipynb",
                marksAwarded = 24,
                feedback = "Outstanding implementation! Clear metric graphs and error analysis.",
                status = "GRADED"
            )
        )

        // 7. Placement Drives
        val drives = listOf(
            PlacementDriveEntity(
                companyName = "Google",
                roleTitle = "Software Development Engineer (Campus)",
                ctcLpa = 28.5,
                jobLocation = "Bengaluru / Hyderabad",
                minCgpa = 8.0,
                maxBacklogs = 0,
                eligibleBranches = "CSE, IT, ECE",
                deadlineDate = "Oct 22, 2026",
                description = "Building large-scale infrastructure, cloud systems, and consumer apps. Coding rounds on algorithms & data structures.",
                driveStatus = "ACTIVE"
            ),
            PlacementDriveEntity(
                companyName = "Microsoft",
                roleTitle = "Software Engineer I",
                ctcLpa = 24.0,
                jobLocation = "Hyderabad / Noida",
                minCgpa = 7.5,
                maxBacklogs = 0,
                eligibleBranches = "CSE, IT, ECE, EEE",
                deadlineDate = "Oct 28, 2026",
                description = "Core Azure engineering and Office365 modern productivity suites.",
                driveStatus = "ACTIVE"
            ),
            PlacementDriveEntity(
                companyName = "Amazon",
                roleTitle = "SDE I (Operations & AWS)",
                ctcLpa = 26.0,
                jobLocation = "Bengaluru / Chennai",
                minCgpa = 7.5,
                maxBacklogs = 0,
                eligibleBranches = "All Engineering Branches",
                deadlineDate = "Nov 05, 2026",
                description = "High-scale e-commerce logistics and AWS cloud services.",
                driveStatus = "ACTIVE"
            ),
            PlacementDriveEntity(
                companyName = "TCS Digital",
                roleTitle = "Systems Engineer (Digital)",
                ctcLpa = 7.5,
                jobLocation = "Pan India",
                minCgpa = 6.5,
                maxBacklogs = 1,
                eligibleBranches = "All Branches",
                deadlineDate = "Nov 15, 2026",
                description = "Fast-track digital transformation unit focusing on AI, IoT, and Cloud.",
                driveStatus = "ACTIVE"
            )
        )
        drives.forEach { placementDao.insertDrive(it) }

        // Placement Applications for Arjun
        placementDao.insertApplication(
            PlacementApplicationEntity(
                driveId = 1, // Google
                studentId = "CB-2023-CS101",
                studentName = "Arjun Verma",
                currentStage = "Coding Test",
                applicationStatus = "CODING",
                interviewSchedule = "Oct 25, 2026 - 10:00 AM",
                assessmentLink = "https://hackerank.com/test/google-campus-cb-2026",
                remarks = "Shortlisted in Aptitude round with 96th percentile."
            )
        )
        placementDao.insertApplication(
            PlacementApplicationEntity(
                driveId = 2, // Microsoft
                studentId = "CB-2023-CS101",
                studentName = "Arjun Verma",
                currentStage = "Resume Screening",
                applicationStatus = "SHORTLISTED",
                interviewSchedule = null,
                assessmentLink = null,
                remarks = "Profile verified. Eligible for Stage 2 online assessment."
            )
        )

        // 8. Communication Group Messages
        val messages = listOf(
            GroupMessageEntity(
                channelId = "COLLEGE_ALL",
                channelName = "College-Wide Announcements",
                senderId = "usr_principal_1",
                senderName = "Dr. Meenakshi Sundaram (Principal)",
                senderRole = "PRINCIPAL",
                messageText = "Welcome to the Academic Year 2026-27! The CareerBridge unified portal is officially operational for all students, parents, and faculty.",
                timestamp = System.currentTimeMillis() - 86400000L * 4
            ),
            GroupMessageEntity(
                channelId = "COLLEGE_ALL",
                channelName = "College-Wide Announcements",
                senderId = "usr_mgmt_1",
                senderName = "Management Office",
                senderRole = "MANAGEMENT",
                messageText = "Annual Hackathon 'InnoBridge 2026' registrations are now open. Cash prizes worth ₹5 Lakhs. Check the notifications tab for link.",
                timestamp = System.currentTimeMillis() - 86400000L * 2
            ),
            GroupMessageEntity(
                channelId = "CLASS_CS3A",
                channelName = "CSE 3rd Year - Section A",
                senderId = "usr_teacher_1",
                senderName = "Prof. Rajesh Sharma",
                senderRole = "TEACHER",
                messageText = "Reminder to all Section A students: Mid-term lab evaluation for Cloud Architecture is scheduled for this Friday. Ensure Docker setups are ready.",
                timestamp = System.currentTimeMillis() - 3600000L * 5
            ),
            GroupMessageEntity(
                channelId = "CLASS_CS3A",
                channelName = "CSE 3rd Year - Section A",
                senderId = "usr_student_1",
                senderName = "Arjun Verma",
                senderRole = "STUDENT",
                messageText = "Sir, are we allowed to use Docker Compose with persistent named volumes or host directory mounts?",
                timestamp = System.currentTimeMillis() - 3600000L * 3
            ),
            GroupMessageEntity(
                channelId = "CLASS_CS3A",
                channelName = "CSE 3rd Year - Section A",
                senderId = "usr_teacher_1",
                senderName = "Prof. Rajesh Sharma",
                senderRole = "TEACHER",
                messageText = "Named volumes are preferred for production parity, Arjun. Either is acceptable for this evaluation.",
                timestamp = System.currentTimeMillis() - 3600000L * 2
            ),
            GroupMessageEntity(
                channelId = "PLACEMENT_2026",
                channelName = "Placement Cell Announcements",
                senderId = "usr_place_1",
                senderName = "Dr. Ananya Roy (Placement Head)",
                senderRole = "PLACEMENT_OFFICER",
                messageText = "Google Campus hiring registration closes on Oct 22. All eligible CSE/IT students above 8.0 CGPA must register immediately.",
                timestamp = System.currentTimeMillis() - 3600000L * 12
            )
        )
        messages.forEach { commDao.insertMessage(it) }

        // 9. Notifications
        val notifs = listOf(
            NotificationEntity(
                recipientUserId = "ALL",
                title = "Campus Placement Drive: Google SDE 2026",
                body = "Google hiring drive announced. Package: 28.5 LPA. Last date to apply: Oct 22.",
                category = "PLACEMENT",
                timestamp = System.currentTimeMillis() - 3600000L * 10
            ),
            NotificationEntity(
                recipientUserId = "usr_student_1",
                title = "Shortlisted: Google Coding Round",
                body = "Congratulations Arjun! You have advanced to Stage 3 Coding Assessment.",
                category = "PLACEMENT",
                timestamp = System.currentTimeMillis() - 3600000L * 4
            ),
            NotificationEntity(
                recipientUserId = "usr_student_1",
                title = "Assignment Graded: ML Assignment 3",
                body = "Prof. Rajesh Sharma awarded 24/25 marks: 'Outstanding implementation!'",
                category = "ACADEMIC",
                timestamp = System.currentTimeMillis() - 3600000L * 2
            ),
            NotificationEntity(
                recipientUserId = "usr_parent_1",
                title = "Attendance Alert: Arjun Verma",
                body = "Arjun's overall attendance is healthy at 88.5% across all 6 subjects this month.",
                category = "ATTENDANCE",
                timestamp = System.currentTimeMillis() - 86400000L
            )
        )
        notifs.forEach { commDao.insertNotification(it) }
    }
}
