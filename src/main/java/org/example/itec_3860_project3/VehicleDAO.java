package org.example.itec_3860_project3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class VehicleDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    public List<Vehicle> findTop10ByOrderByIdDesc()
    {
        return entityManager.createQuery("SELECT v FROM Vehicle v ORDER BY v.id DESC", Vehicle.class)
                .setMaxResults(10)
                .getResultList();
    }

    public Vehicle getVehicleByID(int id)
    {
        return entityManager.find(Vehicle.class, id);
    }

    public Vehicle save(Vehicle vehicle)
    {
        if (vehicle.getId() == 0)
        {
            entityManager.persist(vehicle); // New vehicle
        }
        else
        {
            entityManager.merge(vehicle); // Update existing vehicle
        }
        return vehicle;
    }

    public List<Vehicle> findAll()
    {
        return entityManager.createQuery("SELECT v FROM Vehicle v", Vehicle.class)
                .getResultList();
    }

    public void addVehicle(Vehicle vehicle)
    {
        entityManager.persist(vehicle);
    }

    public Vehicle updateVehicle(Vehicle vehicle)
    {
        return entityManager.merge(vehicle);
    }

    public void delete(Vehicle vehicle)
    {
        if (entityManager.contains(vehicle))
        {
            entityManager.remove(vehicle);
        }
        else
        {
            Vehicle managedVehicle = entityManager.merge(vehicle);
            entityManager.remove(managedVehicle);
        }
    }

    public int getLatestVehicleID()
    {
        Query query = entityManager.createNativeQuery("SELECT max(id) FROM vehicles");
        Integer id = (Integer) query.getSingleResult();
        return (id != null) ? id : 0; // Return 0 if no vehicle is found
    }
}