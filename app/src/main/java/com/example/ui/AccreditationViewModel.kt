package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccreditationViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AccreditationDatabase.getDatabase(application)
    private val repository = AccreditationRepository(database.dao())

    val gaps: StateFlow<List<GapItem>> = repository.allGaps
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val evidence: StateFlow<List<EvidenceItem>> = repository.allEvidence
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val narratives: StateFlow<List<NarrativeSection>> = repository.allNarratives
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        seedInitialDataIfEmpty()
    }

    private fun seedInitialDataIfEmpty() {
        viewModelScope.launch {
            // Check if narratives are empty
            repository.allNarratives.first().let { currentList ->
                if (currentList.isEmpty()) {
                    // Seed narratives
                    repository.insertNarrative(
                        NarrativeSection(
                            id = "1.2",
                            title = "1.2 Academic Flexibility",
                            narrativeContent = "The institution has established a robust framework for academic flexibility, ensuring that curricula are responsive to the evolving needs of the global job market while remaining rooted in core academic values. All departments have successfully transitioned to the Choice Based Credit System (CBCS), allowing students to select from a diverse range of elective courses across disciplines.\n\nIn the last five years, we have introduced 14 new vocational programs and over 40 certificate courses aimed at skill enhancement. The curriculum design process involves extensive consultation with industry experts, alumni, and academic peers, ensuring that the learning outcomes are measurable and aligned with the National Skills Qualifications Framework (NSQF).\n\nFurthermore, the institution has pioneered interdisciplinary projects that encourage students to apply theoretical knowledge to real-world challenges, particularly in the fields of sustainable development and digital ethics...",
                            targetWordLimit = 500,
                            qualityCheckPassed = true,
                            editor = "Dr. Sarah Jenkins"
                        )
                    )
                    repository.insertNarrative(
                        NarrativeSection(
                            id = "1.1",
                            title = "1.1 Curriculum Design & Development",
                            narrativeContent = "Curriculum development is conducted following strict systematic workflows with stakeholder consultations. We ensure program educational objectives are clearly defined for all courses.",
                            targetWordLimit = 500,
                            qualityCheckPassed = true,
                            editor = "Dr. Sharma"
                        )
                    )
                    repository.insertNarrative(
                        NarrativeSection(
                            id = "1.3",
                            title = "1.3 Curriculum Enrichment",
                            narrativeContent = "Academic programs integrate cross-cutting issues relevant to professional ethics, gender, human values, environment and sustainability into the curriculum through specialized electives and projects.",
                            targetWordLimit = 500,
                            qualityCheckPassed = false,
                            editor = "Dr. Hughes"
                        )
                    )
                }
            }

            // Check if gaps are empty
            repository.allGaps.first().let { currentList ->
                if (currentList.isEmpty()) {
                    repository.insertGap(
                        GapItem(
                            department = "Computer Science",
                            criteria = "1.1 Student Placement",
                            status = "Ready",
                            issueDescription = "All 450 student records verified for the current cycle. Audit trail complete.",
                            findings = "All records vetted\n450 placements signed off",
                            assignedTo = "Dr. Sharma",
                            assignedDate = System.currentTimeMillis()
                        )
                    )
                    repository.insertGap(
                        GapItem(
                            department = "Mechanical Engineering",
                            criteria = "1.1 Student Placement",
                            status = "Insufficient",
                            issueDescription = "Missing digital copies of internship certificates for 15 students in the Automotive stream.",
                            findings = "15 certificates missing in Automotive stream\nRequires signature from Director of Placements",
                            assignedTo = null
                        )
                    )
                    repository.insertGap(
                        GapItem(
                            department = "Humanities & Social Sciences",
                            criteria = "1.1 Student Placement",
                            status = "Missing",
                            issueDescription = "Complete failure to submit 2024-25 Placement Certificates. Deadline exceeded by 14 days.",
                            findings = "Zero placement records submitted\nPast due by 2 weeks",
                            assignedTo = null
                        )
                    )
                    repository.insertGap(
                        GapItem(
                            department = "Business Administration",
                            criteria = "1.1 Student Placement",
                            status = "Ready",
                            issueDescription = "Verification complete. High alignment with NAAC criteria observed.",
                            findings = "All 120 MBA records processed\nExcellent documentation standards",
                            assignedTo = "Sarah Jenkins",
                            assignedDate = System.currentTimeMillis()
                        )
                    )
                    repository.insertGap(
                        GapItem(
                            department = "Life Sciences",
                            criteria = "1.1 Student Placement",
                            status = "Missing",
                            issueDescription = "Database sync failure. No verifiable placement data exists for the current reporting period.",
                            findings = "Data sync failure on server\nUnable to verify online listings",
                            assignedTo = null
                        )
                    )
                    repository.insertGap(
                        GapItem(
                            department = "Computer Science",
                            criteria = "2.1 Infrastructure Log",
                            status = "Missing",
                            issueDescription = "No catalog of laboratory hardware upgrades exists for FY 24-25.",
                            findings = "Missing upgrade catalog\nQA audit flagged 3 hardware clusters outstanding",
                            assignedTo = null
                        )
                    )
                    repository.insertGap(
                        GapItem(
                            department = "Mechanical Engineering",
                            criteria = "2.4 Budget Utilization",
                            status = "Missing",
                            issueDescription = "No documentation of expenditure for workshop expansion.",
                            findings = "Workshop invoices not filed\nPending finance department approval",
                            assignedTo = null
                        )
                    )
                }
            }

            // Check if evidence is empty
            repository.allEvidence.first().let { currentList ->
                if (currentList.isEmpty()) {
                    repository.insertEvidence(
                        EvidenceItem(
                            documentName = "Mission_Statement_Final_v2.pdf",
                            sizeLabel = "1.2 MB",
                            academicYear = "2023-2024",
                            department = "QA Office",
                            status = "Verified",
                            uploadedBy = "Sarah Hughes"
                        )
                    )
                    repository.insertEvidence(
                        EvidenceItem(
                            documentName = "Strategic_Map_Visual.jpg",
                            sizeLabel = "4.5 MB",
                            academicYear = "2023-2024",
                            department = "Strategy Unit",
                            status = "Draft",
                            uploadedBy = "Marcus King"
                        )
                    )
                    repository.insertEvidence(
                        EvidenceItem(
                            documentName = "Governance_Chart_Locked.pdf",
                            sizeLabel = "2.1 MB",
                            academicYear = "2022-2023",
                            department = "Registrar",
                            status = "Locked",
                            uploadedBy = "Admin Lead"
                        )
                    )
                    repository.insertEvidence(
                        EvidenceItem(
                            documentName = "Review_Board_Minutes_Q3.docx",
                            sizeLabel = "850 KB",
                            academicYear = "2023-2024",
                            department = "QA Office",
                            status = "Incomplete",
                            uploadedBy = "Sarah Hughes"
                        )
                    )
                }
            }
        }
    }

    // Actions
    fun assignGapClosure(gapId: Int, assignedTo: String) {
        viewModelScope.launch {
            val list = gaps.value
            val match = list.find { it.id == gapId }
            if (match != null) {
                repository.updateGap(match.copy(assignedTo = assignedTo, assignedDate = System.currentTimeMillis()))
            }
        }
    }

    fun addGapItem(gap: GapItem) {
        viewModelScope.launch {
            repository.insertGap(gap)
        }
    }

    fun addEvidenceItem(evidenceItem: EvidenceItem) {
        viewModelScope.launch {
            repository.insertEvidence(evidenceItem)
        }
    }

    fun removeEvidenceItem(evidenceItem: EvidenceItem) {
        viewModelScope.launch {
            repository.deleteEvidence(evidenceItem)
        }
    }

    fun updateNarrativeSection(id: String, content: String) {
        viewModelScope.launch {
            val list = narratives.value
            val match = list.find { it.id == id }
            if (match != null) {
                repository.insertNarrative(match.copy(
                    narrativeContent = content,
                    lastEdited = System.currentTimeMillis(),
                    qualityCheckPassed = content.trim().split("\\s+".toRegex()).count { it.isNotEmpty() } <= match.targetWordLimit
                ))
            }
        }
    }
}
