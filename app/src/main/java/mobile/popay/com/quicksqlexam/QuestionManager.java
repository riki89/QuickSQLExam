package mobile.popay.com.quicksqlexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import modele.Question;

/**
 * Created by mac on 26/04/2018.
 */

public class QuestionManager extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    // Database Name

    public static final String DATABASE_NAME = "sql_exam.db";
    public static final String TABLE_NAME = "question";

    public QuestionManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public QuestionManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_QUESTION = "CREATE TABLE " +  TABLE_NAME + "("
                + Question.KEY_num + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Question.KEY_title + " TEXT, " + Question.KEY_contents + "TEXT, "
                + Question.KEY_img + " TEXT, "
                + Question.KEY_type + " TEXT, "
                + Question.KEY_nb_option + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_QUESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public  int insertData(Question q){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(q.KEY_title, q.getTitle());
        content.put(q.KEY_contents, q.getContents());
        content.put(q.KEY_img, q.getImg());
        content.put(q.KEY_type, q.getType());
        content.put(q.KEY_nb_option, q.getNbOptions());

        long quizz_id = db.insert(TABLE_NAME, null, content);
        db.close();
        return  (int) quizz_id ;
    }

    public void delete(int quizz_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(TABLE_NAME, Question.KEY_num + "= ?", new String[] { String.valueOf(quizz_Id) });
        db.close(); // Closing database connection
    }

    public void update(Question q) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(q.KEY_num, q.getNum());
        values.put(q.KEY_title, q.getTitle());
        values.put(q.KEY_contents, q.getContents());
        values.put(q.KEY_img, q.getImg());
        values.put(q.KEY_type, q.getType());
        values.put(q.KEY_nb_option, q.getNbOptions());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(TABLE_NAME, values, q.KEY_num + "= ?", new String[] { String.valueOf(q.KEY_num) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getQuizzList() {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Question.KEY_num + "," +
                Question.KEY_title + "," +
                Question.KEY_contents + "," +
                Question.KEY_img + ","+
                Question.KEY_type+ ","+
                Question.KEY_nb_option+
                " FROM " + TABLE_NAME;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> quizzList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> quizz = new HashMap<String, String>();
                quizz.put("id", cursor.getString(cursor.getColumnIndex(Question.KEY_num)));
                quizz.put("title", cursor.getString(cursor.getColumnIndex(Question.KEY_title)));
                quizz.put("contents", cursor.getString(cursor.getColumnIndex(Question.KEY_contents)));
                quizz.put("img", cursor.getString(cursor.getColumnIndex(Question.KEY_img)));
                quizz.put("type", cursor.getString(cursor.getColumnIndex(Question.KEY_type)));
                quizz.put("nb_option", cursor.getString(cursor.getColumnIndex(Question.KEY_nb_option)));
                quizzList.add(quizz);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return quizzList;

    }

    public Question getQuestionById(int Id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Question.KEY_num + "," +
                Question.KEY_title + "," +
                Question.KEY_contents + "," +
                Question.KEY_img + "," +
                Question.KEY_type + "," +
                Question.KEY_nb_option +
                " FROM " + TABLE_NAME
                + " WHERE " +
                Question.KEY_num + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        Question question = new Question();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                question.setNum(cursor.getInt(cursor.getColumnIndex(Question.KEY_num)));
                question.setTitle(cursor.getString(cursor.getColumnIndex(Question.KEY_title)));
                question.setContents(cursor.getString(cursor.getColumnIndex(Question.KEY_contents)));
                question.setImg(cursor.getString(cursor.getColumnIndex(Question.KEY_img)));
                question.setType(cursor.getString(cursor.getColumnIndex(Question.KEY_type)));
                question.setNbOptions(cursor.getInt(cursor.getColumnIndex(Question.KEY_nb_option)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return question;
    }
}
