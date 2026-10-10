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
import com.example.data.model.AssignmentEntity
import com.example.data.model.StudyMaterialEntity
import com.example.ui.components.BadgePill
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.TealSecondary
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun StudyMaterialsAssignmentsScreen(viewModel: CareerBridgeViewModel, initialTab: Int = 0) {
    var selectedTab by remember { mutableStateOf(initialTab) } // 0: Study Materials, 1: Assignments
    val materials by viewModel.studyMaterials.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val submissions by viewModel.submissions.collectAsState()

    var showSubmitDialogForAssignment by remember { mutableStateOf<AssignmentEntity?>(null) }
    var submissionFileName by remember { mutableStateOf("") }
    var showDownloadedToast by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("materials_assignments_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Study Materials & Notes", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                modifier = Modifier.testTag("tab_materials")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Assignments & Tests", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                modifier = Modifier.testTag("tab_assignments")
            )
        }

        if (showDownloadedToast != null) {
            Surface(
                color = AccentGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DownloadDone, contentDescription = null, tint = AccentGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Downloaded '$showDownloadedToast' successfully!",
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentGreen
                    )
                }
            }
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    SectionHeader(title = "Verified Faculty Notes & Syllabus Materials")
                }

                items(materials) { item ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().testTag("material_item_${item.id}"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(RoyalBluePrimary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = RoyalBluePrimary
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${item.subjectName} • Uploaded by ${item.uploadedBy}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                BadgePill(text = item.fileType)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Size: ${item.fileSize} • ${item.uploadDate}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                OutlinedButton(
                                    onClick = { showDownloadedToast = item.title },
                                    modifier = Modifier.testTag("btn_download_${item.id}"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Download PDF")
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Assignments View
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    SectionHeader(title = "Class Assignments & Laboratory Evaluations")
                }

                items(assignments) { assign ->
                    val submission = submissions.find { it.assignmentId == assign.id }
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().testTag("assignment_card_${assign.id}"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BadgePill(text = assign.subjectName)
                                BadgePill(
                                    text = if (submission != null) submission.status else "PENDING",
                                    backgroundColor = if (submission != null) AccentGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                                    textColor = if (submission != null) AccentGreen else MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = assign.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = assign.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Due: ${assign.dueDate} • Max Marks: ${assign.maxMarks} • Teacher: ${assign.createdByTeacher}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (submission != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Submitted File: ${submission.fileAttachmentName}",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        if (submission.marksAwarded != null) {
                                            Text(
                                                text = "Score: ${submission.marksAwarded}/${assign.maxMarks} Marks",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                color = AccentGreen
                                            )
                                        }
                                        if (!submission.feedback.isNullOrBlank()) {
                                            Text(
                                                text = "Teacher Feedback: \"${submission.feedback}\"",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        showSubmitDialogForAssignment = assign
                                        submissionFileName = "${assign.title.take(15).replace(" ", "_")}_Solution.pdf"
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_submit_assignment_${assign.id}"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Upload & Submit Assignment")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Assignment Submission Dialog
        if (showSubmitDialogForAssignment != null) {
            val assign = showSubmitDialogForAssignment!!
            AlertDialog(
                onDismissRequest = { showSubmitDialogForAssignment = null },
                title = { Text("Submit: ${assign.title}") },
                text = {
                    Column {
                        Text(
                            text = "Upload your solution document (PDF, ZIP, or Notebook):",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = submissionFileName,
                            onValueChange = { submissionFileName = it },
                            label = { Text("Attachment File Name") },
                            leadingIcon = { Icon(Icons.Default.AttachFile, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_submission_file_name")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (submissionFileName.isNotBlank()) {
                                viewModel.submitStudentAssignment(assign.id, submissionFileName)
                                showSubmitDialogForAssignment = null
                            }
                        },
                        modifier = Modifier.testTag("btn_confirm_submit_assignment")
                    ) {
                        Text("Confirm Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSubmitDialogForAssignment = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
