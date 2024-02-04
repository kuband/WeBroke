package com.we.broke.payment.model;

import com.we.broke.app.model.entity.Organisation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PaymentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "payment_details_id")
    @Builder.Default
    private Set<Organisation> organisations = new HashSet<>();

    // if different from null then the trial is over
    private LocalDateTime trialsEnd;

    private String subscriptionId;

    private String stripeCustomerId;

    private Long creditsAmount;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @LastModifiedDate
    private long lastModifiedDate;
}
