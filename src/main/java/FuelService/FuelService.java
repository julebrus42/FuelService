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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
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
     /**
     * Uses the findFuelStationsByFavoritedIds method to link the Ids from getFavorite() method.
     *
     * @return
     */
    @GET
    @Path("favoriteStations")
    @RolesAllowed({Group.USER})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getFavoriteFuelStations() {
         
        List<FuelStation> favoriteStations;
        
        favoriteStations = findFuelStationsByFavoritedIds(getFavorite());
   
     return  Response.ok(favoriteStations).build();
     
       /**
     * Finds the FuelStations with the id listed in the favoriteId param.
     * @param favoriteId
     * @return
     */
    }
     private List<FuelStation> findFuelStationsByFavoritedIds( List<String> favoriteId) {
        return favoriteId.size() > 0 ? em.createNamedQuery(FuelStation.FIND_FUELSTATIONS_BY_IDs, FuelStation.class)
                .setParameter("ids",favoriteId)
                .getResultList() : new ArrayList<>();
    }
     
     
    @GET
    @Path("cheapestStations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FuelStation> getCheapestFuelStations() {
        return em.createNamedQuery(FuelStation.FIND_FUELSTATIONS_BY_PRICE, FuelStation.class).getResultList();
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
    @Path("priceChange")
    public Response changePrice (){
        
    
               
          List<FuelStation> fuelStationList;
          fuelStationList = getFuelStations();
        
         for (FuelStation fuelstation : fuelStationList) {
             
                   Random rng = new Random();
                   
            double petrolPrice = (rng.nextInt(160)-130) / 10.00;
            double dieselPrice = ((rng.nextInt(160)-130) / 10.00) + 0.87;
            
             fuelstation.setPetrolPrice(petrolPrice);
             fuelstation.setDieselPrice(dieselPrice);
                    }
         return Response.ok().build();
    }
    @PUT
    @Path("setFavorite")
    @RolesAllowed({Group.USER})
    public Response setFavorite(
            @QueryParam("FuelStationId") String id){
        
        User user = this.getCurrentUser();
        user.addFavorite(id);
       
        return Response.ok().build();
        
    }
      
    @GET
    @Path("removeFavorite")
    @RolesAllowed({Group.USER})
    public Response removeFavorite(@QueryParam("fuelStationId") String id) {
        
        User user = this.getCurrentUser();
        user.removeFavorite(id);
       
        return Response.ok().build();
         
    }
    
            
    
    @POST
    @Path("addCar")
    @RolesAllowed({Group.USER})
    public Response addCar(
            @FormParam("RegNumber") String RegNumber,
            @FormParam("manufacturer") String manufacturer,
            @FormParam("model") String model,
            @FormParam("petrol") boolean petrol) {
        
        User carOwner = this.getCurrentUser();
        Car car = new Car();
        
        String ownerId = carOwner.getUserid();
        
        car.setOwnerId(ownerId);
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
    @GET
    @Path("getFavorite")
    @RolesAllowed({Group.USER})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getFavoriteResponse(){
        User user = new User();
        user = getCurrentUser();
        List <String> favoriteStations;
        favoriteStations = user.getFavoriteStation();
    
        GenericEntity<List<String>> result
        = new GenericEntity<List<String>>(favoriteStations) {};
        
        return Response.ok(favoriteStations).build();
       
    }
    
    public   List<String> getFavorite(){
        
        User user = new User();
        user = getCurrentUser();
        List <String> favoriteStations;
        favoriteStations = user.getFavoriteStation();
    
        GenericEntity<List<String>> result
        = new GenericEntity<List<String>>(favoriteStations) {};
        
     
        return favoriteStations;
    }
    
    @GET
    @Path("getOwnerCar")
    @RolesAllowed({Group.USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserCars() {
        
        User user = new User();
        user = getCurrentUser();
        
        String ownerId = user.getUserid();
        
        List<Car> UserCars;
        
        UserCars = findUserCars(ownerId);     
        
     return  Response.ok(UserCars).build();
     
    }
     private List<Car> findUserCars(String ownerId) { 
        
        List<Car> cars;
        List<Car> ownerCars = new ArrayList<Car>();
         
        cars = getCars();
        
        for (Car car : cars) {
            if(car.getOwnerId().equals(ownerId)) {
                ownerCars.add(car);
            }
        }
        return ownerCars;
         
    }
  
}
 
    
