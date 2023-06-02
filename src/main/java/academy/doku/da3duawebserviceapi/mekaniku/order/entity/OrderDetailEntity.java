package academy.doku.da3duawebserviceapi.mekaniku.order.entity;

import academy.doku.da3duawebserviceapi.config.PostgreSQLEnumType;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopProductEntity;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "order_details")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private WorkshopProductEntity product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "product_type")
    @Type(type = "pgsql_enum")
    private ProductType type;

    @Column(nullable = false)
    private Integer quantity;

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public OrderDetailEntity(OrderEntity order, WorkshopProductEntity product, String name, Double price, ProductType type, Integer quantity) {
        this.order = order;
        this.product = product;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }
}
