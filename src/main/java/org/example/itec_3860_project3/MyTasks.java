package org.example.itec_3860_project3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;

@Component
public class MyTasks
{
    private int count = 1;
    private RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ApplicationHome home = new ApplicationHome(Itec3860Project3Application.class);

    @Autowired
    public MyTasks(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "*/3 * * * * *")
    public void addVehicle()
    {
        String makeModel = RandomStringUtils.randomAlphabetic(10);
        Random r = new Random();
        int year = r.nextInt(1986, 2017);
        int price = r.nextInt(15000, 45001);
        Vehicle v = new Vehicle(count++, makeModel, year, price);
        restTemplate.postForObject("http://localhost:8080/api/vehicles/addVehicle", v, Vehicle.class);
    }
    @Scheduled(cron = "*/15 * * * * *")
    public void deleteVehicle()
    {
        int id = random.nextInt(50000);
        try
        {
            // Check if the vehicle with this ID exists
            Vehicle vehicle = restTemplate.getForObject("http://localhost:8080/api/vehicles/" + id, Vehicle.class);

            if (vehicle != null)
            {
                // If the vehicle exists, proceed with the deletion
                restTemplate.delete("http://localhost:8080/api/vehicles/deleteVehicle/" + id);
                System.out.println("Deleted Vehicle with ID: " + id);
            }
            else
            {
                // Vehicle does not exist
                System.out.println("Vehicle with ID " + id + " not found, cannot delete.");
            }
        }
        catch (HttpClientErrorException.NotFound e)
        {
            // Handle case where vehicle doesn't exist (404)
            System.out.println("Vehicle with ID " + id + " not found, cannot delete.");
        }
        catch (Exception e)
        {
            // Handle other exceptions
            System.out.println("Error occurred while deleting vehicle: " + e.getMessage());
        }
    }
    @Scheduled(cron = "*/15 * * * * *")
    public void updateVehicle()
    {
        int id = random.nextInt(50000);  // Get a random ID
        try
        {
            // Check if the vehicle with this ID exists
            Vehicle existingVehicle = restTemplate.getForObject("http://localhost:8080/api/vehicles/" + id, Vehicle.class);

            if (existingVehicle != null)
            {
                // If the vehicle exists, proceed with the update
                Vehicle updatedVehicle = new Vehicle(id, "UpdatedMakeModel", 2020, 40000);
                restTemplate.put("http://localhost:8080/api/vehicles/" + id, updatedVehicle);

                // Retrieve and print the updated vehicle
                Vehicle retrievedVehicle = restTemplate.getForObject("http://localhost:8080/api/vehicles/" + id, Vehicle.class);
                System.out.println("Updated Vehicle: " + retrievedVehicle);
            }
            else
            {
                // Vehicle doesn't exist, handle accordingly
                System.out.println("Vehicle with ID " + id + " does not exist. Cannot update.");
            }
        }
        catch (HttpClientErrorException.NotFound e)
        {
            // If the vehicle doesn't exist, catch the 404 error
            System.out.println("Vehicle with ID " + id + " not found. Cannot update.");
        }
        catch (Exception e)
        {
            // Handle other exceptions
            System.out.println("Error occurred while updating vehicle: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    //@Scheduled(cron = "0 0 * * * *")
    public void printLatestVehicles()
    {
        Vehicle[] latestVehicles = restTemplate.getForObject("http://localhost:8080/api/vehicles/recentVehicles", Vehicle[].class);
        System.out.println("Latest vehicles report:");
        for (Vehicle vehicle : latestVehicles)
        {
            System.out.println(vehicle);
        }
    }
}