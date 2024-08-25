package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_atr")
@NoArgsConstructor
@Getter
public class ItemAtr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "itematr_id")
    private Long id;
    private String color;
    private String size;
    private int count;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemAtr(String color, String size, int count, Item item) {
        this.color = color;
        this.size = size;
        this.count = count;
        this.item = item;
    }
}
