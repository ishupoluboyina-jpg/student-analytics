package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BadgePill
import com.example.ui.components.MetricProgressBar
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CareerBridgeViewModel

@Composable
fun StudentDashboardScreen(viewModel: CareerBridgeViewModel) {
    val studentProfile by viewModel.currentStudentProfile.collectAsState()
    val academicRecords by viewModel.academicRecords.collectAsState()
    val placementDrives by viewModel.placementDrives.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val scrollState = rememberScrollState()

    val studentName = studentProfile?.fullName ?: "Arjun Verma"
    val studentRoll = studentProfile?.rollNumber ?: "2023BCSE042"
    val studentCgpa = studentProfile?.cgpa ?: 8.74
    val studentAttendance = studentProfile?.attendancePercentage ?: 88.5
    val studentBacklogs = studentProfile?.backlogsCount ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("student_dashboard_screen")
    ) {
        // Hero Visual Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "CareerBridge University Campus",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC0A2647), Color(0xF00A2647))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    BadgePill(
                        text = "3rd Year • B.Tech CSE",
                        backgroundColor = TealSecondary,
                        textColor = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Welcome back, $studentName!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Roll No: $studentRoll • CareerBridge Unified System",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE2E8F0))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4 Key Analytics Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Overall CGPA",
                value = "$studentCgpa",
                subtitle = "Top 5% of Batch",
                icon = Icons.Default.Grade,
                iconTint = RoyalBluePrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Attendance",
                value = "$studentAttendance%",
                subtitle = "Safe Threshold (>75%)",
                icon = Icons.Default.EventAvailable,
                iconTint = AccentGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Placement Score",
                value = "88 / 100",
                subtitle = "Eligible for Tier 1",
                icon = Icons.Default.WorkOutline,
                iconTint = AccentAmber,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Backlogs",
                value = "$studentBacklogs",
                subtitle = "Zero Backlog Status",
                icon = Icons.Default.Verified,
                iconTint = TealSecondary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Career Guidance Teaser Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppDestination.AI_GUIDANCE) }
                .testTag("card_ai_career_guidance"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(RoyalBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Career",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Career Recommendation Engine",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Top Match: Cloud & Distributed Systems (96% Match)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Open",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Navigation Buttons Grid
        SectionHeader(title = "Academic & Portal Hub")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.navigateTo(AppDestination.STUDENT_ACADEMICS) },
                modifier = Modifier.weight(1f).testTag("btn_hub_academics"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Academics", fontSize = 11.sp, maxLines = 1)
                }
            }
            OutlinedButton(
                onClick = { viewModel.navigateTo(AppDestination.STUDENT_ATTENDANCE) },
                modifier = Modifier.weight(1f).testTag("btn_hub_attendance"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Attendance", fontSize = 11.sp, maxLines = 1)
                }
            }
            OutlinedButton(
                onClick = { viewModel.navigateTo(AppDestination.STUDENT_MATERIALS) },
                modifier = Modifier.weight(1f).testTag("btn_hub_materials"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Materials", fontSize = 11.sp, maxLines = 1)
                }
            }
            OutlinedButton(
                onClick = { viewModel.navigateTo(AppDestination.PLACEMENT_PORTAL) },
                modifier = Modifier.weight(1f).testTag("btn_hub_placements"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Placements", fontSize = 11.sp, maxLines = 1)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Explainable Readiness Model
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Placement Readiness Breakdown",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Transparent scoring based on academic consistency, DSA assessments, and projects:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                MetricProgressBar(
                    label = "Academic Performance (CGPA 8.74)",
                    progress = 0.90f,
                    valueText = "90%",
                    color = RoyalBluePrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                MetricProgressBar(
                    label = "Data Structures & Algorithmic Problem Solving",
                    progress = 0.85f,
                    valueText = "85%",
                    color = TealSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                MetricProgressBar(
                    label = "Technical Projects & Practical Implementations",
                    progress = 0.88f,
                    valueText = "88%",
                    color = AccentGreen
                )
                Spacer(modifier = Modifier.height(10.dp))
                MetricProgressBar(
                    label = "Aptitude & Communication Skills",
                    progress = 0.82f,
                    valueText = "82%",
                    color = AccentAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Active Placement Drives Preview
        SectionHeader(
            title = "Active Campus Placement Drives",
            actionText = "View All",
            onActionClick = { viewModel.navigateTo(AppDestination.PLACEMENT_PORTAL) }
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            placementDrives.take(2).forEach { drive ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().testTag("drive_card_${drive.id}"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RoyalBluePrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = drive.companyName,
                                tint = RoyalBluePrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = drive.companyName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Notifications
        SectionHeader(
            title = "Recent Announcements & Alerts",
            actionText = "All Alerts",
            onActionClick = { viewModel.navigateTo(AppDestination.NOTIFICATIONS) }
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            notifications.take(3).forEach { notif ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (notif.category) {
                                "PLACEMENT" -> Icons.Default.Work
                                "ACADEMIC" -> Icons.Default.School
                                else -> Icons.Default.Notifications
                            },
                            contentDescription = notif.category,
                            tint = RoyalBluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = notif.title,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = notif.body,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
