/*     */ package edu.iris.timeutils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class TimeStamp extends Timestamp
/*     */ {
/*     */   private static final int DATE_TIME_OUTPUT = 1;
/*     */   private static final int EPOCH_TIME_OUTPUT = 5;
/*     */   private static final int MAX_NANO_DIGITS = 9;
/*     */   private static final int NANOSEC_PER_SEC = 1000000000;
/*     */   private static final int SEED_TIME_OUTPUT = 3;
/*     */   public static final String cvsid = "$Id: TimeStamp.java,v 1.3 2001/12/19 07:02:28 hwh Exp $";
/*     */   
/*     */   public TimeStamp(long millis)
/*     */   {
/*  24 */     super(millis);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TimeStamp buildTimestamp(String sqlTimestamp)
/*     */   {
/*  33 */     int yr = 1970;int jday = 1;int mo = 0;int day = 1;
/*  34 */     int hr = 0;int min = 0;int sec = 0;
/*  35 */     boolean isDateTime = false;
/*  36 */     boolean isSeedTime = false;
/*  37 */     ArrayList dateTimeVals = null;
/*  38 */     long millis = 0L;
/*  39 */     int nanosec = 0;
/*     */     
/*     */ 
/*     */ 
/*  43 */     int numCount = 0;
/*     */     
/*  45 */     if (isDouble(sqlTimestamp)) {
/*  46 */       numCount = 1;
/*     */     } else {
/*  48 */       dateTimeVals = parseDateTime(sqlTimestamp, "-: ");
/*  49 */       if (dateTimeVals == null) {
/*  50 */         dateTimeVals = parseDateTime(sqlTimestamp, "/: ");
/*  51 */         if (dateTimeVals == null) {
/*  52 */           dateTimeVals = parseDateTime(sqlTimestamp, ",:");
/*  53 */           if (dateTimeVals != null) {
/*  54 */             isSeedTime = true;
/*     */           }
/*     */         } else {
/*  57 */           isDateTime = true;
/*     */         }
/*     */       } else {
/*  60 */         isDateTime = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */     if (numCount == 1)
/*     */     {
/*  70 */       int intValLength = sqlTimestamp.indexOf('.');
/*  71 */       String intStringVal = "0";
/*     */       
/*     */ 
/*     */ 
/*  75 */       BigDecimal bdec = new BigDecimal(intStringVal);
/*  76 */       if (intValLength == -1) {
/*  77 */         nanosec = 0;
/*  78 */         bdec = new BigDecimal(sqlTimestamp);
/*  79 */       } else if (intValLength > 0) {
/*  80 */         intStringVal = sqlTimestamp.substring(0, intValLength);
/*  81 */         bdec = new BigDecimal(intStringVal);
/*     */       }
/*     */       
/*  84 */       millis = bdec.movePointRight(3).longValue();
/*     */       
/*  86 */       nanosec = getNanos(sqlTimestamp);
/*     */     }
/*  88 */     else if (isDateTime)
/*     */     {
/*  90 */       int nvals = dateTimeVals.size();
/*  91 */       String tmpStr = null;
/*  92 */       if (nvals == 6) {
/*  93 */         tmpStr = (String)dateTimeVals.get(5);
/*  94 */         sec = new Double(tmpStr).intValue();
/*  95 */         nanosec = getNanos(tmpStr);
/*  96 */         nvals--;
/*     */       }
/*  98 */       for (int i = 0; i < nvals; i++) {
/*  99 */         tmpStr = (String)dateTimeVals.get(i);
/* 100 */         int tmpInt = new Integer(tmpStr).intValue();
/* 101 */         if (i == 0) {
/* 102 */           yr = tmpInt;
/* 103 */         } else if (i == 1) {
/* 104 */           mo = tmpInt;
/* 105 */         } else if (i == 2) {
/* 106 */           day = tmpInt;
/* 107 */         } else if (i == 3) {
/* 108 */           hr = tmpInt;
/* 109 */         } else if (i == 4) {
/* 110 */           min = tmpInt;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 116 */       mo--;
/*     */       
/*     */ 
/*     */ 
/* 120 */       TimeStampCalendar cal = new TimeStampCalendar(yr, mo, day, hr, min, sec);
/*     */       
/*     */ 
/* 123 */       cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       
/*     */ 
/* 126 */       millis = cal.getTimeInMillis();
/*     */     }
/* 128 */     else if (isSeedTime)
/*     */     {
/* 130 */       int nvals = dateTimeVals.size();
/* 131 */       String tmpStr = null;
/* 132 */       if (nvals == 5) {
/* 133 */         tmpStr = (String)dateTimeVals.get(4);
/* 134 */         sec = new Double(tmpStr).intValue();
/* 135 */         nanosec = getNanos(tmpStr);
/* 136 */         nvals--;
/*     */       }
/* 138 */       for (int i = 0; i < nvals; i++) {
/* 139 */         tmpStr = (String)dateTimeVals.get(i);
/* 140 */         int tmpInt = new Integer(tmpStr).intValue();
/* 141 */         if (i == 0) {
/* 142 */           yr = tmpInt;
/* 143 */         } else if (i == 1) {
/* 144 */           jday = tmpInt;
/* 145 */         } else if (i == 2) {
/* 146 */           hr = tmpInt;
/* 147 */         } else if (i == 3) {
/* 148 */           min = tmpInt;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 154 */       TimeStampCalendar cal = new TimeStampCalendar(yr, jday, hr, min, sec);
/*     */       
/*     */ 
/* 157 */       cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       
/*     */ 
/* 160 */       millis = cal.getTimeInMillis();
/*     */     }
/*     */     else
/*     */     {
/* 164 */       System.err.println("Error parsing time string");
/* 165 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */     if (nanosec >= 1000000000) {
/* 174 */       int nsec = nanosec / 1000000000;
/* 175 */       millis += nsec * 1000;
/* 176 */       nanosec -= nsec * 1000000000;
/*     */     }
/*     */     
/*     */ 
/* 180 */     TimeStamp mts = new TimeStamp(millis);
/*     */     
/*     */ 
/* 183 */     mts.setNanos(nanosec);
/*     */     
/*     */ 
/* 186 */     return mts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TimeStamp buildTimestamp(double epochTime, int precision)
/*     */   {
/* 197 */     BigDecimal bd = new BigDecimal(epochTime);
/*     */     BigDecimal bdShifted;
/* 199 */     if (epochTime >= 0.0D) {
/* 200 */       bdShifted = bd.movePointRight(precision).add(new BigDecimal(0.5D));
/*     */     } else {
/* 202 */       bdShifted = bd.movePointRight(precision).subtract(new BigDecimal(0.5D));
/*     */     }
/* 204 */     java.math.BigInteger bi = bdShifted.toBigInteger();
/* 205 */     BigDecimal newbd = new BigDecimal(bi).movePointLeft(precision);
/* 206 */     TimeStamp mts = buildTimestamp(newbd.toString());
/*     */     
/*     */ 
/* 209 */     return mts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toEpochString()
/*     */   {
/* 220 */     long millis = getTime();
/* 221 */     int nanos = getNanos();
/*     */     
/* 223 */     BigDecimal bdm = new BigDecimal(new Long(millis).toString());
/* 224 */     BigDecimal bdn = new BigDecimal(new Integer(nanos).toString());
/*     */     
/*     */     BigDecimal sumVal;
/* 227 */     if (millis >= 0L) {
/* 228 */       sumVal = bdm.movePointLeft(3).add(bdn.movePointLeft(9));
/*     */     } else {
/* 230 */       sumVal = bdm.movePointLeft(3).subtract(bdn.movePointLeft(9));
/*     */     }
/* 232 */     int scale = sumVal.scale();
/*     */     
/* 234 */     BigDecimal epochTime = sumVal;
/* 235 */     for (int i = scale - 1; i >= 0; i--) {
/*     */       try {
/* 237 */         epochTime = sumVal.setScale(i, 7);
/*     */       }
/*     */       catch (ArithmeticException e) {
/*     */         break;
/*     */       } catch (IllegalArgumentException e) {
/*     */         break;
/*     */       }
/*     */     }
/* 245 */     return epochTime.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toSeedString()
/*     */   {
/* 256 */     SimpleDateFormat formatter = new SimpleDateFormat("yyyy,DDD,HH:mm:ss");
/* 257 */     formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     
/* 259 */     String dateTimeStr = formatter.format(this);
/* 260 */     int lenDateTime = dateTimeStr.length();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 277 */     if (getNanos() != 0) {
/* 278 */       String nanosStr = getNanosStr();
/* 279 */       return dateTimeStr + "." + nanosStr; }
/* 280 */     if (dateTimeStr.substring(lenDateTime - 3).compareTo(":00") != 0)
/* 281 */       return dateTimeStr;
/* 282 */     if (dateTimeStr.substring(lenDateTime - 6).compareTo(":00:00") != 0)
/* 283 */       return dateTimeStr.substring(0, lenDateTime - 3);
/* 284 */     if (dateTimeStr.substring(lenDateTime - 9).compareTo(",00:00:00") != 0) {
/* 285 */       return dateTimeStr.substring(0, lenDateTime - 6);
/*     */     }
/* 287 */     return dateTimeStr.substring(0, lenDateTime - 9);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 298 */     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 299 */     formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     
/* 301 */     return formatter.format(this) + "." + getNanosStr();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   String getNanosStr()
/*     */   {
/* 317 */     int nanosVal = getNanos();
/*     */     
/* 319 */     if (nanosVal == 0)
/*     */     {
/* 321 */       return new Integer(nanosVal).toString();
/*     */     }
/*     */     
/*     */ 
/* 325 */     String sval = new Integer(nanosVal).toString();
/* 326 */     int len = sval.length();
/* 327 */     int ndigits = len;
/* 328 */     if (len > 1) {
/* 329 */       int lensave = len;
/* 330 */       for (int i = lensave - 1; i >= 0; i--) {
/* 331 */         if (sval.charAt(i) != '0') {
/*     */           break;
/*     */         }
/* 334 */         len--;
/*     */       }
/*     */       
/* 337 */       if (len < lensave) {
/* 338 */         sval = sval.substring(0, len);
/*     */       }
/*     */     }
/*     */     
/* 342 */     String initPad = "";
/* 343 */     for (int i = ndigits; i < 9; i++) {
/* 344 */       initPad = initPad + "0";
/*     */     }
/*     */     
/* 347 */     return initPad + sval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDateTime()
/*     */   {
/* 360 */     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 361 */     formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     
/* 363 */     String dateTimeString = formatter.format(this);
/*     */     
/* 365 */     return dateTimeString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getNanoSecs()
/*     */   {
/* 376 */     return getNanos();
/*     */   }
/*     */   
/*     */   public java.util.Date toDate()
/*     */   {
/* 381 */     long ms = getTime() + getNanos() / 1000000;
/* 382 */     return new java.util.Date(ms);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double toEpochTime()
/*     */   {
/* 392 */     long millis = getTime();
/* 393 */     int nanos = getNanos();
/*     */     
/* 395 */     BigDecimal bdMillis = new BigDecimal(new Long(millis).toString());
/* 396 */     BigDecimal bdNanos = new BigDecimal(new Integer(nanos).toString());
/*     */     
/* 398 */     if (millis >= 0L) {
/* 399 */       return bdMillis.movePointLeft(3).add(bdNanos.movePointLeft(9)).doubleValue();
/*     */     }
/* 401 */     return bdMillis.movePointLeft(3).subtract(bdNanos.movePointLeft(9)).doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int getNanos(String epochTime)
/*     */   {
/* 413 */     int fieldLength = epochTime.length();
/* 414 */     int intValLength = epochTime.indexOf('.');
/* 415 */     String intStringVal = "0";
/* 416 */     double tmp = 0.0D;
/* 417 */     int nanosec = 0;
/*     */     
/*     */ 
/*     */ 
/* 421 */     if (intValLength == -1)
/* 422 */       return 0;
/* 423 */     if (intValLength > 0) {
/* 424 */       intStringVal = epochTime.substring(0, intValLength);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 431 */     BigDecimal bd1 = new BigDecimal(epochTime);
/* 432 */     BigDecimal bd2 = new BigDecimal(intStringVal);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 437 */     int ndigits = intValLength;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 442 */     int nshift = fieldLength - ndigits - 1;
/*     */     
/*     */ 
/*     */ 
/*     */     BigDecimal diff;
/*     */     
/*     */ 
/* 449 */     if (bd1.signum() < 0) {
/* 450 */       diff = bd2.movePointRight(nshift).subtract(bd1.movePointRight(nshift));
/*     */     } else {
/* 452 */       diff = bd1.movePointRight(nshift).subtract(bd2.movePointRight(nshift));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 458 */     nshift = Math.min(9 - nshift, 9);
/* 459 */     if (nshift > 0) {
/* 460 */       nanosec = diff.movePointRight(nshift).intValue();
/*     */     } else {
/* 462 */       nanosec = (int)(diff.movePointLeft(-nshift).doubleValue() + 0.5D);
/*     */     }
/*     */     
/* 465 */     return nanosec;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isDouble(String sqlTimeString)
/*     */   {
/*     */     try
/*     */     {
/* 483 */       tmp = new Double(sqlTimeString);
/*     */     }
/*     */     catch (NumberFormatException e) {
/*     */       Double tmp;
/* 487 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 491 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList parseDateTime(String sqlTimeString, String sepChars)
/*     */   {
/* 507 */     ArrayList vals = new ArrayList();
/* 508 */     int nfields = 0;
/*     */     
/*     */ 
/*     */ 
/* 512 */     StringTokenizer st = new StringTokenizer(sqlTimeString, sepChars);
/*     */     
/*     */ 
/*     */ 
/* 516 */     while (st.hasMoreTokens()) {
/* 517 */       vals.add(st.nextToken());
/*     */       try {
/* 519 */         tmp = new Double((String)vals.get(nfields));
/*     */       } catch (NumberFormatException e) { Double tmp;
/* 521 */         return null;
/*     */       }
/* 523 */       nfields++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 528 */     return vals;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void printUsage()
/*     */   {
/* 537 */     System.err.println("Usage: java edu.iris.timeutils.TimeStamp [-{u|U|h|H} | -dateTime | -seedTime | -epochTime ]");
/* 538 */     System.err.println("  Output options; -{u|U|h|H} = for any of these options, print usage and exit");
/* 539 */     System.err.println("                  -dateTime  = YYYY-MO-DY HR:MM:SS.NNNNNNNNN");
/* 540 */     System.err.println("                  -seedTime  = YYYY,DOY,HR:MM:SS.NNNNNNNNN");
/* 541 */     System.err.println("                  -epochTime = the epoch time as a string (with arbitrary precision)");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 563 */     ArrayList vals = new ArrayList();
/* 564 */     int outFormatFlag = 0;
/* 565 */     TimeStamp tmpVal = null;
/*     */     
/* 567 */     if ((args.length > 1) || ((args.length == 1) && ((args[0].equals("-H")) || (args[0].equals("-h"))))) {
/* 568 */       printUsage();
/* 569 */       System.exit(1);
/* 570 */     } else if (args.length == 1) {
/* 571 */       if ((args[0].equals("-H")) || (args[0].equals("-h")) || (args[0].equals("-U")) || (args[0].equals("-u")))
/*     */       {
/* 573 */         printUsage();
/* 574 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     
/* 578 */     if (args.length == 0) {
/* 579 */       outFormatFlag = 1;
/* 580 */     } else if (args[0].equals("-dateTime")) {
/* 581 */       outFormatFlag = 1;
/* 582 */     } else if (args[0].equals("-seedTime")) {
/* 583 */       outFormatFlag = 3;
/* 584 */     } else if (args[0].equals("-epochTime")) {
/* 585 */       outFormatFlag = 5;
/*     */     } else {
/* 587 */       System.err.println("ERROR(TimeStamp): Unrecognized output type encountered: " + args[0]);
/* 588 */       printUsage();
/* 589 */       System.exit(1);
/*     */     }
/*     */     
/* 592 */     BufferedReader in = new BufferedReader(new java.io.InputStreamReader(System.in));
/*     */     try {
/*     */       String s;
/* 595 */       while ((s = in.readLine()) != null) { String str1;
/* 596 */         if ((str1 != null) && (str1.length() > 0)) {
/* 597 */           tmpVal = buildTimestamp(str1);
/* 598 */           switch (outFormatFlag) {
/*     */           case 1: 
/* 600 */             System.out.println(tmpVal.toString());
/* 601 */             break;
/*     */           case 3: 
/* 603 */             System.out.println(tmpVal.toSeedString());
/* 604 */             break;
/*     */           case 5: 
/* 606 */             System.out.println(tmpVal.toEpochString());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (java.io.IOException e) {
/* 612 */       e.printStackTrace();
/* 613 */       System.exit(2);
/*     */     }
/*     */     
/* 616 */     System.exit(0);
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/timeutils/TimeStamp.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */