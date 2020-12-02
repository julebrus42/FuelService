/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import Auth.User;
import static Objects.Car.FIND_ALL_CARS;
import static Objects.Car.FIND_CAR_BY_OWNER_IDs;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Flamin Veinz
 */
@Entity
@Table(name = "Cars")
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = FIND_ALL_CARS, query = "select i from Car i")
@NamedQuery(name = FIND_CAR_BY_OWNER_IDs, query = "select i from Car i where i.carOwner.userid in :ids")
public class Car implements Serializable {

    public static final String FIND_ALL_CARS = "Car.findAllCars";
    public static final String FIND_CAR_BY_OWNER_IDs = "Car.findCarByIds";

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;

    private String regNumber;
    private String manufacturer;
    private String model;
    private boolean petrol; 
    private String ownerId;
    private double fuelUsage;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User carOwner;

    public void setOwner(String ownerId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
