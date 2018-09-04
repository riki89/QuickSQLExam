package mobile.popay.com.quicksqlexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mac on 17/11/2017.
 */

public class DBManager extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    public static final int DATABASE_VERSION = 1;

    // Database Name

    public static final String DATABASE_NAME = "sql_exam.db";
    public static final String TABLE_NAME = "exam_table";
    /*
    public static final String NUMBER = "number";
    public static final String QUESTION = "qcuestion";
    public static final String Rep1 = "Rep1";
    public static final String Rep2 = "Rep2";
    public static final String Rep3 = "Rep3";
    public static final String Rep4 = "Rep4";
    public static final String GoodRep = "good_rep";
    */

    //
    public static final String KEY_numquizz = "id";
    public static final String KEY_imgquizz = "imquizz";
    public static final String KEY_good_rep = "reponse";
    public static final String KEY_question = "question";
    public static final String KEY_typeRep  = "typerep";
    public static final String KEY_option1 = "option1";
    public static final String KEY_option2 = "option2";
    public static final String KEY_option3 = "option3";
    public static final String KEY_option4 = "option4";
    public static final String KEY_option5 = "option5";

    //QuestionLibrary quizz = new QuestionLibrary();



    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_QUESTION = "CREATE TABLE " +  TABLE_NAME + "("
                + Questionnaire.KEY_numquizz  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Questionnaire.KEY_imgquizz + " TEXT, " + Questionnaire.KEY_good_rep + "TEXT, "
                + Questionnaire.KEY_question + " TEXT, "
                + Questionnaire.KEY_typeRep + " TEXT, "
                + Questionnaire.KEY_option1 + " TEXT, "+ Questionnaire.KEY_option2 + " TEXT, "
                + Questionnaire.KEY_option3 + " TEXT, "+ Questionnaire.KEY_option3 + " TEXT, "
                + Questionnaire.KEY_option4 + "TEXT, " + Questionnaire.KEY_option5 + " TEXT )";

        sqLiteDatabase.execSQL(CREATE_TABLE_QUESTION);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public  int insertData(Questionnaire q){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(q.KEY_numquizz, q.getId());
        content.put(q.KEY_imgquizz, q.getImgQuizz());
        content.put(q.KEY_good_rep, q.getmRep());
        content.put(q.KEY_question, q.getmQuestion());
        content.put(q.KEY_typeRep, q.getTypeRep());
        content.put(q.KEY_option1, q.getmOption1());
        content.put(q.KEY_option2, q.getmOption2());
        content.put(q.KEY_option3, q.getmOption3());
        content.put(q.KEY_option4, q.getmOption4());
        content.put(q.KEY_option5, q.getmOption5());

        long quizz_id = db.insert(TABLE_NAME, null, content);
        db.close();
        return  (int) quizz_id ;
    }

    public void delete(int quizz_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(TABLE_NAME, Questionnaire.KEY_numquizz + "= ?", new String[] { String.valueOf(quizz_Id) });
        db.close(); // Closing database connection
    }

    public void update(Questionnaire q) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(q.KEY_numquizz, q.getId());
        values.put(q.KEY_imgquizz, q.getImgQuizz());
        values.put(q.KEY_good_rep, q.getmRep());
        values.put(q.KEY_question, q.getmQuestion());
        values.put(q.KEY_typeRep, q.getTypeRep());
        values.put(q.KEY_option1, q.getmOption1());
        values.put(q.KEY_option2, q.getmOption2());
        values.put(q.KEY_option3, q.getmOption3());
        values.put(q.KEY_option4, q.getmOption4());
        values.put(q.KEY_option5, q.getmOption5());


        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(TABLE_NAME, values, q.KEY_numquizz + "= ?", new String[] { String.valueOf(q.KEY_numquizz) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getQuizzList() {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Questionnaire.KEY_numquizz + "," +
                Questionnaire.KEY_imgquizz + "," +
                Questionnaire.KEY_good_rep + "," +
                Questionnaire.KEY_question + ","+
                Questionnaire.KEY_typeRep + ","+
                Questionnaire.KEY_option1+ ","+
                Questionnaire.KEY_option2+ ","+
                Questionnaire.KEY_option3+ ","+
                Questionnaire.KEY_option4+ ","+
                Questionnaire.KEY_option5+
                " FROM " + TABLE_NAME;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> quizzList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> quizz = new HashMap<String, String>();
                quizz.put("id", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_numquizz)));
                quizz.put("imquizz", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_imgquizz)));
                quizz.put("reponse", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_good_rep)));
                quizz.put("question", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_question)));
                quizz.put("typerep", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_typeRep)));
                quizz.put("option1", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_option1)));
                quizz.put("option2", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_option2)));
                quizz.put("option3", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_option3)));
                quizz.put("option4", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_option4)));
                quizz.put("option5", cursor.getString(cursor.getColumnIndex(Questionnaire.KEY_option5)));
                quizzList.add(quizz);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return quizzList;

    }

}
