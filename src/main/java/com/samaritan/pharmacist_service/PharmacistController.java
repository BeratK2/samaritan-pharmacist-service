package com.samaritan.pharmacist_service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
		
		public PharmacistController(MedicationRepository medicationRepository, 
									CustomerRepository customerRepository, 
									StoreRepository storeRepository) {
			this.medicationRepository = medicationRepository;
			this.customerRepository = customerRepository;
			this.storeRepository = storeRepository;
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
		
		// Get notifications of medications and their customers at a given store where the arrival date is in the past
		@GetMapping("/stores/{storeId}/notifications")
		public ResponseEntity<List<StoreRepository.NotificationView>> notificaitons(@PathVariable Long storeId) {
			if (!storeRepository.existsById(storeId)) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(storeRepository.findNotificationsByStoreId(storeId));
		}
}
