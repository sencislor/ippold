/*     */ package cn.gd.io;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UTFDataFormatException;
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
/*     */ public class LittleEndianOutputStream
/*     */   extends FilterOutputStream
/*     */   implements DataOutput
/*     */ {
/*     */   public static final String cvsid = "$Id: LittleEndianOutputStream.java,v 1.5 2003/12/03 04:25:20 hwh Exp $";
/*     */   protected int written;
/*     */   
/*     */   public LittleEndianOutputStream(OutputStream out)
/*     */   {
/*  40 */     super(out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void write(int b)
/*     */     throws IOException
/*     */   {
/*  50 */     this.out.write(b);
/*  51 */     this.written += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void write(byte[] data, int offset, int length)
/*     */     throws IOException
/*     */   {
/*  64 */     this.out.write(data, offset, length);
/*  65 */     this.written += length;
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
/*     */   public final void writeBoolean(boolean b)
/*     */     throws IOException
/*     */   {
/*  79 */     if (b) write(1); else {
/*  80 */       write(0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeByte(int b)
/*     */     throws IOException
/*     */   {
/*  91 */     this.out.write(b);
/*  92 */     this.written += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void writeShort(int s)
/*     */     throws IOException
/*     */   {
/* 104 */     this.out.write(s & 0xFF);
/* 105 */     this.out.write(s >>> 8 & 0xFF);
/* 106 */     this.written += 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void writeChar(int c)
/*     */     throws IOException
/*     */   {
/* 119 */     this.out.write(c & 0xFF);
/* 120 */     this.out.write(c >>> 8 & 0xFF);
/* 121 */     this.written += 2;
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
/*     */   public final void writeInt24(int i)
/*     */     throws IOException
/*     */   {
/* 135 */     this.out.write(i & 0xFF);
/* 136 */     this.out.write(i >>> 8 & 0xFF);
/* 137 */     this.out.write(i >>> 16 & 0xFF);
/* 138 */     this.written += 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void writeInt(int i)
/*     */     throws IOException
/*     */   {
/* 151 */     this.out.write(i & 0xFF);
/* 152 */     this.out.write(i >>> 8 & 0xFF);
/* 153 */     this.out.write(i >>> 16 & 0xFF);
/* 154 */     this.out.write(i >>> 24 & 0xFF);
/* 155 */     this.written += 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void writeLong(long l)
/*     */     throws IOException
/*     */   {
/* 168 */     this.out.write((int)l & 0xFF);
/* 169 */     this.out.write((int)(l >>> 8) & 0xFF);
/* 170 */     this.out.write((int)(l >>> 16) & 0xFF);
/* 171 */     this.out.write((int)(l >>> 24) & 0xFF);
/* 172 */     this.out.write((int)(l >>> 32) & 0xFF);
/* 173 */     this.out.write((int)(l >>> 40) & 0xFF);
/* 174 */     this.out.write((int)(l >>> 48) & 0xFF);
/* 175 */     this.out.write((int)(l >>> 56) & 0xFF);
/* 176 */     this.written += 8;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void writeFloat(float f)
/*     */     throws IOException
/*     */   {
/* 189 */     writeInt(Float.floatToIntBits(f));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void writeDouble(double d)
/*     */     throws IOException
/*     */   {
/* 202 */     writeLong(Double.doubleToLongBits(d));
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
/*     */   public final void writeBytes(String s)
/*     */     throws IOException
/*     */   {
/* 218 */     int length = s.length();
/* 219 */     for (int i = 0; i < length; i++) {
/* 220 */       this.out.write((byte)s.charAt(i));
/*     */     }
/* 222 */     this.written += length;
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
/*     */   public final void writeBytes(byte[] s, int len)
/*     */     throws IOException
/*     */   {
/* 238 */     int length = s.length;
/* 239 */     if (len < length) {
/* 240 */       for (int i = 0; i < len; i++) {
/* 241 */         this.out.write(s[i]);
/*     */       }
/* 243 */       this.written += len;
/* 244 */       return;
/*     */     }
/* 246 */     for (int i = 0; i < length; i++) {
/* 247 */       this.out.write(s[i]);
/*     */     }
/* 249 */     this.written += length;
/* 250 */     for (int i = 0; i < len - length; i++) {
/* 251 */       this.out.write(0);
/* 252 */       this.written += 1;
/*     */     }
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
/*     */   public final void writeBytes(String s, int len)
/*     */     throws IOException
/*     */   {
/* 268 */     writeBytes(s.getBytes(), len);
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
/*     */   public final void writeChars(String s)
/*     */     throws IOException
/*     */   {
/* 283 */     int length = s.length();
/* 284 */     for (int i = 0; i < length; i++) {
/* 285 */       int c = s.charAt(i);
/* 286 */       this.out.write(c & 0xFF);
/* 287 */       this.out.write(c >>> 8 & 0xFF);
/*     */     }
/* 289 */     this.written += length * 2;
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
/*     */   public final void writeUTF(String s)
/*     */     throws IOException
/*     */   {
/* 310 */     int strlen = s.length();
/* 311 */     int utflen = 0;
/*     */     
/* 313 */     for (int i = 0; i < strlen; i++) {
/* 314 */       int c = s.charAt(i);
/* 315 */       if ((c >= 1) && (c <= 127)) { utflen++;
/* 316 */       } else if (c > 2047) utflen += 3; else {
/* 317 */         utflen += 2;
/*     */       }
/*     */     }
/* 320 */     if (utflen > 65535) { throw new UTFDataFormatException();
/*     */     }
/* 322 */     this.out.write(utflen & 0xFF);
/* 323 */     this.out.write(utflen >>> 8 & 0xFF);
/* 324 */     for (int i = 0; i < strlen; i++) {
/* 325 */       int c = s.charAt(i);
/* 326 */       if ((c >= 1) && (c <= 127)) {
/* 327 */         this.out.write(c);
/*     */       }
/* 329 */       else if (c > 2047) {
/* 330 */         this.out.write(0x80 | c & 0x3F);
/* 331 */         this.out.write(0x80 | c >> 6 & 0x3F);
/* 332 */         this.out.write(0xE0 | c >> 12 & 0xF);
/* 333 */         this.written += 2;
/*     */       }
/*     */       else {
/* 336 */         this.out.write(0x80 | c & 0x3F);
/* 337 */         this.out.write(0xC0 | c >> 6 & 0x1F);
/* 338 */         this.written += 1;
/*     */       }
/*     */     }
/*     */     
/* 342 */     this.written += strlen + 2;
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
/*     */   public final int size()
/*     */   {
/* 355 */     return this.written;
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/io/LittleEndianOutputStream.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */