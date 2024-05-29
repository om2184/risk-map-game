package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a country in the world map. Each country has a name, a continent, a tax rate and a
 * list of neighbouring countries.
 */
public class Country {

  private String name;
  private String continent;
  private int tax;
  private List<Country> neighbours;

  public Country(String name, String continent, int tax) {
    this.name = name;
    this.continent = continent;
    this.tax = tax;
    this.neighbours = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public String getContinent() {
    return continent;
  }

  public int getTax() {
    return tax;
  }

  public List<Country> getNeighbours() {
    return neighbours;
  }

  public void addNeighbour(Country neighbour) {
    neighbours.add(neighbour);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Country other = (Country) obj;
    // We only compare the name of the country as it is the only unique identifier
    if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }
}
