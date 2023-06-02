//package com.example.da3duawebserviceapi.entity;
//
//import io.hypersistence.utils.hibernate.type.array.ListArrayType;
//import lombok.*;
//import org.hibernate.annotations.Type;
//import org.hibernate.annotations.TypeDef;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@Table(name = "workshops")
//@TypeDef(name = "list-array", typeClass = ListArrayType.class)
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class WorkshopEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false, columnDefinition = "text")
//    private String description;
//
//    @Column(nullable = false)
//    private String address;
//
//    @Column(nullable = false)
//    private Double latitude;
//
//    @Column(nullable = false)
//    private Double longitude;
//
//    @Column(nullable = false)
//    private Boolean motorcycle;
//
//    @Column(nullable = false)
//    private Boolean car;
//
//    @Column(nullable = false)
//    private LocalTime openTime;
//
//    @Column(nullable = false)
//    private LocalTime closeTime;
//
////    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
////    @JoinColumn(name = "workshop_id")
////    private List<WorkshopPhotoEntity> photos;
//
//    @Type(type = "list-array")
//    @Column(columnDefinition = "integer[]")
//    private List<Integer> closeDay;
//
//    @CreatedDate
//    @Setter(AccessLevel.NONE)
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Setter(AccessLevel.NONE)
//    @Column(nullable = false)
//    private LocalDateTime modifiedAt;
//}
