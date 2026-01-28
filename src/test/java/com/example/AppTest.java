package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testToUpper() {
        assertEquals("DEVOPS", App.toUpper("DevOps"));
    }

    @Test
    void testReverse() {
        assertEquals("spOveD", App.reverse("DevOps"));
    }

    @Test
    void testNullInput() {
        assertEquals("", App.toUpper(null));
        assertEquals("", App.reverse(null));
    }
}
