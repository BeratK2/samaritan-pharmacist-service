package com.samaritan.pharmacist_service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.samaritan.pharmacist_service.PharmacistController.CustomerMedication;
import com.samaritan.pharmacist_service.Entities.Customer;
import com.samaritan.pharmacist_service.Entities.Medication;
import com.samaritan.pharmacist_service.Repositories.CustomerRepository;
import com.samaritan.pharmacist_service.Repositories.MedicationRepository;
import com.samaritan.pharmacist_service.Repositories.StoreRepository;

@RestController
public class PharmacistController {
		private final MedicationRepository medicationRepository;
		private final CustomerRepository customerRepository;
		private final StoreRepository storeRepository;
		private final JdbcTemplate jdbcTemplate;

		public PharmacistController(MedicationRepository medicationRepository, 
									CustomerRepository customerRepository, 
									StoreRepository storeRepository,
									JdbcTemplate jdbcTemplate) {
			this.medicationRepository = medicationRepository;
			this.customerRepository = customerRepository;
			this.storeRepository = storeRepository;
			this.jdbcTemplate = jdbcTemplate;
		}
		
		// View all medications
		@GetMapping("medications")
		public Iterable<Medication> medications(){
			return medicationRepository.findAll();
		}
		
		// Get medication by ID
		@GetMapping("/medications/{id}")
		public ResponseEntity<Medication> medicationById(@PathVariable Long id) {
			Optional<Medication > medication = medicationRepository.findById(id);
			return medication.map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
		}
		
		// Get list of customers from a given store
		@GetMapping("/stores/{storeId}/customers")
		public ResponseEntity<List<Customer>> customersByStore(@PathVariable Long storeId) {
			if (!storeRepository.existsById(storeId)) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(customerRepository.findByStoreId(storeId));
		}
		
<<<<<<< HEAD
		// Get a single customer's details and medications
		public record CustomerMedication(long medication_id, String medication_name,
				String medication_description, LocalDateTime arrival_date) {}

		public record CustomerDetails(Customer customer, List<CustomerMedication> medications) {}

		@GetMapping("/customers/{customerId}")
		public ResponseEntity<CustomerDetails> customerDetails(@PathVariable Long customerId) {
				Optional<Customer> customer = customerRepository.findById(customerId);
				if (customer.isEmpty()) {
						return ResponseEntity.notFound().build();
				}

				List<CustomerMedication> medications = jdbcTemplate.query("""
						SELECT m.medication_id, m.medication_name, m.medication_description, cm.arrival_date
						FROM customer_medication cm
						JOIN medication.medication m ON m.medication_id = cm.medication_id
						WHERE cm.customer_id = ?
						ORDER BY m.medication_name
						""",
						(rs, rowNum) -> new CustomerMedication(
								rs.getLong("medication_id"),
								rs.getString("medication_name"),
								rs.getString("medication_description"),
								rs.getTimestamp("arrival_date") == null
										? null
										: rs.getTimestamp("arrival_date").toLocalDateTime()),
						customerId);

				return ResponseEntity.ok(new CustomerDetails(customer.get(), medications));
				}
=======
		// Get notifications of medications and their customers at a given store where the arrival date is in the past
		@GetMapping("/stores/{storeId}/notifications")
		public ResponseEntity<List<StoreRepository.NotificationView>> notificaitons(@PathVariable Long storeId) {
			if (!storeRepository.existsById(storeId)) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(storeRepository.findNotificationsByStoreId(storeId));
		}
>>>>>>> 3a1e07ffa4070e2d98bf6d3009c91285f7c9e151
}
