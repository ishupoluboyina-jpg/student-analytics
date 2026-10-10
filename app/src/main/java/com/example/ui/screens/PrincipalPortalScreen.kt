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
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun PrincipalPortalScreen(viewModel: CareerBridgeViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()

    var announcementText by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("principal_portal_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RoyalBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = currentUser?.name ?: "Dr. Meenakshi Sundaram",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Principal • Institutional Oversight & Academic Governance",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
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
            SectionHeader(title = "Key Institutional Metrics (AY 2026-27)")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Students",
                    value = "1,450",
                    subtitle = "Across 4 Departments",
                    icon = Icons.Default.Groups,
                    iconTint = RoyalBluePrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Faculty Strength",
                    value = "120",
                    subtitle = "1:12 Faculty Ratio",
                    icon = Icons.Default.Badge,
                    iconTint = TealSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Placement Rate",
                    value = "86.4%",
                    subtitle = "380+ Offers Secured",
                    icon = Icons.Default.TrendingUp,
                    iconTint = AccentGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Average Package",
                    value = "8.5 LPA",
                    subtitle = "Highest: 28.5 LPA",
                    icon = Icons.Default.MonetizationOn,
                    iconTint = AccentAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            SectionHeader(title = "Department-Wise Enrollment & Placement Readiness")
            ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    MetricProgressBar(
                        label = "Computer Science & Engineering (480 Students)",
                        progress = 0.94f,
                        valueText = "94% Readiness",
                        color = RoyalBluePrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MetricProgressBar(
                        label = "Information Technology (280 Students)",
                        progress = 0.88f,
                        valueText = "88% Readiness",
                        color = TealSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MetricProgressBar(
                        label = "Electronics & Communication (320 Students)",
                        progress = 0.82f,
                        valueText = "82% Readiness",
                        color = AccentGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MetricProgressBar(
                        label = "Mechanical Engineering (370 Students)",
                        progress = 0.76f,
                        valueText = "76% Readiness",
                        color = AccentAmber
                    )
                }
            }
        }

        item {
            SectionHeader(title = "Publish Official College Announcement")
            ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Broadcast an announcement to all students, faculty, and parents:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = announcementText,
                        onValueChange = { announcementText = it },
                        placeholder = { Text("Enter announcement details...") },
                        modifier = Modifier.fillMaxWidth().testTag("input_principal_announcement"),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (announcementText.isNotBlank()) {
                                viewModel.sendChannelMessage(announcementText)
                                toastMessage = "Official announcement broadcasted to campus!"
                                announcementText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("btn_broadcast_announcement"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Broadcast College Announcement")
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Academic Intervention Roster (At-Risk Monitoring)")
        }

        items(allStudents) { st ->
            Card(
                modifier = Modifier.fillMaxWidth().testTag("intervention_student_${st.studentId}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = st.fullName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${st.department} • CGPA: ${st.cgpa} • Att: ${st.attendancePercentage}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    BadgePill(
                        text = if (st.attendancePercentage >= 80.0) "Good Standing" else "Requires Support",
                        backgroundColor = if (st.attendancePercentage >= 80.0) AccentGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                        textColor = if (st.attendancePercentage >= 80.0) AccentGreen else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
