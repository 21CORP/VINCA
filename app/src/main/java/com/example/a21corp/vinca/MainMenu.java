package com.example.a21corp.vinca;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    Button create;
    Button load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu2);
        create = (Button) findViewById(R.id.newProjectButton);
        create.setOnClickListener(this);
        load = (Button) findViewById(R.id.loadMenuButton);
        load.setOnClickListener(this);

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
                break;
            }

        }
    }
}
