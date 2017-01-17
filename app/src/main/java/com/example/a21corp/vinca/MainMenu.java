package com.example.a21corp.vinca;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

import io.fabric.sdk.android.Fabric;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private static final int WRITE_EXTERNAL_STORAGE_STATE = 255;

    Button create;
    Button load;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_menu2);
        logo = (ImageView) findViewById(R.id.logoImage);

        create = (Button) findViewById(R.id.newProjectButton);
        create.setOnClickListener(this);
        load = (Button) findViewById(R.id.loadMenuButton);
        load.setOnClickListener(this);
        checkForAnim();
        Uri data = getIntent().getData();
        if (data != null) {
            uriToFile(data);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainMenu", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        //MainMenu is handling this as CreateMenuPopup is a fragment and hence its onActivity is not called for whatever reason
        if(requestCode == CreateMenuPopUp.FILE_SELECT_CODE){
            if(resultCode == RESULT_OK){

                final Uri uri = data.getData();
                uriToFile(uri);


            }
        }
        Log.d("MainMenu", "onActivityResult - done");
    }

    private void uriToFile(Uri uri) {
        // Get the File path from the Uri
        String path = FileUtils.getPath(this, uri);

        // Alternatively, use FileUtils.getFile(Context, Uri)
        if (path == null || !FileUtils.isLocal(path)) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }

        String title = path.substring(path.lastIndexOf("/")+1).replace(".ser", "");
        path = path.substring(0, path.lastIndexOf("/"));
        System.out.println(path);
        System.out.println(title);
        if(ProjectManager.inputCheck(title, getFilesDir().getAbsolutePath() + File.separator + "workspaces")) {
            if (checkPermissionForWriting()) {
                try {
                    Intent editor = new Intent(this, EditorActivity.class);
                    editor.putExtra("title", title);
                    editor.putExtra("dirPath", path);
                    startActivity(editor);
                    //ProjectManager.loadProject(path); //TODO useless path
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "File not compatible, please load a VincaApp file", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "There is already a file with that name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newProjectButton:{
                CreateMenuPopUp p = new CreateMenuPopUp();
                p.show(getFragmentManager(),"pop");

                break;
            }
            case R.id.loadMenuButton:{
                startActivity(new Intent(this, LoadActivity.class));
                //overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            }

        }
    }

    public boolean checkPermissionForWriting() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionCheck =
                    this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (this.shouldShowRequestPermissionRationale
                        (android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    this.requestPermissions
                            (new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    , WRITE_EXTERNAL_STORAGE_STATE);
                }
                if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    public void checkForAnim(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_main_menu);
        if(linearLayout.getTag() != null) {
            String ori = (String) linearLayout.getTag();
            if(!ori.equals("land")){
                Animation rotateLogo = AnimationUtils.loadAnimation(this, R.anim.main_menu_rotate);
                logo.startAnimation(rotateLogo);
                System.out.println("Jep");
            }
        }
    }
}
