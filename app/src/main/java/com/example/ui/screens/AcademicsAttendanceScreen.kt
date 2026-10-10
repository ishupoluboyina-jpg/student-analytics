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
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun AcademicsAttendanceScreen(viewModel: CareerBridgeViewModel, initialTab: Int = 0) {
    var selectedTab by remember { mutableStateOf(initialTab) } // 0: Academics, 1: Attendance
    val records by viewModel.academicRecords.collectAsState()
    val attendanceLogs by viewModel.attendanceRecords.collectAsState()
    val studentProfile by viewModel.currentStudentProfile.collectAsState()

    val cgpa = studentProfile?.cgpa ?: 8.74
    val attendance = studentProfile?.attendancePercentage ?: 88.5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("academics_attendance_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Academic Marks & CGPA", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.School, contentDescription = null) },
                modifier = Modifier.testTag("tab_academics")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Attendance Analytics", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.EventAvailable, contentDescription = null) },
                modifier = Modifier.testTag("tab_attendance")
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // CGPA & Semester summary card
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
                                        text = "Cumulative GPA",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "$cgpa / 10.0",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = RoyalBluePrimary
                                    )
                                }
                                BadgePill(
                                    text = "0 Backlogs • Verified",
                                    backgroundColor = AccentGreen.copy(alpha = 0.2f),
                                    textColor = AccentGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Academic Success Score: 92/100 • Risk Indicator: Low Risk",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                item {
                    SectionHeader(title = "Subject-Wise Grade Breakdown")
                }

                items(records) { rec ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().testTag("academic_record_${rec.subjectCode}"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${rec.subjectCode}: ${rec.subjectName}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Semester ${rec.semester} • Credits: ${rec.credits}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                BadgePill(
                                    text = rec.grade,
                                    backgroundColor = RoyalBluePrimary.copy(alpha = 0.12f),
                                    textColor = RoyalBluePrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Internal: ${rec.internalMarks}/30",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "External: ${rec.externalMarks}/70",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Total: ${rec.totalMarks}/100",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalBluePrimary
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Attendance View
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Overall Attendance",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$attendance%",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (attendance >= 75.0) AccentGreen else MaterialTheme.colorScheme.error
                                    )
                                }
                                BadgePill(
                                    text = if (attendance >= 75.0) "Eligible for Exams" else "Attendance Alert (<75%)",
                                    backgroundColor = if (attendance >= 75.0) AccentGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                                    textColor = if (attendance >= 75.0) AccentGreen else MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            MetricProgressBar(
                                label = "Threshold Requirement",
                                progress = (attendance / 100.0).toFloat().coerceIn(0f, 1f),
                                valueText = "$attendance% / 75.0% min",
                                color = if (attendance >= 75.0) AccentGreen else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                item {
                    SectionHeader(title = "Subject-Wise Attendance Metrics")
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val subjects = listOf(
                            "Machine Learning & AI" to 92.5f,
                            "Cloud Architecture & DevOps" to 85.0f,
                            "Full Stack Web & Mobile" to 95.0f,
                            "Algorithms & Data Structures" to 90.0f
                        )
                        subjects.forEach { (name, percent) ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    BadgePill(
                                        text = "$percent%",
                                        backgroundColor = AccentGreen.copy(alpha = 0.15f),
                                        textColor = AccentGreen
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    SectionHeader(title = "Recent Attendance Logs")
                }

                items(attendanceLogs) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("attendance_log_${log.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (log.status == "PRESENT") AccentGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (log.status == "PRESENT") Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = log.status,
                                        tint = if (log.status == "PRESENT") AccentGreen else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = log.subjectName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = log.date,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            BadgePill(
                                text = log.status,
                                backgroundColor = if (log.status == "PRESENT") AccentGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                                textColor = if (log.status == "PRESENT") AccentGreen else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
