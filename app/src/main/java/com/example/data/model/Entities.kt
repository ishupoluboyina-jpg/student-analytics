package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val displayName: String) {
    STUDENT("Student"),
    TEACHER("Teacher / Faculty"),
    PARENT("Parent"),
    PRINCIPAL("Principal"),
    MANAGEMENT("College Management"),
    PLACEMENT_OFFICER("Placement Department"),
    RECRUITER("Company Recruiter")
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String,
    val phone: String = "",
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "student_profiles")
data class StudentProfileEntity(
    @PrimaryKey val studentId: String,
    val userId: String,
    val fullName: String,
    val rollNumber: String,
    val department: String,
    val semester: Int,
    val academicYear: String,
    val section: String,
    val cgpa: Double,
    val attendancePercentage: Double,
    val backlogsCount: Int,
    val skills: String, // comma-separated
    val certifications: String,
    val projects: String,
    val internships: String,
    val resumeSummary: String,
    val preferredJobRoles: String,
    val parentUserId: String? = null
)

@Entity(tableName = "academic_records")
data class AcademicRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val semester: Int,
    val subjectCode: String,
    val subjectName: String,
    val credits: Int,
    val internalMarks: Double,
    val externalMarks: Double,
    val totalMarks: Double,
    val grade: String,
    val isBacklog: Boolean = false
)

@Entity(tableName = "attendance_records")
data class AttendanceRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val date: String,
    val subjectName: String,
    val status: String // PRESENT, ABSENT, LATE
)

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val classId: String,
    val subjectName: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val maxMarks: Int,
    val createdByTeacher: String
)

@Entity(tableName = "assignment_submissions")
data class AssignmentSubmissionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val assignmentId: Int,
    val studentId: String,
    val studentName: String,
    val submissionDate: String,
    val fileAttachmentName: String,
    val marksAwarded: Int? = null,
    val feedback: String? = null,
    val status: String = "SUBMITTED" // SUBMITTED, GRADED
)

@Entity(tableName = "study_materials")
data class StudyMaterialEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectName: String,
    val title: String,
    val description: String,
    val fileType: String, // PDF, DOC, PPT, CODE
    val fileSize: String,
    val uploadedBy: String,
    val uploadDate: String
)

@Entity(tableName = "placement_drives")
data class PlacementDriveEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyName: String,
    val roleTitle: String,
    val ctcLpa: Double,
    val jobLocation: String,
    val minCgpa: Double,
    val maxBacklogs: Int,
    val eligibleBranches: String,
    val deadlineDate: String,
    val description: String,
    val driveStatus: String = "ACTIVE" // ACTIVE, SHORTLISTING, COMPLETED
)

@Entity(tableName = "placement_applications")
data class PlacementApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val driveId: Int,
    val studentId: String,
    val studentName: String,
    val currentStage: String, // Eligibility, Resume Screening, Aptitude Test, Coding Test, Technical Interview, HR Interview, Final Selection
    val applicationStatus: String, // REGISTERED, SHORTLISTED, APTITUDE, CODING, TECH_INTERVIEW, HR_INTERVIEW, SELECTED, REJECTED
    val interviewSchedule: String? = null,
    val assessmentLink: String? = null,
    val remarks: String? = null
)

@Entity(tableName = "group_messages")
data class GroupMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val channelId: String, // COLLEGE_ALL, CLASS_CS3A, PLACEMENT_2026, DIRECT_...
    val channelName: String,
    val senderId: String,
    val senderName: String,
    val senderRole: String,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attachmentName: String? = null
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipientUserId: String, // or "ALL"
    val title: String,
    val body: String,
    val category: String, // ACADEMIC, PLACEMENT, ATTENDANCE, EVENT
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "feedback_records")
data class FeedbackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val submitterRole: String,
    val submitterName: String,
    val category: String,
    val comments: String,
    val rating: Int,
    val timestamp: Long = System.currentTimeMillis()
)
