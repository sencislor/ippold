/*      */ package edu.iris.miniseedutils.steim;
/*      */ 
/*      */ import edu.iris.corejava.Format;
/*      */ import edu.iris.timeutils.TimeStamp;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.Date;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GenericMiniSeedRecord
/*      */   extends GenericDataRecord
/*      */   implements Comparable
/*      */ {
/*   96 */   private ChannelMask chanMask = new ChannelMask();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  102 */   private static Format fmt2_2s = new Format("%-2s");
/*  103 */   private static Format fmt3_3s = new Format("%-3s");
/*  104 */   private static Format fmt5_5s = new Format("%-5s");
/*  105 */   private static Format fmt2_2d = new Format("%2.2d");
/*  106 */   private static Format fmt3_3d = new Format("%3.3d");
/*  107 */   private static Format fmt4_4d = new Format("%4.4d");
/*  108 */   private static Format fmt7_4f = new Format("%07.4f");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  130 */   private static Steim steim = new Steim();
/*      */   
/*      */   private static final int BLKT1000_LEN = 8;
/*      */   
/*      */   private static final int BLKT1001_LEN = 8;
/*      */   
/*      */   private static final int BLKT100_LEN = 12;
/*      */   
/*      */   private static final int BLKT200_LEN = 28;
/*      */   private static final int BLKT201_LEN = 36;
/*      */   private static final int BLKT300_LEN = 32;
/*      */   private static final int BLKT310_LEN = 32;
/*      */   private static final int BLKT320_LEN = 28;
/*      */   private static final int BLKT390_LEN = 28;
/*      */   private static final int BLKT395_LEN = 16;
/*      */   private static final int BLKT400_LEN = 16;
/*      */   private static final int DEFAULTHEADERLENGTH = 48;
/*      */   private static final int DEFAULTRECORDLENGTH = 4096;
/*      */   private static final int FSDH_SIZE = 48;
/*      */   private static final int MAXRECORDLENGTH = 65536;
/*      */   protected boolean byteSwappedRec;
/*      */   public static final String cvsid = "$Id: GenericMiniSeedRecord.java,v 1.16 2004/02/08 23:13:03 hwh Exp $";
/*      */   protected int[] udata;
/*      */   
/*      */   public GenericMiniSeedRecord(int headLen, int recLen)
/*      */   {
/*  156 */     this.byteSwappedRec = false;
/*  157 */     this.recordLength = recLen;
/*  158 */     this.headLength = headLen;
/*  159 */     this.dataLength = (this.recordLength - this.headLength);
/*  160 */     this.head = new byte[headLen];
/*  161 */     this.data = new byte[this.dataLength];
/*      */     
/*  163 */     this.frameLength = 64;
/*  164 */     int numRecordFrames = recLen / this.frameLength;
/*  165 */     this.numHeaderFrames = ((int)Math.ceil(headLen / this.frameLength));
/*  166 */     if (this.numHeaderFrames >= numRecordFrames) {
/*  167 */       return;
/*      */     }
/*  169 */     this.numDataFrames = (numRecordFrames - this.numHeaderFrames);
/*  170 */     int recordHeaderLength = this.numHeaderFrames * this.frameLength;
/*  171 */     int recordDataLength = this.recordLength - recordHeaderLength;
/*  172 */     this.head = new byte[recordHeaderLength];
/*  173 */     this.frames = new CompressedFrame[this.numDataFrames];
/*  174 */     for (int i = 0; i < this.frames.length; i++) {
/*  175 */       this.frames[i] = new CompressedFrame();
/*      */     }
/*      */   }
/*      */   
/*      */   public void setEncodeBuffer(byte[] buf) {
/*  180 */     if (buf[6] != 68) {
/*  181 */       System.err.println("Header Data is not MinSeed Header");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */     if (this.head == null) {
/*  190 */       System.err.println("Internal Error1 in GenericMiniSeedRecord");
/*      */     }
/*  192 */     System.arraycopy(buf, 0, this.head, 0, 64);
/*  193 */     System.arraycopy(buf, 64, this.data, 0, buf.length - 64);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  202 */       this.byteSwappedRec = isByteSwapped(this.head);
/*      */     } catch (MiniseedFormatException e) {
/*  204 */       e.printStackTrace();
/*      */     }
/*      */     
/*  207 */     int k = 0;
/*  208 */     for (int i = 0; i < this.numDataFrames; i++) {
/*  209 */       for (int j = 0; j < 16; j++) {
/*  210 */         this.frames[i].cfp[j] = Steim.makeInt(this.data[k], this.data[(k + 1)], this.data[(k + 2)], this.data[(k + 3)], this.byteSwappedRec);
/*      */         
/*  212 */         k += 4;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getNextSeqNumber(String seqNumber)
/*      */   {
/*  226 */     int seq = Integer.parseInt(seqNumber);
/*  227 */     DecimalFormat fmt = new DecimalFormat("000000");
/*  228 */     seq = (seq + 1) % 1000000;
/*  229 */     return fmt.format(seq);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getHeaderLength(byte[] bytes)
/*      */     throws MiniseedFormatException, UnrecognizedBlocketteException, UnrecognizedFormatException
/*      */   {
/*      */     String errStr;
/*      */     
/*  239 */     if (bytes.length < 48) {
/*  240 */       errStr = "buffer is too short to contain a complete FSDH (length=" + bytes.length + ", but need at least " + 48 + " bytes)";
/*      */       
/*  242 */       throw new UnrecognizedFormatException(errStr);
/*      */     }
/*  244 */     if (bytes[6] != 68) {
/*  245 */       errStr = "buffer is not a miniSEED data record (should contain a 'D' in the 6th byte, found a '" + bytes[6] + "' instead)";
/*      */       
/*  247 */       throw new UnrecognizedFormatException(errStr);
/*      */     }
/*  249 */     int hdrLen = 48;
/*  250 */     boolean byteSwappedRec = isByteSwapped(bytes);
/*  251 */     int nextOffset = Steim.makeShort(bytes[46], bytes[47], byteSwappedRec);
/*      */     
/*  253 */     while (nextOffset > 0) {
/*  254 */       if (nextOffset + 1 > bytes.length) {
/*  255 */         throw new MiniseedFormatException("can't determine miniSEED header type");
/*      */       }
/*  257 */       int blktType = Steim.makeShort(bytes[nextOffset], bytes[(nextOffset + 1)], byteSwappedRec);
/*  258 */       int blktLen; switch (blktType) {
/*      */       case 100: 
/*  260 */         blktLen = 12;
/*  261 */         break;
/*      */       case 200: 
/*  263 */         blktLen = 28;
/*  264 */         break;
/*      */       case 201: 
/*  266 */         blktLen = 36;
/*  267 */         break;
/*      */       case 300: 
/*  269 */         blktLen = 32;
/*  270 */         break;
/*      */       case 310: 
/*  272 */         blktLen = 32;
/*  273 */         break;
/*      */       case 320: 
/*  275 */         blktLen = 28;
/*  276 */         break;
/*      */       case 390: 
/*  278 */         blktLen = 28;
/*  279 */         break;
/*      */       case 395: 
/*  281 */         blktLen = 16;
/*  282 */         break;
/*      */       case 400: 
/*  284 */         blktLen = 16;
/*  285 */         break;
/*      */       case 1000: 
/*  287 */         blktLen = 8;
/*  288 */         break;
/*      */       case 1001: 
/*  290 */         blktLen = 8;
/*  291 */         break;
/*      */       default: 
/*  293 */         errStr = "blkt id of " + blktType + " found in data record";
/*  294 */         throw new UnrecognizedBlocketteException(errStr);
/*      */       }
/*  296 */       if (nextOffset + blktLen > bytes.length) {
/*  297 */         throw new MiniseedFormatException("incomplete miniSEED header found");
/*      */       }
/*  299 */       hdrLen += blktLen;
/*  300 */       nextOffset = Steim.makeShort(bytes[(nextOffset + 2)], bytes[(nextOffset + 3)], byteSwappedRec);
/*      */     }
/*  302 */     return hdrLen;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getRecordLength(byte[] bytes)
/*      */     throws MiniseedFormatException, UnrecognizedBlocketteException, UnrecognizedFormatException
/*      */   {
/*      */     String errStr;
/*      */     
/*      */ 
/*  313 */     if (bytes.length < 48) {
/*  314 */       errStr = "buffer is too short to contain a complete FSDH (length=" + bytes.length + ", but need at least " + 48 + " bytes)";
/*      */       
/*  316 */       throw new UnrecognizedFormatException(errStr);
/*      */     }
/*  318 */     if (bytes[6] != 68) {
/*  319 */       errStr = "buffer is not a miniSEED data record (should contain a 'D' in the 6th byte, found a '" + bytes[6] + "' instead)";
/*      */       
/*  321 */       throw new UnrecognizedFormatException(errStr);
/*      */     }
/*  323 */     int hdrLen = 48;
/*  324 */     boolean byteSwappedRec = isByteSwapped(bytes);
/*  325 */     int nextOffset = Steim.makeShort(bytes[46], bytes[47], byteSwappedRec);
/*      */     
/*  327 */     while (nextOffset > 0) {
/*  328 */       if (nextOffset + 1 > bytes.length) {
/*  329 */         throw new MiniseedFormatException("can't determine miniSEED header type");
/*      */       }
/*  331 */       int blktType = Steim.makeShort(bytes[nextOffset], bytes[(nextOffset + 1)], byteSwappedRec);
/*  332 */       if (blktType == 1000) {
/*  333 */         int lenExp = bytes[(nextOffset + 6)];
/*  334 */         return (int)Math.pow(2.0D, lenExp);
/*      */       }
/*  336 */       nextOffset = Steim.makeShort(bytes[(nextOffset + 2)], bytes[(nextOffset + 3)], byteSwappedRec);
/*      */     }
/*  338 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] getHeader(InputStream is)
/*      */     throws MiniseedFormatException
/*      */   {
/*  349 */     int blktSize = 48;
/*  350 */     byte[] readBuf = new byte[blktSize];
/*  351 */     byte[] headerBuf = new byte[blktSize];
/*  352 */     int nbytes = 0;int headerLength = 0;int bufferOffset = 0;
/*  353 */     boolean wordOrderChecked = false;
/*      */     for (;;)
/*      */     {
/*  356 */       int nbytesRead = 0;
/*      */       do {
/*      */         try {
/*  359 */           if ((nbytes = is.read(readBuf, nbytesRead, blktSize - nbytesRead)) == -1) {
/*  360 */             throw new MiniseedFormatException("End of file reached while reading a miniSEED data record's header");
/*      */           }
/*      */           
/*  363 */           nbytesRead += nbytes;
/*      */         } catch (IOException e) {
/*  365 */           e.printStackTrace();
/*  366 */           return null;
/*      */         }
/*  368 */       } while (nbytesRead < blktSize);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  373 */       System.arraycopy(readBuf, 0, headerBuf, bufferOffset, readBuf.length);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/*  380 */         headerLength = getHeaderLength(headerBuf);
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (MiniseedFormatException e)
/*      */       {
/*      */ 
/*      */ 
/*  389 */         int newSize = headerBuf.length + readBuf.length;
/*  390 */         bufferOffset += readBuf.length;
/*  391 */         byte[] tmpBuf = new byte[newSize];
/*  392 */         System.arraycopy(headerBuf, 0, tmpBuf, 0, headerBuf.length);
/*  393 */         headerBuf = tmpBuf;
/*      */       }
/*      */       catch (UnrecognizedBlocketteException e)
/*      */       {
/*  397 */         e.printStackTrace();
/*  398 */         return null;
/*      */       } catch (UnrecognizedFormatException e) {
/*  400 */         e.printStackTrace();
/*  401 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  405 */     return headerBuf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isByteSwapped(byte[] headBuf)
/*      */     throws MiniseedFormatException
/*      */   {
/*  414 */     byte[] timeBuf = new byte[10];
/*  415 */     System.arraycopy(headBuf, 20, timeBuf, 0, 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  423 */     short doy = Steim.makeShort(timeBuf[2], timeBuf[3]);
/*  424 */     if ((doy < 1) || (doy > 366)) {
/*  425 */       doy = Steim.makeShort(timeBuf[3], timeBuf[2]);
/*  426 */       if ((doy < 1) || (doy > 366)) {
/*  427 */         String errStr = "Illegal day of year found in FSDH start time (found byte values of " + Integer.toHexString(timeBuf[2]) + " " + Integer.toHexString(timeBuf[3]) + " in doy field";
/*      */         
/*      */ 
/*  430 */         throw new MiniseedFormatException(errStr);
/*      */       }
/*      */       
/*  433 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  439 */     short year = Steim.makeShort(timeBuf[0], timeBuf[1]);
/*  440 */     if ((year < 0) || (year > 5138)) {
/*  441 */       year = Steim.makeShort(timeBuf[1], timeBuf[0]);
/*  442 */       if ((year < 0) || (year > 5138)) {
/*  443 */         String errStr = "Illegal year found in FSDH start time (found byte values of " + Integer.toHexString(timeBuf[0]) + " " + Integer.toHexString(timeBuf[1]) + " in year field";
/*      */         
/*      */ 
/*  446 */         throw new MiniseedFormatException(errStr);
/*      */       }
/*      */       
/*  449 */       return true;
/*      */     }
/*      */     
/*  452 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getStartTimeStr()
/*      */   {
/*  468 */     byte[] startTime = new byte[10];
/*  469 */     System.arraycopy(this.head, 20, startTime, 0, 10);
/*      */     
/*      */ 
/*  472 */     int year = Steim.makeShort(startTime[0], startTime[1], this.byteSwappedRec);
/*  473 */     int doy = Steim.makeShort(startTime[2], startTime[3], this.byteSwappedRec);
/*  474 */     int hour = startTime[4];
/*  475 */     int min = startTime[5];
/*  476 */     int intSecs = startTime[6];
/*  477 */     int seedDeciSecs = Steim.makeShort(startTime[8], startTime[9], this.byteSwappedRec);
/*  478 */     float secs = intSecs + seedDeciSecs / 10000.0F;
/*  479 */     return fmt4_4d.format(year) + "," + fmt3_3d.format(doy) + "," + fmt2_2d.format(hour) + ":" + fmt2_2d.format(min) + ":" + fmt7_4f.format(secs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSequenceNumber()
/*      */   {
/*  488 */     return new String(this.head, 0, 6).trim();
/*      */   }
/*      */   
/*  491 */   public String getStation() { return new String(this.head, 8, 5).trim(); }
/*      */   
/*      */   public String getLocID() {
/*  494 */     return new String(this.head, 13, 2).trim();
/*      */   }
/*      */   
/*  497 */   public String getChannel() { return new String(this.head, 15, 3).trim(); }
/*      */   
/*      */   public String getNetwork() {
/*  500 */     return new String(this.head, 18, 2).trim();
/*      */   }
/*      */   
/*  503 */   public TimeStamp getStartTime() { return TimeStamp.buildTimestamp(getStartTimeStr()); }
/*      */   
/*      */   public Timestamp getEndTime()
/*      */   {
/*  507 */     Timestamp endTS = null;
/*  508 */     long ns = (getNumSamples() * 1000 / getSampleRate()) * 1000000L + getStartTime().getNanos();
/*      */     
/*      */ 
/*      */ 
/*  512 */     endTS = new Timestamp((getStartTime().getTime() / 1000L + ns / 1000000000L) * 1000L);
/*      */     
/*  514 */     ns %= 1000000000L;
/*      */     
/*  516 */     endTS.setNanos((int)ns);
/*  517 */     return endTS;
/*      */   }
/*      */   
/*      */   public double getSampleRate() {
/*  521 */     int hdrLen = 48;
/*  522 */     int nextOffset = Steim.makeShort(this.head[46], this.head[47], this.byteSwappedRec);
/*      */     
/*  524 */     while (nextOffset > 0) {
/*  525 */       if (nextOffset + 1 > this.head.length) {
/*      */         break;
/*      */       }
/*  528 */       int blktType = Steim.makeShort(this.head[nextOffset], this.head[(nextOffset + 1)], this.byteSwappedRec);
/*  529 */       if (blktType == 100) {
/*  530 */         return Steim.makeFloat(this.head[(nextOffset + 4)], this.head[(nextOffset + 5)], this.head[(nextOffset + 6)], this.head[(nextOffset + 7)], this.byteSwappedRec);
/*      */       }
/*      */       
/*  533 */       nextOffset = Steim.makeShort(this.head[(nextOffset + 2)], this.head[(nextOffset + 3)], this.byteSwappedRec);
/*      */     }
/*  535 */     int srate_fact = Steim.makeShort(this.head[32], this.head[33], this.byteSwappedRec);
/*  536 */     int srate_mult = Steim.makeShort(this.head[34], this.head[35], this.byteSwappedRec);
/*  537 */     if ((srate_fact >= 0) && (srate_mult >= 0))
/*  538 */       return srate_fact * srate_mult;
/*  539 */     if ((srate_fact >= 0) && (srate_mult < 0))
/*  540 */       return -(srate_fact / srate_mult);
/*  541 */     if ((srate_fact < 0) && (srate_mult >= 0)) {
/*  542 */       return -(srate_mult / srate_fact);
/*      */     }
/*  544 */     return 1.0D / (srate_mult * srate_fact);
/*      */   }
/*      */   
/*  547 */   public int getNumSamples() { return Steim.makeShort(this.head[30], this.head[31], this.byteSwappedRec); }
/*      */   
/*      */   public int getActiveFlag()
/*      */   {
/*  551 */     return this.head[36];
/*      */   }
/*      */   
/*      */   public int getIOFlag() {
/*  555 */     return this.head[37];
/*      */   }
/*      */   
/*      */   public short getWordOrder() {
/*  559 */     int hdrLen = 48;
/*  560 */     int nextOffset = Steim.makeShort(this.head[46], this.head[47], this.byteSwappedRec);
/*      */     
/*  562 */     while (nextOffset > 0) {
/*  563 */       if (nextOffset + 1 > this.head.length) {
/*  564 */         System.err.println("can't determine miniSEED header type");
/*  565 */         return -1;
/*      */       }
/*  567 */       int blktType = Steim.makeShort(this.head[nextOffset], this.head[(nextOffset + 1)], this.byteSwappedRec);
/*  568 */       if (blktType == 1000) {
/*  569 */         return (short)this.head[(nextOffset + 5)];
/*      */       }
/*  571 */       nextOffset = Steim.makeShort(this.head[(nextOffset + 2)], this.head[(nextOffset + 3)], this.byteSwappedRec);
/*      */     }
/*  573 */     return -1;
/*      */   }
/*      */   
/*      */   public short getDataFormat() {
/*  577 */     int hdrLen = 48;
/*  578 */     int nextOffset = Steim.makeShort(this.head[46], this.head[47], this.byteSwappedRec);
/*      */     
/*  580 */     while (nextOffset > 0) {
/*  581 */       if (nextOffset + 1 > this.head.length) {
/*  582 */         System.err.println("can't determine miniSEED header type");
/*  583 */         return -1;
/*      */       }
/*  585 */       int blktType = Steim.makeShort(this.head[nextOffset], this.head[(nextOffset + 1)], this.byteSwappedRec);
/*  586 */       if (blktType == 1000) {
/*  587 */         return (short)this.head[(nextOffset + 4)];
/*      */       }
/*  589 */       nextOffset = Steim.makeShort(this.head[(nextOffset + 2)], this.head[(nextOffset + 3)], this.byteSwappedRec);
/*      */     }
/*  591 */     return -1;
/*      */   }
/*      */   
/*      */   public int getBlockType() {
/*  595 */     int hdrLen = 48;
/*  596 */     int nextOffset = Steim.makeShort(this.head[46], this.head[47], this.byteSwappedRec);
/*  597 */     int blktType = Steim.makeShort(this.head[nextOffset], this.head[(nextOffset + 1)], this.byteSwappedRec);
/*  598 */     return blktType;
/*      */   }
/*      */   
/*      */   public short getExpRecLength() {
/*  602 */     int hdrLen = 48;
/*  603 */     int nextOffset = Steim.makeShort(this.head[46], this.head[47], this.byteSwappedRec);
/*      */     
/*  605 */     while (nextOffset > 0) {
/*  606 */       if (nextOffset + 1 > this.head.length) {
/*  607 */         System.err.println("can't determine miniSEED header type");
/*  608 */         return -1;
/*      */       }
/*  610 */       int blktType = Steim.makeShort(this.head[nextOffset], this.head[(nextOffset + 1)], this.byteSwappedRec);
/*  611 */       if (blktType == 1000) {
/*  612 */         return (short)this.head[(nextOffset + 6)];
/*      */       }
/*  614 */       nextOffset = Steim.makeShort(this.head[(nextOffset + 2)], this.head[(nextOffset + 3)], this.byteSwappedRec);
/*      */     }
/*  616 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRecordLength()
/*      */   {
/*  625 */     return this.head.length + this.dataLength;
/*      */   }
/*      */   
/*      */   short getDataOffset() {
/*  629 */     return Steim.makeShort(this.head[44], this.head[45], this.byteSwappedRec);
/*      */   }
/*      */   
/*      */   public int[] getUData() {
/*  633 */     return this.udata;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] getHeaderBytes()
/*      */   {
/*  641 */     return this.head;
/*      */   }
/*      */   
/*  644 */   protected byte[] getDataBytes() { return this.data; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  653 */     return getSequenceNumber() + " " + getNetwork() + "/" + getStation() + "/" + getLocID() + "/" + getChannel() + " " + getStartTime() + "\tnsamples " + getNumSamples() + " sps " + getSampleRate() + " ActiveFlag:" + getActiveFlag() + " CaliFlag:" + getIOFlag();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte[] getBytes()
/*      */   {
/*  660 */     byte[] Bytes = new byte[this.head.length + this.data.length];
/*  661 */     System.arraycopy(this.head, 0, Bytes, 0, this.head.length);
/*  662 */     System.arraycopy(this.data, 0, Bytes, this.head.length, this.data.length);
/*  663 */     return Bytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static GenericMiniSeedRecord buildMiniSeedRecord(byte[] miniSeedRecored)
/*      */   {
/*  672 */     GenericMiniSeedRecord gmsr = null;
/*      */     try {
/*  674 */       int headerLength = 64;
/*  675 */       int arecLen = getRecordLength(miniSeedRecored);
/*  676 */       if (arecLen > miniSeedRecored.length)
/*  677 */         return null;
/*  678 */       byte[] ahead = new byte[headerLength];
/*  679 */       byte[] adata = new byte[arecLen - headerLength];
/*  680 */       System.arraycopy(miniSeedRecored, 0, ahead, 0, headerLength);
/*  681 */       System.arraycopy(miniSeedRecored, headerLength, adata, 0, adata.length);
/*  682 */       return buildMiniSeedRecord(ahead, adata, arecLen);
/*      */     } catch (UnrecognizedFormatException ufEx) {
/*  684 */       ufEx.printStackTrace();
/*  685 */       return null;
/*      */     } catch (MiniseedFormatException msfEx) {
/*  687 */       msfEx.printStackTrace();
/*  688 */       return null;
/*      */     } catch (UnrecognizedBlocketteException uBEx) {
/*  690 */       uBEx.printStackTrace();
/*  691 */       return null;
/*      */     }
/*      */     catch (Exception ex) {
/*  694 */       ex.printStackTrace(); }
/*  695 */     return null;
/*      */   }
/*      */   
/*      */   public static GenericMiniSeedRecord buildMiniSeedRecord(byte[] head, byte[] data)
/*      */   {
/*  700 */     return buildMiniSeedRecord(head, data, 4096);
/*      */   }
/*      */   
/*      */   public static GenericMiniSeedRecord buildMiniSeedRecord(byte[] head, byte[] data, int recLen) {
/*  704 */     if (head.length + data.length != recLen) {
/*  705 */       System.err.println("buildMiniSeedRecord (ERROR):  illegal record length specified");
/*  706 */       return null;
/*      */     }
/*  708 */     if (head[6] != 68) {
/*  709 */       System.err.println("Header Data is not MinSeed Header");
/*  710 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  715 */     GenericMiniSeedRecord gmsRec = new GenericMiniSeedRecord(head.length, recLen);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  724 */       gmsRec.byteSwappedRec = isByteSwapped(head);
/*      */     } catch (MiniseedFormatException e) {
/*  726 */       e.printStackTrace();
/*  727 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  734 */     if (head == null) {
/*  735 */       System.err.println("Internal Error1 in GenericMiniSeedRecord");
/*      */     }
/*  737 */     if (gmsRec.head == null) {
/*  738 */       System.err.println("Internal Error2 in GenericMiniSeedRecord");
/*      */     }
/*  740 */     System.arraycopy(head, 0, gmsRec.head, 0, head.length);
/*  741 */     System.arraycopy(data, 0, gmsRec.data, 0, data.length);
/*  742 */     int k = 0;
/*  743 */     for (int i = 0; i < gmsRec.numDataFrames; i++) {
/*  744 */       for (int j = 0; j < 16; j++) {
/*  745 */         gmsRec.frames[i].cfp[j] = Steim.makeInt(data[k], data[(k + 1)], data[(k + 2)], data[(k + 3)], gmsRec.byteSwappedRec);
/*      */         
/*  747 */         k += 4;
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  753 */       gmsRec.chanMask.set(gmsRec.getChannel(), gmsRec.getLocID());
/*      */     }
/*      */     catch (Exception ex) {
/*  756 */       System.err.println("DEBUG:\t" + new Date() + gmsRec.toString());
/*  757 */       ex.printStackTrace();
/*  758 */       gmsRec = null;
/*      */     }
/*  760 */     return gmsRec;
/*      */   }
/*      */   
/*      */   public boolean decompress() {
/*  764 */     boolean sucess = false;
/*      */     try {
/*  766 */       this.udata = new int[getNumSamples()];
/*  767 */       if (decompress(this.udata)) {
/*  768 */         sucess = true;
/*      */       } else
/*  770 */         sucess = false;
/*      */     } catch (MiniseedFormatException msfEx) {
/*  772 */       msfEx.printStackTrace();
/*  773 */       sucess = false;
/*      */     }
/*  775 */     return sucess;
/*      */   }
/*      */   
/*      */   public boolean decompress(int[] udata)
/*      */     throws MiniseedFormatException
/*      */   {
/*  781 */     if (getBlockType() != 1000) {
/*  782 */       return false;
/*      */     }
/*  784 */     short flip = 0;
/*  785 */     short firstframe = 0;
/*  786 */     short level = 3;
/*  787 */     short dframes = 63;
/*      */     
/*      */ 
/*      */ 
/*  791 */     int headertotal = getNumSamples();
/*  792 */     short hfirstdata = getDataOffset();
/*  793 */     if (hfirstdata == -1) {
/*  794 */       throw new MiniseedFormatException("shit...");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  801 */     if (hfirstdata % this.frameLength != 0) {
/*  802 */       throw new MiniseedFormatException("illegal start-of-data is found");
/*      */     }
/*      */     
/*  805 */     firstframe = (short)(hfirstdata / this.frameLength);
/*  806 */     firstframe = (short)(firstframe - 1);
/*  807 */     if (firstframe < 0) {
/*  808 */       System.out.println(hfirstdata);
/*  809 */       throw new MiniseedFormatException("shit");
/*      */     }
/*  811 */     int formatCode = getDataFormat();
/*  812 */     switch (formatCode) {
/*  813 */     case 10:  level = 1; break;
/*  814 */     case 11:  level = 2; break;
/*      */     case 19: case 20: 
/*  816 */       level = 3; break;
/*  817 */     default:  throw new MiniseedFormatException("unknown encoding format");
/*      */     }
/*  819 */     switch (getExpRecLength()) {
/*  820 */     case 12:  dframes = 63; break;
/*  821 */     case 11:  dframes = 31; break;
/*  822 */     case 10:  dframes = 15; break;
/*  823 */     case 9:  dframes = 7; break;
/*  824 */     case 8:  dframes = 3; break;
/*  825 */     case 7:  dframes = 1; break;
/*  826 */     default:  throw new MiniseedFormatException("unknown record Length");
/*      */     }
/*      */     
/*  829 */     DecompressionContinuity dcp = steim.initGenericDecompression();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  834 */     short statcode = 0;
/*  835 */     int rectotal = steim.decompressGenericRecord(this, udata, statcode, dcp, firstframe, headertotal, level, flip, dframes);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  845 */     return !steim.dfErrorFatal(statcode, System.err);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printWaveformHeader(PrintStream os)
/*      */   {
/*  853 */     os.println("\n===============================================================================");
/*      */     
/*  855 */     os.println("STATION  LOCATION  CHANNEL  NETWORK  TIME");
/*      */   }
/*      */   
/*      */   private String getWordOrderStr()
/*      */   {
/*  860 */     switch (getWordOrder()) {
/*      */     case 1: 
/*  862 */       return "68000/SPARC word order";
/*      */     }
/*      */     
/*      */     
/*  866 */     return "VAX/8086 word order";
/*      */   }
/*      */   
/*      */   private String getDataFormatStr() {
/*  870 */     int formatCode = getDataFormat();
/*  871 */     switch (formatCode) {
/*      */     case 1: 
/*  873 */       return "16 bit integers";
/*      */     case 2: 
/*  875 */       return "24 bit integers";
/*      */     case 3: 
/*  877 */       return "32 bit integers";
/*      */     case 4: 
/*  879 */       return "IEEE floating point";
/*      */     case 5: 
/*  881 */       return "IEEE double precision floating point";
/*      */     case 10: 
/*  883 */       return "STEIM (1) Compression";
/*      */     case 11: 
/*  885 */       return "STEIM (2) Compression";
/*      */     case 12: 
/*  887 */       return "GEOSCOPE Multiplexed Format 24 bit integer";
/*      */     case 13: 
/*  889 */       return "GEOSCOPE Multiplexed Format 16 bit gain-ranged, 3 bit exponent";
/*      */     case 14: 
/*  891 */       return "GEOSCOPE Multiplexed Format 16 bit gain-ranged, 4 bit exponent";
/*      */     case 15: 
/*  893 */       return "US National Network compression";
/*      */     case 16: 
/*  895 */       return "CDSN 16 bit gain ranged";
/*      */     case 17: 
/*  897 */       return "Graefenberg 16 bit gain ranged";
/*      */     case 18: 
/*  899 */       return "IPG - Strasbourg 16 bit gain ranged";
/*      */     case 19: 
/*  901 */       return "STEIM (3) Compression";
/*      */     case 30: 
/*  903 */       return "SRO Format";
/*      */     case 31: 
/*  905 */       return "HGLP Format";
/*      */     case 32: 
/*  907 */       return "DWWSSN gain ranged format";
/*      */     case 33: 
/*  909 */       return "RSTN 16 bit gain ranged";
/*      */     }
/*      */     
/*      */     
/*  913 */     return "Unrecognized Data Format (" + formatCode + ")";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void printBlockette100(PrintStream os, int offset)
/*      */   {
/*  921 */     os.println("        BLOCKETTE 100:");
/*  922 */     float srate = Steim.makeFloat(this.head[(offset + 4)], this.head[(offset + 5)], this.head[(offset + 6)], this.head[(offset + 7)], this.byteSwappedRec);
/*      */     
/*  924 */     os.println("                  actual sample rate: " + srate);
/*  925 */     os.println("                               flags: to be defined - " + buildFlagString(this.head[(offset + 8)]));
/*      */     
/*  927 */     os.println("                       reserved[0-2]: " + buildFlagString(this.head[(offset + 9)]) + " " + buildFlagString(this.head[(offset + 10)]) + " " + buildFlagString(this.head[(offset + 11)]) + " ");
/*      */   }
/*      */   
/*      */   private void printBlockette1000(PrintStream os, int offset)
/*      */   {
/*  932 */     os.println("       BLOCKETTE 1000:");
/*  933 */     os.println("                     encoding format: " + getDataFormatStr() + " (val:" + this.head[(offset + 4)] + ")");
/*      */     
/*  935 */     os.println("                          word order: " + getWordOrderStr());
/*  936 */     os.println("                  data record length: " + this.head[(offset + 6)]);
/*  937 */     os.println("                            reserved: " + buildFlagString(this.head[(offset + 7)]));
/*      */   }
/*      */   
/*  940 */   private void printBlockette1001(PrintStream os, int offset) { os.println("       BLOCKETTE 1001:");
/*  941 */     os.println("                      timing quality: " + this.head[(offset + 4)]);
/*  942 */     os.println("                data start time usec: " + this.head[(offset + 5)]);
/*  943 */     os.println("                            reserved: " + buildFlagString(this.head[(offset + 6)]));
/*  944 */     os.println("                         frame count: " + this.head[(offset + 7)]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printDataBlockettes(PrintStream os)
/*      */   {
/*  953 */     os.print(fmt5_5s.format(getStation()) + "    ");
/*  954 */     os.print(fmt2_2s.format(getLocID()) + "        ");
/*  955 */     os.print(fmt3_3s.format(getChannel()) + "      ");
/*  956 */     os.print(fmt2_2s.format(getNetwork()) + "       ");
/*  957 */     os.print(getStartTimeStr());
/*  958 */     if (this.byteSwappedRec) {
/*  959 */       os.println("   [WORD SWAP MODE]");
/*      */     } else {
/*  961 */       os.println("   [NO SWAP MODE]");
/*      */     }
/*  963 */     int intVal = Steim.makeShort(this.head[30], this.head[31], this.byteSwappedRec);
/*  964 */     os.println("         # samples in record: " + intVal);
/*  965 */     intVal = Steim.makeShort(this.head[32], this.head[33], this.byteSwappedRec);
/*  966 */     os.println("                 sample_rate: " + intVal);
/*  967 */     intVal = Steim.makeShort(this.head[34], this.head[35], this.byteSwappedRec);
/*  968 */     os.println("                  multiplier: " + intVal);
/*  969 */     os.println("              activity flags: " + buildFlagString(this.head[36]));
/*  970 */     os.println("         I/O and clock flags: " + buildFlagString(this.head[37]));
/*  971 */     os.println("          data quality flags: " + buildFlagString(this.head[38]));
/*  972 */     os.println("             # of blockettes: " + this.head[39]);
/*  973 */     intVal = Steim.makeInt(this.head[40], this.head[41], this.head[42], this.head[43], this.byteSwappedRec);
/*  974 */     os.println("             time correction: " + intVal);
/*  975 */     intVal = Steim.makeShort(this.head[44], this.head[45], this.byteSwappedRec);
/*  976 */     os.println("           begin data offset: " + intVal);
/*  977 */     intVal = Steim.makeShort(this.head[46], this.head[47], this.byteSwappedRec);
/*  978 */     os.println("        begin blkette offset: " + intVal);
/*  979 */     int nextOffset = intVal;
/*  980 */     int blktLen = 0;
/*      */     
/*  982 */     while (nextOffset > 0) { String errStr;
/*  983 */       if (nextOffset + 1 > this.head.length) {
/*  984 */         errStr = "printDataBlockettes (ERROR): can't determine miniSEED header type";
/*  985 */         System.err.println(errStr);
/*  986 */         System.exit(10);
/*      */       }
/*  988 */       int blktType = Steim.makeShort(this.head[nextOffset], this.head[(nextOffset + 1)], this.byteSwappedRec);
/*  989 */       switch (blktType) {
/*      */       case 100: 
/*  991 */         blktLen = 12;
/*  992 */         printBlockette100(os, nextOffset);
/*  993 */         break;
/*      */       case 200: 
/*  995 */         blktLen = 28;
/*  996 */         break;
/*      */       case 201: 
/*  998 */         blktLen = 36;
/*  999 */         break;
/*      */       case 300: 
/* 1001 */         blktLen = 32;
/* 1002 */         break;
/*      */       case 310: 
/* 1004 */         blktLen = 32;
/* 1005 */         break;
/*      */       case 320: 
/* 1007 */         blktLen = 28;
/* 1008 */         break;
/*      */       case 390: 
/* 1010 */         blktLen = 28;
/* 1011 */         break;
/*      */       case 395: 
/* 1013 */         blktLen = 16;
/* 1014 */         break;
/*      */       case 400: 
/* 1016 */         blktLen = 16;
/* 1017 */         break;
/*      */       case 1000: 
/* 1019 */         blktLen = 8;
/* 1020 */         printBlockette1000(os, nextOffset);
/* 1021 */         break;
/*      */       case 1001: 
/* 1023 */         blktLen = 8;
/* 1024 */         printBlockette1001(os, nextOffset);
/* 1025 */         break;
/*      */       default: 
/* 1027 */         errStr = "printDataBlockettes (ERROR): unrecognized blockette (type = " + blktType + ") found in data record";
/*      */         
/* 1029 */         System.err.println(errStr);
/* 1030 */         System.exit(10);
/*      */       }
/* 1032 */       if (nextOffset + blktLen > this.head.length) {
/* 1033 */         errStr = "printDataBlockettes (ERROR): incomplete miniSEED header found";
/* 1034 */         System.err.println(errStr);
/* 1035 */         System.exit(10);
/*      */       }
/* 1037 */       nextOffset = Steim.makeShort(this.head[(nextOffset + 2)], this.head[(nextOffset + 3)], this.byteSwappedRec);
/*      */     }
/*      */   }
/*      */   
/*      */   public int compareTo(Object obj) {
/* 1042 */     GenericMiniSeedRecord gmsrObj = (GenericMiniSeedRecord)obj;
/*      */     
/* 1044 */     if (!getChannel().equals(gmsrObj.getChannel()))
/* 1045 */       return -1;
/* 1046 */     if (getStartTime().after(gmsrObj.getStartTime()))
/* 1047 */       return 1;
/* 1048 */     if (getStartTime().before(gmsrObj.getStartTime())) {
/* 1049 */       return -1;
/*      */     }
/* 1051 */     return getChannel().compareTo(gmsrObj.getChannel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChannelMask getChannelMask()
/*      */   {
/* 1059 */     return this.chanMask;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRealTimePacket()
/*      */   {
/* 1068 */     int locID = 0;
/*      */     try {
/* 1070 */       locID = Integer.parseInt(getLocID());
/* 1071 */       if (locID < 50) {
/* 1072 */         return true;
/*      */       }
/* 1074 */       return false;
/*      */     }
/*      */     catch (NumberFormatException ex) {}
/* 1077 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isTriggerEventPacket()
/*      */   {
/* 1082 */     return !isRealTimePacket();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isStrongMotionPacket()
/*      */   {
/* 1089 */     char flag = getChannel().charAt(1);
/* 1090 */     return flag == 'A';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isChannelUD()
/*      */   {
/* 1097 */     char flag = getChannel().charAt(2);
/* 1098 */     return flag == 'Z';
/*      */   }
/*      */   
/*      */   public boolean isChannelNE() {
/* 1102 */     char flag = getChannel().charAt(2);
/* 1103 */     return flag == 'N';
/*      */   }
/*      */   
/*      */   public boolean isChannelEW() {
/* 1107 */     char flag = getChannel().charAt(2);
/* 1108 */     return flag == 'E';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String buildFlagString(byte flags)
/*      */   {
/* 1115 */     String flagStr = Integer.toBinaryString(flags);
/* 1116 */     int strLength = flagStr.length();
/* 1117 */     if (strLength < 8) {
/* 1118 */       int padLength = 8 - strLength;
/* 1119 */       String formatStr = "%" + padLength + "." + padLength + "d";
/* 1120 */       Format fmts = new Format(formatStr);
/* 1121 */       flagStr = fmts.format(0) + flagStr;
/*      */     }
/* 1123 */     return flagStr;
/*      */   }
/*      */   
/*      */   public static void testDecompress(String fname) {
/*      */     try {
/* 1128 */       BufferedInputStream bis = bis = new BufferedInputStream(new FileInputStream(fname));
/*      */       
/* 1130 */       bis.skip(0L);
/* 1131 */       int headLen = 64;
/* 1132 */       int dataLen = 448;
/* 1133 */       int recLen = 512;
/* 1134 */       byte[] aHead = new byte[headLen];
/* 1135 */       byte[] aData = new byte[dataLen];
/* 1136 */       bis.read(aHead, 0, headLen);
/* 1137 */       bis.read(aData, 0, dataLen);
/* 1138 */       GenericMiniSeedRecord gmsRec = buildMiniSeedRecord(aHead, aData, recLen);
/*      */       
/* 1140 */       int[] udata = new int[gmsRec.getNumSamples()];
/* 1141 */       if (gmsRec.decompress(udata)) {
/* 1142 */         System.out.println("OK");
/*      */       } else
/* 1144 */         System.out.println("Error");
/* 1145 */       System.out.print(gmsRec.getStation() + "/" + gmsRec.getChannel() + " ");
/* 1146 */       System.out.println(gmsRec.getStartTime());
/* 1147 */       for (int i = 0; i < udata.length; i++) {
/* 1148 */         System.out.println(udata[i]);
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 1151 */       ioEx.printStackTrace();
/*      */     } catch (MiniseedFormatException msfEx) {
/* 1153 */       msfEx.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void testDecompress1(String fname) {
/*      */     try {
/* 1159 */       BufferedInputStream bis = bis = new BufferedInputStream(new FileInputStream(fname));
/*      */       
/* 1161 */       bis.skip(0L);
/* 1162 */       int recLen = 512;
/* 1163 */       byte[] buf = new byte[recLen];
/*      */       
/*      */ 
/* 1166 */       long now_ms = new Date().getTime();
/* 1167 */       for (int i = 0; i < 25600; i++) {
/* 1168 */         bis.read(buf, 0, recLen);
/* 1169 */         GenericMiniSeedRecord gmsRec = buildMiniSeedRecord(buf);
/* 1170 */         long check_ms = gmsRec.getStartTime().getTime() / 1000L * 1000L + gmsRec.getStartTime().getNanos() / 1000000;
/*      */         
/*      */ 
/* 1173 */         int[] udata = new int[gmsRec.getNumSamples()];
/* 1174 */         if (gmsRec.decompress(udata)) {
/* 1175 */           System.out.println("OK");
/*      */         } else
/* 1177 */           System.out.println("Error");
/* 1178 */         System.out.print(gmsRec.getStation() + "/" + gmsRec.getChannel() + " ");
/* 1179 */         System.out.println(gmsRec.getStartTime());
/*      */         
/* 1181 */         for (int j = 0; j < udata.length; j++) {
/* 1182 */           System.out.println(udata[j]);
/*      */         }
/*      */       }
/*      */       
/* 1186 */       long n_now_ms = new Date().getTime();
/* 1187 */       System.err.println("Load one DECODE cycle elapse time: " + (n_now_ms - now_ms) + " ms.");
/*      */     } catch (IOException ioEx) {
/* 1189 */       ioEx.printStackTrace();
/*      */     } catch (Exception msfEx) {
/* 1191 */       msfEx.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/* 1195 */   static int indexOf(byte[] buffer, byte[] str, int fromIndex) { return indexOf(buffer, 0, buffer.length, str, 0, str.length, fromIndex); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int indexOf(byte[] source, int sourceOffset, int sourceCount, byte[] target, int targetOffset, int targetCount, int fromIndex)
/*      */   {
/* 1203 */     if (fromIndex >= sourceCount) {
/* 1204 */       return targetCount == 0 ? sourceCount : -1;
/*      */     }
/* 1206 */     if (fromIndex < 0) {
/* 1207 */       fromIndex = 0;
/*      */     }
/* 1209 */     if (targetCount == 0) {
/* 1210 */       return fromIndex;
/*      */     }
/*      */     
/* 1213 */     byte first = target[targetOffset];
/* 1214 */     int i = sourceOffset + fromIndex;
/* 1215 */     int max = sourceOffset + (sourceCount - targetCount);
/*      */     
/*      */     break label64;
/*      */     
/*      */     break label64;
/*      */     
/* 1221 */     i++;
/*      */     label64:
/*      */     int j;
/*      */     int end;
/*      */     int k;
/* 1220 */     if ((i > max) || (source[i] == first))
/*      */     {
/*      */ 
/* 1223 */       if (i > max) {
/* 1224 */         return -1;
/*      */       }
/*      */       
/*      */ 
/* 1228 */       j = i + 1;
/* 1229 */       end = j + targetCount - 1;
/* 1230 */       k = targetOffset + 1; }
/* 1231 */     while (j < end) {
/* 1232 */       if (source[(j++)] != target[(k++)]) {
/* 1233 */         i++;
/*      */         break label64;
/*      */         break;
/*      */       }
/*      */     }
/* 1238 */     return i - sourceOffset;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1248 */     byte[] str = { -86, -86, -86, -86, -86, -86, -86, -86, -86 };
/* 1249 */     byte[] buffer = { 97, -85, -86, -86, -84, -86, -86, -86, -86, -86, 98 };
/*      */     
/* 1251 */     int ret = indexOf(buffer, str, 0);
/* 1252 */     System.err.println(ret);
/* 1253 */     testDecompress1(args[0]);
/*      */   }
/*      */   
/*      */   private GenericMiniSeedRecord() {}
/*      */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/miniseedutils/steim/GenericMiniSeedRecord.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */