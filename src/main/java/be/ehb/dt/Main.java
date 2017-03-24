package be.ehb.dt;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Workspace;
import org.junit.BeforeClass;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by SaFu on 23/03/2017.
 */
public class Main {

    private static JToggl jToggl;
    private static Workspace workspace;

    //private static String token = "9fd0df2abb101c7fb80072ec424ebae9";

    public static void main(String[] args) throws Exception {
        String togglApiToken = System.getenv("9fd0df2abb101c7fb80072ec424ebae9");
        if (togglApiToken == null) {
            togglApiToken = System.getProperty("9fd0df2abb101c7fb80072ec424ebae9");
            if (togglApiToken == null) {
                throw new RuntimeException("TOGGL_API_TOKEN not set.");
            }
        }
        jToggl = new JToggl(togglApiToken, "9fd0df2abb101c7fb80072ec424ebae9");
        jToggl.setThrottlePeriod(500l);
        jToggl.switchLoggingOn();

        List<Workspace> workspaces = jToggl.getWorkspaces();
        assertTrue(workspaces.size() > 0);
        workspace = workspaces.get(0);
        System.out.println(workspaces);
    }

}
