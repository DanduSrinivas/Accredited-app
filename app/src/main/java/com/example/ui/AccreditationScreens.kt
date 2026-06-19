package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.EvidenceItem
import com.example.data.GapItem
import com.example.data.NarrativeSection
import java.text.SimpleDateFormat
import java.util.*

enum class AppTab {
    Dashboard,
    Tracker,
    Repository,
    ReportBuilder
}

// Accent & Aesthetic Colors (Beautifully matched to the "Natural Tones" design guidelines)
val NavyDark = Color(0xFF233F2F)      // Rich Forest Pine Green
val NavyLight = Color(0xFF405D4C)     // Clean Earthy Sage Spruce
val SecondaryBlue = Color(0xFF5A7A68) // Muted Moss Green Accent
val TealAccent = Color(0xFF059669)     // Rich Herb Green
val ErrorAccent = Color(0xFF9B1C1C)    // Warm Terracotta Red
val AmberAccent = Color(0xFFD97706)    // Sunset Clay Ochre Orange
val CardBackground = Color(0xFFFFFFFF) // Fresh clean card background
val PageBackground = Color(0xFFF3F5F3) // Soft warm sage-tinted milk background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccreditationPortalApp(
    viewModel: AccreditationViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(AppTab.Dashboard) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val gapsState by viewModel.gaps.collectAsState()
    val evidenceState by viewModel.evidence.collectAsState()
    val narrativesState by viewModel.narratives.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "Portal Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "Institutional Portal",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyDark
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .background(SecondaryBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JD",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.Dashboard,
                    onClick = { currentTab = AppTab.Dashboard },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = NavyDark,
                        indicatorColor = PageBackground
                    ),
                    modifier = Modifier.testTag("tab_dashboard")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.Tracker,
                    onClick = { currentTab = AppTab.Tracker },
                    icon = { Icon(Icons.Default.FactCheck, contentDescription = "Tracker") },
                    label = { Text("Tracker") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = NavyDark,
                        indicatorColor = PageBackground
                    ),
                    modifier = Modifier.testTag("tab_tracker")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.Repository,
                    onClick = { currentTab = AppTab.Repository },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "Repository") },
                    label = { Text("Repository") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = NavyDark,
                        indicatorColor = PageBackground
                    ),
                    modifier = Modifier.testTag("tab_repository")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.ReportBuilder,
                    onClick = { currentTab = AppTab.ReportBuilder },
                    icon = { Icon(Icons.Default.EditNote, contentDescription = "SSR Editor") },
                    label = { Text("SSR Editor") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = NavyDark,
                        indicatorColor = PageBackground
                    ),
                    modifier = Modifier.testTag("tab_editor")
                )
            }
        },
        containerColor = PageBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    AppTab.Dashboard -> DashboardScreen(
                        gaps = gapsState,
                        evidence = evidenceState,
                        onNavigateToRepository = { currentTab = AppTab.Repository }
                    )
                    AppTab.Tracker -> TrackerScreen(
                        gaps = gapsState,
                        onAssignClosure = { gapId, name ->
                            viewModel.assignGapClosure(gapId, name)
                        },
                        onNotifyHead = { dept, criteria ->
                            // Simple simulation trigger
                        }
                    )
                    AppTab.Repository -> RepositoryScreen(
                        evidence = evidenceState,
                        onUpload = { item -> viewModel.addEvidenceItem(item) },
                        onDelete = { item -> viewModel.removeEvidenceItem(item) }
                    )
                    AppTab.ReportBuilder -> ReportBuilderScreen(
                        narratives = narrativesState,
                        onSaveDraft = { id, content ->
                            viewModel.updateNarrativeSection(id, content)
                        }
                    )
                }
            }
        }
    }
}

// ==========================================
// SCREEN 1: EXECUTIVE DASHBOARD
// ==========================================
@Composable
fun DashboardScreen(
    gaps: List<GapItem>,
    evidence: List<EvidenceItem>,
    onNavigateToRepository: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Headline
        Column {
            Text(
                text = "Institutional Readiness",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyDark
                )
            )
            Text(
                text = "Accreditation Self-Study & Gap Verification audit desk.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Horizontal HUD Cards - Matched with HTML designs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardKpiCard(
                modifier = Modifier.weight(1f),
                title = "Days to NAAC Submission",
                value = "42 Days",
                icon = Icons.Default.CalendarToday,
                color = SecondaryBlue
            )
            DashboardKpiCard(
                modifier = Modifier.weight(1f),
                title = "Pending Reviews",
                value = "127 Documents",
                icon = Icons.Default.AssignmentLate,
                color = ErrorAccent
            )
        }

        // Central Readiness circular chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Overall Accreditation Readiness",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val scoreFraction = 0.72f
                    val sweepDegrees by animateFloatAsState(
                        targetValue = scoreFraction * 360f,
                        animationSpec = tween(1000),
                        label = "ReadyProg"
                    )

                    Canvas(modifier = Modifier.size(140.dp)) {
                        // Track Arc
                        drawArc(
                            color = PageBackground,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                        )
                        // Active Arc
                        drawArc(
                            brush = Brush.linearGradient(
                                colors = listOf(SecondaryBlue, TealAccent)
                            ),
                            startAngle = -90f,
                            sweepAngle = sweepDegrees,
                            useCenter = false,
                            style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "72%",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 36.sp
                            ),
                            color = NavyDark
                        )
                        Text(
                            text = "Target 90%",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IndicatorDot(color = SecondaryBlue, text = "Current Readiness")
                    IndicatorDot(color = Color.LightGray, text = "Submission Gaps")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your college is on track for an A+ grade. Address 14 remaining gaps to finalize files.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // Custom Department comparison score bar chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Accreditation Score by Department",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark
                )
                Spacer(modifier = Modifier.height(16.dp))

                DepartmentBarItem(departmentName = "Computer Science", percentage = 88f, color = TealAccent)
                DepartmentBarItem(departmentName = "Mechanical Eng.", percentage = 64f, color = SecondaryBlue)
                DepartmentBarItem(departmentName = "Electrical & Electronics", percentage = 76f, color = SecondaryBlue)
                DepartmentBarItem(departmentName = "Business Administration", percentage = 52f, color = ErrorAccent)
                DepartmentBarItem(departmentName = "Applied Sciences", percentage = 81f, color = TealAccent)
            }
        }

        // Recent activity flow
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History Tracker",
                        tint = SecondaryBlue
                    )
                    Text(
                        text = "Recent Task Feed",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                ActivityItemRow(
                    title = "Dr. Sarah Jenkins updated Criterion 1.2 narrative.",
                    subText = "2 hours ago • Self-Study Report drafting",
                    icon = Icons.Default.Description,
                    iconColor = TealAccent
                )
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = PageBackground)
                ActivityItemRow(
                    title = "Sarah Hughes uploaded standard Criterion 2.1 evidence.",
                    subText = "15 mins ago • CSE Department",
                    icon = Icons.Default.FileUpload,
                    iconColor = SecondaryBlue
                )
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = PageBackground)
                ActivityItemRow(
                    title = "Reviewer Beta flagged 3 critical placement gaps.",
                    subText = "4 hours ago • MBA Dept audits",
                    icon = Icons.Default.Warning,
                    iconColor = ErrorAccent
                )
            }
        }

        // Evidence Vault quick access banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToRepository() },
            colors = CardDefaults.cardColors(containerColor = NavyDark)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Evidence Vault Repository",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "4,281 global files hosted. Vetted, signed off directories.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                }
                Icon(
                    imageVector = Icons.Default.FolderOpen,
                    contentDescription = "Open Folder",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun DashboardKpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = NavyDark
            )
        }
    }
}

@Composable
fun IndicatorDot(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun DepartmentBarItem(
    departmentName: String,
    percentage: Float,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = departmentName, style = MaterialTheme.typography.bodySmall, color = NavyDark, fontWeight = FontWeight.Medium)
            Text(text = "${percentage.toInt()}%", style = MaterialTheme.typography.bodySmall, color = SecondaryBlue, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(PageBackground, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .height(8.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun ActivityItemRow(
    title: String,
    subText: String,
    icon: ImageVector,
    iconColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = NavyDark
            )
            Text(
                text = subText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}


// ==========================================
// SCREEN 2: ACCREDITATION GAP TRACKER (HEATMAP)
// ==========================================
private val departmentList = listOf("Computer Science", "Mechanical Engineering", "Humanities & Social Sciences", "Business Administration", "Life Sciences")
private val criteriaList = listOf("1.1 Student Placement", "1.2 Faculty Research", "2.1 Infrastructure Log", "2.4 Budget Utilization")

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    gaps: List<GapItem>,
    onAssignClosure: (Int, String) -> Unit,
    onNotifyHead: (String, String) -> Unit
) {
    var selectedDept by remember { mutableStateOf(departmentList[0]) }
    var selectedCriteria by remember { mutableStateOf(criteriaList[0]) }
    var showAssignDialog by remember { mutableStateOf(false) }
    var assigneeNameInput by remember { mutableStateOf("") }
    
    // Find matching gap in state
    val matchedGap = gaps.find { 
        it.department.contains(selectedDept, ignoreCase = true) && 
        it.criteria.contains(selectedCriteria, ignoreCase = true) 
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tracker Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Gap Analysis Heatmap matrix",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Track file completeness, missing evidence and allocate closures to coordinators.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                )
            }
        }

        // Layout: Grid Selection Chips
        Text(
            text = "Step 1: Choose Intersection Cell",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
        )

        // Dropdowns or scrolling choices for coordinate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Select Department", style = MaterialTheme.typography.labelSmall, color = NavyDark, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                DepartmentDropdownSelector(
                    selected = selectedDept,
                    options = departmentList,
                    onSelect = { selectedDept = it }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Select Criteria", style = MaterialTheme.typography.labelSmall, color = NavyDark, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                CriteriaDropdownSelector(
                    selected = selectedCriteria,
                    options = criteriaList,
                    onSelect = { selectedCriteria = it }
                )
            }
        }

        // visual representation matching HTML cell grid indicator
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val statusColor = when (matchedGap?.status) {
                        "Ready" -> TealAccent
                        "Insufficient" -> AmberAccent
                        "Missing" -> ErrorAccent
                        else -> Color.DarkGray
                    }
                    val statusIcon = when (matchedGap?.status) {
                        "Ready" -> Icons.Default.CheckCircle
                        "Insufficient" -> Icons.Default.Warning
                        "Missing" -> Icons.Default.Cancel
                        else -> Icons.Default.HelpOutline
                    }

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = matchedGap?.status ?: "Unknown",
                            tint = statusColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedDept,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyDark
                        )
                        Text(
                            text = selectedCriteria,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = matchedGap?.status ?: "No state",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = statusColor
                        )
                    }
                }
            }
        }

        // Interactive audit information drawer block based on selected cell
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Audit Findings & Narrative Logs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (matchedGap != null) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Issue Description:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SecondaryBlue
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = matchedGap.issueDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }

                        item {
                            Text(
                                text = "Audit Checkpoints:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SecondaryBlue
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            matchedGap.findings.split("\n").forEach { bullet ->
                                if (bullet.isNotBlank()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (matchedGap.status == "Ready") Icons.Default.Check else Icons.Default.Cancel,
                                            contentDescription = null,
                                            tint = if (matchedGap.status == "Ready") TealAccent else ErrorAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = bullet,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Assigned Coordinator:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SecondaryBlue
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (matchedGap.assignedTo != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = "Assigned",
                                        tint = TealAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "${matchedGap.assignedTo} (Allocated)",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = NavyDark
                                    )
                                }
                            } else {
                                Text(
                                    text = "Unassigned Gaps (Action Required)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = ErrorAccent
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Assign Coordinator buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                assigneeNameInput = matchedGap.assignedTo ?: ""
                                showAssignDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Assign Gap Clo.")
                        }

                        OutlinedButton(
                            onClick = {
                                android.widget.Toast.makeText(
                                    context,
                                    "Notification email dispatched to Head of $selectedDept",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            },
                            border = BorderStroke(1.dp, NavyDark),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.MailOutline, contentDescription = null, tint = NavyDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Notify Head", color = NavyDark)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No audit findings found.")
                    }
                }
            }
        }
    }

    // Assign Dialog Component
    if (showAssignDialog && matchedGap != null) {
        Dialog(onDismissRequest = { showAssignDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Assign Gap Closure Task",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Specify the coordinator name responsible for validating ${selectedCriteria} in ${selectedDept}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = assigneeNameInput,
                        onValueChange = { assigneeNameInput = it },
                        label = { Text("Assignee / Coordinator Name") },
                        singleLine = true,
                        placeholder = { Text("e.g. Dr. Jennifer Sharma") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = { showAssignDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (assigneeNameInput.isNotBlank()) {
                                    onAssignClosure(matchedGap.id, assigneeNameInput)
                                    showAssignDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Allocate")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentDropdownSelector(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onSelect(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriteriaDropdownSelector(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onSelect(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}


// ==========================================
// SCREEN 3: EVIDENCE REPOSITORY
// ==========================================
@Composable
fun RepositoryScreen(
    evidence: List<EvidenceItem>,
    onUpload: (EvidenceItem) -> Unit,
    onDelete: (EvidenceItem) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedAyFilter by remember { mutableStateOf("All") }
    var selectedDeptFilter by remember { mutableStateOf("All") }
    var showUploadDialog by remember { mutableStateOf(false) }

    // Dialog state
    var newDocName by remember { mutableStateOf("") }
    var newDocSize by remember { mutableStateOf("") }
    var newDocAy by remember { mutableStateOf("2023-2024") }
    var newDocDept by remember { mutableStateOf("QA Office") }
    var newDocAuthor by remember { mutableStateOf("Sarah Hughes") }

    val filteredList = evidence.filter { item ->
        val matchesQuery = item.documentName.contains(query, ignoreCase = true)
        val matchesAy = selectedAyFilter == "All" || item.academicYear == selectedAyFilter
        val matchesDept = selectedDeptFilter == "All" || item.department == selectedDeptFilter
        matchesQuery && matchesAy && matchesDept
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Input Row
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                placeholder = { Text("Search evidence files...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Filtering Chips (Row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // AY Filter select
                RepositoryFilterDropdown(
                    label = "AY: $selectedAyFilter",
                    options = listOf("All", "2023-2024", "2022-2023"),
                    onSelect = { selectedAyFilter = it },
                    modifier = Modifier.weight(1f)
                )

                // Department Filter select
                RepositoryFilterDropdown(
                    label = "Dept: $selectedDeptFilter",
                    options = listOf("All", "QA Office", "Strategy Unit", "Registrar"),
                    onSelect = { selectedDeptFilter = it },
                    modifier = Modifier.weight(11f)
                )
            }

            // Evidence Tree header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Vetted Files (${filteredList.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark
                )
                Text(
                    text = "AY $selectedAyFilter",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            // Scrollable List of Files
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (filteredList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No items matching current filters.", color = Color.Gray)
                            }
                        }
                    }
                } else {
                    items(filteredList) { item ->
                        EvidenceRowItem(
                            item = item,
                            onDelete = { onDelete(item) }
                        )
                    }
                }
            }
        }

        // Float Upload Button
        FloatingActionButton(
            onClick = { showUploadDialog = true },
            containerColor = SecondaryBlue,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("upload_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = "Upload")
                Text("Bulk Upload", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }

    // Upload dialog content
    if (showUploadDialog) {
        Dialog(onDismissRequest = { showUploadDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Add Evidence to Vault",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )

                    OutlinedTextField(
                        value = newDocName,
                        onValueChange = { newDocName = it },
                        label = { Text("Document Name") },
                        placeholder = { Text("e.g. Lab_Equipment_Audit.pdf") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newDocSize,
                            onValueChange = { newDocSize = it },
                            label = { Text("File Size") },
                            placeholder = { Text("1.5 MB") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newDocAy,
                            onValueChange = { newDocAy = it },
                            label = { Text("Acad. Year") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = newDocDept,
                        onValueChange = { newDocDept = it },
                        label = { Text("Department / Unit") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newDocAuthor,
                        onValueChange = { newDocAuthor = it },
                        label = { Text("Uploaded By") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = { showUploadDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Dismiss")
                        }
                        Button(
                            onClick = {
                                if (newDocName.isNotBlank()) {
                                    val finalSize = if (newDocSize.isBlank()) "1.0 MB" else newDocSize
                                    onUpload(
                                        EvidenceItem(
                                            documentName = newDocName,
                                            sizeLabel = finalSize,
                                            academicYear = newDocAy,
                                            department = newDocDept,
                                            status = "Draft",
                                            uploadedBy = newDocAuthor
                                        )
                                    )
                                    // Reset inputs
                                    newDocName = ""
                                    newDocSize = ""
                                    showUploadDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Store")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EvidenceRowItem(
    item: EvidenceItem,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, PageBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Document type thumbnail avatar indicator
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(NavyDark.copy(alpha = 0.08f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        item.documentName.endsWith(".pdf", ignoreCase = true) -> Icons.Default.PictureAsPdf
                        item.documentName.endsWith(".jpg", ignoreCase = true) || item.documentName.endsWith(".png", ignoreCase = true) -> Icons.Default.Image
                        else -> Icons.Default.Description
                    },
                    contentDescription = null,
                    tint = SecondaryBlue
                )
            }

            // Name details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.documentName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.sizeLabel} • ${item.department} • Uploaded by ${item.uploadedBy}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Status chip
            val statusColor = when (item.status) {
                "Verified" -> TealAccent
                "Draft" -> AmberAccent
                "Locked" -> NavyLight
                else -> ErrorAccent
            }

            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = item.status,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = statusColor
                )
            }

            // Delete action
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove file",
                    tint = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryFilterDropdown(
    label: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedCard(
            onClick = { expanded = true },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onSelect(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}


// ==========================================
// SCREEN 4: NARRATIVE REPORT BUILDER (SSR)
// ==========================================
@Composable
fun ReportBuilderScreen(
    narratives: List<NarrativeSection>,
    onSaveDraft: (String, String) -> Unit
) {
    var selectedNarrativeIndex by remember { mutableIntStateOf(0) }
    
    val currentNarrative = narratives.getOrNull(selectedNarrativeIndex)

    var editorText by remember { mutableStateOf("") }
    var lastSyncedId by remember { mutableStateOf("") }
    var showGuidelineCard by remember { mutableStateOf(false) }

    // Sync state when selection changes
    if (currentNarrative != null && currentNarrative.id != lastSyncedId) {
        editorText = currentNarrative.narrativeContent
        lastSyncedId = currentNarrative.id
    }

    val liveWordCount = remember(editorText) {
        editorText.trim().split("\\s+".toRegex()).count { it.isNotEmpty() }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Horizonal switcher of available criteria sections
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            narratives.forEachIndexed { idx, section ->
                FilterChip(
                    selected = selectedNarrativeIndex == idx,
                    onClick = { selectedNarrativeIndex = idx },
                    label = { Text("Crit ${section.id}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavyDark,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (currentNarrative != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = currentNarrative.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                    Text(
                        text = "Assigned Editor: ${currentNarrative.editor}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = { showGuidelineCard = !showGuidelineCard },
                    modifier = Modifier.background(NavyDark.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Show Guidelines",
                        tint = NavyDark
                    )
                }
            }

            // Word Limit guidelines toggler card block
            AnimatedVisibility(visible = showGuidelineCard) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Accreditation guidelines & criteria context:",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Detail vocational program additions.\n• Incorporate choice based credit system (CBCS).\n• Summarize stakeholder feedback alignment.\n• Max word target: 500 words.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Textarea editor
            OutlinedTextField(
                value = editorText,
                onValueChange = { editorText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("narrative_editor_input"),
                placeholder = { Text("Typing accreditation narrative descriptions here...") },
                textStyle = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecondaryBlue,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            // Editor state statistics & counts row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Word Count:",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$liveWordCount / ${currentNarrative.targetWordLimit}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (liveWordCount > currentNarrative.targetWordLimit) ErrorAccent else TealAccent
                    )
                }

                Button(
                    onClick = {
                        onSaveDraft(currentNarrative.id, editorText)
                        android.widget.Toast.makeText(context, "Draft has been auto-saved to Room", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Draft", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

// Icon helper
fun ImageVector.size(dp: Int): Modifier = Modifier.size(dp.dp)
