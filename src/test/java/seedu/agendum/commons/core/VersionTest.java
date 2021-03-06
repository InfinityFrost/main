package seedu.agendum.commons.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VersionTest {

    @Test
    public void versionParsingAcceptableVersionStringParsedVersionCorrectly() {
        verifyVersionParsedCorrectly("V0.0.0ea", 0, 0, 0, true);
        verifyVersionParsedCorrectly("V3.10.2", 3, 10, 2, false);
        verifyVersionParsedCorrectly("V100.100.100ea", 100, 100, 100, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void versionParsingWrongVersionStringThrowIllegalArgumentException() {
        Version.fromString("This is not a version string");
    }

    @Test
    public void versionConstructorCorrectParameterValueAsExpected() {
        Version version = new Version(19, 10, 20, true);

        assertEquals(19, version.getMajor());
        assertEquals(10, version.getMinor());
        assertEquals(20, version.getPatch());
        assertEquals(true, version.isEarlyAccess());
    }

    @Test
    public void versionToStringValidVersionCorrectStringRepresentation() {
        // boundary at 0
        Version version = new Version(0, 0, 0, true);
        assertEquals("V0.0.0ea", version.toString());

        // normal values
        version = new Version(4, 10, 5, false);
        assertEquals("V4.10.5", version.toString());

        // big numbers
        version = new Version(100, 100, 100, true);
        assertEquals("V100.100.100ea", version.toString());
    }

    @Test
    public void versionComparableValidVersionCompareToIsCorrect() {
        Version one, another;

        // Tests equality
        one = new Version(0, 0, 0, true);
        another = new  Version(0, 0, 0, true);
        assertTrue(one.compareTo(another) == 0);

        one = new Version(11, 12, 13, false);
        another = new  Version(11, 12, 13, false);
        assertTrue(one.compareTo(another) == 0);

        // Tests different patch
        one = new Version(0, 0, 5, false);
        another = new  Version(0, 0, 0, false);
        assertTrue(one.compareTo(another) > 0);

        // Tests different minor
        one = new Version(0, 0, 0, false);
        another = new  Version(0, 5, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests different major
        one = new Version(10, 0, 0, true);
        another = new  Version(0, 0, 0, true);
        assertTrue(one.compareTo(another) > 0);

        // Tests high major vs low minor
        one = new Version(10, 0, 0, true);
        another = new  Version(0, 1, 0, true);
        assertTrue(one.compareTo(another) > 0);

        // Tests high patch vs low minor
        one = new Version(0, 0, 10, false);
        another = new  Version(0, 1, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests same major minor different patch
        one = new Version(2, 15, 0, false);
        another = new  Version(2, 15, 5, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access vs not early access on same version number
        one = new Version(2, 15, 0, true);
        another = new  Version(2, 15, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access lower version vs not early access higher version compare by version number first
        one = new Version(2, 15, 0, true);
        another = new  Version(2, 15, 5, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access higher version vs not early access lower version compare by version number first
        one = new Version(2, 15, 0, false);
        another = new  Version(2, 15, 5, true);
        assertTrue(one.compareTo(another) < 0);
    }

    @Test
    public void versionComparableValidVersionHashCodeIsCorrect() {
        Version version = new Version(100, 100, 100, true);
        assertEquals(100100100, version.hashCode());

        version = new Version(10, 10, 10, false);
        assertEquals(1010010010, version.hashCode());
    }

    @Test
    public void versionComparableValidVersionEqualIsCorrect() {
        Version one, another;

        one = new Version(0, 0, 0, false);
        another = new  Version(0, 0, 0, false);
        assertTrue(one.equals(another));

        one = new Version(100, 191, 275, true);
        another = new  Version(100, 191, 275, true);
        assertTrue(one.equals(another));
    }
    
    //@@author A0148095X
    @Test
    public void versionComparableNotEqual() {
        Version original = new Version(0, 0, 0, false);

        // null
        Object nullObj = null;
        assertFalse(original.equals(nullObj));
        
        // Different object
        Object obj = new Object();
        assertFalse(original.equals(obj));
    }
    //@@author
    
    private void verifyVersionParsedCorrectly(String versionString,
                                              int major, int minor, int patch, boolean isEarlyAccess) {
        assertEquals(new Version(major, minor, patch, isEarlyAccess), Version.fromString(versionString));
    }
}
