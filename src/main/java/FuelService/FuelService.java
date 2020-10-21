/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FuelService;

import com.mycompany.fuelservice.resources.DatasourceProducer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import Objects.FuelStation;
import javax.ws.rs.GET;

/**
 *
 * @author danie
 */

@Path("service")
@Stateless
public class FuelService {
    
    @PersistenceContext
    EntityManager em;
    
    
    @GET
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
    
   
    @POST
    @Path("addFuelStation")
    public Response addFuelStation(
            @FormParam("name") String name,
            @FormParam("coordinates") String coordinates,
            @FormParam("petrolPrice") int petrolPrice,
            @FormParam("dieselPrice") int dieselprice,
            @FormParam("petrol") boolean petrol,
            @FormParam("diesel") boolean diesel) {
        
        FuelStation fuelStation = new FuelStation();
        
        fuelStation.setName(name);
        fuelStation.setCoordinates(coordinates);
        fuelStation.setPetrolPrice(petrolPrice);
        fuelStation.setDieselPrice(dieselprice);
        fuelStation.setPetrol(petrol);
        fuelStation.setDiesel(diesel);
        
        em.persist(fuelStation);
        
        return Response.ok().build();
    }
    
    
}