package app.entities;

import app.dtos.AdviceDTO;
import app.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "advice")
@Check(constraints = "advice_rating BETWEEN 1 AND 10")
public class Advice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advice_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "advice_text", nullable = false, unique = true, length = 500)
    private String adviceText;

    @Setter
    @NotNull
    @Min(1)
    @Max(10)
    @Column(name = "advice_rating", nullable = false)
    private Integer rating;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "advice_category", nullable = false)
    private Category category;

    public Advice(String adviceText, Integer rating, Category category) {
        this.adviceText = adviceText;
        this.rating = rating;
        this.category = category;
    }

    public Advice(AdviceDTO adviceDTO) {
        this.id = adviceDTO.getId();
        this.adviceText = adviceDTO.getAdviceText();
        this.rating = adviceDTO.getRating();
        this.category = adviceDTO.getCategory();
    }
}
