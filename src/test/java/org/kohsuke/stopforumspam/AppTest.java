package org.kohsuke.stopforumspam;

import junit.framework.TestCase;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public class AppTest extends TestCase {
    public void testApp() throws IOException, SAXException {
        StopForumSpam sfs = new StopForumSpam();
        List<Answer> a = sfs.build().ip("199.15.234.84").username("kohsuke").query();
        System.out.println(a);

        System.out.println(sfs.ip("199.15.234.84"));
    }
}
