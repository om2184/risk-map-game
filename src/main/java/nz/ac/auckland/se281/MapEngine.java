package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
      Country country = mapData.get(mainCountry);
      for (int i = 1; i < adjacencyData.length; i++) {
        String neighbor = adjacencyData[i].trim();
        // Ensure the neighbor country is valid and in the map
        if (mapData.containsKey(neighbor)) {
          country.addNeighbour(mapData.get(neighbor));
        }
      }
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    MessageCli.INSERT_COUNTRY.printMessage();
    while (true) {
      try {
        Country country = this.validCountryName();
        MessageCli.COUNTRY_INFO.printMessage(
            country.getName(), country.getContinent(), Integer.toString(country.getTax()));
        break;
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(e.getMessage());
      }
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {

    Country startNode = null;
    Country destinationCountry = null;

    MessageCli.INSERT_SOURCE.printMessage();
    while (true) {
      try {
        startNode = this.validCountryName();
        break;
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(e.getMessage());
      }
    }

    MessageCli.INSERT_DESTINATION.printMessage();
    while (true) {
      try {
        destinationCountry = this.validCountryName();
        break;
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(e.getMessage());
      }
    }

    if (startNode.getName().equals(destinationCountry.getName())) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // add code here to find the shortest path between the two countries
    Map<Country, Country> parentMap = new HashMap<>();
    Set<Country> visited = new HashSet<>();
    Queue<Country> queue = new LinkedList<>();

    queue.add(startNode);
    visited.add(startNode);
    parentMap.put(startNode, null);

    while (!queue.isEmpty()) {
      Country node = queue.poll();
      List<Country> adjacentNodes = node.getNeighbours();
      for (Country n : adjacentNodes) {
        if (!visited.contains(n)) {
          visited.add(n);
          parentMap.put(n, node);
          queue.add(n);

          if (n.equals(destinationCountry)) {
            List<Country> path = new ArrayList<>();
          }
        }
      }
    }
  }

  public Country validCountryName() throws InvalidCountryException {
    String playerInput = Utils.scanner.nextLine();
    String countryName = Utils.capitalizeFirstLetterOfEachWord(playerInput);

    if (mapData.containsKey(countryName)) {
      return mapData.get(countryName);
    } else {
      throw new InvalidCountryException(countryName);
    }
  }
}
