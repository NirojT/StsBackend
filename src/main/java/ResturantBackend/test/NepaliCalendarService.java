package ResturantBackend.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NepaliCalendarService {
    private static long totalEngDaysCount;
    private int nepaliYear;
    private int nepaliMonth;
    private int nepaliDay;

    private final int startingEngYear = 1944;
    private final int startingEngMonth = 1;
    private final int startingEngDay = 1;
    private final int dayOfWeek = Calendar.SATURDAY; // 1944/1/1 is Saturday
    private final int startingNepYear = 2000;
    private final int startingNepMonth = 9;
    private final int startingNepDay = 17;
    private final NepaliCalendarRepository calendarRepository;

    @Autowired
    public NepaliCalendarService(NepaliCalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public void saveNepaliCalendarData(Map<Integer, int[]> nepaliMap) {
        for (Map.Entry<Integer, int[]> entry : nepaliMap.entrySet()) {
            int year = entry.getKey();
            int[] monthData = entry.getValue();

            NepaliCalendar nepaliCalendar = new NepaliCalendar();
            nepaliCalendar.setYear(year);
            nepaliCalendar.setMonthData(monthData);

            calendarRepository.save(nepaliCalendar);
        }
    }

    public List<NepaliCalendar> getALl(){
      return   calendarRepository.findAll();
    }

    public String convertToNepaliDate(Date date) {
        int engYear = date.getYear() + 1900;
        int engMonth = date.getMonth() + 1;
        int engDay = date.getDate();

        // Find the most recent Nepali year and the corresponding base English date
        int nepaliYear = 0;
        Calendar baseEngDate = new GregorianCalendar();

        List<NepaliCalendar> nepaliDateEntities = calendarRepository.findAll();
        for (NepaliCalendar nepaliDateEntity : nepaliDateEntities) {
            if (engYear >= nepaliDateEntity.getYear()) {
                nepaliYear = nepaliDateEntity.getYear();
                baseEngDate.set(nepaliDateEntity.getYear(), 0, 1);
            } else {
                break;
            }
        }

        // Calculate the difference in days between the input English date and the base English date
        Calendar currentEngDate = new GregorianCalendar(engYear, engMonth - 1, engDay);
        long totalEngDaysCount = daysBetween(baseEngDate, currentEngDate);

        // Initialize Nepali date
        int nepaliMonth = 1;
        int nepaliDay = 1;

        while (totalEngDaysCount > 0) {
            int daysInIthMonth = nepaliDateEntities.get(nepaliYear - startingNepYear).getMonthData()[nepaliMonth - 1];

            nepaliDay++;
            if (nepaliDay > daysInIthMonth) {
                nepaliMonth++;
                nepaliDay = 1;
            }
            if (nepaliMonth > 12) {
                nepaliYear++;
                nepaliMonth = 1;
            }
            totalEngDaysCount--;
        }

        return String.format("%04d/%02d/%02d", nepaliYear, nepaliMonth, nepaliDay);
    }


    private static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public String getCurrentNepaliYearMonth() {
        Date currentDate = new Date();
        String nepaliDate = convertToNepaliDate(currentDate);
        String[] parts = nepaliDate.split("/");
        int nepaliYear = Integer.parseInt(parts[0]);
        int nepaliMonth = Integer.parseInt(parts[1]);
        return String.format("%04d/%02d", nepaliYear, nepaliMonth);
    }

    public int getCurrentNepaliYear() {
        Date currentDate = new Date();
        String nepaliDate = convertToNepaliDate(currentDate);
        String[] parts = nepaliDate.split("/");
        return Integer.parseInt(parts[0]);
    }

}
