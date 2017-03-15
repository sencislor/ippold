/*     */ package cn.gd.seismology.liss.message;
/*     */ 
/*     */ import cn.gd.convert.CStringToJava;
/*     */ import cn.gd.io.LittleEndianInputStream;
/*     */ import cn.gd.io.LittleEndianOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Result
/*     */ {
/*  35 */   static int MAX_MESSAGE_LENGTH = 1048576;
/*     */ 
/*  47 */   private static final String[] PRIORITY_NAME = { "EMERG", "ALERT", "CRIT", "ERR", "WARNING", "NOTICE", "INFO", "DEBUG" };
/*     */   
/*  75 */   protected byte[] bytes = null;
/*  80 */   protected int wordOrder = -1;
/*  90 */   protected static final byte[] reserved1 = new byte[2];
/* 123 */   protected static final byte[] reserved2 = new byte[24];
/*     */   
/*     */   static final int HEAD_LENGTH = 96;
/*     */   public static final int LOCALTION_CATA = 65540;
/*     */   public static final int LOCALTION_IAS = 65538;
/*     */   public static final int LOCALTION_REAL = 65537;
/*     */   
/*     */   public Result(Result ares)
/*     */   {
/* 132 */     this.bytes = ares.bytes;
/* 133 */     this.wordOrder = ares.wordOrder;
/* 134 */     this.priority = ares.priority;
/* 135 */     this.length = ares.length;
/* 136 */     this.subType = ares.subType;
/* 137 */     this.networkID = ares.networkID;
/* 138 */     this.nodeID = ares.nodeID;
/* 139 */     this.netFlag = ares.netFlag;
/* 140 */     this.msgTime = ares.msgTime;
/*     */   }
/*     */   
/*     */   public byte[] getBytes()
/*     */   {
/* 147 */     return this.bytes;
/*     */   }
/*     */   
/*     */   public byte[] getData()
/*     */   {
/* 154 */     byte[] data = new byte[this.bytes.length - 96];
/* 155 */     System.arraycopy(this.bytes, 96, data, 0, data.length);
/* 156 */     return data;
/*     */   }
/*     */   
/*     */   public int getLength()
/*     */   {
/* 163 */     return this.length;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 170 */     return this.subType;
/*     */   }
/*     */   
/*     */   public static final int LOCALTION_TEST = 65544;
/*     */   public static final int LOCALTION_XML = 65568;
/*     */   public static final int MAIN_TYPE_DYNAMICS = 262144;
/*     */   public static final int MAIN_TYPE_INTENSITY = 524288;
/*     */   public int getPriority() {
/* 178 */     return this.priority;
/*     */   }
/*     */   
/*     */   public String getNetworkID()
/*     */   {
/* 185 */     return this.networkID;
/*     */   }
/*     */ 
/*     */   public String getNodeID()
/*     */   {
/* 192 */     return this.nodeID;
/*     */   }
/*     */ 
/*     */   public int getNetFlag()
/*     */   {
/* 199 */     return this.netFlag;
/*     */   }
/*     */   
/*     */   public Date getMsgTime()
/*     */   {
/* 206 */     return this.msgTime;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 210 */     StringBuffer sbuf = new StringBuffer();
/* 211 */     sbuf.append("[word order: \t" + (this.wordOrder == 0 ? "Intel\r\n" : "SPARC\r\n"));
/* 212 */     sbuf.append(" Frame Length: \t" + this.length + "\r\n");
/* 213 */     sbuf.append(" priority: \t" + PRIORITY_NAME[this.priority] + "\r\n");
/* 214 */     sbuf.append(" Info Type: \t0x" + Integer.toHexString(this.subType) + "\r\n");
/* 215 */     sbuf.append(" network ID: \t" + this.networkID + "\r\n");
/* 216 */     sbuf.append(" Node ID: \t" + this.nodeID + "\r\n");
/* 217 */     sbuf.append(" NetFlag: \t" + (this.netFlag == 0 ? "Native\r\n" : "OutSide\r\n"));
/* 218 */     sbuf.append(" Msg Time: \t" + this.msgTime + " ]\r\n");
/*     */     
/* 220 */     return sbuf.substring(0);
/*     */   }
/*     */   
/*     */   public static final int MAIN_TYPE_LOCALTION = 65536;
/*     */   public static final int MAIN_TYPE_MONITOR = 131072;
/*     */   public static final int PRIORITY_ALERT = 1;
/*     */   public static final int PRIORITY_CRIT = 2;
/*     */   
/*     */   public static Result buildResult(boolean bIntel, int priority, int type, String networkID, String nodeID, int netFlag, Date msgTime, byte[] data) {
/* 229 */     Result res = new Result();
/* 230 */     if (bIntel) {
/* 231 */       res.wordOrder = 0;
/*     */     } else {
/* 233 */       res.wordOrder = 1;
/*     */     }
/* 235 */     res.length = (96 + data.length - 4 - 4);
/* 236 */     res.priority = priority;
/* 237 */     if ((priority < 0) || (priority > 8)) {
/* 238 */       priority = 7;
/*     */     }
/* 240 */     res.subType = type;
/* 241 */     res.networkID = networkID;
/* 242 */     res.nodeID = nodeID;
/* 243 */     res.netFlag = netFlag;
/* 244 */     res.msgTime = msgTime;
/* 245 */     ByteArrayOutputStream bos = null;
/* 246 */     DataOutputStream dos = null;
/* 247 */     LittleEndianOutputStream leis = null;
/*     */     try {
/* 249 */       bos = new ByteArrayOutputStream(96 + data.length);
/* 250 */       if (bIntel) {
/* 251 */         leis = new LittleEndianOutputStream(bos);
/* 252 */         leis.writeByte(res.wordOrder);
/* 253 */         leis.writeByte(res.priority);
/* 254 */         leis.write(reserved1);
/* 255 */         leis.writeInt(res.length);
/* 256 */         leis.writeInt(res.subType);
/* 257 */         leis.write(CStringToJava.pad(res.networkID, 16, '\000').getBytes());
/*     */         
/* 259 */         leis.write(CStringToJava.pad(res.nodeID, 32, '\000').getBytes());
/*     */         
/* 261 */         leis.writeInt(res.netFlag);
/* 262 */         leis.writeLong(res.msgTime.getTime());
/* 263 */         leis.write(reserved2);
/* 264 */         leis.write(data);
/* 265 */         leis.close();
/* 266 */         bos.close();
/* 267 */         res.bytes = bos.toByteArray();
/*     */       }
/*     */       else {
/* 270 */         dos = new DataOutputStream(bos);
/* 271 */         dos.writeByte(res.wordOrder);
/* 272 */         dos.writeByte(res.priority);
/* 273 */         dos.write(reserved1);
/* 274 */         dos.writeInt(res.length);
/* 275 */         dos.writeInt(res.subType);
/* 276 */         dos.write(CStringToJava.pad(res.networkID, 16, '\000').getBytes());
/*     */         
/* 278 */         dos.write(CStringToJava.pad(res.nodeID, 32, '\000').getBytes());
/*     */         
/* 280 */         dos.writeInt(res.netFlag);
/* 281 */         dos.writeLong(res.msgTime.getTime());
/* 282 */         dos.write(reserved2);
/* 283 */         dos.write(data);
/* 284 */         dos.close();
/* 285 */         bos.close();
/* 286 */         res.bytes = bos.toByteArray();
/*     */       }
/*     */     }
/*     */     catch (IOException ioEx) {
/* 290 */       ioEx.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       try {
/* 294 */         if (dos != null)
/* 295 */           dos.close();
/* 296 */         if (leis != null)
/* 297 */           leis.close();
/* 298 */         if (bos != null) {
/* 299 */           bos.close();
/*     */         }
/*     */       } catch (IOException ioEx) {}
/*     */     }
/* 303 */     return res;
/*     */   }
/*     */   
/*     */   public static final int PRIORITY_DEBUG = 7;
/*     */   public static final int PRIORITY_EMERG = 0;
/*     */   public static final int PRIORITY_ERR = 3;
/*     */   public static final int PRIORITY_INFO = 6;
/*     */   public static final int PRIORITY_NOTICE = 5;
/*     */   public static final int PRIORITY_WARNING = 4;
/*     */   
/*     */   public static Result buildResult(InputStream in) throws IOException {
/* 314 */     Result res = new Result();
/* 315 */     res.wordOrder = in.read();
/* 316 */     if (res.wordOrder == -1)
/* 317 */       throw new EOFException();
/* 318 */     res.priority = in.read();
/* 319 */     if ((res.priority < 0) || (res.priority > 7))
/* 320 */       res.priority = 7;
/* 321 */     in.skip(2L);
/* 322 */     if (res.wordOrder == 0) {
/* 323 */       LittleEndianInputStream leis = new LittleEndianInputStream(in);
/* 324 */       res.length = leis.readInt();
/* 325 */       if (res.length > MAX_MESSAGE_LENGTH)
/* 326 */         res.length = MAX_MESSAGE_LENGTH;
/* 327 */       if (res.length < 96)
/* 328 */         return null;
/* 329 */       res.bytes = new byte[8 + res.length];
/* 330 */       res.bytes[0] = 0;
/* 331 */       res.bytes[1] = ((byte)res.priority);
/* 332 */       res.bytes[4] = ((byte)(res.length >>> 0 & 0xFF));
/* 333 */       res.bytes[5] = ((byte)(res.length >>> 8 & 0xFF));
/* 334 */       res.bytes[6] = ((byte)(res.length >>> 16 & 0xFF));
/* 335 */       res.bytes[7] = ((byte)(res.length >>> 24 & 0xFF));
/* 336 */       readAll(leis, res.bytes, 8, res.length);
/* 337 */       leis = new LittleEndianInputStream(new ByteArrayInputStream(res.bytes, 8, res.length));
/*     */       
/* 339 */       res.subType = leis.readInt();
/* 340 */       byte[] tmp = new byte[16];
/* 341 */       leis.read(tmp);
/* 342 */       res.networkID = new String(tmp).trim();
/* 343 */       tmp = new byte[32];
/* 344 */       leis.read(tmp);
/* 345 */       res.nodeID = new String(tmp).trim();
/* 346 */       res.netFlag = leis.readInt();
/* 347 */       res.msgTime = new Date(leis.readLong());
/* 348 */       leis = null;
/* 349 */       tmp = null;
/*     */     }
/*     */     else {
/* 352 */       DataInputStream dis = new DataInputStream(in);
/* 353 */       res.length = dis.readInt();
/* 354 */       if (res.length > MAX_MESSAGE_LENGTH)
/* 355 */         res.length = MAX_MESSAGE_LENGTH;
/* 356 */       if (res.length < 96)
/* 357 */         return null;
/* 358 */       res.bytes = new byte[8 + res.length];
/* 359 */       res.bytes[0] = 1;
/* 360 */       res.bytes[1] = ((byte)res.priority);
/* 361 */       res.bytes[4] = ((byte)(res.length >>> 24 & 0xFF));
/* 362 */       res.bytes[5] = ((byte)(res.length >>> 16 & 0xFF));
/* 363 */       res.bytes[6] = ((byte)(res.length >>> 8 & 0xFF));
/* 364 */       res.bytes[7] = ((byte)(res.length >>> 0 & 0xFF));
/* 365 */       readAll(dis, res.bytes, 8, res.length);
/* 366 */       dis = new DataInputStream(new ByteArrayInputStream(res.bytes, 8, res.length));
/*     */       
/* 368 */       res.subType = dis.readInt();
/* 369 */       byte[] tmp = new byte[16];
/* 370 */       dis.read(tmp);
/* 371 */       res.networkID = new String(tmp).trim();
/* 372 */       tmp = new byte[32];
/* 373 */       dis.read(tmp);
/* 374 */       res.nodeID = new String(tmp).trim();
/* 375 */       res.netFlag = dis.readInt();
/* 376 */       res.msgTime = new Date(dis.readLong());
/* 377 */       dis = null;
/* 378 */       tmp = null;
/*     */     }
/* 380 */     return res; }
/*     */   
/*     */   public static final int TYPE_CONNECTION_STATUS = 131077;
/*     */   public static final int TYPE_DISK_SPACE = 131073;
/*     */   
/* 385 */   private static void readAll(InputStream in, byte[] buf, int indx, int length) throws IOException { int idx = indx;
/* 386 */     int len = length;
/* 387 */     int nRead = 0;
/* 388 */     while ((nRead = in.read(buf, idx, len)) > 0) {
/* 389 */       idx += nRead;
/* 390 */       len -= nRead;
/* 391 */       if (len == 0)
/* 392 */         return;
/*     */     }
/* 394 */     throw new EOFException();
/*     */   }
/*     */   
/*     */   public static final int TYPE_LOG = 131328;
/*     */   public static final int TYPE_MEMORY_STATUS = 131075;
/*     */   public static final int TYPE_NETWORK_STATUS = 131076;
/*     */   public static final int TYPE_SMS = 131078;
/*     */   public static final int TYPE_SUBSYSTEM = 131200;
/*     */   public static final int TYPE_WAVEFORM_STREAM_STATUS = 131074;
/*     */   protected int length;
/*     */   protected Date msgTime;
/*     */   protected int netFlag;
/*     */   protected String networkID;
/*     */   protected String nodeID;
/*     */   protected int priority;
/*     */   protected int subType;
/*     */   private Result() {}
/*     */ }
