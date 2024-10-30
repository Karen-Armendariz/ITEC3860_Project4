package org.example.itec_3860_project3;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer>
{
    // Custom query to find the 10 most recent vehicles
    List<Vehicle> findTop10ByOrderByIdDesc();
}
