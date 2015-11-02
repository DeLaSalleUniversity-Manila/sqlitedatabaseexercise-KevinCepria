package lbyp24.ulam;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor favoritesCursor;
    private ListView Options;
    private ArrayAdapter<String> adapter;
    private String Opt[]={"BREAKFAST","LUNCH","DINNER"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Opt);
        Options = (ListView) findViewById(R.id.options);
        Options.setAdapter(adapter);
        Options.setOnItemClickListener(new
                                               AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                                       if(position==0){
                                                           Intent intent = new Intent(getBaseContext(), Category.class);
                                                           startActivity(intent);
                                                       }


                                                   }
                                               });
        ListView listFavorites = (ListView)findViewById(R.id.favorites);
        try{
            SQLiteOpenHelper Database = new UlamData(this);
            db = Database.getReadableDatabase();
            favoritesCursor = db.query("BF",
                    new String[] { "_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            favoritesCursor,
                            new String[]{"NAME"},
                            new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
//Navigate to DrinkActivity if a drink is clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id)
            {
                Intent intent = new Intent(MainActivity.this, Drink.class);
                intent.putExtra(Drink.EXTRA_BREAK, (int)id);
                startActivity(intent);
            }
        });
    }
    //Close the cursor and database in the onDestroy() method
    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }

   @Override
    public void onRestart() {
        super.onRestart();
        try{
            UlamData Database = new UlamData(this);
            db = Database.getReadableDatabase();
            Cursor newCursor = db.query("BF",
                    new String[] { "_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            ListView listFavorites = (ListView)findViewById(R.id.favorites);
            CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
            adapter.changeCursor(newCursor);
            favoritesCursor = newCursor;
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    }
