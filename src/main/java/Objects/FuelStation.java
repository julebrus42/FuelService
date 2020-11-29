/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 *
 * @author Petter Høvik Lintoft
 */



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import static Objects.FuelStation.FIND_ALL_FUELSTATIONS;
import static Objects.FuelStation.FIND_FUELSTATIONS_BY_IDs;
import static Objects.FuelStation.FIND_FUELSTATIONS_BY_PRICE;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor


@Table(name = "FuelStations")
@NamedQuery(name = FIND_ALL_FUELSTATIONS, query = "select i from FuelStation i")
@NamedQuery(name = FIND_FUELSTATIONS_BY_PRICE, query = "select i from FuelStation i order by i.petrolPrice")
@NamedQuery(name = FIND_FUELSTATIONS_BY_IDs, query = "select i from FuelStation i where i.id in :ids")


public class FuelStation implements Serializable {
    public static final String FIND_ALL_FUELSTATIONS = "FuelStation.findAllFuelStations";
    public static final String FIND_FUELSTATIONS_BY_IDs = "FuelStation.findStationById";
    public static final String FIND_FUELSTATIONS_BY_PRICE = "FuelStation.findAllStationsByPrice";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String name;
    private String coordinates;
    private double petrolPrice;
    private double dieselPrice;
    private boolean diesel;
    private boolean petrol;
   // private double avragePrice = (petrolPrice + dieselPrice)/2;
    
}
