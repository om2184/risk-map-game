package nz.ac.auckland.se281;

import java.util.LinkedList;
import java.util.List;

public class Country {

  private String name;
  private String continent;
  private int tax;
  private List<String> neighbours;

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

  public List<String> getNeighbours() {
    return neighbours;
  }

  public void addNeighbour(String neighbour) {
    neighbours.add(neighbour);
  }
}
