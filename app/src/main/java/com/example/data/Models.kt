package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gap_items")
data class GapItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val department: String,
    val criteria: String,
    val status: String, // "Ready", "Insufficient", "Missing"
    val issueDescription: String,
    val findings: String, // Separated by newlines
    val assignedTo: String? = null,
    val assignedDate: Long? = null
)

@Entity(tableName = "evidence_items")
data class EvidenceItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val documentName: String,
    val sizeLabel: String,
    val academicYear: String,
    val department: String,
    val status: String, // "Verified", "Draft", "Locked", "Incomplete"
    val uploadedBy: String,
    val uploadedDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "narrative_sections")
data class NarrativeSection(
    @PrimaryKey val id: String, // e.g. "1.1", "1.2", "1.3"
    val title: String,
    val narrativeContent: String,
    val targetWordLimit: Int = 500,
    val qualityCheckPassed: Boolean = false,
    val lastEdited: Long = System.currentTimeMillis(),
    val editor: String = "Dr. Jenkins"
)
