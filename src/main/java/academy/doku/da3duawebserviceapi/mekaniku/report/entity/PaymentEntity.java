package academy.doku.da3duawebserviceapi.mekaniku.report.entity;


import academy.doku.da3duawebserviceapi.config.PostgreSQLEnumType;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderEntity;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentMethodEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.enums.PaymentStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payments")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethodEntity paymentMethod;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;


    private String vaNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "payment_status")
    @Type(type = "pgsql_enum")
    private PaymentStatus status;

    private Boolean finalStatus;

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
