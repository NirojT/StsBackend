package ResturantBackend.test;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "nepali_calendar")
@Data
public class NepaliCalendar {
    @Id
    private String id;
    private int year;
    private int[] monthData;

    // Constructors, getters, setters
}
