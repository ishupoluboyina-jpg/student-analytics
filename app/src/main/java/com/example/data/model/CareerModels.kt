package com.example.data.model

data class RoadmapStep(
    val stage: String,
    val duration: String,
    val topics: List<String>
)

data class CareerRecommendation(
    val title: String,
    val domain: String,
    val matchScore: Int,
    val salaryRange: String,
    val whyItSuitsYou: String,
    val requiredSkills: List<String>,
    val currentSkillGaps: List<String>,
    val recommendedCertifications: List<String>,
    val recommendedProjects: List<String>,
    val roadmap: List<RoadmapStep>
)

data class AiChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class PerformanceMetrics(
    val cgpa: Double,
    val academicSuccessScore: Int,
    val placementReadinessScore: Int,
    val attendanceRate: Double,
    val backlogs: Int,
    val riskLevel: String // "Low Risk", "Moderate", "At Risk"
)
