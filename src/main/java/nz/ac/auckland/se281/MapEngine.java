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

    MessageCli.INSERT_SOURCE.printMessage();
    while (true) {
      try {
        Country startNode = this.validCountryName();
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(e.getMessage());
      }
    }

    // Set<String> visited = new HashSet<>();
    // Map<String, Country> parentMap = new HashMap<>();
    // Stack<String> stack = new Stack<>();
    // stack.push(startNode);
    // parentMap.put(startNode, null);

    // while (!stack.isEmpty()) {
    //     T current = stack.pop();
    //     if (!visited.contains(current)) {
    //         visited.add(current);
    //         for (T neighbor : adjacencyMap.get(current)) {
    //             if (!visited.contains(neighbor)) {
    //                 stack.push(neighbor);
    //                 parentMap.put(neighbor, current);
    //             } else if (!neighbor.equals(parentMap.get(current)) &&
    // neighbor.equals(startNode)) {
    //                 // Cycle detected, reconstruct the path
    //                 List<T> cyclePath = new ArrayList<>();
    //                 cyclePath.add(neighbor);
    //                 T node = current;
    //                 while (node != null) {
    //                     cyclePath.add(node);
    //                     node = parentMap.get(node);
    //                     if (node != null && node.equals(neighbor)) {
    //                         cyclePath.add(node);
    //                         break;
    //                     }
    //                 }
    //                 Collections.reverse(cyclePath);
    //                 return cyclePath;
    //             }
    //         }
    //     }
    // }
    // return null; // No cycle found
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
