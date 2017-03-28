package be.ehb.dt;/**
 * Created by sammi on 3/27/2017.
 */

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.Workspace;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class InternshipLoggerApplication {

    private static JToggl jToggl;
    private static Workspace workspace;
    private static Project project;

    private static Date startDate;
    private static Date stopDate;

    private static String togglApiToken = "9fd0df2abb101c7fb80072ec424ebae9";
    private static String workspaceName = "Sammi Fux";
    private static String projectName = "Prestige";
    private static String startDateString = "2017-02-13";
    private static String stopDateString = "2017-03-25";

    public static void main(String[] args) throws IOException {
        jToggl = new JToggl(togglApiToken, "api_token");
        jToggl.setThrottlePeriod(500l);
        jToggl.switchLoggingOn();

        List<Workspace> workspaces = jToggl.getWorkspaces();
        assertTrue(workspaces.size() > 0);
        Iterator<Workspace> workspaceIterator = workspaces.iterator();

        while(workspaceIterator.hasNext()) {
            Workspace cw = workspaceIterator.next();
            if (cw.getName().equals(workspaceName)) {
                workspace = cw;
                break;
            }
        }

        if (workspace == null) throw new RuntimeException("Workspace Name not found, please fill in correct Workspace Name.");

        List<Project> projects = jToggl.getWorkspaceProjects(workspace.getId());
        Iterator<Project> projectIterator = projects.iterator();

        while(projectIterator.hasNext()) {
            Project cp = projectIterator.next();
            if (cp.getName().equals(projectName)) {
                project = cp;
                break;
            }
        }

        if (project == null) throw new RuntimeException("Project Name not found, please fill in correct Project Name.");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            startDate = sdf.parse(startDateString);
            stopDate = sdf.parse(stopDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<TimeEntry> timeEntries = jToggl.getTimeEntries(startDate, stopDate);
        Iterator<TimeEntry> entryIterator = timeEntries.iterator();
        ArrayList<TimeEntry> projectTimeEntries = new ArrayList<TimeEntry>();

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH mm");
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));

        while(entryIterator.hasNext()) {
            TimeEntry ce = entryIterator.next();
            if (ce.getPid().equals(project.getId())) {
                projectTimeEntries.add(ce);

                String[] splittedStartString = sdf2.format(ce.getStart()).split("\\s+");
                String[] splittedStopString = sdf2.format(ce.getStop()).split("\\s+");

                System.out.println(splittedStartString[1]);

                URL url = new URL("https://internship.ehb.be/Multec/nl/LogBook/Edit");
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setDoOutput(true);

                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
                httpCon.setRequestProperty("Accept-Encoding", "br");
                httpCon.setRequestProperty("Cookie", "__RequestVerificationToken_L011bHRlYw2=PJ7kOJ-j8BA5ldul67wFf2ZY97KeZOfGC20US46OWOZRKOu5-956UIFs1n8DlY05iUCJWJU7CJ3dfP3KdRSSyfEjQTLAzTBB24ODDE2K_cI1; .ASPXAUTH=67EA66A857B2EFDB5D6BA1772AD6E6F3B87FF1D247C6404DDB3730B18D0A2615DEAFD3EBEF22E6FD0EF98BBB6588032A51F50B05B7848AC15B6C467A0336F11ADA036055DBF8D83534C8F62C63540AE44EF5A56C70BB8F73F8798F1853997A1109726460C96C35DF24498F6C42325129FB84A50D2DCFA5D1BC34E701BF6AB43D");

                OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

                String str =  "Id=0&Day=" + splittedStartString[0] + "&IshipLogEntry_Internship=1225&StartHr=" + splittedStartString[1] + "&StartMin=" + splittedStartString[2] + "&EndHr=" + splittedStopString[1] + "&EndMin=" + splittedStopString[2] + "&Description=" + ce.getDescription();
                byte[] outputInBytes = str.getBytes("UTF-8");

                httpCon.getOutputStream().write(outputInBytes);
                System.out.println(httpCon.getResponseCode());
                System.out.println(httpCon.getResponseMessage());

                out.close();
            }
        }

        if (projectTimeEntries.isEmpty()) throw new RuntimeException("No tracked time entries found in selected project.");

        System.out.println(projectTimeEntries);
    }

    public void test() {
        System.out.println("niks");
    }

    /*@Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../InternshipLogger.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("EHB Internship Logger");
        primaryStage.show();
    }*/
}
