package academy.doku.da3duawebserviceapi.mekaniku.workshop.entity;

import academy.doku.da3duawebserviceapi.config.PostgreSQLEnumType;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.*;
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
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "workshop_schedules")
@Data
@TypeDef(name = "psql_enum", typeClass = PostgreSQLEnumType.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkshopScheduleEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne //(cascade = CascadeType.ALL)
    @JoinColumn(name = "workshop_id")
    private WorkshopEntity workshop;

    @Column(nullable = false)
    private LocalDate forDate;

    @Enumerated(EnumType.STRING)
    @Type(type = "psql_enum")
    @Column(nullable = false)
    private ScheduleStatus status;

    private LocalTime openTime;

    private LocalTime closeTime;

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
