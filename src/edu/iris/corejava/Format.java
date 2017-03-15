/*     */ package edu.iris.corejava;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public class Format
/*     */ {
/*     */   public static final String cvsid = "$Id: Format.java,v 1.2 2001/12/19 07:02:41 hwh Exp $";
/*     */   private int width;
/*     */   private int precision;
/*     */   private String pre;
/*     */   private String post;
/*     */   private boolean leading_zeroes;
/*     */   private boolean show_plus;
/*     */   private boolean alternate;
/*     */   private boolean show_space;
/*     */   private boolean left_align;
/*     */   private char fmt;
/*     */   
/*     */   public Format(String s)
/*     */   {
/*  56 */     this.width = 0;
/*  57 */     this.precision = -1;
/*  58 */     this.pre = "";
/*  59 */     this.post = "";
/*  60 */     this.leading_zeroes = false;
/*  61 */     this.show_plus = false;
/*  62 */     this.alternate = false;
/*  63 */     this.show_space = false;
/*  64 */     this.left_align = false;
/*  65 */     this.fmt = ' ';
/*     */     
/*  67 */     int state = 0;
/*  68 */     int length = s.length();
/*  69 */     int parse_state = 0;
/*     */     
/*     */ 
/*  72 */     int i = 0;
/*     */     
/*  74 */     while (parse_state == 0) {
/*  75 */       if (i >= length) { parse_state = 5;
/*  76 */       } else if (s.charAt(i) == '%') {
/*  77 */         if (i < length - 1) {
/*  78 */           if (s.charAt(i + 1) == '%') {
/*  79 */             this.pre += '%';
/*  80 */             i++;
/*     */           }
/*     */           else {
/*  83 */             parse_state = 1;
/*     */           }
/*  85 */         } else throw new IllegalArgumentException();
/*     */       }
/*     */       else
/*  88 */         this.pre += s.charAt(i);
/*  89 */       i++;
/*     */     }
/*  91 */     while (parse_state == 1) {
/*  92 */       if (i >= length) { parse_state = 5;
/*  93 */       } else if (s.charAt(i) == ' ') { this.show_space = true;
/*  94 */       } else if (s.charAt(i) == '-') { this.left_align = true;
/*  95 */       } else if (s.charAt(i) == '+') { this.show_plus = true;
/*  96 */       } else if (s.charAt(i) == '0') { this.leading_zeroes = true;
/*  97 */       } else if (s.charAt(i) == '#') { this.alternate = true;
/*  98 */       } else { parse_state = 2;i--; }
/*  99 */       i++;
/*     */     }
/* 101 */     while (parse_state == 2)
/* 102 */       if (i >= length) { parse_state = 5;
/* 103 */       } else if (('0' <= s.charAt(i)) && (s.charAt(i) <= '9')) {
/* 104 */         this.width = (this.width * 10 + s.charAt(i) - 48);
/* 105 */         i++;
/*     */       }
/* 107 */       else if (s.charAt(i) == '.') {
/* 108 */         parse_state = 3;
/* 109 */         this.precision = 0;
/* 110 */         i++;
/*     */       }
/*     */       else {
/* 113 */         parse_state = 4;
/*     */       }
/* 115 */     while (parse_state == 3)
/* 116 */       if (i >= length) { parse_state = 5;
/* 117 */       } else if (('0' <= s.charAt(i)) && (s.charAt(i) <= '9')) {
/* 118 */         this.precision = (this.precision * 10 + s.charAt(i) - 48);
/* 119 */         i++;
/*     */       }
/*     */       else {
/* 122 */         parse_state = 4;
/*     */       }
/* 124 */     if (parse_state == 4) {
/* 125 */       if (i >= length) parse_state = 5; else
/* 126 */         this.fmt = s.charAt(i);
/* 127 */       i++;
/*     */     }
/* 129 */     if (i < length) {
/* 130 */       this.post = s.substring(i, length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printf(String fmt, double x)
/*     */   {
/* 141 */     System.out.print(new Format(fmt).format(x));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printf(String fmt, int x)
/*     */   {
/* 152 */     System.out.print(new Format(fmt).format(x));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printf(String fmt, long x)
/*     */   {
/* 163 */     System.out.print(new Format(fmt).format(x));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printf(String fmt, char x)
/*     */   {
/* 174 */     System.out.print(new Format(fmt).format(x));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printf(String fmt, String x)
/*     */   {
/* 185 */     System.out.print(new Format(fmt).format(x));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int atoi(String s)
/*     */   {
/* 196 */     return (int)atol(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long atol(String s)
/*     */   {
/* 207 */     int i = 0;
/*     */     
/* 209 */     while ((i < s.length()) && (Character.isWhitespace(s.charAt(i)))) i++;
/* 210 */     if ((i < s.length()) && (s.charAt(i) == '0')) {
/* 211 */       if ((i + 1 < s.length()) && ((s.charAt(i + 1) == 'x') || (s.charAt(i + 1) == 'X')))
/* 212 */         return parseLong(s.substring(i + 2), 16);
/* 213 */       return parseLong(s, 8);
/*     */     }
/* 215 */     return parseLong(s, 10);
/*     */   }
/*     */   
/*     */   private static long parseLong(String s, int base) {
/* 219 */     int i = 0;
/* 220 */     int sign = 1;
/* 221 */     long r = 0L;
/*     */     
/* 223 */     while ((i < s.length()) && (Character.isWhitespace(s.charAt(i)))) i++;
/* 224 */     if ((i < s.length()) && (s.charAt(i) == '-')) { sign = -1;i++;
/* 225 */     } else if ((i < s.length()) && (s.charAt(i) == '+')) { i++; }
/* 226 */     while (i < s.length()) {
/* 227 */       char ch = s.charAt(i);
/* 228 */       if (('0' <= ch) && (ch < 48 + base)) {
/* 229 */         r = r * base + ch - 48L;
/* 230 */       } else if (('A' <= ch) && (ch < 65 + base - 10)) {
/* 231 */         r = r * base + ch - 65L + 10L;
/* 232 */       } else if (('a' <= ch) && (ch < 97 + base - 10)) {
/* 233 */         r = r * base + ch - 97L + 10L;
/*     */       } else
/* 235 */         return r * sign;
/* 236 */       i++;
/*     */     }
/* 238 */     return r * sign;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double atof(String s)
/*     */   {
/* 248 */     int i = 0;
/* 249 */     int sign = 1;
/* 250 */     double r = 0.0D;
/* 251 */     double f = 0.0D;
/* 252 */     double p = 1.0D;
/* 253 */     int state = 0;
/*     */     
/* 255 */     while ((i < s.length()) && (Character.isWhitespace(s.charAt(i)))) i++;
/* 256 */     if ((i < s.length()) && (s.charAt(i) == '-')) { sign = -1;i++;
/* 257 */     } else if ((i < s.length()) && (s.charAt(i) == '+')) { i++; }
/* 258 */     while (i < s.length()) {
/* 259 */       char ch = s.charAt(i);
/* 260 */       if (('0' <= ch) && (ch <= '9')) {
/* 261 */         if (state == 0) {
/* 262 */           r = r * 10.0D + ch - 48.0D;
/* 263 */         } else if (state == 1) {
/* 264 */           p /= 10.0D;
/* 265 */           r += p * (ch - '0');
/*     */         }
/*     */       }
/* 268 */       else if (ch == '.') {
/* 269 */         if (state == 0) state = 1; else
/* 270 */           return sign * r;
/*     */       } else {
/* 272 */         if ((ch == 'e') || (ch == 'E')) {
/* 273 */           long e = (int)parseLong(s.substring(i + 1), 10);
/* 274 */           return sign * r * Math.pow(10.0D, e);
/*     */         }
/* 276 */         return sign * r; }
/* 277 */       i++;
/*     */     }
/* 279 */     return sign * r;
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
/*     */   public String format(double x)
/*     */   {
/* 292 */     if (this.precision < 0) this.precision = 6;
/* 293 */     int s = 1;
/* 294 */     if (x < 0.0D) { x = -x;s = -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 300 */     int logTenFact = (int)Math.floor(Math.log(x) / Math.log(10.0D));
/*     */     
/*     */ 
/*     */ 
/* 304 */     double multFact = Math.pow(10.0D, this.precision - logTenFact);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */     double xx = x;
/* 311 */     if (((this.fmt == 'f') && (x < 1.0D)) || (this.fmt != 'f')) {
/* 312 */       xx = Math.round(x * multFact) / multFact;
/*     */     }
/*     */     
/*     */     String r;
/* 316 */     if (this.fmt == 'f') {
/* 317 */       r = fixed_format(xx);
/* 318 */     } else if ((this.fmt == 'e') || (this.fmt == 'E') || (this.fmt == 'g') || (this.fmt == 'G'))
/* 319 */       r = exp_format(xx); else {
/* 320 */       throw new IllegalArgumentException();
/*     */     }
/* 322 */     return pad(sign(s, r));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String format(int x)
/*     */   {
/* 333 */     long lx = x;
/* 334 */     if ((this.fmt == 'o') || (this.fmt == 'x') || (this.fmt == 'X'))
/* 335 */       lx &= 0xFFFFFFFF;
/* 336 */     return format(lx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String format(long x)
/*     */   {
/* 348 */     int s = 0;
/* 349 */     String r; if ((this.fmt == 'd') || (this.fmt == 'i')) {
/* 350 */       if (x < 0L) {
/* 351 */         r = ("" + x).substring(1);
/* 352 */         s = -1;
/*     */       }
/*     */       else {
/* 355 */         r = "" + x;
/* 356 */         s = 1;
/*     */       }
/*     */     }
/* 359 */     else if (this.fmt == 'o') {
/* 360 */       r = convert(x, 3, 7, "01234567");
/* 361 */     } else if (this.fmt == 'x') {
/* 362 */       r = convert(x, 4, 15, "0123456789abcdef");
/* 363 */     } else if (this.fmt == 'X')
/* 364 */       r = convert(x, 4, 15, "0123456789ABCDEF"); else {
/* 365 */       throw new IllegalArgumentException();
/*     */     }
/* 367 */     return pad(sign(s, r));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String format(char c)
/*     */   {
/* 378 */     if (this.fmt != 'c') {
/* 379 */       throw new IllegalArgumentException();
/*     */     }
/* 381 */     String r = "" + c;
/* 382 */     return pad(r);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String format(String s)
/*     */   {
/* 393 */     if (this.fmt != 's')
/* 394 */       throw new IllegalArgumentException();
/* 395 */     if ((this.precision >= 0) && (this.precision < s.length()))
/* 396 */       s = s.substring(0, this.precision);
/* 397 */     return pad(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] a)
/*     */   {
/* 406 */     double x = 1.23456789012D;
/* 407 */     double y = 123.0D;
/* 408 */     double z = 1.2345E30D;
/* 409 */     double w = 1.02D;
/* 410 */     double u = 1.234E-5D;
/* 411 */     double d1 = 999.9945D;
/* 412 */     double d2 = 0.09999945D;
/*     */     
/* 414 */     printf("d1 = |%12.6E|\n", d1);
/* 415 */     printf("d1 = |%11.5E|\n", d1);
/* 416 */     printf("d1 = |%10.4E|\n", d1);
/* 417 */     printf("d1 = |%7.3f|\n", d1);
/* 418 */     printf("d1 = |%6.2f|\n", d1);
/* 419 */     printf("d1 = |%6.1f|\n", d1);
/* 420 */     printf("d2 = |%7.5f|\n", d2);
/* 421 */     printf("d2 = |%8.6f|\n", d2);
/* 422 */     printf("d2 = |%10.4E|\n", d2);
/* 423 */     printf("d2 = |%11.5E|\n", d2);
/* 424 */     printf("d2 = |%12.6E|\n", d2);
/*     */     
/* 426 */     int d = 51966;
/* 427 */     printf("x = |%f|\n", x);
/* 428 */     printf("u = |%20f|\n", u);
/* 429 */     printf("x = |% .5f|\n", x);
/* 430 */     printf("w = |%20.5f|\n", w);
/* 431 */     printf("x = |%020.5f|\n", x);
/* 432 */     printf("x = |%+20.5f|\n", x);
/* 433 */     printf("x = |%+020.5f|\n", x);
/* 434 */     printf("x = |% 020.5f|\n", x);
/* 435 */     printf("y = |%#+20.5f|\n", y);
/* 436 */     printf("y = |%-+20.5f|\n", y);
/* 437 */     printf("z = |%20.5f|\n", z);
/*     */     
/* 439 */     printf("x = |%e|\n", x);
/* 440 */     printf("u = |%20e|\n", u);
/* 441 */     printf("x = |% .5e|\n", x);
/* 442 */     printf("w = |%20.5e|\n", w);
/* 443 */     printf("x = |%020.5e|\n", x);
/* 444 */     printf("x = |%+20.5e|\n", x);
/* 445 */     printf("x = |%+020.5e|\n", x);
/* 446 */     printf("x = |% 020.5e|\n", x);
/* 447 */     printf("y = |%#+20.5e|\n", y);
/* 448 */     printf("y = |%-+20.5e|\n", y);
/*     */     
/* 450 */     printf("x = |%g|\n", x);
/* 451 */     printf("z = |%g|\n", z);
/* 452 */     printf("w = |%g|\n", w);
/* 453 */     printf("u = |%g|\n", u);
/* 454 */     printf("y = |%.2g|\n", y);
/* 455 */     printf("y = |%#.2g|\n", y);
/*     */     
/* 457 */     printf("d = |%d|\n", d);
/* 458 */     printf("d = |%20d|\n", d);
/* 459 */     printf("d = |%020d|\n", d);
/* 460 */     printf("d = |%+20d|\n", d);
/* 461 */     printf("d = |% 020d|\n", d);
/* 462 */     printf("d = |%-20d|\n", d);
/* 463 */     printf("d = |%20.8d|\n", d);
/* 464 */     printf("d = |%x|\n", d);
/* 465 */     printf("d = |%20X|\n", d);
/* 466 */     printf("d = |%#20x|\n", d);
/* 467 */     printf("d = |%020X|\n", d);
/* 468 */     printf("d = |%20.8x|\n", d);
/* 469 */     printf("d = |%o|\n", d);
/* 470 */     printf("d = |%020o|\n", d);
/* 471 */     printf("d = |%#20o|\n", d);
/* 472 */     printf("d = |%#020o|\n", d);
/* 473 */     printf("d = |%20.12o|\n", d);
/*     */     
/* 475 */     printf("s = |%-20s|\n", "Hello");
/* 476 */     printf("s = |%-20c|\n", '!');
/*     */     
/*     */ 
/*     */ 
/* 480 */     printf("|%i|\n", Long.MIN_VALUE);
/*     */     
/* 482 */     printf("|%6.2e|\n", 0.0D);
/* 483 */     printf("|%6.2g|\n", 0.0D);
/*     */     
/* 485 */     printf("|%6.2f|\n", 9.99D);
/* 486 */     printf("|%6.2f|\n", 9.999D);
/*     */     
/* 488 */     printf("|%6.0f|\n", 9.999D);
/*     */     
/* 490 */     d = -1;
/* 491 */     printf("d = |%X|\n", d);
/*     */   }
/*     */   
/*     */   private static String repeat(char c, int n) {
/* 495 */     if (n <= 0) return "";
/* 496 */     StringBuffer s = new StringBuffer(n);
/* 497 */     for (int i = 0; i < n; i++) s.append(c);
/* 498 */     return s.toString();
/*     */   }
/*     */   
/*     */   private static String convert(long x, int n, int m, String d) {
/* 502 */     if (x == 0L) return "0";
/* 503 */     String r = "";
/* 504 */     while (x != 0L) {
/* 505 */       r = d.charAt((int)(x & m)) + r;
/* 506 */       x >>>= n;
/*     */     }
/* 508 */     return r;
/*     */   }
/*     */   
/*     */   private String pad(String r) {
/* 512 */     String p = repeat(' ', this.width - r.length());
/* 513 */     if (this.left_align) return this.pre + r + p + this.post;
/* 514 */     return this.pre + p + r + this.post;
/*     */   }
/*     */   
/*     */   private String sign(int s, String r) {
/* 518 */     String p = "";
/* 519 */     if (s < 0) { p = "-";
/* 520 */     } else if (s > 0) {
/* 521 */       if (this.show_plus) { p = "+";
/* 522 */       } else if (this.show_space) { p = " ";
/*     */       }
/*     */     }
/* 525 */     else if ((this.fmt == 'o') && (this.alternate) && (r.length() > 0) && (r.charAt(0) != '0')) { p = "0";
/* 526 */     } else if ((this.fmt == 'x') && (this.alternate)) { p = "0x";
/* 527 */     } else if ((this.fmt == 'X') && (this.alternate)) { p = "0X";
/*     */     }
/* 529 */     int w = 0;
/* 530 */     if (this.leading_zeroes) {
/* 531 */       w = this.width;
/* 532 */     } else if (((this.fmt == 'd') || (this.fmt == 'i') || (this.fmt == 'x') || (this.fmt == 'X') || (this.fmt == 'o')) && (this.precision > 0)) {
/* 533 */       w = this.precision;
/*     */     }
/* 535 */     return p + repeat('0', w - p.length() - r.length()) + r;
/*     */   }
/*     */   
/*     */   private String fixed_format(double d) {
/* 539 */     boolean removeTrailing = ((this.fmt == 'G') || (this.fmt == 'g')) && (!this.alternate);
/*     */     
/*     */ 
/*     */ 
/* 543 */     if (d > 9.223372036854776E18D) return exp_format(d);
/* 544 */     if (this.precision == 0) {
/* 545 */       return (d + 0.5D) + (removeTrailing ? "" : ".");
/*     */     }
/* 547 */     long whole = d;
/* 548 */     double fr = d - whole;
/* 549 */     if ((fr >= 1.0D) || (fr < 0.0D)) { return exp_format(d);
/*     */     }
/* 551 */     double factor = 1.0D;
/* 552 */     String leading_zeroes = "";
/* 553 */     for (int i = 1; (i <= this.precision) && (factor <= 9.223372036854776E18D); i++) {
/* 554 */       factor *= 10.0D;
/* 555 */       leading_zeroes = leading_zeroes + "0";
/*     */     }
/* 557 */     long l = (factor * fr + 0.5D);
/* 558 */     if (l >= factor) { l = 0L;whole += 1L;
/*     */     }
/* 560 */     String z = leading_zeroes + l;
/* 561 */     z = "." + z.substring(z.length() - this.precision, z.length());
/*     */     
/* 563 */     if (removeTrailing) {
/* 564 */       int t = z.length() - 1;
/* 565 */       while ((t >= 0) && (z.charAt(t) == '0')) t--;
/* 566 */       if ((t >= 0) && (z.charAt(t) == '.')) t--;
/* 567 */       z = z.substring(0, t + 1);
/*     */     }
/*     */     
/* 570 */     return whole + z;
/*     */   }
/*     */   
/*     */   private String exp_format(double d) {
/* 574 */     String f = "";
/* 575 */     int e = 0;
/* 576 */     double dd = d;
/* 577 */     double factor = 1.0D;
/* 578 */     if (d != 0.0D) {
/* 579 */       for (; dd >= 10.0D; dd /= 10.0D) { e++;factor /= 10.0D; }
/* 580 */       for (; dd < 1.0D; dd *= 10.0D) { e--;factor *= 10.0D;
/*     */       } }
/* 582 */     if (((this.fmt == 'g') || (this.fmt == 'G')) && (e >= -4) && (e < this.precision)) {
/* 583 */       return fixed_format(d);
/*     */     }
/* 585 */     d *= factor;
/* 586 */     f = f + fixed_format(d);
/*     */     
/* 588 */     if ((this.fmt == 'e') || (this.fmt == 'g')) {
/* 589 */       f = f + "e";
/*     */     } else {
/* 591 */       f = f + "E";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 596 */     String p = "00";
/* 597 */     if (e >= 0) {
/* 598 */       f = f + "+";
/* 599 */       p = p + e;
/*     */     }
/*     */     else {
/* 602 */       f = f + "-";
/* 603 */       p = p + -e;
/*     */     }
/*     */     
/*     */ 
/* 607 */     return f + p.substring(p.length() - 2, p.length());
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/corejava/Format.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */