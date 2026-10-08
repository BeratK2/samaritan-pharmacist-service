package com.samaritan.pharmacist_service.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.samaritan.pharmacist_service.Entities.Customer;
import com.samaritan.pharmacist_service.Entities.Store;

public interface StoreRepository extends CrudRepository<Store, Long> {
	// Customers linked to a store through the store_customer join table
	@Query("SELECT c FROM Store s JOIN s.customers c WHERE s.store_id = :storeId ORDER BY c.customer_name")
	List<Customer> findByStoreId(@Param("storeId") long storeId);

	// One row per customer/medication pair whose arrival date has passed
	interface NotificationView {
		Long getCustomerId();
		String getCustomerName();
		String getCustomerPhone();
		String getCustomerEmail();
		Long getMedicationId();
		String getMedicationName();
		LocalDateTime getArrivalDate();
	}

	@Query(value = """
			SELECT c.customer_id AS customerId,
			       c.customer_name AS customerName,
			       c.customer_phone AS customerPhone,
			       c.customer_email AS customerEmail,
			       m.medication_id AS medicationId,
			       m.medication_name AS medicationName,
			       cm.arrival_date AS arrivalDate
			FROM store_customer sc
			JOIN customer c ON c.customer_id = sc.customer_id
			JOIN customer_medication cm ON cm.customer_id = c.customer_id
			JOIN medication.medication m ON m.medication_id = cm.medication_id
			WHERE sc.store_id = :storeId
			  AND cm.arrival_date < NOW()
			ORDER BY cm.arrival_date
			""", nativeQuery = true)
	List<NotificationView> findNotificationsByStoreId(@Param("storeId") long storeId);
}