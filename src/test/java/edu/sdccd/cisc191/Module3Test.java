package edu.sdccd.cisc191;

import edu.sdccd.cisc191.roles.Class_role;
import edu.sdccd.cisc191.roles.Dps;
import edu.sdccd.cisc191.roles.Healer;
import edu.sdccd.cisc191.roles.Tank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Module3Test {

    @Test
    void testInheritance() {
        Dps a = new Dps();
        Healer b = new Healer();
        Tank c = new Tank();
        assertTrue(a instanceof Class_role, "A should be a child class of Class_role");
        assertTrue(b instanceof Class_role, "B should be a child class of Class_role");
        assertTrue(c instanceof Class_role, "C should be a child class of Class_role");
    }
}
