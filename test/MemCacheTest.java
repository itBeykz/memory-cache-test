import bg.sofia.uni.fmi.mjt.cache.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.cache.MemCache;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static junit.framework.TestCase.*;

public class MemCacheTest {
    private MemCache<Integer, String> tester;

    @Before
    public void setup() {

    }

    @Test
    public void testDefaultCache() {
        tester = new MemCache<Integer, String>();
        assertEquals("Default HitRate not 0.0", 0.0, tester.getHitRate());
        assertEquals("Default capacity not 10000", 10000, tester.getCapacity());

    }

    @Test
    public void testSetMethodSize() {
        tester = new MemCache(3);
        tester.set(5, "1", LocalDateTime.now());
        assertEquals("Size missmatch", 1, tester.size());

    }

    @Test
    public void testKeyAndValueForNull() throws CapacityExceededException {
        tester = new MemCache<>();
        tester.set(5, "1", LocalDateTime.now());
        assertNotNull(tester);
    }

    @Test
    public void testValueReplacement() throws CapacityExceededException{
        tester = new MemCache<>();
        tester.set(5, "1", LocalDateTime.of(2020,12,12,5,1));
        MemCache<Integer, String> testerTemp = tester;
        tester.set(5, "3", LocalDateTime.of(2020,9,2,4,10,10));
        assertSame(testerTemp, tester);
    }

    @Test
    public void testIfExpiredItemRemoved() throws CapacityExceededException{
        tester = new MemCache<>(2);
        tester.set(5, "1", LocalDateTime.now());
        tester.set(3, "5", LocalDateTime.of(2000, 12, 12, 5, 1));
        MemCache<Integer, String> testerTemp = tester;
        tester.set(3, "4", LocalDateTime.of(2020, 12, 12, 5, 1));
        assertSame(testerTemp, tester);
    }

    @Test
    public void testCapacityIfNoExpiredItemsFound() throws CapacityExceededException {
        tester = new MemCache<>(2);
        boolean thrown = false;
        try {
            tester.set(5, "1", LocalDateTime.of(2020, 12, 12, 5, 1));
            tester.set(6, "2", LocalDateTime.of(2030, 12, 12, 5, 1));
            tester.set(5, "1", LocalDateTime.of(2019, 12, 12, 2, 1));
        }
        catch (CapacityExceededException e){
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetValue() throws CapacityExceededException{
        tester = new MemCache<>(3);
        tester.set(1, "one", LocalDateTime.of(2020, 12, 12, 5, 1));
        tester.set(2, "two", LocalDateTime.of(2030, 12, 12, 5, 1));
        tester.set(3, "three", LocalDateTime.of(2018, 10, 3, 4, 20));
        assertEquals("two", tester.get(2));
        assertEquals(null, tester.get(3));
    }

    @Test
    public void testGetMethodForExpiredItem() throws CapacityExceededException{
        tester = new MemCache<>(3);
        tester.set(1, "one", LocalDateTime.of(2018, 12, 12, 5, 1));
        assertEquals(null, tester.get(1));
    }

    @Test
    public void testGetExpirationForInvalidKey() {
        tester = new MemCache<>(3);
        tester.set(1, "one", LocalDateTime.of(2020, 12, 12, 5, 1));
        assertEquals(null, tester.getExpiration(3));
    }

    @Test
    public void testRemoveMethod() throws CapacityExceededException {
        tester = new MemCache<>(3);
        tester.set(1, "one", LocalDateTime.of(2020, 12, 12, 5, 1));
        tester.remove(1);
        assertEquals(0, tester.size());
        assertEquals(false, tester.remove(5));
    }

    @Test
    public void testSizeMethod() throws CapacityExceededException {
        tester = new MemCache<>(3);
        tester.set(1, "one", LocalDateTime.of(2020, 12, 12, 5, 1));
        assertEquals(1, tester.size());
    }

    @Test
    public void testClear() throws CapacityExceededException {
        tester = new MemCache<>(3);
        tester.set(1, "one", LocalDateTime.of(2020, 12, 12, 5, 1));
        tester.set(2, "one", LocalDateTime.of(2020, 12, 12, 5, 1));
        tester.clear();
        assertEquals(0, tester.size());
    }

    @Test
    public void testHitRate() throws CapacityExceededException {
        MemCache<Integer, String> cache = new MemCache<>(3);
        cache.set(1, "a", LocalDateTime.of(2020, 12, 12, 5, 1));
        cache.get(1);
        cache.get(0);
        cache.get(0);

        assertEquals("Get hit rate should be calculated correctly.", 0.5, cache.getHitRate(), 0.1);
    }

    @Test
    public void testWithZeroCapacity() throws CapacityExceededException {
        MemCache<Integer, String> cache = new MemCache<>(0);
            cache.set(1, "one", null);
    }
}
