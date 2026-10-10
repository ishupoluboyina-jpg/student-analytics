package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.ui.components.BadgePill
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun NotificationsFeedbackScreen(viewModel: CareerBridgeViewModel, initialTab: Int = 0) {
    var selectedTab by remember { mutableStateOf(initialTab) } // 0: Notifications, 1: Feedback, 2: My Profile
    val notifications by viewModel.notifications.collectAsState()
    val studentProfile by viewModel.currentStudentProfile.collectAsState()

    var feedbackCategory by remember { mutableStateOf("Academic Curriculum") }
    var feedbackComments by remember { mutableStateOf("") }
    var feedbackRating by remember { mutableStateOf(5) }
    var feedbackSubmittedToast by remember { mutableStateOf(false) }

    // Profile Edit State
    var editSkills by remember(studentProfile) { mutableStateOf(studentProfile?.skills ?: "") }
    var editCerts by remember(studentProfile) { mutableStateOf(studentProfile?.certifications ?: "") }
    var editProjects by remember(studentProfile) { mutableStateOf(studentProfile?.projects ?: "") }
    var editInternships by remember(studentProfile) { mutableStateOf(studentProfile?.internships ?: "") }
    var editResumeSummary by remember(studentProfile) { mutableStateOf(studentProfile?.resumeSummary ?: "") }
    var editPreferredRoles by remember(studentProfile) { mutableStateOf(studentProfile?.preferredJobRoles ?: "") }
    var profileSavedToast by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("notifications_feedback_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Notifications", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                modifier = Modifier.testTag("tab_notifications")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Feedback", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.RateReview, contentDescription = null) },
                modifier = Modifier.testTag("tab_feedback")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("My Profile", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.testTag("tab_profile")
            )
        }

        when (selectedTab) {
            0 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SectionHeader(title = "Campus & Placement Alerts Center")
                    }

                    items(notifications) { notif ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.markNotificationAsRead(notif.id) }
                                .testTag("notification_item_${notif.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = if (notif.isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(RoyalBluePrimary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (notif.category) {
                                            "PLACEMENT" -> Icons.Default.Work
                                            "ACADEMIC" -> Icons.Default.School
                                            "ATTENDANCE" -> Icons.Default.EventAvailable
                                            else -> Icons.Default.Notifications
                                        },
                                        contentDescription = notif.category,
                                        tint = RoyalBluePrimary
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = notif.title,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        BadgePill(text = notif.category)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = notif.body,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Feedback Form
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
                                    text = "Institutional Feedback & Support",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Your feedback directly helps department chairs, placement staff, and college management improve institutional facilities.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }

                    if (feedbackSubmittedToast) {
                        item {
                            Surface(
                                color = AccentGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Thank you! Your feedback has been recorded.", style = MaterialTheme.typography.bodySmall, color = AccentGreen)
                                }
                            }
                        }
                    }

                    item {
                        ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Feedback Category:", style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(6.dp))

                                val categories = listOf("Academic Curriculum", "Faculty & Teaching", "Placement Training", "Campus Infrastructure")
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    categories.take(2).forEach { cat ->
                                        FilterChip(
                                            selected = feedbackCategory == cat,
                                            onClick = { feedbackCategory = cat },
                                            label = { Text(cat, fontSize = 11.sp) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    categories.drop(2).forEach { cat ->
                                        FilterChip(
                                            selected = feedbackCategory == cat,
                                            onClick = { feedbackCategory = cat },
                                            label = { Text(cat, fontSize = 11.sp) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Text("Experience Rating (1-5 Stars):", style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    (1..5).forEach { star ->
                                        IconButton(
                                            onClick = { feedbackRating = star },
                                            modifier = Modifier.testTag("star_$star")
                                        ) {
                                            Icon(
                                                imageVector = if (star <= feedbackRating) Icons.Default.Star else Icons.Default.StarBorder,
                                                contentDescription = "$star stars",
                                                tint = AccentAmber,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = feedbackComments,
                                    onValueChange = { feedbackComments = it },
                                    label = { Text("Detailed Comments & Suggestions") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_feedback_comments"),
                                    maxLines = 4
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        if (feedbackComments.isNotBlank()) {
                                            viewModel.submitUserFeedback(feedbackCategory, feedbackComments, feedbackRating)
                                            feedbackComments = ""
                                            feedbackSubmittedToast = true
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_submit_feedback"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Submit Official Feedback")
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Profile View & Edit
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(RoyalBluePrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text(
                                            text = studentProfile?.fullName ?: "Arjun Verma",
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "Roll: ${studentProfile?.rollNumber ?: "2023BCSE042"} • ${studentProfile?.department ?: "CSE"}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "CGPA: ${studentProfile?.cgpa ?: 8.74} • Attendance: ${studentProfile?.attendancePercentage ?: 88.5}%",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = RoyalBluePrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (profileSavedToast) {
                        item {
                            Surface(
                                color = AccentGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Profile details updated successfully!", style = MaterialTheme.typography.bodySmall, color = AccentGreen)
                                }
                            }
                        }
                    }

                    item {
                        SectionHeader(title = "Placement Profile & Skills Editor")
                        ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                OutlinedTextField(
                                    value = editSkills,
                                    onValueChange = { editSkills = it },
                                    label = { Text("Technical Skills (Comma-separated)") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_profile_skills")
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = editCerts,
                                    onValueChange = { editCerts = it },
                                    label = { Text("Certifications") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_profile_certs")
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = editProjects,
                                    onValueChange = { editProjects = it },
                                    label = { Text("Key Projects") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_profile_projects")
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = editInternships,
                                    onValueChange = { editInternships = it },
                                    label = { Text("Internships") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_profile_internships")
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = editPreferredRoles,
                                    onValueChange = { editPreferredRoles = it },
                                    label = { Text("Preferred Job Roles") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_profile_roles")
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = editResumeSummary,
                                    onValueChange = { editResumeSummary = it },
                                    label = { Text("Resume Summary / Bio") },
                                    modifier = Modifier.fillMaxWidth().testTag("input_profile_summary"),
                                    maxLines = 4
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        viewModel.updateStudentProfileDetails(
                                            editSkills,
                                            editCerts,
                                            editProjects,
                                            editInternships,
                                            editResumeSummary,
                                            editPreferredRoles
                                        )
                                        profileSavedToast = true
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_save_profile"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Save Placement Profile")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
