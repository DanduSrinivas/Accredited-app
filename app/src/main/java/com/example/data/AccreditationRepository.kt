package com.example.data

import kotlinx.coroutines.flow.Flow

class AccreditationRepository(private val dao: AccreditationDao) {
    val allGaps: Flow<List<GapItem>> = dao.getAllGaps()
    val allEvidence: Flow<List<EvidenceItem>> = dao.getAllEvidence()
    val allNarratives: Flow<List<NarrativeSection>> = dao.getAllNarratives()

    suspend fun insertGap(gap: GapItem) = dao.insertGap(gap)
    suspend fun updateGap(gap: GapItem) = dao.updateGap(gap)
    suspend fun deleteGap(gap: GapItem) = dao.deleteGap(gap)

    suspend fun insertEvidence(evidence: EvidenceItem) = dao.insertEvidence(evidence)
    suspend fun deleteEvidence(evidence: EvidenceItem) = dao.deleteEvidence(evidence)

    suspend fun insertNarrative(narrative: NarrativeSection) = dao.insertNarrative(narrative)
    suspend fun getNarrativeById(id: String): NarrativeSection? = dao.getNarrativeById(id)
}
