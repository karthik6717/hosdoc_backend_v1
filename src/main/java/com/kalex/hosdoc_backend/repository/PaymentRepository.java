package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByAppointmentId(Long appointmentId);
	Optional<Payment> findByProviderPaymentId(String providerPaymentId);
}

