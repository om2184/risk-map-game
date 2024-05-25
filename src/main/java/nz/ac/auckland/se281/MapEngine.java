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

    for (String adjacency : adjacencies) {
      String[] adjacencyData = adjacency.split(",");
      String mainCountry = adjacencyData[0];

      if (mapData.containsKey(mainCountry)) {
        Country country = mapData.get(mainCountry);
        for (int i = 1; i < adjacencyData.length; i++) {
          String neighbor = adjacencyData[i].trim();
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

    while (true) {
      MessageCli.INSERT_COUNTRY.printMessage();
      String playerInput = Utils.scanner.nextLine();
      String countryName = Utils.capitalizeFirstLetterOfEachWord(playerInput);

      if (mapData.containsKey(countryName)) {
        Country country = mapData.get(countryName);
        MessageCli.COUNTRY_INFO.printMessage(
            country.getName(), country.getContinent(), Integer.toString(country.getTax()));
        break;
      } else {
        MessageCli.INVALID_COUNTRY.printMessage(countryName);
      }
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}
