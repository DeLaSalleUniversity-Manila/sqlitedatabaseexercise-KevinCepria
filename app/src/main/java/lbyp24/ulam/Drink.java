package lbyp24.ulam;

/**
 * Created by Kevin Cepria on 11/2/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.CheckBox;
import android.content.ContentValues;
public class Drink extends Activity {
    public static final String EXTRA_BREAK = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as);
//Get the drink from the intent
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_BREAK);
//Create a cursor
        try {
            SQLiteOpenHelper Database = new UlamData(this);
            SQLiteDatabase db = Database.getWritableDatabase();
            Cursor cursor = db.query("BF",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkNo)},
                    null, null, null);
//Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
//Get the drink details from the cursor
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);
                //Populate the drink name
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);
//Populate the drink description
                TextView description = (TextView) findViewById(R.id.des);
                description.setText(descriptionText);
//Populate the drink image
                ImageView photo = (ImageView) findViewById(R.id.imageView);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
//Populate the favorite checkbox
                CheckBox favorite = (CheckBox) findViewById(R.id.fav);
                favorite.setChecked(isFavorite);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onFavoriteClicked();
                    }
                });



            }
            ;
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        onFavoriteClicked();
    }

    //Update the database when the checkbox is clicked
    public void onFavoriteClicked() {
        int drinkNo = (Integer) getIntent().getExtras().get("drinkNo");
        CheckBox favorite = (CheckBox) findViewById(R.id.fav);
        ContentValues Breakfast = new ContentValues();
        Breakfast.put("FAVORITE", favorite.isChecked());
        SQLiteOpenHelper Database =
                new UlamData(Drink.this);
        try {
            SQLiteDatabase db = Database.getWritableDatabase();
            db.update("BF", Breakfast,
                    "_id = ?", new String[]{Integer.toString(drinkNo)});
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}