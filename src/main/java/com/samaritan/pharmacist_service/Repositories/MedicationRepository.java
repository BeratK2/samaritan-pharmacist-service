package com.samaritan.pharmacist_service.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.samaritan.pharmacist_service.Entities.Medication;

public interface MedicationRepository extends CrudRepository<Medication, Long>{
 
}
