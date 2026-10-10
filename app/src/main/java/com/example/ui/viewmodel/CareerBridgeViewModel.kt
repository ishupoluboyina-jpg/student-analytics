package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.CareerBridgeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppDestination(val title: String) {
    AUTH("Sign In"),
    STUDENT_DASHBOARD("Student Dashboard"),
    STUDENT_ACADEMICS("Academics & Performance"),
    STUDENT_ATTENDANCE("Attendance Analytics"),
    STUDENT_MATERIALS("Study Materials & Notes"),
    STUDENT_ASSIGNMENTS("Assignments & Tests"),
    AI_GUIDANCE("AI Career Guidance & Roadmap"),
    AI_CHATBOT("CareerBridge AI Assistant"),
    PLACEMENT_PORTAL("Smart Placement Portal"),
    TEACHER_PORTAL("Teacher & Faculty Portal"),
    PRINCIPAL_PORTAL("Principal & Management"),
    PARENT_PORTAL("Parent Student Portal"),
    MESSAGES("Campus Communication & Groups"),
    NOTIFICATIONS("Notifications & Alerts"),
    FEEDBACK("Feedback & Support"),
    PROFILE("My Profile")
}

class CareerBridgeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = CareerBridgeRepository(db)

    // Current Navigation State
    private val _currentDestination = MutableStateFlow(AppDestination.AUTH)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Auth State
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Student Profile State
    val currentStudentProfile: StateFlow<StudentProfileEntity?> = _currentUser.flatMapLatest { user ->
        if (user != null && user.role == UserRole.STUDENT.name) {
            repository.getStudentByUserId(user.id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Parent Linked Child Profile
    val parentChildProfile: StateFlow<StudentProfileEntity?> = _currentUser.flatMapLatest { user ->
        if (user != null && user.role == UserRole.PARENT.name) {
            repository.getStudentByParentUserId(user.id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // All Students list (for Teachers, Principal, Placement Officers, Recruiters)
    val allStudents: StateFlow<List<StudentProfileEntity>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Student ID for viewing records (defaults to current student or Arjun Verma for parent/teacher)
    private val _inspectedStudentId = MutableStateFlow("CB-2023-CS101")
    val inspectedStudentId: StateFlow<String> = _inspectedStudentId.asStateFlow()

    // Academic & Attendance Records
    val academicRecords: StateFlow<List<AcademicRecordEntity>> = _inspectedStudentId.flatMapLatest { id ->
        repository.getAcademicRecords(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendanceRecords: StateFlow<List<AttendanceRecordEntity>> = _inspectedStudentId.flatMapLatest { id ->
        repository.getAttendanceRecords(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Study Materials & Assignments
    val studyMaterials: StateFlow<List<StudyMaterialEntity>> = repository.getAllStudyMaterials()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assignments: StateFlow<List<AssignmentEntity>> = repository.getAllAssignments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val submissions: StateFlow<List<AssignmentSubmissionEntity>> = _inspectedStudentId.flatMapLatest { id ->
        repository.getSubmissionsForStudent(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Placement Drives & Applications
    val placementDrives: StateFlow<List<PlacementDriveEntity>> = repository.getAllPlacementDrives()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentApplications: StateFlow<List<PlacementApplicationEntity>> = _inspectedStudentId.flatMapLatest { id ->
        repository.getApplicationsForStudent(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPlacementApplications: StateFlow<List<PlacementApplicationEntity>> = repository.getAllPlacementApplications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Communication Channels
    private val _selectedChannel = MutableStateFlow("COLLEGE_ALL")
    val selectedChannel: StateFlow<String> = _selectedChannel.asStateFlow()

    val currentChannelMessages: StateFlow<List<GroupMessageEntity>> = _selectedChannel.flatMapLatest { ch ->
        repository.getMessagesForChannel(ch)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications & Feedback
    val notifications: StateFlow<List<NotificationEntity>> = _currentUser.flatMapLatest { user ->
        repository.getNotificationsForUser(user?.id ?: "ALL")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFeedback: StateFlow<List<FeedbackEntity>> = repository.getAllFeedback()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Chat State
    private val _aiChatMessages = MutableStateFlow<List<AiChatMessage>>(listOf(
        AiChatMessage(
            isUser = false,
            message = "👋 Hello! I am CareerBridge AI Assistant, your personal career counselor, academic analytics guide, and campus placement coach. How can I help you excel today?"
        )
    ))
    val aiChatMessages: StateFlow<List<AiChatMessage>> = _aiChatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    init {
        // Automatically check database initialization
        viewModelScope.launch {
            db.seedInitialData()
        }
    }

    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun selectChannel(channelId: String) {
        _selectedChannel.value = channelId
    }

    fun setInspectedStudent(studentId: String) {
        _inspectedStudentId.value = studentId
    }

    // --- Authentication Actions ---
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            val user = repository.login(email, password)
            _isLoading.value = false
            if (user != null) {
                _currentUser.value = user
                routeUserToRoleDashboard(user.role)
            } else {
                _authError.value = "Invalid email or password. Please verify credentials or use Quick Role Demo Access."
            }
        }
    }

    fun loginAsRole(role: UserRole) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            val email = when (role) {
                UserRole.STUDENT -> "student@careerbridge.edu"
                UserRole.TEACHER -> "teacher@careerbridge.edu"
                UserRole.PARENT -> "parent@careerbridge.edu"
                UserRole.PRINCIPAL -> "principal@careerbridge.edu"
                UserRole.MANAGEMENT -> "management@careerbridge.edu"
                UserRole.PLACEMENT_OFFICER -> "placement@careerbridge.edu"
                UserRole.RECRUITER -> "recruiter@careerbridge.edu"
            }
            val user = repository.login(email, "password123")
            _isLoading.value = false
            if (user != null) {
                _currentUser.value = user
                routeUserToRoleDashboard(user.role)
            } else {
                // If not yet seeded, seed now and retry
                db.seedInitialData()
                val retryUser = repository.login(email, "password123")
                if (retryUser != null) {
                    _currentUser.value = retryUser
                    routeUserToRoleDashboard(retryUser.role)
                } else {
                    _authError.value = "Account setup in progress, please tap again."
                }
            }
        }
    }

    fun register(name: String, email: String, pass: String, confirmPass: String, role: UserRole = UserRole.STUDENT) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _authError.value = "Please complete all registration fields."
            return
        }
        if (pass != confirmPass) {
            _authError.value = "Passwords do not match."
            return
        }
        if (pass.length < 6) {
            _authError.value = "Password must be at least 6 characters long."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            val result = repository.register(name, email, pass, role.name)
            _isLoading.value = false
            result.onSuccess { user ->
                _currentUser.value = user
                _authSuccessMessage.value = "Registration successful! Welcome to CareerBridge."
                routeUserToRoleDashboard(user.role)
            }.onFailure { err ->
                _authError.value = err.message ?: "Registration failed."
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentDestination.value = AppDestination.AUTH
        _authError.value = null
        _authSuccessMessage.value = null
    }

    private fun routeUserToRoleDashboard(role: String) {
        _currentDestination.value = when (role) {
            UserRole.STUDENT.name -> {
                _inspectedStudentId.value = "CB-2023-CS101"
                AppDestination.STUDENT_DASHBOARD
            }
            UserRole.TEACHER.name -> AppDestination.TEACHER_PORTAL
            UserRole.PARENT.name -> {
                _inspectedStudentId.value = "CB-2023-CS101"
                AppDestination.PARENT_PORTAL
            }
            UserRole.PRINCIPAL.name, UserRole.MANAGEMENT.name -> AppDestination.PRINCIPAL_PORTAL
            UserRole.PLACEMENT_OFFICER.name, UserRole.RECRUITER.name -> AppDestination.PLACEMENT_PORTAL
            else -> AppDestination.STUDENT_DASHBOARD
        }
    }

    // --- AI Chat Actions ---
    fun sendAiMessage(promptText: String) {
        if (promptText.isBlank()) return
        val userMsg = AiChatMessage(isUser = true, message = promptText.trim())
        _aiChatMessages.value = _aiChatMessages.value + userMsg

        viewModelScope.launch {
            _isAiThinking.value = true
            val history = _aiChatMessages.value.map { it.isUser to it.message }
            val student = currentStudentProfile.value ?: repository.getStudentByStudentId(_inspectedStudentId.value)
            val reply = repository.getAiChatResponse(promptText, student, history)
            _isAiThinking.value = false
            _aiChatMessages.value = _aiChatMessages.value + AiChatMessage(isUser = false, message = reply)
        }
    }

    fun clearAiChat() {
        _aiChatMessages.value = listOf(
            AiChatMessage(
                isUser = false,
                message = "Conversation cleared. Feel free to ask about career paths, resume tips, coding rounds, or academic subjects!"
            )
        )
    }

    // --- Communication Actions ---
    fun sendChannelMessage(text: String, attachmentName: String? = null) {
        val user = _currentUser.value ?: return
        if (text.isBlank()) return

        viewModelScope.launch {
            val channelName = when (_selectedChannel.value) {
                "COLLEGE_ALL" -> "College-Wide Announcements"
                "CLASS_CS3A" -> "CSE 3rd Year - Section A"
                "PLACEMENT_2026" -> "Placement Cell Announcements"
                else -> "Group Discussion"
            }
            repository.sendMessage(
                GroupMessageEntity(
                    channelId = _selectedChannel.value,
                    channelName = channelName,
                    senderId = user.id,
                    senderName = user.name,
                    senderRole = user.role,
                    messageText = text.trim(),
                    attachmentName = attachmentName
                )
            )
        }
    }

    // --- Placement Actions ---
    fun applyForPlacementDrive(drive: PlacementDriveEntity) {
        val student = currentStudentProfile.value ?: return
        viewModelScope.launch {
            val existing = studentApplications.value.any { it.driveId == drive.id }
            if (!existing) {
                repository.applyForDrive(
                    PlacementApplicationEntity(
                        driveId = drive.id,
                        studentId = student.studentId,
                        studentName = student.fullName,
                        currentStage = "Eligibility Verified",
                        applicationStatus = "REGISTERED",
                        remarks = "Registration confirmed. Resume submitted for screening."
                    )
                )
                repository.sendNotification(
                    NotificationEntity(
                        recipientUserId = student.userId,
                        title = "Registered for ${drive.companyName}",
                        body = "You have successfully registered for ${drive.roleTitle} (${drive.ctcLpa} LPA).",
                        category = "PLACEMENT"
                    )
                )
            }
        }
    }

    fun advanceCandidateStage(
        application: PlacementApplicationEntity,
        newStage: String,
        newStatus: String,
        interviewSchedule: String? = null,
        assessmentLink: String? = null
    ) {
        viewModelScope.launch {
            repository.updateApplicationStatus(
                application.copy(
                    currentStage = newStage,
                    applicationStatus = newStatus,
                    interviewSchedule = interviewSchedule ?: application.interviewSchedule,
                    assessmentLink = assessmentLink ?: application.assessmentLink
                )
            )
            repository.sendNotification(
                NotificationEntity(
                    recipientUserId = "ALL",
                    title = "Placement Update: $newStage",
                    body = "${application.studentName} updated to $newStage ($newStatus).",
                    category = "PLACEMENT"
                )
            )
        }
    }

    // --- Teacher Actions ---
    fun createAssignment(subjectName: String, title: String, description: String, dueDate: String, maxMarks: Int) {
        val teacherName = _currentUser.value?.name ?: "Faculty"
        viewModelScope.launch {
            repository.addAssignment(
                AssignmentEntity(
                    classId = "CLASS_CS3A",
                    subjectName = subjectName,
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    maxMarks = maxMarks,
                    createdByTeacher = teacherName
                )
            )
            repository.sendNotification(
                NotificationEntity(
                    recipientUserId = "ALL",
                    title = "New Assignment: $title",
                    body = "$subjectName: Due by $dueDate ($maxMarks Marks).",
                    category = "ACADEMIC"
                )
            )
        }
    }

    fun submitStudentAssignment(assignmentId: Int, fileName: String) {
        val student = currentStudentProfile.value ?: return
        viewModelScope.launch {
            repository.submitAssignment(
                AssignmentSubmissionEntity(
                    assignmentId = assignmentId,
                    studentId = student.studentId,
                    studentName = student.fullName,
                    submissionDate = "Oct 10, 2026",
                    fileAttachmentName = fileName,
                    status = "SUBMITTED"
                )
            )
        }
    }

    fun gradeStudentSubmission(submission: AssignmentSubmissionEntity, marks: Int, feedback: String) {
        viewModelScope.launch {
            repository.gradeSubmission(
                submission.copy(
                    marksAwarded = marks,
                    feedback = feedback,
                    status = "GRADED"
                )
            )
        }
    }

    fun uploadStudyMaterial(subject: String, title: String, description: String, fileType: String, size: String) {
        val teacherName = _currentUser.value?.name ?: "Faculty"
        viewModelScope.launch {
            repository.addStudyMaterial(
                StudyMaterialEntity(
                    subjectName = subject,
                    title = title,
                    description = description,
                    fileType = fileType,
                    fileSize = size,
                    uploadedBy = teacherName,
                    uploadDate = "Oct 10, 2026"
                )
            )
        }
    }

    fun recordStudentAttendance(studentId: String, subject: String, status: String) {
        viewModelScope.launch {
            repository.addAttendance(
                AttendanceRecordEntity(
                    studentId = studentId,
                    date = "2026-10-10",
                    subjectName = subject,
                    status = status
                )
            )
        }
    }

    fun addStudentMarks(
        studentId: String,
        semester: Int,
        code: String,
        name: String,
        credits: Int,
        internal: Double,
        external: Double,
        grade: String
    ) {
        viewModelScope.launch {
            val total = internal + external
            repository.addAcademicRecord(
                AcademicRecordEntity(
                    studentId = studentId,
                    semester = semester,
                    subjectCode = code,
                    subjectName = name,
                    credits = credits,
                    internalMarks = internal,
                    externalMarks = external,
                    totalMarks = total,
                    grade = grade
                )
            )
        }
    }

    // --- Feedback ---
    fun submitUserFeedback(category: String, comments: String, rating: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.submitFeedback(
                FeedbackEntity(
                    submitterRole = user.role,
                    submitterName = user.name,
                    category = category,
                    comments = comments,
                    rating = rating
                )
            )
        }
    }

    fun markNotificationAsRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    fun updateStudentProfileDetails(
        skills: String,
        certifications: String,
        projects: String,
        internships: String,
        resumeSummary: String,
        preferredRoles: String
    ) {
        val student = currentStudentProfile.value ?: return
        viewModelScope.launch {
            repository.updateStudentProfile(
                student.copy(
                    skills = skills,
                    certifications = certifications,
                    projects = projects,
                    internships = internships,
                    resumeSummary = resumeSummary,
                    preferredJobRoles = preferredRoles
                )
            )
        }
    }
}
