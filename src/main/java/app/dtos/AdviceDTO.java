package app.dtos;

import app.entities.Advice;
import app.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class AdviceDTO {

    private Integer id;
    private String adviceText;
    private Integer rating;
    private Category category;

    public AdviceDTO(Advice advice) {
        this.id = advice.getId();
        this.adviceText = advice.getAdviceText();
        this.rating = advice.getRating();
        this.category = advice.getCategory();
    }

    public static List<AdviceDTO> toDTOList(List<Advice> adviceList) {
        return adviceList.stream().map(AdviceDTO::new).toList();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AdviceDTO adviceDTO)) return false;

        return getId().equals(adviceDTO.getId());
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }

}
