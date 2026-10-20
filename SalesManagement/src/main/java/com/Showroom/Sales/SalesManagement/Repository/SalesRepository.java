package com.Showroom.Sales.SalesManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Showroom.Sales.SalesManagement.Entities.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long>{

}
