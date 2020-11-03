/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import Auth.User;
import static Objects.Car.FIND_ALL_CARS;
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
public class Car implements Serializable {

    public static final String FIND_ALL_CARS = "Car.findAllCars";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;

    private String RegNumber;
    private String manufacturer;
    private String model;
    private boolean petrol; 

    @ManyToOne
    @JoinColumn(nullable = false)
    private User carOwner;
    

}
