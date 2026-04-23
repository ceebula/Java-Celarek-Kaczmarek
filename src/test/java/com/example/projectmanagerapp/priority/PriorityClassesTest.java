package com.example.projectmanagerapp.priority;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriorityClassesTest {

    @Test
    @DisplayName("HighPriority should return HIGH")
    void highPriorityShouldReturnHigh() {
        HighPriority highPriority = new HighPriority();

        assertEquals("HIGH", highPriority.getPriority());
    }

    @Test
    @DisplayName("MediumPriority should return MEDIUM")
    void mediumPriorityShouldReturnMedium() {
        MediumPriority mediumPriority = new MediumPriority();

        assertEquals("MEDIUM", mediumPriority.getPriority());
    }

    @Test
    @DisplayName("LowPriority should return LOW")
    void lowPriorityShouldReturnLow() {
        LowPriority lowPriority = new LowPriority();

        assertEquals("LOW", lowPriority.getPriority());
    }
}