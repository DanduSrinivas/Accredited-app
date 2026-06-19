package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccreditationDao {
    // Gap Items
    @Query("SELECT * FROM gap_items")
    fun getAllGaps(): Flow<List<GapItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGap(gap: GapItem)

    @Update
    suspend fun updateGap(gap: GapItem)

    @Delete
    suspend fun deleteGap(gap: GapItem)

    // Evidence Items
    @Query("SELECT * FROM evidence_items ORDER BY uploadedDate DESC")
    fun getAllEvidence(): Flow<List<EvidenceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: EvidenceItem)

    @Delete
    suspend fun deleteEvidence(evidence: EvidenceItem)

    // Narrative Sections
    @Query("SELECT * FROM narrative_sections")
    fun getAllNarratives(): Flow<List<NarrativeSection>>

    @Query("SELECT * FROM narrative_sections WHERE id = :id")
    suspend fun getNarrativeById(id: String): NarrativeSection?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNarrative(narrative: NarrativeSection)
}
