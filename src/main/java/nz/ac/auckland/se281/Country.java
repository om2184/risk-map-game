package nz.ac.auckland.se281;

import java.util.LinkedList;
import java.util.List;

public class Country {

  private String name;
  private String continent;
  private int tax;
  private List<Country> neighbours;

  public Country(String name, String continent, int tax) {
    this.name = name;
    this.continent = continent;
    this.tax = tax;
    this.neighbours = new LinkedList<>();
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
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Country other = (Country) obj;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }
}
