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

    // Create a map of countries
    for (String country : countries) {
      String[] countryData = country.split(",");
      String name = countryData[0];
      String continent = countryData[1];
      int tax = Integer.parseInt(countryData[2]);
      Country newCountry = new Country(name, continent, tax);
      mapData.put(name, newCountry);
    }

    // Add the neighbors to the countries
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

    // prompt the user for the source and destination countries
    Country sourceCountry = promptForCountry(MessageCli.INSERT_SOURCE, MessageCli.INVALID_COUNTRY);
    Country destinationCountry =
        promptForCountry(MessageCli.INSERT_DESTINATION, MessageCli.INVALID_COUNTRY);

    // check if the source and destination countries are the same
    if (sourceCountry.getName().equals(destinationCountry.getName())) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // find the shortest path between the source and destination countries
    List<Country> path = findShortestPath(sourceCountry, destinationCountry);

    // Create string sets to store the continents and countries in the path
    Set<String> continents = new LinkedHashSet<>();
    Set<String> countries = new LinkedHashSet<>();
    int totalTax = 0;

    // Add the countries and continents to the sets
    for (Country country : path) {
      continents.add(country.getContinent());
      countries.add(country.getName());
      if (!country.equals(sourceCountry)) {
        totalTax += country.getTax();
      }
    }

    // Convert the sets to strings
    String pathCountryInfo = pathToString(countries);
    String pathContinentInfo = pathToString(continents);

    // Print the path information
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

  public Country promptForCountry(MessageCli insertCountry, MessageCli invalidCountry) {
    Country country = null;
    insertCountry.printMessage();
    // Keep prompting the user until a valid country is entered
    while (true) {
      try {
        country = validCountryName();
        break;
      } catch (InvalidCountryException e) {
        invalidCountry.printMessage(e.getMessage());
      }
    }
    return country;
  }

  public List<Country> findShortestPath(Country startNode, Country destinationCountry) {

    Map<Country, Country> parentMap = new HashMap<>(); // to store the parent of each country
    Set<Country> visited = new HashSet<>();
    Queue<Country> queue = new LinkedList<>();

    queue.add(startNode);
    visited.add(startNode);
    parentMap.put(startNode, null);

    // Perform a BFS to find the shortest path
    while (!queue.isEmpty()) {
      Country node = queue.poll();
      List<Country> adjacentNodes = node.getNeighbours();
      for (Country n : adjacentNodes) {
        if (!visited.contains(n)) {
          visited.add(n);
          parentMap.put(n, node);
          queue.add(n);

          // If the destination country is found, return the path
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

  public String pathToString(Set<String> path) {
    // Convert the set to a string
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
