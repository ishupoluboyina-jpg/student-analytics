package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CareerBridgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalSignUpScreen(
    viewModel: CareerBridgeViewModel,
    onBackToLogin: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) } // 1: Account Details, 2: Personal Info, 3: College Details, 4: Verify & Complete

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var designation by remember { mutableStateOf("Principal") }
    var collegeName by remember { mutableStateOf("") }
    var collegeCode by remember { mutableStateOf("") }

    // Step 2 & 3 additional fields
    var qualification by remember { mutableStateOf("Ph.D in Engineering / Administration") }
    var experienceYears by remember { mutableStateOf("15+ Years") }
    var affiliatedUniversity by remember { mutableStateOf("State Technical University") }
    var campusAddress by remember { mutableStateOf("Main University Campus, Tech Enclave") }

    var expandedDesignation by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    // Brand Colors matching image
    val brandNavy = Color(0xFF0D253F)
    val brandCyan = Color(0xFF0099FF)
    val brandBlue = Color(0xFF1570EF)
    val brandSkyBg = Color(0xFFEAF3FC)
    val borderStrokeColor = Color(0xFFD0D5DD)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brandSkyBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState)
            .testTag("principal_signup_screen")
    ) {
        // --- 1. Top Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Graduation cap logo with bars icon
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0D253F)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "CareerGuide",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "CareerGuide",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = brandNavy,
                            fontSize = 18.sp,
                            letterSpacing = (-0.3).sp
                        )
                    )
                    Text(
                        text = "Student Career Guidance",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Back to Login Link
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onBackToLogin() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .testTag("btn_back_to_login"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = brandBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Back to Login",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = brandBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
            }
        }

        // --- 2. Hero Section: Headline & Image ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Headline & Subtext (approx 58% width)
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .padding(end = 8.dp)
                ) {
                    val headlineText = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = brandNavy, fontWeight = FontWeight.Bold)) {
                            append("Empowering Principals\nto Build a ")
                        }
                        withStyle(style = SpanStyle(color = brandCyan, fontWeight = FontWeight.Bold)) {
                            append("Brighter Future")
                        }
                    }
                    Text(
                        text = headlineText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            lineHeight = 26.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Monitor progress, support your faculty and students, and make data-driven decisions for a successful academic journey.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF475569),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }

                // Right: Principal overlooking university campus
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .height(135.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_principal_hero),
                        contentDescription = "Principal overseeing campus",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 3. Principal Sign Up Card ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .testTag("signup_card_surface"),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Card Header: Profile Circle + "Principal Sign Up"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = brandBlue,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Principal Sign Up",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = brandNavy,
                            fontSize = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Create your account to access the Principal Portal and manage your college efficiently.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // --- 4. Stepper (1. Account Details, 2. Personal Information, 3. College Details, 4. Verify & Complete) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StepItem(
                        stepNumber = 1,
                        label = "Account\nDetails",
                        isActive = currentStep >= 1,
                        isCurrent = currentStep == 1,
                        modifier = Modifier.weight(1f)
                    )
                    StepLine(isCompleted = currentStep > 1, modifier = Modifier.weight(0.4f))
                    StepItem(
                        stepNumber = 2,
                        label = "Personal\nInformation",
                        isActive = currentStep >= 2,
                        isCurrent = currentStep == 2,
                        modifier = Modifier.weight(1f)
                    )
                    StepLine(isCompleted = currentStep > 2, modifier = Modifier.weight(0.4f))
                    StepItem(
                        stepNumber = 3,
                        label = "College\nDetails",
                        isActive = currentStep >= 3,
                        isCurrent = currentStep == 3,
                        modifier = Modifier.weight(1f)
                    )
                    StepLine(isCompleted = currentStep > 3, modifier = Modifier.weight(0.4f))
                    StepItem(
                        stepNumber = 4,
                        label = "Verify &\nComplete",
                        isActive = currentStep >= 4,
                        isCurrent = currentStep == 4,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (errorMessage != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // --- 5. Form Fields According to Active Step ---
                if (currentStep == 1) {
                    // Row 1: Full Name * | Email Address *
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FieldLabel(text = "Full Name *")
                            CustomInputField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                placeholder = "Enter your full name",
                                leadingIcon = Icons.Outlined.Person,
                                testTag = "input_full_name"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            FieldLabel(text = "Email Address *")
                            CustomInputField(
                                value = emailAddress,
                                onValueChange = { emailAddress = it },
                                placeholder = "Enter your official email",
                                leadingIcon = Icons.Outlined.Email,
                                keyboardType = KeyboardType.Email,
                                testTag = "input_email_address"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row 2: Mobile Number * | Password *
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FieldLabel(text = "Mobile Number *")
                            CustomInputField(
                                value = mobileNumber,
                                onValueChange = { mobileNumber = it },
                                placeholder = "Enter your mobile number",
                                leadingIcon = Icons.Outlined.Phone,
                                keyboardType = KeyboardType.Phone,
                                testTag = "input_mobile_number"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            FieldLabel(text = "Password *")
                            CustomInputField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = "Create a strong password",
                                leadingIcon = Icons.Outlined.Lock,
                                isPassword = true,
                                passwordVisible = passwordVisible,
                                onToggleVisibility = { passwordVisible = !passwordVisible },
                                testTag = "input_password"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row 3: Confirm Password *
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FieldLabel(text = "Confirm Password *")
                        CustomInputField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            placeholder = "Re-enter your password",
                            leadingIcon = Icons.Outlined.Lock,
                            isPassword = true,
                            passwordVisible = confirmPasswordVisible,
                            onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible },
                            testTag = "input_confirm_password"
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row 4: Designation * | College Name *
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FieldLabel(text = "Designation *")
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = designation,
                                    onValueChange = { designation = it },
                                    readOnly = true,
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.WorkOutline,
                                            contentDescription = null,
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { expandedDesignation = !expandedDesignation }) {
                                            Icon(
                                                Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Select Designation",
                                                tint = Color(0xFF64748B)
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = brandBlue,
                                        unfocusedBorderColor = borderStrokeColor,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    ),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = brandNavy),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("dropdown_designation")
                                        .clickable { expandedDesignation = true }
                                )
                                DropdownMenu(
                                    expanded = expandedDesignation,
                                    onDismissRequest = { expandedDesignation = false }
                                ) {
                                    listOf("Principal", "Vice Principal", "Dean of Academics", "Director").forEach { roleName ->
                                        DropdownMenuItem(
                                            text = { Text(roleName) },
                                            onClick = {
                                                designation = roleName
                                                expandedDesignation = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            FieldLabel(text = "College Name *")
                            CustomInputField(
                                value = collegeName,
                                onValueChange = { collegeName = it },
                                placeholder = "Enter your college name",
                                leadingIcon = Icons.Outlined.AccountBalance,
                                testTag = "input_college_name"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row 5: College Code / AISHE Code *
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FieldLabel(text = "College Code / AISHE Code *")
                        CustomInputField(
                            value = collegeCode,
                            onValueChange = { collegeCode = it },
                            placeholder = "Enter college code",
                            leadingIcon = Icons.Outlined.Tag,
                            testTag = "input_college_code"
                        )
                    }
                } else if (currentStep == 2) {
                    // Step 2: Personal Information
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Column {
                            FieldLabel(text = "Highest Academic Qualification *")
                            CustomInputField(
                                value = qualification,
                                onValueChange = { qualification = it },
                                placeholder = "e.g. Ph.D, M.Tech, Ed.D",
                                leadingIcon = Icons.Outlined.School,
                                testTag = "input_qualification"
                            )
                        }
                        Column {
                            FieldLabel(text = "Total Experience in Academic Leadership *")
                            CustomInputField(
                                value = experienceYears,
                                onValueChange = { experienceYears = it },
                                placeholder = "e.g. 15 Years",
                                leadingIcon = Icons.Outlined.Timer,
                                testTag = "input_experience"
                            )
                        }
                    }
                } else if (currentStep == 3) {
                    // Step 3: College Details
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Column {
                            FieldLabel(text = "Affiliated University / Board *")
                            CustomInputField(
                                value = affiliatedUniversity,
                                onValueChange = { affiliatedUniversity = it },
                                placeholder = "e.g. State Technological University",
                                leadingIcon = Icons.Outlined.AccountBalance,
                                testTag = "input_affiliated_univ"
                            )
                        }
                        Column {
                            FieldLabel(text = "Campus Location & Address *")
                            CustomInputField(
                                value = campusAddress,
                                onValueChange = { campusAddress = it },
                                placeholder = "City, State, Pincode",
                                leadingIcon = Icons.Outlined.Place,
                                testTag = "input_campus_address"
                            )
                        }
                    }
                } else {
                    // Step 4: Verify & Complete
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Summary of Principal Registration:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = brandNavy)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Principal: ${fullName.ifBlank { "Dr. Meenakshi Sundaram" }}", style = MaterialTheme.typography.bodySmall)
                            Text("Official Email: ${emailAddress.ifBlank { "principal@careerbridge.edu" }}", style = MaterialTheme.typography.bodySmall)
                            Text("Institution: ${collegeName.ifBlank { "National Institute of Engineering & Technology" }}", style = MaterialTheme.typography.bodySmall)
                            Text("Designation: $designation", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ready to establish institutional governance credentials.", style = MaterialTheme.typography.labelSmall, color = Color(0xFF16A34A))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // --- 6. Primary Button: Next: Personal Information -> ---
                Button(
                    onClick = {
                        if (currentStep < 4) {
                            currentStep++
                        } else {
                            // Register or log in directly into Principal Portal
                            val targetEmail = emailAddress.ifBlank { "principal@careerbridge.edu" }
                            val targetName = fullName.ifBlank { "Dr. Meenakshi Sundaram" }
                            val targetPass = password.ifBlank { "password123" }
                            viewModel.register(targetName, targetEmail, targetPass, targetPass, UserRole.PRINCIPAL)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_next_step"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = brandBlue)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = when (currentStep) {
                                1 -> "Next: Personal Information"
                                2 -> "Next: College Details"
                                3 -> "Next: Verify & Complete"
                                else -> "Complete Registration & Access Portal"
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // --- 7. Divider: OR ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                    Text(
                        text = "OR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                }

                Spacer(modifier = Modifier.height(18.dp))

                // --- 8. Secondary Button: Sign Up with Google ---
                OutlinedButton(
                    onClick = {
                        // Instant Google Sign-in demo as Principal
                        viewModel.loginAsRole(UserRole.PRINCIPAL)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_google_signup"),
                    shape = RoundedCornerShape(10.dp),
                    border = borderStrokeColor.let { androidx.compose.foundation.BorderStroke(1.dp, it) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = brandNavy)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GoogleLogoIcon(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sign Up with Google",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = brandNavy,
                                fontSize = 14.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // --- 9. Footer: Already have an account? Login ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B), fontSize = 14.sp)
                    )
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = brandBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .clickable { onBackToLogin() }
                            .testTag("link_login_footer")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Switch to other role demo logins
                TextButton(
                    onClick = { showLoginDialog = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally).testTag("btn_switch_portal_roles")
                ) {
                    Text(
                        text = "Switch Role Portal (Student / Teacher / Parent / Recruiter)",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF0284C7))
                    )
                }
            }
        }
    }

    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Select Portal to Launch") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        UserRole.PRINCIPAL to "🏛️ Principal & Management Portal",
                        UserRole.STUDENT to "🎓 Student Dashboard & AI Guidance",
                        UserRole.TEACHER to "👩‍🏫 Teacher & Attendance Portal",
                        UserRole.PARENT to "👨‍👩‍👦 Parent Student Portal",
                        UserRole.PLACEMENT_OFFICER to "🎯 Placement Management Cell",
                        UserRole.RECRUITER to "🏢 Company Recruiter Drive"
                    ).forEach { (r, label) ->
                        Button(
                            onClick = {
                                viewModel.loginAsRole(r)
                                showLoginDialog = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = if (r == UserRole.PRINCIPAL) brandBlue else MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                label,
                                color = if (r == UserRole.PRINCIPAL) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B),
            fontSize = 12.sp
        ),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onToggleVisibility: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    testTag: String
) {
    val borderStrokeColor = Color(0xFFD0D5DD)
    val brandBlue = Color(0xFF1570EF)
    val brandNavy = Color(0xFF0D253F)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF94A3B8), fontSize = 13.sp)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(19.dp)
            )
        },
        trailingIcon = if (isPassword && onToggleVisibility != null) {
            {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = brandBlue,
            unfocusedBorderColor = borderStrokeColor,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = brandNavy, fontSize = 13.sp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    )
}

@Composable
fun StepItem(
    stepNumber: Int,
    label: String,
    isActive: Boolean,
    isCurrent: Boolean,
    modifier: Modifier = Modifier
) {
    val brandBlue = Color(0xFF1570EF)
    val inactiveColor = Color(0xFF94A3B8)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isActive) brandBlue else Color.White)
                .border(
                    width = if (isActive) 0.dp else 1.5.dp,
                    color = if (isActive) brandBlue else Color(0xFFCBD5E1),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepNumber",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.White else inactiveColor,
                    fontSize = 12.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent) brandBlue else inactiveColor,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StepLine(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val brandBlue = Color(0xFF1570EF)
    val inactiveLineColor = Color(0xFFE2E8F0)

    Box(
        modifier = modifier
            .padding(top = 13.dp)
            .height(2.dp)
            .background(if (isCompleted) brandBlue else inactiveLineColor)
    )
}

@Composable
fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f
        val radius = width * 0.42f
        val strokeW = width * 0.18f

        // Draw Google G arcs: Blue, Green, Yellow, Red
        // Red top arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 180f,
            sweepAngle = 120f,
            useCenter = false,
            style = Stroke(width = strokeW),
            topLeft = Offset(cx - radius, cy - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
        )
        // Yellow left-bottom arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 120f,
            sweepAngle = 60f,
            useCenter = false,
            style = Stroke(width = strokeW),
            topLeft = Offset(cx - radius, cy - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
        )
        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 0f,
            sweepAngle = 120f,
            useCenter = false,
            style = Stroke(width = strokeW),
            topLeft = Offset(cx - radius, cy - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
        )
        // Blue right & bar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -30f,
            sweepAngle = 70f,
            useCenter = false,
            style = Stroke(width = strokeW),
            topLeft = Offset(cx - radius, cy - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
        )
        // Blue horizontal crossbar
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(cx, cy),
            end = Offset(cx + radius + strokeW / 4f, cy),
            strokeWidth = strokeW
        )
    }
}
