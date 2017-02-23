package be.formation.gamedbapplication;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import be.formation.gamedbapplication.db.DbHelper;
import be.formation.gamedbapplication.db.GameDAO;
import be.formation.gamedbapplication.model.Game;

public class MainActivity extends AppCompatActivity {

    private ListView lvMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMainList = (ListView) findViewById(R.id.lv_main_list);

        Calendar c = Calendar.getInstance();
        c.set(2004, Calendar.OCTOBER, 26);

        Game g1 = new Game("GTA San Andreas",
                "Ca douille",
                "http://www.rockstargames.com/sanandreas/image/mobile_fob.png",
                "Action",
                "Rockstar",
                c.getTime(),
                5.0f,
                true);

        GameDAO gameDAO = new GameDAO(this);
        gameDAO.openWritable();
        long id = gameDAO.insert(g1);
        gameDAO.close();
        Toast.makeText(this, "id : " + id, Toast.LENGTH_LONG).show();

        GameDAO gameDAO2 = new GameDAO(this);
        gameDAO2.openReadable();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                gameDAO2.getGamesCursor(),
                new String[]{GameDAO.COL_TITLE, GameDAO.COL_EDITOR},
                new int[] {android.R.id.text1, android.R.id.text2});

        lvMainList.setAdapter(adapter);

        for (Game g : gameDAO2.getGames()) {
            Log.i("MainActivity", g.getTitle());
        }

        gameDAO2.close();
    }
}
