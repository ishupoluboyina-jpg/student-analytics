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
import com.example.data.model.PlacementApplicationEntity
import com.example.data.model.PlacementDriveEntity
import com.example.data.model.UserRole
import com.example.ui.components.BadgePill
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun PlacementPortalScreen(viewModel: CareerBridgeViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val drives by viewModel.placementDrives.collectAsState()
    val studentApps by viewModel.studentApplications.collectAsState()
    val allApps by viewModel.allPlacementApplications.collectAsState()
    val studentProfile by viewModel.currentStudentProfile.collectAsState()

    val isPrivileged = currentUser?.role == UserRole.PLACEMENT_OFFICER.name || currentUser?.role == UserRole.RECRUITER.name

    var selectedTab by remember { mutableStateOf(0) } // 0: Drives & Applications, 1: AI Placement Coach, 2: Recruiter Stage Manager (if privileged)
    var showAdvanceDialogForApp by remember { mutableStateOf<PlacementApplicationEntity?>(null) }
    var newStageSelected by remember { mutableStateOf("Technical Interview") }
    var newStatusSelected by remember { mutableStateOf("TECH_INTERVIEW") }
    var interviewScheduleInput by remember { mutableStateOf("Oct 26, 2026 - 11:00 AM") }
    var assessmentLinkInput by remember { mutableStateOf("https://meet.google.com/xyz-tech-interview") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("placement_portal_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Placement Drives", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Work, contentDescription = null) },
                modifier = Modifier.testTag("tab_placement_drives")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("AI Placement Assistant", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                modifier = Modifier.testTag("tab_placement_ai")
            )
            if (isPrivileged) {
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Recruitment Manager", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.ManageAccounts, contentDescription = null) },
                    modifier = Modifier.testTag("tab_recruiter_manager")
                )
            }
        }

        when (selectedTab) {
            0 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Placement Statistics Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                title = "Highest Package",
                                value = "28.5 LPA",
                                subtitle = "Google Campus Drive",
                                icon = Icons.Default.TrendingUp,
                                iconTint = AccentGreen,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Average CTC",
                                value = "15.2 LPA",
                                subtitle = "Tier 1 Tech Batch",
                                icon = Icons.Default.MonetizationOn,
                                iconTint = RoyalBluePrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Student Application Status (if student)
                    if (studentApps.isNotEmpty()) {
                        item {
                            SectionHeader(title = "My Registered Drives & Live Stage Tracker")
                        }

                        items(studentApps) { app ->
                            val drive = drives.find { it.id == app.driveId }
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth().testTag("app_status_card_${app.id}"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = drive?.companyName ?: "Campus Drive",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        BadgePill(
                                            text = app.applicationStatus,
                                            backgroundColor = AccentGreen.copy(alpha = 0.15f),
                                            textColor = AccentGreen
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Current Stage: ${app.currentStage}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = RoyalBluePrimary
                                    )
                                    if (!app.remarks.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Status Note: ${app.remarks}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (!app.interviewSchedule.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = TealSecondary.copy(alpha = 0.12f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Event, contentDescription = null, tint = TealSecondary, modifier = Modifier.size(20.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Scheduled Round: ${app.interviewSchedule}",
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                    color = TealSecondary
                                                )
                                            }
                                        }
                                    }
                                    if (!app.assessmentLink.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Link: ${app.assessmentLink}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = RoyalBluePrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        SectionHeader(title = "Upcoming Campus Recruitment Drives")
                    }

                    items(drives) { drive ->
                        val isApplied = studentApps.any { it.driveId == drive.id }
                        val isEligible = (studentProfile?.cgpa ?: 8.74) >= drive.minCgpa && (studentProfile?.backlogsCount ?: 0) <= drive.maxBacklogs

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().testTag("placement_drive_${drive.id}"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = drive.companyName,
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${drive.roleTitle} • ${drive.jobLocation}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    BadgePill(
                                        text = "${drive.ctcLpa} LPA",
                                        backgroundColor = AccentGreen.copy(alpha = 0.15f),
                                        textColor = AccentGreen
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = drive.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Min CGPA: ${drive.minCgpa} • Max Backlogs: ${drive.maxBacklogs}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Deadline: ${drive.deadlineDate}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AccentAmber
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    BadgePill(
                                        text = if (isEligible) "Eligible for Drive" else "Eligibility Criteria Not Met",
                                        backgroundColor = if (isEligible) TealSecondary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                                        textColor = if (isEligible) TealSecondary else MaterialTheme.colorScheme.error
                                    )
                                    if (isApplied) {
                                        BadgePill(
                                            text = "Application Submitted",
                                            backgroundColor = RoyalBluePrimary.copy(alpha = 0.15f),
                                            textColor = RoyalBluePrimary
                                        )
                                    } else {
                                        Button(
                                            onClick = { viewModel.applyForPlacementDrive(drive) },
                                            enabled = isEligible,
                                            modifier = Modifier.testTag("btn_register_drive_${drive.id}"),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Register Now")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // AI Placement Assistant Tab
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "🤖 AI Placement Prep Coach",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Personalized technical interview drills, behavioral mock questions, and LeetCode focus patterns for your campus drives.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }

                    item {
                        SectionHeader(title = "Company-Specific Interview Questions")
                    }

                    item {
                        ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BadgePill(text = "Google SDE Preparation")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("High Probability Coding Pattern", style = MaterialTheme.typography.labelSmall)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Problem: Find the Median of Two Sorted Arrays in O(log(min(n, m))) time.",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Approach: Binary search over the smaller array to partition elements evenly so that max(left) <= min(right). Handles edge cases where partition falls on bounds.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BadgePill(text = "Microsoft Azure SDE Preparation")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Systems & Concurrency", style = MaterialTheme.typography.labelSmall)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Question: Explain Deadlock Prevention vs Avoidance (Banker's Algorithm).",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Key concepts: Mutual exclusion, Hold and wait, No preemption, Circular wait. Break circular wait by ordering lock acquisition globally.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "HR & Behavioral Question (STAR Method)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "\"Describe an instance where a team member did not complete their part of a project. How did you resolve the situation?\"",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "AI Tip: Focus on empathy, proactive communication, reassignment of tasks, and how the team achieved the overall milestone without toxic blame.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TealSecondary
                                )
                            }
                        }
                    }
                }
            }
            2 -> {
                // Recruiter / Placement Staff Stage Manager
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        SectionHeader(title = "Candidate Recruitment Pipeline Manager")
                    }

                    items(allApps) { app ->
                        val drive = drives.find { it.id == app.driveId }
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().testTag("recruiter_app_card_${app.id}"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = app.studentName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${drive?.companyName ?: "Drive"} • ID: ${app.studentId}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    BadgePill(text = app.currentStage)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Status: ${app.applicationStatus} • Remarks: ${app.remarks ?: "None"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { showAdvanceDialogForApp = app },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_advance_candidate_${app.id}"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Advance Candidate Stage / Release Results")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Advance Candidate Dialog
        if (showAdvanceDialogForApp != null) {
            val app = showAdvanceDialogForApp!!
            val stages = listOf(
                "Resume Shortlisted" to "SHORTLISTED",
                "Aptitude Test" to "APTITUDE",
                "Coding Test" to "CODING",
                "Technical Interview" to "TECH_INTERVIEW",
                "HR Interview" to "HR_INTERVIEW",
                "Final Selection Offer" to "SELECTED"
            )

            AlertDialog(
                onDismissRequest = { showAdvanceDialogForApp = null },
                title = { Text("Update Stage: ${app.studentName}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Select New Recruitment Stage:", style = MaterialTheme.typography.labelMedium)
                        stages.forEach { (stg, status) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = newStageSelected == stg,
                                    onClick = {
                                        newStageSelected = stg
                                        newStatusSelected = status
                                    }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(stg, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        OutlinedTextField(
                            value = interviewScheduleInput,
                            onValueChange = { interviewScheduleInput = it },
                            label = { Text("Interview / Assessment Date") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_schedule")
                        )
                        OutlinedTextField(
                            value = assessmentLinkInput,
                            onValueChange = { assessmentLinkInput = it },
                            label = { Text("Meeting / Assessment URL") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_assessment_link")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.advanceCandidateStage(
                                app,
                                newStageSelected,
                                newStatusSelected,
                                interviewScheduleInput,
                                assessmentLinkInput
                            )
                            showAdvanceDialogForApp = null
                        },
                        modifier = Modifier.testTag("btn_confirm_advance")
                    ) {
                        Text("Save & Notify Candidate")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAdvanceDialogForApp = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
