package com.example.student.gamedbapp;

import android.app.AlertDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.student.gamedbapp.db.GameDAO;
import com.example.student.gamedbapp.model.Game;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView listVieuwMain;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listVieuwMain = (ListView) findViewById(R.id.list);
        Calendar c = Calendar.getInstance();
        c.set(2004, Calendar.OCTOBER, 23);
        Date timeInMillis = c.getTime();
        // Date date = new Date(timeInMillis);


        Game gta = new Game ("GTA San Andreas",
                "ca douille",
                "http://vignette1.wikia.nocookie.net/gtawiki/images/9/92/GTA_San_Andreas_Box_Art.jpg/revision/latest?cb=20090429021856",
                "action",
                "Rockstar",
                timeInMillis,
                5.0f,
                true);


        GameDAO gameDAO = new GameDAO(this);
        gameDAO.openWritable();
        long id = gameDAO.insert(gta);
        gameDAO.close();
        Toast.makeText(this, "id : " + id, Toast.LENGTH_LONG).show();

        GameDAO gameDAO2 = new GameDAO(this);
        gameDAO2.openReadable();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{GameDAO.COL_TITLE, GameDAO.COL_EDITOR},
                new int[]{android.R.id.text1, android.R.id.text2});

        listVieuwMain.setAdapter(adapter);
        for (Game g : gameDAO2.getGames()){
            Log.i("MainActivity", g.getTitle());
        }
        gameDAO2.close();
    }
}
