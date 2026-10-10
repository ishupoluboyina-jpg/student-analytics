package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssignmentSubmissionEntity
import com.example.ui.components.BadgePill
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerBridgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPortalScreen(viewModel: CareerBridgeViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val submissions by viewModel.submissions.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Class Roster & Attendance, 1: Enter Marks, 2: Create Material/Assignment
    var showCreateAssignDialog by remember { mutableStateOf(false) }
    var showUploadMaterialDialog by remember { mutableStateOf(false) }
    var showGradeDialogForSubmission by remember { mutableStateOf<AssignmentSubmissionEntity?>(null) }

    // Forms
    var assignSubject by remember { mutableStateOf("Machine Learning & AI") }
    var assignTitle by remember { mutableStateOf("") }
    var assignDesc by remember { mutableStateOf("") }
    var assignDueDate by remember { mutableStateOf("Nov 01, 2026") }
    var assignMaxMarks by remember { mutableStateOf("25") }

    var matSubject by remember { mutableStateOf("Cloud Architecture & DevOps") }
    var matTitle by remember { mutableStateOf("") }
    var matDesc by remember { mutableStateOf("") }
    var matType by remember { mutableStateOf("PDF") }

    var markSubjectCode by remember { mutableStateOf("CS601") }
    var markSubjectName by remember { mutableStateOf("Machine Learning & AI") }
    var markInternal by remember { mutableStateOf("28.0") }
    var markExternal by remember { mutableStateOf("64.0") }
    var markGrade by remember { mutableStateOf("O") }
    var selectedStudentIdForMark by remember { mutableStateOf("CB-2023-CS101") }

    var gradeScoreInput by remember { mutableStateOf("25") }
    var gradeFeedbackInput by remember { mutableStateOf("Exceptional code structure and accurate mathematical derivations.") }

    var actionToast by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("teacher_portal_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Class Roster & Attendance", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.People, contentDescription = null) },
                modifier = Modifier.testTag("tab_teacher_roster")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Enter Marks & Submissions", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.EditNote, contentDescription = null) },
                modifier = Modifier.testTag("tab_teacher_marks")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Course Materials & Actions", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
                modifier = Modifier.testTag("tab_teacher_actions")
            )
        }

        if (actionToast != null) {
            Surface(
                color = AccentGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = actionToast ?: "", style = MaterialTheme.typography.bodySmall, color = AccentGreen)
                }
            }
        }

        when (selectedTab) {
            0 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Faculty: ${currentUser?.name ?: "Prof. Rajesh Sharma"}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Department: CSE • Assigned Section: 3rd Year (Section A)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }

                    item {
                        SectionHeader(title = "Student Class Roster & Attendance Marking")
                    }

                    items(allStudents) { st ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().testTag("student_roster_card_${st.studentId}"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = st.fullName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "Roll: ${st.rollNumber} • CGPA: ${st.cgpa} • Att: ${st.attendancePercentage}%",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    BadgePill(
                                        text = if (st.attendancePercentage >= 75.0) "Regular" else "At-Risk",
                                        backgroundColor = if (st.attendancePercentage >= 75.0) AccentGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                                        textColor = if (st.attendancePercentage >= 75.0) AccentGreen else MaterialTheme.colorScheme.error
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.recordStudentAttendance(st.studentId, "Machine Learning & AI", "PRESENT")
                                            actionToast = "Recorded PRESENT for ${st.fullName}"
                                        },
                                        modifier = Modifier.weight(1f).testTag("btn_mark_present_${st.studentId}"),
                                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Mark Present", fontSize = 12.sp)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.recordStudentAttendance(st.studentId, "Machine Learning & AI", "ABSENT")
                                            actionToast = "Recorded ABSENT for ${st.fullName}"
                                        },
                                        modifier = Modifier.weight(1f).testTag("btn_mark_absent_${st.studentId}"),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Mark Absent", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Marks entry & Submissions grading
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Direct Academic Marks Entry",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = markSubjectCode,
                                    onValueChange = { markSubjectCode = it },
                                    label = { Text("Subject Code") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_mark_code")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = markSubjectName,
                                    onValueChange = { markSubjectName = it },
                                    label = { Text("Subject Name") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_mark_name")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = markInternal,
                                        onValueChange = { markInternal = it },
                                        label = { Text("Internal (/30)") },
                                        modifier = Modifier.weight(1f).testTag("input_mark_internal")
                                    )
                                    OutlinedTextField(
                                        value = markExternal,
                                        onValueChange = { markExternal = it },
                                        label = { Text("External (/70)") },
                                        modifier = Modifier.weight(1f).testTag("input_mark_external")
                                    )
                                    OutlinedTextField(
                                        value = markGrade,
                                        onValueChange = { markGrade = it },
                                        label = { Text("Grade") },
                                        modifier = Modifier.weight(1f).testTag("input_mark_grade")
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.addStudentMarks(
                                            selectedStudentIdForMark,
                                            6,
                                            markSubjectCode,
                                            markSubjectName,
                                            4,
                                            markInternal.toDoubleOrNull() ?: 25.0,
                                            markExternal.toDoubleOrNull() ?: 60.0,
                                            markGrade
                                        )
                                        actionToast = "Marks recorded successfully for student!"
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_save_marks"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Save Official Marks")
                                }
                            }
                        }
                    }

                    item {
                        SectionHeader(title = "Submitted Student Assignments (Needs Review)")
                    }

                    items(submissions) { sub ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().testTag("teacher_sub_card_${sub.id}"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = sub.studentName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    BadgePill(text = sub.status)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "File: ${sub.fileAttachmentName} • Submitted: ${sub.submissionDate}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (sub.marksAwarded != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Current Score: ${sub.marksAwarded} Marks • Feedback: \"${sub.feedback}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AccentGreen
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { showGradeDialogForSubmission = sub },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_grade_sub_${sub.id}"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Grade & Award Feedback")
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Actions: Create assignment or Upload study material
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().testTag("card_teacher_action_assign"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PostAdd, contentDescription = null, tint = RoyalBluePrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Publish New Class Assignment", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = assignSubject,
                                    onValueChange = { assignSubject = it },
                                    label = { Text("Subject Name") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_assign_subject")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = assignTitle,
                                    onValueChange = { assignTitle = it },
                                    label = { Text("Assignment Title") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_assign_title")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = assignDesc,
                                    onValueChange = { assignDesc = it },
                                    label = { Text("Instructions & Rubric") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_assign_desc")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = assignDueDate,
                                        onValueChange = { assignDueDate = it },
                                        label = { Text("Due Date") },
                                        modifier = Modifier.weight(1f).testTag("input_assign_due")
                                    )
                                    OutlinedTextField(
                                        value = assignMaxMarks,
                                        onValueChange = { assignMaxMarks = it },
                                        label = { Text("Max Marks") },
                                        modifier = Modifier.weight(1f).testTag("input_assign_marks")
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        if (assignTitle.isNotBlank()) {
                                            viewModel.createAssignment(
                                                assignSubject,
                                                assignTitle,
                                                assignDesc,
                                                assignDueDate,
                                                assignMaxMarks.toIntOrNull() ?: 20
                                            )
                                            assignTitle = ""
                                            assignDesc = ""
                                            actionToast = "Assignment published to class!"
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_publish_assignment"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Publish Assignment to Class")
                                }
                            }
                        }
                    }

                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().testTag("card_teacher_action_material"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = TealSecondary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Upload Subject Study Material", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = matSubject,
                                    onValueChange = { matSubject = it },
                                    label = { Text("Subject") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_mat_subject")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = matTitle,
                                    onValueChange = { matTitle = it },
                                    label = { Text("Material Title (e.g. Unit 5 Notes)") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_mat_title")
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = matDesc,
                                    onValueChange = { matDesc = it },
                                    label = { Text("Description") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_mat_desc")
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        if (matTitle.isNotBlank()) {
                                            viewModel.uploadStudyMaterial(
                                                matSubject,
                                                matTitle,
                                                matDesc,
                                                matType,
                                                "3.4 MB"
                                            )
                                            matTitle = ""
                                            matDesc = ""
                                            actionToast = "Study material uploaded and available to students!"
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_upload_material"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Upload File to Portal")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Grading Dialog
        if (showGradeDialogForSubmission != null) {
            val sub = showGradeDialogForSubmission!!
            AlertDialog(
                onDismissRequest = { showGradeDialogForSubmission = null },
                title = { Text("Grade: ${sub.studentName}") },
                text = {
                    Column {
                        Text("File: ${sub.fileAttachmentName}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = gradeScoreInput,
                            onValueChange = { gradeScoreInput = it },
                            label = { Text("Marks Awarded") },
                            modifier = Modifier.fillMaxWidth().testTag("input_grade_score")
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = gradeFeedbackInput,
                            onValueChange = { gradeFeedbackInput = it },
                            label = { Text("Teacher Feedback") },
                            modifier = Modifier.fillMaxWidth().testTag("input_grade_feedback")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.gradeStudentSubmission(
                                sub,
                                gradeScoreInput.toIntOrNull() ?: 20,
                                gradeFeedbackInput
                            )
                            showGradeDialogForSubmission = null
                            actionToast = "Graded submission for ${sub.studentName}!"
                        },
                        modifier = Modifier.testTag("btn_confirm_grade")
                    ) {
                        Text("Submit Grade")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showGradeDialogForSubmission = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
