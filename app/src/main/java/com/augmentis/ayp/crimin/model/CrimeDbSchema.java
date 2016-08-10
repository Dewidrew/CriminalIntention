package com.augmentis.ayp.crimin.model;

/**
 * Created by Hattapong on 8/1/2016.
 */
public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "UUID";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
