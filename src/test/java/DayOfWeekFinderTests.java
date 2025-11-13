import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayOfWeekFinderTests {
    @Test
    void currentDate() {
        Calendar calendar = new GregorianCalendar();
        int expectedDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int actualDayOfWeek = DayOfWeekFinder.getDayOfWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedDayOfWeek, actualDayOfWeek);
    }
}
