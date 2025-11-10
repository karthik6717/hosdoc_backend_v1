package com.kalex.hosdoc_backend.entity;

import com.kalex.hosdoc_backend.enums.PaymentTransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "appointment_id", nullable = false)
	private Long appointmentId;
	
	@Column(name = "amount", precision = 10, scale = 2, nullable = false)
	private BigDecimal amount;
	
	@Column(name = "currency", length = 10)
	private String currency = "INR";
	
	@Column(name = "provider", length = 100)
	private String provider;
	
	@Column(name = "provider_payment_id", length = 255)
	private String providerPaymentId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 50)
	private PaymentTransactionStatus status = PaymentTransactionStatus.INIT;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", insertable = false, updatable = false)
	private Appointment appointment;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}

