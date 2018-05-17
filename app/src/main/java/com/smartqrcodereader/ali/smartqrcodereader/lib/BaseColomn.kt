package com.smartqrcodereader.ali.smartqrcodereader.lib

class BaseColomn {
    companion object {
        const val TABLE_ATTENDANCE = "_attendance"
        private const val COL_NO = "_no"
        private const val COL_EMAIL = "_email"
        private const val COL_TIME = "_time"
        private const val COL_LOCATION = "_location"
        private const val COL_MEETING = "_meeting"

        val tableAttendanceColomn = arrayOf(
                TABLE_ATTENDANCE,
                COL_NO,
                COL_EMAIL,
                COL_TIME,
                COL_LOCATION,
                COL_MEETING
        )

        val CREATE_TABLE_ATTENDANCE = ("CREATE TABLE IF NOT EXISTS "
                + tableAttendanceColomn[0] +
                "("
                + tableAttendanceColomn[1] +
                " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + tableAttendanceColomn[2] +
                " TEXT, "
                + tableAttendanceColomn[3] +
                " TEXT, "
                + tableAttendanceColomn[4] +
                " TEXT, "
                + tableAttendanceColomn[5] +
                " TEXT"
                + ")"
                )


        const val TABLE_LOGIN = "_login"
        val tableLogin = arrayOf(
                TABLE_LOGIN,
                COL_EMAIL
        )
        val CREATE_TABLE_LOGIN = ("CREATE TABLE IF NOT EXISTS "
                + tableLogin[0] +
                "("
                + tableLogin[1] +
                " TEXT"
                + ")"
                )
    }
}