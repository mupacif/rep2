package be.formation.gamedbapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.formation.gamedbapplication.model.Game;

public class GameDAO {

    // region Constantes

    public static final String TABLE_NAME = "game";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";
    public static final String COL_IMAGE = "image_url";
    public static final String COL_GENRE = "genre";
    public static final String COL_EDITOR = "editor";
    public static final String COL_PUBDATE = "pub_date";
    public static final String COL_RATING = "rating";
    public static final String COL_OWNED = "owned";

    public static final String CREATE_REQUEST = "CREATE TABLE " + TABLE_NAME
            + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT NOT NULL, "
            + COL_DESC + " TEXT, "
            + COL_IMAGE + " TEXT, "
            + COL_GENRE + " TEXT NOT NULL, "
            + COL_EDITOR + " TEXT, "
            + COL_PUBDATE + " INTEGER, "
            + COL_RATING + " REAL NOT NULL, "
            + COL_OWNED + " INTEGER NOT NULL"
            + ");";

    public static final String DROP_REQUEST = "DROP TABLE " + TABLE_NAME + ";";

    // endregion

    private DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public GameDAO(Context context) {
        this.context = context;
    }

    public void openWritable() {
        dbHelper = new DbHelper(this.context);
        db = dbHelper.getWritableDatabase();
    }

    public void openReadable() {
        dbHelper = new DbHelper(this.context);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public long insert(Game game) {

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, game.getTitle());
        cv.put(COL_DESC, game.getDescription());
        cv.put(COL_GENRE, game.getGenre());
        cv.put(COL_EDITOR, game.getEditor());
        cv.put(COL_IMAGE, game.getImageUrl());
        cv.put(COL_RATING, game.getRating());
        cv.put(COL_PUBDATE, game.getPubDate().getTime());
        cv.put(COL_OWNED, game.isOwned() ? 1 : 0);
        return db.insert(TABLE_NAME, null, cv);
    }

    public Cursor getGamesCursor() {

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c;
        } else {
            return null;
        }
    }

    public Cursor getGameCursorById(long id) {

        Cursor c = db.query(TABLE_NAME, null, COL_ID + "=" + id, null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c;
        } else {
            return null;
        }
    }

    public Game cursorToGame(Cursor c) {
        return new Game(
                c.getLong(c.getColumnIndex(COL_ID)),
                c.getString(c.getColumnIndex(COL_TITLE)),
                c.getString(c.getColumnIndex(COL_DESC)),
                c.getString(c.getColumnIndex(COL_IMAGE)),
                c.getString(c.getColumnIndex(COL_GENRE)),
                c.getString(c.getColumnIndex(COL_EDITOR)),
                new Date(c.getLong(c.getColumnIndex(COL_PUBDATE))),
                c.getFloat(c.getColumnIndex(COL_RATING)),
                c.getInt(c.getColumnIndex(COL_OWNED)) == 1
        );
    }

    public List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        Cursor c = getGamesCursor();
        if (c != null) {
            do {
                games.add(cursorToGame(c));
            } while (c.moveToNext());
        }
        return games;
    }

    public Game getGameById(long id) {
        Cursor c = getGameCursorById(id);
        if (c != null) {
            return cursorToGame(c);
        } else {
            return null;
        }
    }
}
