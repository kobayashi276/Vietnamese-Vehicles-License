package com.example.recyclerview;

import android.provider.BaseColumns;

//Khai báo Table
public class Table {
    private Table(){}
        public static class CategoriesTable implements BaseColumns {
            public static final String TABLE_NAME = "categories";
            public static final String COLUMN_NAME = "name";
        }

        public static class QuestionTable implements BaseColumns{
            public static final String TABLE_NAME = "questions";
            public static final String COLUMN_NAME = "question";
            public static final String COLUMN_OPTION1 = "option1";
            public static final String COLUMN_OPTION2 = "option2";
            public static final String COLUMN_OPTION3 = "option3";
            public static final String COLUMN_OPTION4 = "option4";
            public static final String COLUMN_ANSWER = "answer";
            public static final String COLUMN_CATEGORY_ID = "id_categories";
            public static final String COLUMN_IMAGE_QUESTION = "img_question";
        }
}
