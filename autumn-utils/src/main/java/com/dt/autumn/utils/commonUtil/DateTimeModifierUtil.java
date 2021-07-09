package com.dt.autumn.utils.commonUtil;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeModifierUtil {

    private static volatile DateTimeModifierUtil instance;

    private DateTimeModifierUtil() {

    }

    public static DateTimeModifierUtil getInstance() {
        if (instance == null) {
            synchronized (DateTimeModifierUtil.class) {
                if (instance == null) {
                    instance = new DateTimeModifierUtil();
                }
            }
        }
        return instance;
    }


    public Long getCurrentTimeInMilliSec() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private Long getTimeInMilliSec(String dateTime, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        }catch(ParseException e){
        }
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    private String getDateTimeFromMilliSec(String format, Long dateTime) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date = new Date(dateTime);
        cal.setTime(date);
        return dateFormat.format(cal.getTime());
    }

    public String getCurrentDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        String currentDateTime = dateFormat.format(cal.getTime());
        return currentDateTime;
    }

    public String getFormattedDateTime(String format, String dateTime) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        if (dateTime != null) {
            try {
                date = dateFormat.parse(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.setTime(date);
        String futureDateTime = dateFormat.format(cal.getTime());
        return futureDateTime;
    }


    public String getModifiedDateTimeFromCurrent(String format, int count, String calendarType) {
        String dateTime = getCurrentDateTime(format);
        return getModifiedDateTime(format, count, dateTime, calendarType);
    }

    public String getModifiedDateTime(String format, int count, String dateTime, String calendarType) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        if (dateTime != null) {
            try {
                date = dateFormat.parse(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.setTime(date);
        cal.add(getCalendarType(calendarType), count);
        String futureDateTime = dateFormat.format(cal.getTime());
        return futureDateTime;
    }

    private int getCalendarType(String calendarType) {
        if (calendarType.equalsIgnoreCase("month")) {
            return Calendar.MONTH;
        } else if (calendarType.equalsIgnoreCase("date")) {
            return Calendar.DATE;
        } else if (calendarType.equalsIgnoreCase("year")) {
            return Calendar.YEAR;
        } else if (calendarType.equalsIgnoreCase("hour")) {
            return Calendar.HOUR;
        } else if (calendarType.equalsIgnoreCase("minutes")) {
            return Calendar.MINUTE;
        } else if (calendarType.equalsIgnoreCase("seconds")) {
            return Calendar.SECOND;
        } else if (calendarType.equalsIgnoreCase("milliseconds")) {
            return Calendar.MILLISECOND;
        }
        throw new RuntimeException("Enter valid calendarType");
    }

    public String getFutureDateFromDateTime(String format, int count, String dateTime) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        if (dateTime != null) {
            try {
                date = dateFormat.parse(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.setTime(date);
        cal.add(Calendar.DATE, count);
        String futureDateTime = dateFormat.format(cal.getTime());
        return futureDateTime;
    }

    public int compareDates(String timeStamp1, String timeStamp2, String format) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date d1 = simpleDateFormat.parse(timeStamp1);
            Date d2 = simpleDateFormat.parse(timeStamp2);
            return d1.compareTo(d2);
        }catch(ParseException pe){
            throw pe;
        }
    }
}
