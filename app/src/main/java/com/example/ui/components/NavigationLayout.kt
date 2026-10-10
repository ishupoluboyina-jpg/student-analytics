package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.screens.*
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.TealSecondary
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CareerBridgeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: CareerBridgeViewModel) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showPrincipalSignUpFirstPage by remember { mutableStateOf(true) }

    // Handle Back Press
    BackHandler(enabled = currentDestination != AppDestination.AUTH) {
        val userRole = currentUser?.role
        val defaultHome = when (userRole) {
            UserRole.STUDENT.name -> AppDestination.STUDENT_DASHBOARD
            UserRole.TEACHER.name -> AppDestination.TEACHER_PORTAL
            UserRole.PARENT.name -> AppDestination.PARENT_PORTAL
            UserRole.PRINCIPAL.name, UserRole.MANAGEMENT.name -> AppDestination.PRINCIPAL_PORTAL
            UserRole.PLACEMENT_OFFICER.name, UserRole.RECRUITER.name -> AppDestination.PLACEMENT_PORTAL
            else -> AppDestination.STUDENT_DASHBOARD
        }
        if (currentDestination != defaultHome) {
            viewModel.navigateTo(defaultHome)
        } else {
            viewModel.logout()
        }
    }

    if (currentDestination == AppDestination.AUTH || currentUser == null) {
        if (showPrincipalSignUpFirstPage) {
            PrincipalSignUpScreen(
                viewModel = viewModel,
                onBackToLogin = { showPrincipalSignUpFirstPage = false }
            )
        } else {
            AuthScreen(
                viewModel = viewModel,
                onGoToPrincipalSignUp = { showPrincipalSignUpFirstPage = true }
            )
        }
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(310.dp)
                    .testTag("app_navigation_drawer"),
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Drawer Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RoyalBluePrimary)
                            .padding(20.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = currentUser?.name ?: "User",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = currentUser?.email ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BadgePill(
                                text = currentUser?.role ?: "Student",
                                backgroundColor = TealSecondary,
                                textColor = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val role = currentUser?.role ?: UserRole.STUDENT.name

                    // Navigation Items based on role
                    if (role == UserRole.STUDENT.name) {
                        DrawerNavGroup(title = "Student Center")
                        DrawerNavItem("Dashboard", Icons.Default.Dashboard, currentDestination == AppDestination.STUDENT_DASHBOARD) {
                            viewModel.navigateTo(AppDestination.STUDENT_DASHBOARD)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Academics & Marks", Icons.Default.Grade, currentDestination == AppDestination.STUDENT_ACADEMICS) {
                            viewModel.navigateTo(AppDestination.STUDENT_ACADEMICS)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Attendance", Icons.Default.EventAvailable, currentDestination == AppDestination.STUDENT_ATTENDANCE) {
                            viewModel.navigateTo(AppDestination.STUDENT_ATTENDANCE)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Study Materials", Icons.Default.MenuBook, currentDestination == AppDestination.STUDENT_MATERIALS) {
                            viewModel.navigateTo(AppDestination.STUDENT_MATERIALS)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Assignments & Tests", Icons.Default.Assignment, currentDestination == AppDestination.STUDENT_ASSIGNMENTS) {
                            viewModel.navigateTo(AppDestination.STUDENT_ASSIGNMENTS)
                            coroutineScope.launch { drawerState.close() }
                        }

                        DrawerNavGroup(title = "AI Career & Placements")
                        DrawerNavItem("AI Career Guidance", Icons.Default.Explore, currentDestination == AppDestination.AI_GUIDANCE) {
                            viewModel.navigateTo(AppDestination.AI_GUIDANCE)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("AI Assistant Chatbot", Icons.Default.SmartToy, currentDestination == AppDestination.AI_CHATBOT) {
                            viewModel.navigateTo(AppDestination.AI_CHATBOT)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Campus Placements", Icons.Default.Work, currentDestination == AppDestination.PLACEMENT_PORTAL) {
                            viewModel.navigateTo(AppDestination.PLACEMENT_PORTAL)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("My Profile & Skills", Icons.Default.Person, currentDestination == AppDestination.PROFILE) {
                            viewModel.navigateTo(AppDestination.PROFILE)
                            coroutineScope.launch { drawerState.close() }
                        }
                    } else if (role == UserRole.TEACHER.name) {
                        DrawerNavGroup(title = "Faculty Portal")
                        DrawerNavItem("Teacher Dashboard", Icons.Default.People, currentDestination == AppDestination.TEACHER_PORTAL) {
                            viewModel.navigateTo(AppDestination.TEACHER_PORTAL)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Study Materials", Icons.Default.MenuBook, currentDestination == AppDestination.STUDENT_MATERIALS) {
                            viewModel.navigateTo(AppDestination.STUDENT_MATERIALS)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Assignments", Icons.Default.Assignment, currentDestination == AppDestination.STUDENT_ASSIGNMENTS) {
                            viewModel.navigateTo(AppDestination.STUDENT_ASSIGNMENTS)
                            coroutineScope.launch { drawerState.close() }
                        }
                    } else if (role == UserRole.PARENT.name) {
                        DrawerNavGroup(title = "Parent Portal")
                        DrawerNavItem("Child Performance", Icons.Default.FamilyRestroom, currentDestination == AppDestination.PARENT_PORTAL) {
                            viewModel.navigateTo(AppDestination.PARENT_PORTAL)
                            coroutineScope.launch { drawerState.close() }
                        }
                    } else if (role == UserRole.PRINCIPAL.name || role == UserRole.MANAGEMENT.name) {
                        DrawerNavGroup(title = "Institutional Governance")
                        DrawerNavItem("Principal Dashboard", Icons.Default.AccountBalance, currentDestination == AppDestination.PRINCIPAL_PORTAL) {
                            viewModel.navigateTo(AppDestination.PRINCIPAL_PORTAL)
                            coroutineScope.launch { drawerState.close() }
                        }
                        DrawerNavItem("Placement Analytics", Icons.Default.Work, currentDestination == AppDestination.PLACEMENT_PORTAL) {
                            viewModel.navigateTo(AppDestination.PLACEMENT_PORTAL)
                            coroutineScope.launch { drawerState.close() }
                        }
                    } else if (role == UserRole.PLACEMENT_OFFICER.name || role == UserRole.RECRUITER.name) {
                        DrawerNavGroup(title = "Campus Placements")
                        DrawerNavItem("Placement Drives", Icons.Default.Work, currentDestination == AppDestination.PLACEMENT_PORTAL) {
                            viewModel.navigateTo(AppDestination.PLACEMENT_PORTAL)
                            coroutineScope.launch { drawerState.close() }
                        }
                    }

                    DrawerNavGroup(title = "Communication & System")
                    DrawerNavItem("Campus Messages", Icons.Default.Chat, currentDestination == AppDestination.MESSAGES) {
                        viewModel.navigateTo(AppDestination.MESSAGES)
                        coroutineScope.launch { drawerState.close() }
                    }
                    DrawerNavItem("Notifications", Icons.Default.Notifications, currentDestination == AppDestination.NOTIFICATIONS) {
                        viewModel.navigateTo(AppDestination.NOTIFICATIONS)
                        coroutineScope.launch { drawerState.close() }
                    }
                    DrawerNavItem("Feedback & Support", Icons.Default.RateReview, currentDestination == AppDestination.FEEDBACK) {
                        viewModel.navigateTo(AppDestination.FEEDBACK)
                        coroutineScope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    DrawerNavItem("Switch Account / Sign Out", Icons.Default.Logout, false) {
                        viewModel.logout()
                        coroutineScope.launch { drawerState.close() }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = currentDestination.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1
                            )
                            Text(
                                text = "CareerBridge • ${currentUser?.role ?: "Student"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("btn_drawer_menu")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Drawer Navigation")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.navigateTo(AppDestination.NOTIFICATIONS) },
                            modifier = Modifier.testTag("btn_top_notifications")
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                        IconButton(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.testTag("btn_top_logout")
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = "Sign Out")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentDestination) {
                    AppDestination.AUTH -> AuthScreen(viewModel)
                    AppDestination.STUDENT_DASHBOARD -> StudentDashboardScreen(viewModel)
                    AppDestination.STUDENT_ACADEMICS -> AcademicsAttendanceScreen(viewModel, initialTab = 0)
                    AppDestination.STUDENT_ATTENDANCE -> AcademicsAttendanceScreen(viewModel, initialTab = 1)
                    AppDestination.STUDENT_MATERIALS -> StudyMaterialsAssignmentsScreen(viewModel, initialTab = 0)
                    AppDestination.STUDENT_ASSIGNMENTS -> StudyMaterialsAssignmentsScreen(viewModel, initialTab = 1)
                    AppDestination.AI_GUIDANCE -> AiGuidanceScreen(viewModel)
                    AppDestination.AI_CHATBOT -> AiGuidanceScreen(viewModel)
                    AppDestination.PLACEMENT_PORTAL -> PlacementPortalScreen(viewModel)
                    AppDestination.TEACHER_PORTAL -> TeacherPortalScreen(viewModel)
                    AppDestination.PRINCIPAL_PORTAL -> PrincipalPortalScreen(viewModel)
                    AppDestination.PARENT_PORTAL -> ParentPortalScreen(viewModel)
                    AppDestination.MESSAGES -> MessagingScreen(viewModel)
                    AppDestination.NOTIFICATIONS -> NotificationsFeedbackScreen(viewModel, initialTab = 0)
                    AppDestination.FEEDBACK -> NotificationsFeedbackScreen(viewModel, initialTab = 1)
                    AppDestination.PROFILE -> NotificationsFeedbackScreen(viewModel, initialTab = 2)
                }
            }
        }
    }
}

@Composable
fun DrawerNavGroup(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
    )
}

@Composable
fun DrawerNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)) },
        icon = { Icon(icon, contentDescription = label) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .testTag("nav_item_${label.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(12.dp)
    )
}
