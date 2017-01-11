package com.example.a21corp.vinca;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Thomas on 11-01-2017.
 */

public class FileImporter extends Activity{

    private static final int FILE_SELECT_CODE = 1;

    public void importFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
    //    try {
            startActivityForResult(Intent.createChooser(intent, "Select file to import"), FILE_SELECT_CODE);
      //  }
      //  catch(Exception e){
      //      e.printStackTrace();
      //  }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_SELECT_CODE){
            if(resultCode == RESULT_OK){
                System.out.println(data.getData());
                System.out.println(data.getDataString());
            }
        }

    }
}
