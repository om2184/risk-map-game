package nz.ac.auckland.se281;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
    Country country = promptForCountry(MessageCli.INSERT_COUNTRY, MessageCli.INVALID_COUNTRY);

    MessageCli.COUNTRY_INFO.printMessage(
        country.getName(), country.getContinent(), Integer.toString(country.getTax()));
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {

    Country startNode = promptForCountry(MessageCli.INSERT_SOURCE, MessageCli.INVALID_COUNTRY);
    Country destinationCountry =
        promptForCountry(MessageCli.INSERT_DESTINATION, MessageCli.INVALID_COUNTRY);

    if (startNode.getName().equals(destinationCountry.getName())) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    List<Country> path = findShortestPath(startNode, destinationCountry);

    Set<String> continents = new LinkedHashSet<>();
    Set<String> countries = new LinkedHashSet<>();
    int totalTax = 0;

    for (Country country : path) {
      continents.add(country.getContinent());
      countries.add(country.getName());
      if (!country.equals(startNode)) {
        totalTax += country.getTax();
      }
    }

    String pathCountryInfo = getPathInfo(countries);
    String pathContinentInfo = getPathInfo(continents);

    MessageCli.ROUTE_INFO.printMessage(pathCountryInfo);
    MessageCli.CONTINENT_INFO.printMessage(pathContinentInfo);
    MessageCli.TAX_INFO.printMessage(Integer.toString(totalTax));
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

  public Country promptForCountry(MessageCli insertMessage, MessageCli invalidMessage) {
    Country country = null;
    insertMessage.printMessage();
    while (true) {
      try {
        country = validCountryName();
        break;
      } catch (InvalidCountryException e) {
        invalidMessage.printMessage(e.getMessage());
      }
    }
    return country;
  }

  public List<Country> findShortestPath(Country startNode, Country destinationCountry) {
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
            List<Country> path = new LinkedList<>();
            Country current = n;
            while (current != null) {
              path.add(current);
              current = parentMap.get(current);
            }
            Collections.reverse(path);
            return path;
          }
        }
      }
    }
    return null;
  }

  public String getPathInfo(Set<String> path) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    Iterator<String> iterator = path.iterator();
    while (iterator.hasNext()) {
      sb.append(iterator.next());
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");

    return sb.toString();
  }
}
