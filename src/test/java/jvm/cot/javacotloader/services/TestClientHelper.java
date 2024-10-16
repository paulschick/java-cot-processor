package jvm.cot.javacotloader.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientHelper {
    @Test
    public void testUrlEncode() {
        String result1 = ClientHelper.urlEncode(null);
        assertTrue(result1.isEmpty());
        String result2 = ClientHelper.urlEncode("$where");
        assertEquals("%24where", result2);
        String testParam = "value > '2024'";
        String testExpected = "value+%3E+%272024%27";
        assertEquals(testExpected, ClientHelper.urlEncode(testParam));
    }

    @Test
    public void testBuildQueryException() {
        assertThrows(IllegalArgumentException.class, () -> ClientHelper.buildQuery(
                "first", "second", "third"
        ));
    }

    @Test
    public void testBuildExpectedUrl() {
        var where = "report_date_as_yyyy_mm_dd > '2024-10-04'";
        var query = ClientHelper.buildQuery(
                "$where", where
        );
        var expected = "%24where=report_date_as_yyyy_mm_dd+%3E+%272024-10-04%27";
        assertEquals(expected, query);
    }
}
