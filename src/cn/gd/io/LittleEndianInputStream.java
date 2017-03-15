/*     */ package cn.gd.io;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
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
/*     */ public class LittleEndianInputStream
/*     */   extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   public static final String cvsid = "$Id: LittleEndianInputStream.java,v 1.3 2002/01/23 03:01:33 hwh Exp $";
/*     */   private char[] lineBuffer;
/*     */   
/*     */   public LittleEndianInputStream(InputStream in)
/*     */   {
/*  36 */     super(in);
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
/*     */   public final void readFully(byte[] b)
/*     */     throws IOException
/*     */   {
/*  53 */     readFully(b, 0, b.length);
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
/*     */   public final void readFully(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/*  72 */     InputStream in = this.in;
/*  73 */     int n = 0;
/*  74 */     while (n < len) {
/*  75 */       int count = in.read(b, off + n, len - n);
/*  76 */       if (count < 0)
/*  77 */         throw new EOFException();
/*  78 */       n += count;
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
/*     */ 
/*     */   public final boolean readBoolean()
/*     */     throws IOException
/*     */   {
/*  95 */     int bool = this.in.read();
/*  96 */     if (bool == -1) throw new EOFException();
/*  97 */     return bool != 0;
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
/*     */   public final byte readByte()
/*     */     throws IOException
/*     */   {
/* 112 */     int temp = this.in.read();
/* 113 */     if (temp == -1) throw new EOFException();
/* 114 */     return (byte)temp;
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
/*     */   public final int readUnsignedByte()
/*     */     throws IOException
/*     */   {
/* 129 */     int temp = this.in.read();
/* 130 */     if (temp == -1) throw new EOFException();
/* 131 */     return temp;
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
/*     */   public final short readShort()
/*     */     throws IOException
/*     */   {
/* 146 */     int byte1 = this.in.read();
/* 147 */     int byte2 = this.in.read();
/*     */     
/*     */ 
/* 150 */     if ((byte2 == -1) || (byte2 == -1)) throw new EOFException();
/* 151 */     return (short)((byte2 << 8) + (byte1 << 0));
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
/*     */   public final int readUnsignedShort()
/*     */     throws IOException
/*     */   {
/* 165 */     int byte1 = this.in.read();
/* 166 */     int byte2 = this.in.read();
/* 167 */     if ((byte2 == -1) || (byte2 == -1)) throw new EOFException();
/* 168 */     return (byte2 << 8) + byte1;
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
/*     */   public final char readChar()
/*     */     throws IOException
/*     */   {
/* 183 */     int byte1 = this.in.read();
/* 184 */     int byte2 = this.in.read();
/* 185 */     if ((byte1 == -1) || (byte2 == -1)) throw new EOFException();
/* 186 */     return (char)((byte2 << 8) + byte1);
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
/*     */   public final int readInt24()
/*     */     throws IOException
/*     */   {
/* 200 */     int byte1 = this.in.read();
/* 201 */     int byte2 = this.in.read();
/* 202 */     int byte3 = this.in.read();
/* 203 */     int byte4 = 0;
/* 204 */     if ((byte3 == -1) || (byte2 == -1) || (byte1 == -1)) {
/* 205 */       throw new EOFException();
/*     */     }
/* 207 */     if (byte3 > 127) {
/* 208 */       byte4 = 255;
/*     */     } else
/* 210 */       byte4 = 0;
/* 211 */     return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int readInt()
/*     */     throws IOException
/*     */   {
/*     */     int byte1;
/*     */     
/*     */ 
/*     */     int byte2;
/*     */     
/*     */     int byte3;
/*     */     
/*     */     int byte4;
/*     */     
/* 228 */     synchronized (this) {
/* 229 */       byte1 = this.in.read();
/* 230 */       byte2 = this.in.read();
/* 231 */       byte3 = this.in.read();
/* 232 */       byte4 = this.in.read();
/*     */     }
/* 234 */     if ((byte4 == -1) || (byte3 == -1) || (byte2 == -1) || (byte1 == -1)) {
/* 235 */       throw new EOFException();
/*     */     }
/* 237 */     return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
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
/*     */   public final long readLong()
/*     */     throws IOException
/*     */   {
/* 252 */     long byte1 = this.in.read();
/* 253 */     long byte2 = this.in.read();
/* 254 */     long byte3 = this.in.read();
/* 255 */     long byte4 = this.in.read();
/* 256 */     long byte5 = this.in.read();
/* 257 */     long byte6 = this.in.read();
/* 258 */     long byte7 = this.in.read();
/* 259 */     long byte8 = this.in.read();
/* 260 */     if ((byte4 == -1L) || (byte3 == -1L) || (byte2 == -1L) || (byte1 == -1L) || (byte8 == -1L) || (byte7 == -1L) || (byte6 == -1L) || (byte5 == -1L))
/*     */     {
/* 262 */       throw new EOFException();
/*     */     }
/* 264 */     return (byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
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
/*     */   public final String readUTF()
/*     */     throws IOException
/*     */   {
/* 285 */     int numbytes = readUnsignedShort();
/* 286 */     char[] result = new char[numbytes];
/* 287 */     int numread = 0;
/* 288 */     int numchars = 0;
/*     */     
/* 290 */     while (numread < numbytes)
/*     */     {
/* 292 */       int c1 = readUnsignedByte();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 297 */       int test = c1 >> 4;
/* 298 */       if (test < 8) {
/* 299 */         numread++;
/* 300 */         result[(numchars++)] = ((char)c1);
/*     */       } else { int c2;
/* 302 */         if ((test == 12) || (test == 13)) {
/* 303 */           numread += 2;
/* 304 */           if (numread > numbytes) throw new UTFDataFormatException();
/* 305 */           c2 = readUnsignedByte();
/* 306 */           if ((c2 & 0xC0) != 128) throw new UTFDataFormatException();
/* 307 */           result[(numchars++)] = ((char)((c1 & 0x1F) << 6 | c2 & 0x3F));
/*     */         }
/* 309 */         else if (test == 14) {
/* 310 */           numread += 3;
/* 311 */           if (numread > numbytes) throw new UTFDataFormatException();
/* 312 */           c2 = readUnsignedByte();
/* 313 */           int c3 = readUnsignedByte();
/* 314 */           if (((c2 & 0xC0) != 128) || ((c3 & 0xC0) != 128)) {
/* 315 */             throw new UTFDataFormatException();
/*     */           }
/* 317 */           result[(numchars++)] = ((char)((c1 & 0xF) << 12 | (c2 & 0x3F) << 6 | c3 & 0x3F));
/*     */         }
/*     */         else
/*     */         {
/* 321 */           throw new UTFDataFormatException();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 326 */     return new String(result, 0, numchars);
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
/*     */   public final double readDouble()
/*     */     throws IOException
/*     */   {
/* 340 */     return Double.longBitsToDouble(readLong());
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
/*     */   public final float readFloat()
/*     */     throws IOException
/*     */   {
/* 354 */     return Float.intBitsToFloat(readInt());
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
/*     */   public final int skipBytes(int n)
/*     */     throws IOException
/*     */   {
/* 372 */     for (int i = 0; i < n; i += (int)skip(n - i)) {}
/* 373 */     return n;
/*     */   }
/*     */   
/*     */   public final String readLine() throws IOException
/*     */   {
/* 378 */     InputStream in = this.in;
/* 379 */     char[] buf = this.lineBuffer;
/*     */     
/* 381 */     if (buf == null) {
/* 382 */       buf = this.lineBuffer = new char[''];
/*     */     }
/*     */     
/* 385 */     int room = buf.length;
/* 386 */     int offset = 0;
/*     */     int c;
/*     */     for (;;)
/*     */     {
/* 390 */       switch (c = in.read())
/*     */       {
/*     */       case -1: 
/*     */       case 10: 
/*     */         break;
/*     */       case 13: 
/* 396 */         int c2 = in.read();
/* 397 */         if ((c2 == 10) || (c2 == -1)) break;
/* 398 */         if (!(in instanceof PushbackInputStream)) {
/* 399 */           in = this.in = new PushbackInputStream(in);
/*     */         }
/* 401 */         ((PushbackInputStream)in).unread(c2); break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 406 */         room--; if (room < 0) {
/* 407 */           buf = new char[offset + 128];
/* 408 */           room = buf.length - offset - 1;
/* 409 */           System.arraycopy(this.lineBuffer, 0, buf, 0, offset);
/* 410 */           this.lineBuffer = buf;
/*     */         }
/* 412 */         buf[(offset++)] = ((char)c);
/*     */       }
/*     */       
/*     */     }
/* 416 */     if ((c == -1) && (offset == 0)) {
/* 417 */       return null;
/*     */     }
/* 419 */     return String.copyValueOf(buf, 0, offset);
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/io/LittleEndianInputStream.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */