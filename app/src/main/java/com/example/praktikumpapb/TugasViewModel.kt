package com.example.praktikumpapb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.praktikumpapb.local.Tugas
import com.example.praktikumpapb.local.TugasRepository

class TugasViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TugasRepository = TugasRepository(application)

    fun getAllTugas(): LiveData<List<Tugas>> = repository.getAllTugas()

    fun insert(tugas: Tugas) {
        repository.insert(tugas)
    }

    fun update(tugas: Tugas) {
        repository.update(tugas)
    }

    fun delete(tugas: Tugas) {
        repository.delete(tugas)
    }
}
