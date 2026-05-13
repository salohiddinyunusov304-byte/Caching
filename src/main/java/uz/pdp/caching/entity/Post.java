package uz.pdp.caching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.ISBN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "posts")
public class Post {
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;
    private String title;
    private String body;
}
