package com.example.a21corp.vinca.AndroidUtilities;

import java.io.File;

/**
 * Created by root on 1/4/17.
 */

public interface OnFileSelectedListener {
    public void onSelected(File f);
    public void OnFileCheckedChanged(File f, boolean checked);
}
