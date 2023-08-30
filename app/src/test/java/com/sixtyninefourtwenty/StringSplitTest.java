package com.sixtyninefourtwenty;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class StringSplitTest {

    @Test
    void splitStrings() {
        final var target = "51,1,342,0,-1,-1,-1,-1,-1,-1,-1,-1,12,0,-1,";
        final var arr = target.split(",");
        assertArrayEquals(new String[]{"51", "1", "342", "0", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "12", "0", "-1"}, arr);
    }

}
