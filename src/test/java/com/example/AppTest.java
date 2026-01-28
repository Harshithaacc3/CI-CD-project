package com.example;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {

    @Test
    public void testToUpper() {
        Assert.assertEquals("DEVOPS", App.toUpper("devops"));
    }

    @Test
    public void testReverse() {
        Assert.assertEquals("spOveD", App.reverse("DevOps"));
    }

    @Test
    public void testNullInput() {
        Assert.assertEquals("", App.toUpper(null));
        Assert.assertEquals("", App.reverse(null));
    }
}
