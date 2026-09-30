package com.samaritan.pharmacist_service.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.samaritan.pharmacist_service.Entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
	// Customers linked to a store through the store_customer join table
	@Query("SELECT c FROM Store s JOIN s.customers c WHERE s.store_id = :storeId ORDER BY c.customer_name")
	List<Customer> findByStoreId(@Param("storeId") long storeId);
}
