import java.util.Scanner;

/**
 * A utility class to find the day of the week for a given date.
 * This implementation does not use any external date/time libraries.
 */
public class DayOfWeekFinder {

    // Array for month day counts (Bug 3: August and December are wrong)
    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30};

    private static final String[] DAY_NAMES = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    /**
     * Checks if a given year is a leap year.
     * (Bug 1: This logic is incomplete and incorrectly identifies
     * century years like 1900 as leap years.)
     *
     * @param year The year to check.
     * @return true if it's considered a leap year, false otherwise.
     */
    private static boolean isLeap(int year) {
        return year % 4 == 0;
    }

    /**
     * Calculates the day of the week for a specified date.
     *
     * @param year The full year (e.g., 2024).
     * @param month The month (1-12).
     * @param day The day (1-31).
     * @return The name of the day of the week (e.g., "Monday").
     */
    public static int getDayOfWeek(int year, int month, int day) {

        long totalDays = 0;

        // We use Jan 1, 1900 as our anchor date.
        // (Bug 2: Jan 1, 1900 was a Monday, not a Sunday.)
        int anchorYear = 1900;
        int anchorDayOfWeek = 0; // 0 = Sunday

        // 1. Add days for all full years passed since the anchor year
        for (int y = anchorYear; y < year; y++) {
            totalDays += isLeap(y) ? 366 : 365;
        }

        // 2. Add days for all full months passed in the current year
        // We use month-1 because the input is 1-based
        for (int m = 0; m < month - 1; m++) {
            totalDays += DAYS_IN_MONTH[m];

            // Add the leap day if it's February in a leap year
            if (m == 1 && isLeap(year)) {
                totalDays++;
            }
        }

        // 3. Add the days of the current month
        // (We subtract 1 because we are counting days *since* the 1st)
        totalDays += (day - 1);

        // 4. Find the final day index
        int dayIndex = (int)((anchorDayOfWeek + totalDays) % 7);

        return dayIndex + 1;
    }

    /**
     * Main method to run the program from the command line.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Year (e.g., 2024):");
        int year = scanner.nextInt();

        System.out.println("Enter Month (1-12):");
        int month = scanner.nextInt();

        System.out.println("Enter Day (1-31):");
        int day = scanner.nextInt();

        int dayOfWeek = getDayOfWeek(year, month, day);

        System.out.printf("The day of the week for %d-%d-%d is: %s\n", month, day, year, dayOfWeek);

        // --- Some "Test" Cases ---
        System.out.println("\n--- Sample Outputs ---");
        System.out.println("Jan 1, 1900: " + getDayOfWeek(1900, 1, 1));
        System.out.println("Mar 1, 1900: " + getDayOfWeek(1900, 3, 1));
        System.out.println("Dec 25, 2024: " + getDayOfWeek(2024, 12, 25));

        scanner.close();
    }
}