package com.example.praktikumpapb.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TugasRepository(application: Application) {
    private val tugasDAO: TugasDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = TugasDB.getDatabase(application)
        tugasDAO = db.tugasDao()
    }

    fun getAllTugas(): LiveData<List<Tugas>> = tugasDAO.getAllTugas()

    fun insert(tugas: Tugas) {
        executorService.execute { tugasDAO.insertTugas(tugas) }
    }

    fun update(tugas: Tugas) {
        executorService.execute { tugasDAO.updateTugas(tugas) }
    }

    fun delete(tugas: Tugas) {
        executorService.execute { tugasDAO.deleteTugas(tugas) }
    }
}
