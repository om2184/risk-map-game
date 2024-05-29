package nz.ac.auckland.se281;

/**
 * Is the exception thrown when the country is invalid.
 *
 * @param countryName the name of the invalid country entered by user.
 */
public class InvalidCountryException extends Exception {
  public InvalidCountryException(String countryName) {
    super(countryName);
  }
}
