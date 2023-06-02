package academy.doku.da3duawebserviceapi.mekaniku.order.entity;

import academy.doku.da3duawebserviceapi.config.PostgreSQLEnumType;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.VehicleType;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id")
    private WorkshopEntity workshop;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetailEntity> orderDetails;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private LocalDate bookForDate;

    @Column(nullable = false)
    private LocalTime bookForTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "vehicle_type")
    @Type(type = "pgsql_enum")
    private VehicleType vehicleType;

    @Column(nullable = false)
    private String vehicleNumber;

    @Column(columnDefinition = "text")
    private String customerNote;

    @Column(columnDefinition = "text")
    private String workshopNote;

    @Column(columnDefinition = "text")
    private String rejectionNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "order_status")
    @Type(type = "pgsql_enum")
    private OrderStatus status;

    private LocalDateTime bookedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime inProcessAt;
    private LocalDateTime processedAt;
    private LocalDateTime doneAt;
    private LocalDateTime rejectedAt;

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Formula("(select sum(od.price*od.quantity) from order_details od inner join orders o on od.order_id=o.id where od.order_id=id and o.status='DONE')")
    private Double totalPrice = 0.0;
}
