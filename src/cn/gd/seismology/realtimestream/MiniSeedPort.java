/*     */ package cn.gd.seismology.realtimestream;
/*     */ 
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEvent;
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener;
/*     */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*     */ import edu.iris.miniseedutils.steim.MiniseedFormatException;
/*     */ import edu.iris.miniseedutils.steim.UnrecognizedBlocketteException;
/*     */ import edu.iris.miniseedutils.steim.UnrecognizedFormatException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.util.Date;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ public abstract class MiniSeedPort
/*     */   implements MiniSeedStreamInf
/*     */ {
/*  24 */   private boolean bdebug = true;
/*  25 */   protected String serverName = "127.0.0.1";
/*  26 */   protected int seedPort = 5000;
/*     */   
/*  28 */   protected Thread sendSeedDataThread = null;
/*  29 */   protected Socket socket = null;
/*  30 */   protected InputStream receiveFromLISS = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   private byte[] buf = new byte[' '];
/*  36 */   private byte[] dummy = new byte['Ȁ'];
/*     */   
/*  38 */   private int trIdx = 0;
/*     */   public static final String cvsid = "$Id: MiniSeedPort.java,v 1.10 2004/02/08 23:12:08 hwh Exp $";
/*     */   
/*     */   public MiniSeedPort(String newServerName, int newSeedPort) {
/*  42 */     this.serverName = newServerName;
/*  43 */     this.seedPort = newSeedPort;
/*     */   }
/*     */   
/*     */   public boolean accept(GenericMiniSeedRecord gmsr) {
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   public void setDebug(boolean bdebug) {
/*  51 */     this.bdebug = bdebug;
/*     */   }
/*     */   
/*     */   private String insertDebugString() {
/*  55 */     if (this.bdebug) {
/*  56 */       return this.serverName + ":" + this.seedPort + "\t" + new Date() + "\t";
/*     */     }
/*  58 */     return "";
/*     */   }
/*     */   
/*     */   public synchronized void addMiniSeedPortEventListener(MiniSeedPortEventListener l) {
/*  62 */     Vector v = this.miniSeedPortEventListeners == null ? new Vector(2) : (Vector)this.miniSeedPortEventListeners.clone();
/*  63 */     if (!v.contains(l)) {
/*  64 */       v.addElement(l);
/*  65 */       this.miniSeedPortEventListeners = v;
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void removeMiniSeedPortEventListener(MiniSeedPortEventListener l) {
/*  70 */     if ((this.miniSeedPortEventListeners != null) && (this.miniSeedPortEventListeners.contains(l))) {
/*  71 */       Vector v = (Vector)this.miniSeedPortEventListeners.clone();
/*  72 */       v.removeElement(l);
/*  73 */       this.miniSeedPortEventListeners = v;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireMiniSeedEvent(MiniSeedPortEvent e) {
/*  78 */     if (this.miniSeedPortEventListeners != null) {
/*  79 */       Vector listeners = this.miniSeedPortEventListeners;
/*  80 */       int count = listeners.size();
/*  81 */       for (int i = 0; i < count; i++) {
/*  82 */         ((MiniSeedPortEventListener)listeners.elementAt(i)).miniSeedEvent(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable
/*     */   {
/*  89 */     super.finalize();
/*  90 */     close();
/*     */   }
/*     */   
/*     */ 
/*     */   private transient Vector miniSeedPortEventListeners;
/*     */   protected MiniSeedPort thisPort;
/*     */   public void close()
/*     */   {
/*  98 */     this.trIdx = 0;
/*     */     try {
/* 100 */       if (this.receiveFromLISS != null)
/* 101 */         this.receiveFromLISS.close();
/* 102 */       this.receiveFromLISS = null;
/* 103 */       if (this.socket != null)
/* 104 */         this.socket.close();
/* 105 */       this.socket = null;
/*     */     }
/*     */     catch (IOException ioEx) {
/* 108 */       ioEx.printStackTrace();
/* 109 */       System.err.println(insertDebugString() + "Failed I/O to " + this.serverName + " : " + ioEx + " in this.close()");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void sendSMinieedData() {
/* 114 */     this.sendSeedDataThread = new Thread() {
/*     */       public void run() {
/* 116 */         MiniSeedPort.this.connectServer();
/* 117 */         GenericMiniSeedRecord gmsRec = null;
/*     */         for (;;) {
/* 119 */           gmsRec = MiniSeedPort.this.getCurrentMiniSeedData();
/* 120 */           if (gmsRec != null)
/*     */           {
/* 122 */             if (MiniSeedPort.this.accept(gmsRec)) {
/* 123 */               MiniSeedPortEvent e = new MiniSeedPortEvent(MiniSeedPort.this.thisPort, gmsRec);
/* 124 */               MiniSeedPort.this.fireMiniSeedEvent(e);
/*     */             } }
/*     */         }
/*     */       }
/* 128 */     };
/* 129 */     this.sendSeedDataThread.setName("SendSeedDataThread");
/* 130 */     this.sendSeedDataThread.start();
/*     */   }
/*     */   
/*     */   private boolean connectOK() {
/* 134 */     return this.receiveFromLISS != null;
/*     */   }
/*     */   
/*     */   public int indexOf(byte[] buffer, byte[] str, int fromIndex) {
/* 138 */     return indexOf(buffer, 0, buffer.length, str, 0, str.length, fromIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static int indexOf(byte[] source, int sourceOffset, int sourceCount, byte[] target, int targetOffset, int targetCount, int fromIndex)
/*     */   {
/* 146 */     if (fromIndex >= sourceCount) {
/* 147 */       return targetCount == 0 ? sourceCount : -1;
/*     */     }
/* 149 */     if (fromIndex < 0) {
/* 150 */       fromIndex = 0;
/*     */     }
/* 152 */     if (targetCount == 0) {
/* 153 */       return fromIndex;
/*     */     }
/*     */     
/* 156 */     byte first = target[targetOffset];
/* 157 */     int i = sourceOffset + fromIndex;
/* 158 */     int max = sourceOffset + (sourceCount - targetCount);
/*     */     
/*     */     break label64;
/*     */     
/*     */     break label64;
/*     */     
/* 164 */     i++;
/*     */     label64:
/*     */     int j;
/*     */     int end;
/*     */     int k;
/* 163 */     if ((i > max) || (source[i] == first))
/*     */     {
/*     */ 
/* 166 */       if (i > max) {
/* 167 */         return -1;
/*     */       }
/*     */       
/*     */ 
/* 171 */       j = i + 1;
/* 172 */       end = j + targetCount - 1;
/* 173 */       k = targetOffset + 1; }
/* 174 */     while (j < end) {
/* 175 */       if (source[(j++)] != target[(k++)]) {
/* 176 */         i++;
/*     */         break label64;
/*     */         break;
/*     */       }
/*     */     }
/* 181 */     return i - sourceOffset;
/*     */   }
/*     */   
/*     */   private GenericMiniSeedRecord getCurrentMiniSeedData()
/*     */   {
/* 186 */     if (!connectOK()) {
/*     */       try {
/* 188 */         Thread.sleep(30000L);
/*     */       }
/*     */       catch (InterruptedException iEx) {}
/* 191 */       if (!connectServer())
/* 192 */         return null;
/*     */     }
/* 194 */     int blkSize = 512;
/*     */     
/* 196 */     int realRecSize = 0;
/* 197 */     GenericMiniSeedRecord gmsr = null;
/* 198 */     byte[] aaaa = { -86, -86, -86, -86, -86, -86, -86, -86 };
/*     */     try {
/* 200 */       while (this.trIdx < blkSize) {
/* 201 */         realRecSize = this.receiveFromLISS.read(this.buf, this.trIdx, blkSize);
/* 202 */         if (realRecSize == -1) {
/* 203 */           System.err.println(new Date() + "\t" + "God, reach the end of Stream");
/* 204 */           throw new SocketException("God, reach the end of Stream");
/*     */         }
/* 206 */         this.trIdx += realRecSize;
/*     */       }
/* 208 */       int kkk = indexOf(this.buf, aaaa, 0);
/* 209 */       if (kkk != -1) {
/* 210 */         System.err.println("got aaaa string at: " + kkk);
/* 211 */         writeBadBlock();
/*     */       }
/*     */       try
/*     */       {
/* 215 */         if (GenericMiniSeedRecord.getRecordLength(this.buf) == -1)
/* 216 */           throw new UnrecognizedBlocketteException("Data only Blockette[1000] NOT FOUND");
/*     */       } catch (MiniseedFormatException msfEx) {
/* 218 */         System.err.println(insertDebugString() + "MiniseedFormatException");
/* 219 */         msfEx.printStackTrace();
/* 220 */         System.out.println();
/* 221 */         writeBadBlock();
/* 222 */         System.arraycopy(this.buf, 0, this.dummy, 0, blkSize);
/* 223 */         this.trIdx -= blkSize;
/* 224 */         System.arraycopy(this.buf, blkSize, this.buf, 0, this.trIdx);
/* 225 */         return null;
/*     */       } catch (UnrecognizedBlocketteException ubEx) {
/* 227 */         System.err.println(insertDebugString() + "UnrecognizedBlocketteException");
/* 228 */         ubEx.printStackTrace();
/* 229 */         writeBadBlock();
/* 230 */         System.arraycopy(this.buf, 0, this.dummy, 0, blkSize);
/* 231 */         this.trIdx -= blkSize;
/* 232 */         System.arraycopy(this.buf, blkSize, this.buf, 0, this.trIdx);
/* 233 */         return null;
/*     */       } catch (UnrecognizedFormatException ufEx) {
/* 235 */         System.err.println(insertDebugString() + "UnrecognizedFormatException");
/* 236 */         ufEx.printStackTrace();
/* 237 */         writeBadBlock();
/* 238 */         System.arraycopy(this.buf, 0, this.dummy, 0, blkSize);
/* 239 */         this.trIdx -= blkSize;
/* 240 */         System.arraycopy(this.buf, blkSize, this.buf, 0, this.trIdx);
/* 241 */         return null;
/*     */       } catch (Exception ex1) {
/* 243 */         System.err.println(insertDebugString() + "Exception");
/* 244 */         ex1.printStackTrace();
/* 245 */         writeBadBlock();
/* 246 */         System.arraycopy(this.buf, 0, this.dummy, 0, blkSize);
/* 247 */         this.trIdx -= blkSize;
/* 248 */         System.arraycopy(this.buf, blkSize, this.buf, 0, this.trIdx);
/* 249 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 262 */         gmsr = GenericMiniSeedRecord.buildMiniSeedRecord(this.buf);
/*     */       } catch (Exception ex) {
/* 264 */         System.out.println(insertDebugString() + "Exception");
/* 265 */         ex.printStackTrace();
/* 266 */         close();
/* 267 */         writeBadBlock();
/* 268 */         return null;
/*     */       }
/*     */       
/* 271 */       if (gmsr == null) {
/* 272 */         System.err.println(insertDebugString() + "Get Null miniseed.");
/* 273 */         writeBadBlock();
/*     */       }
/*     */       
/* 276 */       System.arraycopy(this.buf, 0, this.dummy, 0, blkSize);
/* 277 */       this.trIdx -= blkSize;
/* 278 */       System.arraycopy(this.buf, blkSize, this.buf, 0, this.trIdx);
/*     */     }
/*     */     catch (SocketException sEx) {
/* 281 */       close();
/*     */       try {
/* 283 */         Thread.sleep(5000L);
/*     */       } catch (InterruptedException iEx) {}
/* 285 */       return null;
/*     */     } catch (InterruptedIOException iioEx) {
/* 287 */       System.err.println(insertDebugString() + "InterruptedIOException in MiniSeedPort.GenericMiniSeedRecord");
/* 288 */       close();
/*     */       try {
/* 290 */         Thread.sleep(240000L);
/*     */       } catch (InterruptedException iEx) {}
/* 292 */       return null;
/*     */     } catch (IOException ioEx) {
/* 294 */       close();
/* 295 */       System.err.println("IOException");
/* 296 */       ioEx.printStackTrace();
/*     */     } catch (Exception ex) {
/* 298 */       System.out.println(insertDebugString() + ex);
/* 299 */       System.err.println("trIdx: " + this.trIdx);
/* 300 */       System.err.println("blkSize: " + blkSize);
/* 301 */       close();
/* 302 */       ex.printStackTrace();
/*     */     }
/*     */     
/* 305 */     if (gmsr == null) {
/* 306 */       return null;
/*     */     }
/* 308 */     if (gmsr.getSampleRate() != 0.0D) {
/*     */       try {
/* 310 */         gmsr.decompress();
/*     */       } catch (Exception ex) {
/* 312 */         ex.printStackTrace();
/* 313 */         writeBadBlock();
/* 314 */         return null;
/*     */       }
/* 316 */       return gmsr;
/*     */     }
/* 318 */     return null;
/*     */   }
/*     */   
/*     */   private void writeBadBlock()
/*     */   {
/*     */     try {
/* 324 */       FileOutputStream fos = new FileOutputStream("buildMiniSeedRecordErr.bin", true);
/* 325 */       fos.write(this.dummy);
/* 326 */       fos.write(this.buf);
/* 327 */       fos.flush();
/* 328 */       fos.close();
/*     */     } catch (IOException ioex1) {
/* 330 */       ioex1.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract boolean connectServer();
/*     */   
/*     */   public abstract String[] getStationCode();
/*     */ }
