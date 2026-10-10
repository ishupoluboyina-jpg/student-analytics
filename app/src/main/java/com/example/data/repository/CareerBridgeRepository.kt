package com.example.data.repository

import com.example.data.ai.GeminiAiService
import com.example.data.local.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class CareerBridgeRepository(private val db: AppDatabase) {

    private val userDao = db.userDao()
    private val studentDao = db.studentDao()
    private val academicDao = db.academicDao()
    private val placementDao = db.placementDao()
    private val commDao = db.communicationDao()
    private val aiService = GeminiAiService()

    // --- Authentication & User Operations ---
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.getUserByEmail(email.trim().lowercase())
        return if (user != null && user.passwordHash == password) user else null
    }

    suspend fun register(name: String, email: String, password: String, role: String): Result<UserEntity> {
        val normalizedEmail = email.trim().lowercase()
        val existing = userDao.getUserByEmail(normalizedEmail)
        if (existing != null) {
            return Result.failure(Exception("An account with email $email already exists."))
        }
        val user = UserEntity(
            id = "usr_${UUID.randomUUID().toString().take(8)}",
            name = name.trim(),
            email = normalizedEmail,
            passwordHash = password,
            role = role,
            createdAt = System.currentTimeMillis()
        )
        userDao.insertUser(user)

        // If registered as student, create default student profile
        if (role == UserRole.STUDENT.name) {
            val rollNo = "2026BCSE${(100..999).random()}"
            val studentProfile = StudentProfileEntity(
                studentId = "CB-2026-${rollNo.takeLast(3)}",
                userId = user.id,
                fullName = user.name,
                rollNumber = rollNo,
                department = "Computer Science & Engineering",
                semester = 6,
                academicYear = "3rd Year (2023-2027)",
                section = "Section A",
                cgpa = 8.20,
                attendancePercentage = 85.0,
                backlogsCount = 0,
                skills = "Python, Java, Data Structures, Web Engineering",
                certifications = "Introductory Python Certificate",
                projects = "Campus Management Portal Project",
                internships = "None yet",
                resumeSummary = "Dedicated computer science student aiming for software engineering careers.",
                preferredJobRoles = "Software Engineer, Web Developer",
                parentUserId = null
            )
            studentDao.insertStudent(studentProfile)
        }

        return Result.success(user)
    }

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    suspend fun getUserById(id: String): UserEntity? = userDao.getUserById(id)

    // --- Student Profile Operations ---
    fun getStudentByUserId(userId: String): Flow<StudentProfileEntity?> = studentDao.getStudentByUserId(userId)
    suspend fun getStudentByUserIdOnce(userId: String): StudentProfileEntity? = studentDao.getStudentByUserIdOnce(userId)
    suspend fun getStudentByStudentId(studentId: String): StudentProfileEntity? = studentDao.getStudentByStudentId(studentId)
    fun getStudentByParentUserId(parentId: String): Flow<StudentProfileEntity?> = studentDao.getStudentByParentUserId(parentId)
    fun getAllStudents(): Flow<List<StudentProfileEntity>> = studentDao.getAllStudents()

    suspend fun updateStudentProfile(profile: StudentProfileEntity) {
        studentDao.updateStudent(profile)
    }

    // --- Academic & Attendance Operations ---
    fun getAcademicRecords(studentId: String): Flow<List<AcademicRecordEntity>> = academicDao.getRecordsForStudent(studentId)
    suspend fun addAcademicRecord(record: AcademicRecordEntity) = academicDao.insertRecord(record)

    fun getAttendanceRecords(studentId: String): Flow<List<AttendanceRecordEntity>> = academicDao.getAttendanceForStudent(studentId)
    suspend fun addAttendance(attendance: AttendanceRecordEntity) = academicDao.insertAttendance(attendance)

    fun getAllStudyMaterials(): Flow<List<StudyMaterialEntity>> = academicDao.getAllStudyMaterials()
    suspend fun addStudyMaterial(material: StudyMaterialEntity) = academicDao.insertStudyMaterial(material)

    fun getAllAssignments(): Flow<List<AssignmentEntity>> = academicDao.getAllAssignments()
    suspend fun addAssignment(assignment: AssignmentEntity) = academicDao.insertAssignment(assignment)

    fun getSubmissionsForStudent(studentId: String): Flow<List<AssignmentSubmissionEntity>> = academicDao.getSubmissionsForStudent(studentId)
    suspend fun submitAssignment(submission: AssignmentSubmissionEntity) = academicDao.insertSubmission(submission)
    suspend fun gradeSubmission(submission: AssignmentSubmissionEntity) = academicDao.updateSubmission(submission)

    // --- Placement Operations ---
    fun getAllPlacementDrives(): Flow<List<PlacementDriveEntity>> = placementDao.getAllDrives()
    suspend fun addPlacementDrive(drive: PlacementDriveEntity) = placementDao.insertDrive(drive)
    suspend fun updatePlacementDrive(drive: PlacementDriveEntity) = placementDao.updateDrive(drive)

    fun getApplicationsForStudent(studentId: String): Flow<List<PlacementApplicationEntity>> = placementDao.getApplicationsForStudent(studentId)
    fun getAllPlacementApplications(): Flow<List<PlacementApplicationEntity>> = placementDao.getAllApplications()
    suspend fun applyForDrive(application: PlacementApplicationEntity) = placementDao.insertApplication(application)
    suspend fun updateApplicationStatus(application: PlacementApplicationEntity) = placementDao.updateApplication(application)

    // --- Communication & Messaging ---
    fun getMessagesForChannel(channelId: String): Flow<List<GroupMessageEntity>> = commDao.getMessagesForChannel(channelId)
    suspend fun sendMessage(message: GroupMessageEntity) = commDao.insertMessage(message)

    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>> = commDao.getNotificationsForUser(userId)
    suspend fun sendNotification(notification: NotificationEntity) = commDao.insertNotification(notification)
    suspend fun markNotificationRead(id: Int) = commDao.markNotificationAsRead(id)

    fun getAllFeedback(): Flow<List<FeedbackEntity>> = commDao.getAllFeedback()
    suspend fun submitFeedback(feedback: FeedbackEntity) = commDao.insertFeedback(feedback)

    // --- AI Guidance Operations ---
    suspend fun getAiChatResponse(
        message: String,
        studentProfile: StudentProfileEntity?,
        history: List<Pair<Boolean, String>>
    ): String = aiService.getChatResponse(message, studentProfile, history)

    fun getCareerRecommendations(studentProfile: StudentProfileEntity?): List<CareerRecommendation> =
        aiService.generateTopCareerRecommendations(studentProfile)
}
