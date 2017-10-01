package com.sk.intercom.test.invite;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sk.intercom.test.dto.Customer;

/**
 * This class contains unit tests to validate GreatCircleInvitation. Data file
 * used for unit tests is present in the src/test/resources/gistfile1.txt
 *
 * @author shishir_kumar
 *
 */
public class GreatCircleInvitationTest {

    private static final int EXPECTED_IN_100 = 16;
    private static final int EXPECTED_IN_75 = 10;
    private static final int EXPECTED_IN_10 = 0;

    private GreatCircleInvitation greatCircleInvitation;

    @Before
    public void init() {
        greatCircleInvitation = new GreatCircleInvitation();
    }

    @Test
    public void testListCustomersWithin100Kms() {
        final Map<Long, Customer> calculatedMap = greatCircleInvitation.listCustomers(100);
        Assert.assertEquals(EXPECTED_IN_100, calculatedMap.size());
    }

    @Test
    public void testListCustomersWithin10Kms() {
        final Map<Long, Customer> calculatedMap = greatCircleInvitation.listCustomers(10);
        Assert.assertEquals(EXPECTED_IN_10, calculatedMap.size());
    }

    @Test
    public void testListCustomersWithin75Kms() {
        final Map<Long, Customer> calculatedMap = greatCircleInvitation.listCustomers(75);
        Assert.assertEquals(EXPECTED_IN_75, calculatedMap.size());
    }
}