package com.example.a21corp.vinca;

import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.Editor.Workspace;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit loadactivitymenu, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Workspace workspace;

    @Before
    public void createWorkspace(){
       workspace = ProjectManager.createProject("Testproject");
    }

    @Test
    public void correctWorkspaceName() throws Exception {
        assertEquals("Testproject", workspace.getTitle());
    }



}