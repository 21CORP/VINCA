package com.example.a21corp.vinca;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.a21corp.vinca.Editor.ProjectManager;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    Button create;
    Button load;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_menu2);
        logo = (ImageView) findViewById(R.id.logoImage);
        Animation rotateLogo = AnimationUtils.loadAnimation(this, R.anim.main_menu_rotate);
        logo.startAnimation(rotateLogo);
        create = (Button) findViewById(R.id.newProjectButton);
        create.setOnClickListener(this);
        load = (Button) findViewById(R.id.loadMenuButton);
        load.setOnClickListener(this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //MainMenu is handling this as CreateMenuPopup is a fragment and hence its onActivity is not called for whatever reason
        if(requestCode == CreateMenuPopUp.FILE_SELECT_CODE){
            if(resultCode == RESULT_OK){
                String path = data.getDataString();
                String title = path.substring(path.lastIndexOf("/")+1).replace(".ser", "");
                System.out.println(path);
                System.out.println(title);
                if(ProjectManager.inputCheck(title, getFilesDir().getAbsolutePath() + File.separator + "workspaces")) {
                    try {
                        ProjectManager.loadProject(data.getDataString()); //TODO useless path
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "File not compatible, please load a VincaApp file", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "There is already a file with that name", Toast.LENGTH_SHORT).show();
                }
            }
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
}
