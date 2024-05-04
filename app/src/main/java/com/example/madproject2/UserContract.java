package com.example.madproject2;

import android.provider.BaseColumns;

public final class UserContract {

    private UserContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USER_ID = "user_id"; // Added user_id column
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password"; // You may want to remove this column if you're using Firebase Auth
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_NIC = "nic";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_MOBILE = "mobile";
    }
}
