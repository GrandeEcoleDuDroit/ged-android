package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

class GenerateIdUseCaseTest {
    private lateinit var useCae: GenerateIdUseCase

    @Before
    fun setUp() {
        useCae = GenerateIdUseCase()
    }

    @Test
    fun generateIdUseCase_should_generate_unique_id() {
        for (i in 0..100000) {
            // When
            val id1 = useCae()
            val id2 = useCae()

            // Then
            assertNotEquals(id1, id2)
        }
    }
}