package com.example.madproject2.Admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.madproject2.Course;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "courses.db";

    // Table Names
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_COURSE_BRANCHES = "course_branches"; // New table for course branches

    // Common Column names
    private static final String KEY_ID = "id";

    // Courses Table - Column names
    private static final String COL_COURSE_NAME = "course_name";
    private static final String COL_COURSE_FEE = "course_fee";
    private static final String COL_DURATION = "duration";
    private static final String COL_STARTING_DATE = "starting_date";
    private static final String COL_PUBLISHED_DATE = "published_date";
    private static final String COL_CLOSING_DATE = "closing_date";

    // Course Branches Table - Column names
    private static final String COL_COURSE_ID = "course_id";
    private static final String COL_BRANCH_NAME = "branch_name";

    // Registered Course table
    private static final String TABLE_REGISTERED_COURSES = "registered_courses";
    private static final String COL_BRANCH_ID = "branch_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_PROMOTION_CODE = "promotion_code";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create courses table
        String createCoursesTableQuery = "CREATE TABLE " + TABLE_COURSES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COURSE_NAME + " TEXT, " +
                COL_COURSE_FEE + " TEXT, " +
                COL_DURATION + " TEXT, " +
                COL_STARTING_DATE + " TEXT, " +
                COL_PUBLISHED_DATE + " TEXT, " +
                COL_CLOSING_DATE + " TEXT)";
        db.execSQL(createCoursesTableQuery);

        // Create course branches table
        String createCourseBranchesTableQuery = "CREATE TABLE " + TABLE_COURSE_BRANCHES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COURSE_ID + " INTEGER, " +
                COL_BRANCH_NAME + " TEXT, " +
                "FOREIGN KEY(" + COL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + KEY_ID + "))";
        db.execSQL(createCourseBranchesTableQuery);

        // Create registered courses table
        String createRegisteredCoursesTableQuery = "CREATE TABLE " + TABLE_REGISTERED_COURSES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COURSE_ID + " INTEGER, " +
                COL_BRANCH_ID + " INTEGER, " +
                COL_USER_ID + " INTEGER, " +
                COL_PROMOTION_CODE + " TEXT)";
        db.execSQL(createRegisteredCoursesTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_BRANCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERED_COURSES);
        onCreate(db);
    }

    // Insert a new course into the database
    public long insertCourse(String courseName, String courseFee, String duration,
                             String startingDate, String publishedDate, String closingDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_COURSE_NAME, courseName);
        contentValues.put(COL_COURSE_FEE, courseFee);
        contentValues.put(COL_DURATION, duration);
        contentValues.put(COL_STARTING_DATE, startingDate);
        contentValues.put(COL_PUBLISHED_DATE, publishedDate);
        contentValues.put(COL_CLOSING_DATE, closingDate);
        long courseId = db.insert(TABLE_COURSES, null, contentValues);
        db.close();
        // Return the ID of the newly inserted course
        return courseId;
    }

    // Insert a new course branch into the database
    public long insertCourseBranch(long courseId, String branchName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_COURSE_ID, courseId);
        contentValues.put(COL_BRANCH_NAME, branchName);

        // Check if the course_branches table exists, if not, create it
        if (!isTableExists(TABLE_COURSE_BRANCHES, db)) {
            onCreate(db); // Recreate the tables
        }

        long result = db.insert(TABLE_COURSE_BRANCHES, null, contentValues);
        db.close();
        return result;
    }

    // Method to check if a table exists in the database
    private boolean isTableExists(String tableName, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    // Method to fetch all courses from the database
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_COURSES,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract data from the cursor and create a Course object
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_NAME));
                String courseFee = cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_FEE));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(COL_DURATION));
                String startingDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_STARTING_DATE));
                String publishedDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_PUBLISHED_DATE));
                String closingDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLOSING_DATE));

                // Create a Course object and add it to the list
                Course course = new Course(courseName, courseFee, duration, startingDate, publishedDate, closingDate);
                courses.add(course);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        // Return the list of courses
        return courses;
    }

    // Method to fetch a course ID by its name from the database
    public long getCourseIdByName(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long courseId = -1;

        Cursor cursor = db.query(
                TABLE_COURSES,
                new String[]{KEY_ID},
                COL_COURSE_NAME + " = ?",
                new String[]{courseName},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Extract the course ID from the cursor
                int idIndex = cursor.getColumnIndex(KEY_ID);
                if (idIndex != -1) {
                    courseId = cursor.getLong(idIndex);
                }
            }

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return courseId;
    }

    // Method to fetch branches related to a course from the database
    public List<String> getBranchesForCourse(long courseId) {
        List<String> branches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + COL_BRANCH_NAME + " FROM " + TABLE_COURSE_BRANCHES +
                " WHERE " + COL_COURSE_ID + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(courseId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract branch name from the cursor and add it to the list
                String branchName = cursor.getString(cursor.getColumnIndexOrThrow(COL_BRANCH_NAME));
                branches.add(branchName);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return branches;
    }

    // Insert a new registered course into the database
    public long insertRegisteredCourse(long courseId, long branchId, String userId, String promotionCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_COURSE_ID, courseId);
        contentValues.put(COL_BRANCH_ID, branchId);
        contentValues.put(COL_USER_ID, userId);
        contentValues.put(COL_PROMOTION_CODE, promotionCode);

        // Check if the registered_courses table exists, if not, create it
        if (!isTableExists(TABLE_REGISTERED_COURSES, db)) {
            onCreate(db); // Recreate the tables
        }

        long registeredCourseId = db.insert(TABLE_REGISTERED_COURSES, null, contentValues);
        db.close();
        // Return the ID of the newly inserted registered course
        return registeredCourseId;
    }

    // Method to fetch registered courses for a user from the database
    // Method to fetch registered courses for a user from the database
    public List<Course> getRegisteredCourses(String userId) {
        List<Course> registeredCourses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_REGISTERED_COURSES,
                new String[]{COL_COURSE_ID, COL_BRANCH_ID, COL_USER_ID, COL_PROMOTION_CODE},
                COL_USER_ID + " = ?",
                new String[]{userId},
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long courseId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_COURSE_ID));
                long branchId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_BRANCH_ID));
                String promotionCode = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROMOTION_CODE));

                // Fetch additional course details based on courseId
                Course course = getCourseDetails(courseId); // Get course details using courseId
                if (course != null) {
                    registeredCourses.add(course);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return registeredCourses;
    }


    public Course getCourseDetails(long courseId) {
        SQLiteDatabase db = getReadableDatabase();
        Course course = null;

        Cursor cursor = db.query(
                TABLE_COURSES,
                new String[]{COL_COURSE_NAME, COL_COURSE_FEE,
                        COL_DURATION, COL_STARTING_DATE,
                        COL_PUBLISHED_DATE, COL_CLOSING_DATE},
                KEY_ID + " = ?",
                new String[]{String.valueOf(courseId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_NAME));
            String courseFee = cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_FEE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COL_DURATION));
            String startingDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_STARTING_DATE));
            String publishedDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_PUBLISHED_DATE));
            String closingDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLOSING_DATE));

            course = new Course(courseName, courseFee, duration, startingDate, publishedDate, closingDate);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return course;
    }


}
