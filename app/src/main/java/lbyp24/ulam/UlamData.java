package lbyp24.ulam;

/**
 * Created by Kevin Cepria on 11/2/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
class UlamData extends SQLiteOpenHelper{
    private static final String DB_NAME = "ula"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database
    UlamData(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE BF (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");
            insertBF(db, "Hotsilog", "Hotdog and Fried Egg with Rice", R.drawable.breakfast1);
            insertBF(db, "Bacsilog", "Bacon and Fried Egg with Rice",
                    R.drawable.breakfast2);
            insertBF(db, "Pancakes", "4 medium Pancakes with Butter and Syrup", R.drawable.breakfast3);
        }
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE BF ADD COLUMN FAVORITE NUMERIC;");
        }
    }
    private static void insertBF(SQLiteDatabase db, String name,
                                    String description, int resourceId) {
        ContentValues breakfast = new ContentValues();
        breakfast.put("NAME", name);
        breakfast.put("DESCRIPTION", description);
        breakfast.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("BF", null, breakfast);
    }
}