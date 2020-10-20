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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor


@Table(name = "FUELSTATIONS")
@NamedQuery(name = FIND_ALL_FUELSTATIONS, query = "select i from FuelStaion i")

public class FuelStation implements Serializable {
    public static final String FIND_ALL_FUELSTATIONS = "Item.findAllItems";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String name;
    private String coordinates;
    private int petrolPrice;
    private int dieselPrice;
    private boolean diesel;
    private boolean petrol;
    
}
