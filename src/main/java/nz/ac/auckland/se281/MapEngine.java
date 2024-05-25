package nz.ac.auckland.se281;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {

  private Map<String, Country> mapData;

  public MapEngine() {
    this.mapData = new HashMap<>();
    loadMap(); // keep this mehtod invocation
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();
    // add code here to create your data structures

    // add code here to populate the mapData
    for (String country : countries) {
      String[] countryData = country.split(",");
      String name = countryData[0];
      String continent = countryData[1];
      int tax = Integer.parseInt(countryData[2]);
      Country newCountry = new Country(name, continent, tax);
      mapData.put(name, newCountry);
    }

    for (String neighbour : adjacencies) {
      String[] neighbourData = neighbour.split(",");
      String mainCountry = neighbourData[0];

      // if (mapData.containsKey(mainCountry)) {
      //   Country country = mapData.get(mainCountry);
      //   for (int i = 1; i < neighbourData.length; i++) {
      //     String neighbor = neighbourData[i].trim();
      //     // Ensure the neighbor country is valid and in the map
      //     if (mapData.containsKey(neighbor)) {
      //         country.addNeighbor(neighbor);
      //     }
      if (mapData.containsKey(mainCountry)) {
        Country country = mapData.get(mainCountry);
        for (int i = 1; i < neighbourData.length; i++) {
          String neighbor = neighbourData[i].trim();
          // Ensure the neighbor country is valid and in the map
          if (mapData.containsKey(neighbor)) {
            country.addNeighbour(neighbor);
          }
        }
      }
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    // add code here
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}
