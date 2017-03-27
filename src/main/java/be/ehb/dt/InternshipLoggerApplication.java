package be.ehb.dt;/**
 * Created by sammi on 3/27/2017.
 */

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.Workspace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class InternshipLoggerApplication extends Application {

    private static JToggl jToggl;
    private static Workspace workspace;
    private static Project project;

    private static Date startDate;
    private static Date stopDate;

//    private static String togglApiToken = "9fd0df2abb101c7fb80072ec424ebae9";
//    private static String workspaceName = "Sammi Fux";
//    private static String projectName = "Prestige";
//    private static String startDate = "2017-02-13";
//    private static String stopDate = "2017-03-25";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../InternshipLogger.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("EHB Internship Logger");
        primaryStage.show();
    }

    private void configureJToggle(String togglApiToken) {
        jToggl = new JToggl(togglApiToken, "api_token");
        jToggl.setThrottlePeriod(500l);
        jToggl.switchLoggingOn();
    }

    private void getWorkspace(String workspaceName) {
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
    }

    private void getProject(String projectName) {
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
    }

    private void getTimeEntries(String startDateString, String stopDateString) {
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

        while(entryIterator.hasNext()) {
            TimeEntry ce = entryIterator.next();
            if (ce.getPid().equals(project.getId())) {
                projectTimeEntries.add(ce);
            }
        }

        if (projectTimeEntries.isEmpty()) throw new RuntimeException("No tracked time entries found in selected project.");

        System.out.println(projectTimeEntries);
    }
}
