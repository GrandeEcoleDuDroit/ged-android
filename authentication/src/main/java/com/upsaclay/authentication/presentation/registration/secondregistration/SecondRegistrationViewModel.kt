package com.upsaclay.authentication.presentation.registration.secondregistration

import androidx.lifecycle.ViewModel
import com.upsaclay.common.domain.entity.SchoolLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SecondRegistrationViewModel: ViewModel() {
    val schoolLevels = SchoolLevel.all
    private val _schoolLevel = MutableStateFlow(SchoolLevel.GED_1)
    val schoolLevel: StateFlow<SchoolLevel> = _schoolLevel

    fun onSchoolLevelChange(schoolLevel: SchoolLevel) {
        _schoolLevel.value = schoolLevel
    }
}