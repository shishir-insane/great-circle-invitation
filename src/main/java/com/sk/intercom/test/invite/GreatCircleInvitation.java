package com.sk.intercom.test.invite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sk.intercom.test.dto.Customer;

/**
 * Invite any customer within 100km of our Dublin office (GPS coordinates
 * 53.3381985, -6.2592576) for some food and drinks on us. Program that will
 * read the full list of customers and output the names and user ids of matching
 * customers (within 100km), sorted by user id (ascending).
 */
public class GreatCircleInvitation {

    // Static constants
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String USER_ID = "user_id";
    private static final String NAME = "name";
    private static final String UTF_8 = "UTF-8";

    // Radius of Earth
    private static final double EARTH_RADIUS = 6371.01;

    // Office base location cordinates
    private static final double BASE_LATITUDE = 53.339428;
    private static final double BASE_LONGITUDE = -6.257664;

    // JSON Data file path
    private static final String FILE_PATH = "gistfile1.txt";

    /**
     * Calculates distance of input location from office base location
     *
     * @param latitude
     *            - double
     * @param longitude
     *            - double
     * @return customer distance - double
     */
    private static double calculateDistance(final double latitude, final double longitude) {
        final double baseLat = degreesToRadians(BASE_LATITUDE);
        final double userLat = degreesToRadians(latitude);
        final double baseLon = degreesToRadians(BASE_LONGITUDE);
        final double userLon = degreesToRadians(longitude);

        return Math.acos(Math.sin(baseLat) * Math.sin(userLat)
                + Math.cos(baseLat) * Math.cos(userLat) * Math.cos(baseLon - userLon)) * EARTH_RADIUS;
    }

    /**
     * Convert degree to radians
     *
     * @param latOrLon
     * @return
     */
    private static double degreesToRadians(final double latOrLon) {
        return latOrLon * (Math.PI / 180);
    }

    /**
     * This method reads the JSON file and retrieves the customers within the
     * specified range.
     *
     * @param fileLocation
     * @param distance
     * @return
     */
    private Map<Long, Customer> getCustomersToInvite(final File fileLocation, final int distance) {
        final Map<Long, Customer> finalResult = new TreeMap<>();
        try {
            final Scanner scn = new Scanner(fileLocation, UTF_8);
            while (scn.hasNext()) {
                final JSONObject obj = (JSONObject) new JSONParser().parse(scn.nextLine());
                final Double calculatedDistance = calculateDistance(Double.valueOf((String) obj.get(LATITUDE)),
                        Double.valueOf((String) obj.get(LONGITUDE)));
                if (calculatedDistance <= distance) {
                    finalResult.put((Long) obj.get(USER_ID), new Customer((String) obj.get(NAME), calculatedDistance));
                }
            }
        } catch (final FileNotFoundException e) {
            System.out.println("Invalid file location - " + e.getMessage());
        } catch (final ParseException e) {
            System.out.println("Invalid JSON data found at input file location - " + e.getMessage());
        }
        return finalResult;
    }

    /**
     * This method is the wrapper for great circle calculation logic call
     *
     * @param distance
     * @return
     */
    public Map<Long, Customer> listCustomers(final int distance) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource(FILE_PATH).getFile());
        return getCustomersToInvite(file, distance);
    }

    public static void main(final String args[]) {
        final GreatCircleInvitation circleInvitation = new GreatCircleInvitation();
        final Map<Long, Customer> customerMap = circleInvitation.listCustomers(100);
        System.out.println("===============================================================");
        System.out.println(
                String.format("%d customers found within 100 kms from Intercom Dublin office", customerMap.size()));
        if (0 < customerMap.size()) {
            System.out.println("---------------------------------------------------------------");
            System.out.printf("%3s %20s %20s", "ID", "Name", "Distance");
            System.out.println();
            System.out.println("---------------------------------------------------------------");
            for (final Entry<Long, Customer> entry : customerMap.entrySet()) {
                System.out.printf("%3s %20s %20s", entry.getKey(), entry.getValue().getName(),
                        String.format("%.2f kms", entry.getValue().getDistance()));
                System.out.println();
            }
        }
        System.out.println("===============================================================");
    }
}