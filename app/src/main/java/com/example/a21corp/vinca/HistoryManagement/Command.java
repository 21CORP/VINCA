package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public interface Command {
    void execute();
    void inverse();
}
