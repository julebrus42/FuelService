/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FuelService;

import Auth.Group;
import Auth.User;
import Objects.Car;
import Objects.FuelStation;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import Objects.FuelStation;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import static jdk.nashorn.internal.runtime.Debug.id;

/**
 *
 * @author danie
 */

@Path("service")
@Stateless
public class FuelService {
    
    @PersistenceContext
    EntityManager em;
    
    @Context
    SecurityContext securityContext;
    
    
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
            @FormParam("petrolPrice") double petrolPrice,
            @FormParam("dieselPrice") double dieselprice,
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
 
    
    @GET
    @Path("stations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FuelStation> getFuelStations() {
        return em.createNamedQuery(FuelStation.FIND_ALL_FUELSTATIONS, FuelStation.class).getResultList();
    }
    
    @DELETE
    @Path("delete")
    public Response delete(@QueryParam("stationId") Long id) {
        FuelStation fuelStation = em.find(FuelStation.class, id);
        if (fuelStation != null){
                em.remove(fuelStation);
                return Response.ok().build();
            }
           return Response.notModified().build();
        }
    
    @PUT
    @Path("priceChange")
    public Response changePrice(
            @QueryParam("id") long id,
            @QueryParam("petrolPrice") int petrolPrice,
            @QueryParam("dieselPrice") int dieselPrice) {
        
        FuelStation fuelStation = em.find(FuelStation.class, id);
        
        if (fuelStation == null) {            
            return Response.status(Response.Status.BAD_REQUEST).build(); 
        }
        
        fuelStation.setPetrolPrice(petrolPrice);
        fuelStation.setDieselPrice(dieselPrice);
        return Response.ok().build();
        
    }
    
    @PUT
    @Path("setFavorite")
    @RolesAllowed({Group.USER})
    public Response setFavorite(
            @QueryParam("FuelStationId") String id){
        
        User user = this.getCurrentUser();
        user.setFavoriteStation(id);
       
        return Response.ok(em.merge(user)).build();
        
        
    }
    @GET
    @Path("getFavorite")
    @RolesAllowed({Group.USER})
    public Response getFavorite(){
        
        User user = this.getCurrentUser();
        String firstName = "favorite" + user.getFirstName();
        
        return Response.ok(firstName).build();
    }
            
            
            
    
    @POST
    @Path("addCar")
    @RolesAllowed({Group.USER})
    public Response addCar(
            @FormParam("RegNumber") String RegNumber,
            @FormParam("manufacturer") String manufacturer,
            @FormParam("model") String model,
            @FormParam("petrol") Boolean petrol) {
        
        User carOwner = this.getCurrentUser();
        Car car = new Car();
        
        
        car.setRegNumber(RegNumber);
        car.setManufacturer(manufacturer);
        car.setModel(model);
        car.setPetrol(petrol);
        car.setCarOwner(carOwner);
        
        em.persist(car);
        
        return Response.ok().build();
    }
    
    private User getCurrentUser() {
        return em.find(User.class, securityContext.getUserPrincipal().getName());
    }
    
    @GET
    @Path("cars")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Car> getCars() {
        return em.createNamedQuery(Car.FIND_ALL_CARS, Car.class).getResultList();
    }
    
  
}
 
    
