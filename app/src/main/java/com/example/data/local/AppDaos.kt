package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface StudentDao {
    @Query("SELECT * FROM student_profiles WHERE userId = :userId LIMIT 1")
    fun getStudentByUserId(userId: String): Flow<StudentProfileEntity?>

    @Query("SELECT * FROM student_profiles WHERE userId = :userId LIMIT 1")
    suspend fun getStudentByUserIdOnce(userId: String): StudentProfileEntity?

    @Query("SELECT * FROM student_profiles WHERE studentId = :studentId LIMIT 1")
    suspend fun getStudentByStudentId(studentId: String): StudentProfileEntity?

    @Query("SELECT * FROM student_profiles WHERE parentUserId = :parentId LIMIT 1")
    fun getStudentByParentUserId(parentId: String): Flow<StudentProfileEntity?>

    @Query("SELECT * FROM student_profiles ORDER BY fullName ASC")
    fun getAllStudents(): Flow<List<StudentProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentProfileEntity)

    @Update
    suspend fun updateStudent(student: StudentProfileEntity)
}

@Dao
interface AcademicDao {
    @Query("SELECT * FROM academic_records WHERE studentId = :studentId ORDER BY semester ASC, subjectName ASC")
    fun getRecordsForStudent(studentId: String): Flow<List<AcademicRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AcademicRecordEntity)

    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: String): Flow<List<AttendanceRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceRecordEntity)

    @Query("SELECT * FROM study_materials ORDER BY uploadDate DESC")
    fun getAllStudyMaterials(): Flow<List<StudyMaterialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyMaterial(material: StudyMaterialEntity)

    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity)

    @Query("SELECT * FROM assignment_submissions WHERE studentId = :studentId")
    fun getSubmissionsForStudent(studentId: String): Flow<List<AssignmentSubmissionEntity>>

    @Query("SELECT * FROM assignment_submissions WHERE assignmentId = :assignmentId")
    fun getSubmissionsForAssignment(assignmentId: Int): Flow<List<AssignmentSubmissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: AssignmentSubmissionEntity)

    @Update
    suspend fun updateSubmission(submission: AssignmentSubmissionEntity)
}

@Dao
interface PlacementDao {
    @Query("SELECT * FROM placement_drives ORDER BY deadlineDate ASC")
    fun getAllDrives(): Flow<List<PlacementDriveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrive(drive: PlacementDriveEntity)

    @Update
    suspend fun updateDrive(drive: PlacementDriveEntity)

    @Query("SELECT * FROM placement_applications WHERE studentId = :studentId")
    fun getApplicationsForStudent(studentId: String): Flow<List<PlacementApplicationEntity>>

    @Query("SELECT * FROM placement_applications WHERE driveId = :driveId")
    fun getApplicationsForDrive(driveId: Int): Flow<List<PlacementApplicationEntity>>

    @Query("SELECT * FROM placement_applications")
    fun getAllApplications(): Flow<List<PlacementApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: PlacementApplicationEntity)

    @Update
    suspend fun updateApplication(application: PlacementApplicationEntity)
}

@Dao
interface CommunicationDao {
    @Query("SELECT * FROM group_messages WHERE channelId = :channelId ORDER BY timestamp ASC")
    fun getMessagesForChannel(channelId: String): Flow<List<GroupMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: GroupMessageEntity)

    @Query("SELECT * FROM notifications WHERE recipientUserId = :userId OR recipientUserId = 'ALL' ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Int)

    @Query("SELECT * FROM feedback_records ORDER BY timestamp DESC")
    fun getAllFeedback(): Flow<List<FeedbackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: FeedbackEntity)
}
