package app.dtos;

import app.enums.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class AdviceDTOTest {

    @Test
    void testAdviceDTOProperties()  {

        AdviceDTO advice1 = new AdviceDTO(1, "Stay positive", 5, Category.Career);
        AdviceDTO advice2 = new AdviceDTO(2, "Exercise regularly", 4, Category.Health);
        AdviceDTO advice3 = new AdviceDTO(3, "Read books", 5, Category.Personal_Development);


        List<AdviceDTO> adviceDTOList = List.of(advice1, advice2, advice3);

        // Property assertions
        assertThat(advice1.getAdviceText(), equalTo("Stay positive"));
        assertThat(advice2.getRating(), lessThanOrEqualTo(4));
        assertThat(advice3.getAdviceText(), containsString("Read books"));

        // Collection assertions
        assertThat(adviceDTOList, hasSize(3));
        assertThat(adviceDTOList, containsInAnyOrder(advice1, advice2, advice3));
        assertThat(adviceDTOList, is(not(empty())));
        assertThat(adviceDTOList, hasItem(advice1));
    }

}