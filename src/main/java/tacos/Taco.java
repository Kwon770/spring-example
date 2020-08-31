package tacos;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Data
// JPA 객체
@Entity
public class Taco {
    // 이 속성이 DB의 개체를 고유하게 식별함을 나타냄
    @Id
    // DB가 자동으로 생성해 주는 ID값 사용
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    // Taco, 이것과 연관된 Ingredient들 간의 관계 선언
    // 하나의 Taco 객체는 많은 Ingredient 객체를 가질 수 있으며, 하나의 Ingredient는 여러 Taco 객체에 포함될 수 있음.
    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;

    // 객체가 저장되기 전에 createdAt 속성을 현재 일자와 시간으로 설정
    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
