package org.example.itec_3860_project3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController
{
    @Autowired
    private VehicleDAO vehicleDAO; // Consistent variable name

    @GetMapping("/vehicleIds")
    public ResponseEntity<List<Integer>> getAllVehicleIds()
    {
        List<Vehicle> vehicles = vehicleDAO.findAll();

        // Print out the vehicles to debug
        vehicles.forEach(System.out::println);

        // Proceed with extracting the IDs
        List<Integer> vehicleIds = vehicles.stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList());

        return new ResponseEntity<>(vehicleIds, HttpStatus.OK);
    }
    @PostMapping("/addVehicle")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle newVehicle)
    {
        Vehicle savedVehicle = vehicleDAO.save(newVehicle);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteVehicle/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable int id)
    {
        Vehicle vehicle = vehicleDAO.getVehicleByID(id);
        if (vehicle != null)
        {
            vehicleDAO.delete(vehicle);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Successfully deleted
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Vehicle not found
        }
    }
    @PutMapping("/updateVehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable int id, @RequestBody Vehicle vehicle) throws IOException
    {
        Optional<Vehicle> existingVehicle = Optional.ofNullable(vehicleDAO.getVehicleByID(id));

        if (existingVehicle.isPresent())
        {
            Vehicle updatedVehicle = existingVehicle.get();
            updatedVehicle.setMakeModel(vehicle.getMakeModel());
            updatedVehicle.setYear(vehicle.getYear());
            updatedVehicle.setRetailPrice(vehicle.getRetailPrice());

            // Save the updated vehicle back to the database
            vehicleDAO.save(updatedVehicle);

            // Return the updated vehicle along with HTTP 200 OK
            return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/recentVehicles")
    public ResponseEntity<List<Vehicle>> getRecentVehicles()
    {
        List<Vehicle> recentVehicles = vehicleDAO.findTop10ByOrderByIdDesc();
        return new ResponseEntity<>(recentVehicles, HttpStatus.OK);
    }
}