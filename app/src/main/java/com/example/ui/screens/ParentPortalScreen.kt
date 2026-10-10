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
import com.example.ui.components.BadgePill
import com.example.ui.components.MetricProgressBar
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun ParentPortalScreen(viewModel: CareerBridgeViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val academicRecords by viewModel.academicRecords.collectAsState()
    val attendanceRecords by viewModel.attendanceRecords.collectAsState()

    var showContactTeacherDialog by remember { mutableStateOf(false) }
    var parentMessageInput by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("parent_portal_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Parent & Child Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Parent: ${currentUser?.name ?: "Mr. Suresh Verma"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Child: Arjun Verma",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = RoyalBluePrimary
                            )
                            Text(
                                text = "Roll: 2023BCSE042 • 3rd Year B.Tech CSE",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                        BadgePill(
                            text = "Verified Student Link",
                            backgroundColor = AccentGreen.copy(alpha = 0.2f),
                            textColor = AccentGreen
                        )
                    }
                }
            }
        }

        if (toastMessage != null) {
            item {
                Surface(
                    color = AccentGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = toastMessage ?: "", style = MaterialTheme.typography.bodySmall, color = AccentGreen)
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Academic & Attendance Health Check")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Current CGPA",
                    value = "8.74",
                    subtitle = "Zero Backlogs",
                    icon = Icons.Default.Grade,
                    iconTint = RoyalBluePrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Attendance",
                    value = "88.5%",
                    subtitle = "Safe Standing (>75%)",
                    icon = Icons.Default.EventAvailable,
                    iconTint = AccentGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Attendance Status & Alerts",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MetricProgressBar(
                        label = "Overall Attendance Requirement",
                        progress = 0.885f,
                        valueText = "88.5% (Safe)",
                        color = AccentGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "✅ Arjun has maintained consistent attendance across all core engineering labs and lectures this semester.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            SectionHeader(title = "Recent Subject Marks & Progress")
        }

        items(academicRecords.take(4)) { rec ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().testTag("parent_grade_card_${rec.subjectCode}"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = rec.subjectName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Semester ${rec.semester} • Total: ${rec.totalMarks}/100",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    BadgePill(
                        text = "Grade: ${rec.grade}",
                        backgroundColor = RoyalBluePrimary.copy(alpha = 0.12f),
                        textColor = RoyalBluePrimary
                    )
                }
            }
        }

        item {
            SectionHeader(title = "Teacher Remarks & Contact Faculty")
            ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Class Teacher: Prof. Rajesh Sharma (HOD CSE)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"Arjun demonstrates strong problem-solving abilities and has scored highest in the recent Machine Learning lab assessment. His placement readiness is high.\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showContactTeacherDialog = true },
                        modifier = Modifier.fillMaxWidth().testTag("btn_contact_faculty"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Direct Message to Faculty")
                    }
                }
            }
        }
    }

    if (showContactTeacherDialog) {
        AlertDialog(
            onDismissRequest = { showContactTeacherDialog = false },
            title = { Text("Contact Prof. Rajesh Sharma") },
            text = {
                Column {
                    Text(
                        text = "Inquire regarding Arjun's academic performance or attendance:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = parentMessageInput,
                        onValueChange = { parentMessageInput = it },
                        placeholder = { Text("Enter your message...") },
                        modifier = Modifier.fillMaxWidth().testTag("input_parent_message"),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (parentMessageInput.isNotBlank()) {
                            viewModel.selectChannel("CLASS_CS3A")
                            viewModel.sendChannelMessage("Parent Query (Mr. Suresh Verma): $parentMessageInput")
                            showContactTeacherDialog = false
                            toastMessage = "Message sent to class faculty successfully!"
                        }
                    },
                    modifier = Modifier.testTag("btn_confirm_send_parent_msg")
                ) {
                    Text("Send Message")
                }
            },
            dismissButton = {
                TextButton(onClick = { showContactTeacherDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
