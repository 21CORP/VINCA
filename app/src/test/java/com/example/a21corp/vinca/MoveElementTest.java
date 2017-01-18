package com.example.a21corp.vinca;

import com.example.a21corp.vinca.Editor.Workspace;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by ymuslu on 18-01-2017.
 */

public class MoveElementTest {

    private Workspace workspaceGeneric;
    private Workspace workspaceWithController;
    private Workspace workspaceWithHistorian;
    private int[] indices = new int[50];
    private VincaElement[] vincas;

    @Before
    public void fillWorkspace() {

        vincas = new VincaElement[indices.length];

        for (int i = 0; i < indices.length; i++) {
            indices[i] = (int) (Math.random() * i);
            vincas[i] = VincaElement.create(new Random().nextInt(7));

            //Nodes cannot be added to containers
            while (vincas[i].type == VincaElement.ELEMENT_METHOD) {
                vincas[i] = VincaElement.create(new Random().nextInt(7));
            }
        }

        fillGeneric();
    }

    private void fillGeneric() {
        workspaceGeneric = new Workspace("Test", new ArrayList<Container>());
        int i = 0;
        for (VincaElement element : vincas) {
            ((Container) workspaceGeneric.getCursor()).containerList.add(indices[i], (Element) element);
            i = i + 1;
        }
    }

    private void fillWithHistorian() {
        workspaceWithHistorian = new Workspace("Test", new ArrayList<Container>());
        WorkspaceController workspaceController = new WorkspaceController(workspaceWithHistorian);
        int i = 0;
        for (VincaElement element : vincas) {
            Historian.getInstance().storeAndExecute
                    (new MoveCommand
                            (element, workspaceWithHistorian.getCursor()
                                    , element.parent, indices[i], workspaceController));
            i = i + 1;
        }
    }

    private void fillWithController() {
        workspaceWithController = new Workspace("Test", new ArrayList<Container>());
        WorkspaceController workspaceController = new WorkspaceController(workspaceWithController);
        int i = 0;
        for (VincaElement element : vincas) {
            workspaceController.setParent(element, workspaceWithController.getCursor(), indices[i]);
            i = i + 1;
        }
    }

    @Test
    public void fillWorkspaceWithControllerTest() {
        fillWithController();
        assertArrayEquals((workspaceGeneric.projects.get(0).containerList).toArray()
                , (((Container) workspaceWithController.getCursor()).containerList).toArray());
    }

    @Test
    public void fillWorkspaceWithHistorianTest() {
        fillWithHistorian();
        assertArrayEquals(((Container) workspaceGeneric.getCursor()).containerList.toArray()
                , ((Container) workspaceWithHistorian.getCursor()).containerList.toArray());
    }
}
