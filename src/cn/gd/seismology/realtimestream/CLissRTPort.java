/*     */ package cn.gd.seismology.realtimestream;
/*     */ 
/*     */ import cn.gd.seismology.liss.client.LissClient;
/*     */ import cn.gd.seismology.liss.client.LissException;
/*     */ import cn.gd.seismology.liss.client.LissTransferType;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ public class CLissRTPort extends MiniSeedPort
/*     */ {
/*     */   public static final String cvsid = "$Id: CLissRTPort.java,v 1.6 2002/01/14 08:20:38 hwh Exp $";
/*  50 */   LissClient lissClient = null;
/*     */   private String passwd;
/*     */   private String[] stnCode;
/*     */   private String type;
/*     */   private String user;
/*     */   
/*     */   public CLissRTPort(String newServerName, int newSeedPort, String user, String passwd, String type, String[] stnCode)
/*     */   {
/*  58 */     super(newServerName, newSeedPort);
/*  59 */     this.user = user;
/*  60 */     this.passwd = passwd;
/*  61 */     this.type = type;
/*  62 */     this.stnCode = stnCode;
/*  63 */     this.thisPort = this;
/*  64 */     super.sendSMinieedData();
/*     */   }
/*     */   
/*     */   public CLissRTPort(String newServerName, int newSeedPort, String user, String passwd, String[] stnCode)
/*     */   {
/*  69 */     super(newServerName, newSeedPort);
/*  70 */     this.user = user;
/*  71 */     this.passwd = passwd;
/*  72 */     this.stnCode = stnCode;
/*  73 */     this.type = "";
/*  74 */     this.thisPort = this;
/*  75 */     super.sendSMinieedData();
/*     */   }
/*     */   
/*     */   public String[] getStationCode() {
/*  79 */     return this.stnCode;
/*     */   }
/*     */   
/*     */   public void close() {
/*  83 */     super.close();
/*     */     try {
/*  85 */       if (this.lissClient != null) {
/*  86 */         this.lissClient.quit();
/*     */       }
/*     */     }
/*     */     catch (IOException ioEx) {
/*  90 */       ioEx.printStackTrace();
/*     */     }
/*     */     catch (LissException clEx) {
/*  93 */       clEx.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean connectServer() {
/*  98 */     System.out.println("connectServer... In MiniSeedPort");
/*     */     try {
/* 100 */       this.lissClient = new LissClient(this.serverName, this.seedPort);
/* 101 */       this.lissClient.login(this.user, this.passwd);
/* 102 */       this.lissClient.setType(LissTransferType.BINARY);
/* 103 */       this.lissClient.setRTServerPassiveMode(true);
/* 104 */       this.receiveFromLISS = this.lissClient.retrieveRealTimeStream(this.type, this.stnCode);
/*     */     }
/*     */     catch (LissException lissEx) {
/* 107 */       System.err.println(lissEx);
/* 108 */       return false;
/*     */     }
/*     */     catch (UnknownHostException uhEx) {
/* 111 */       System.err.println("Unknown host " + this.serverName + " : " + uhEx);
/*     */       
/*     */       try
/*     */       {
/* 115 */         Thread.sleep(60000L);
/*     */       } catch (InterruptedException iEx) {}
/* 117 */       return false;
/*     */     }
/*     */     catch (InterruptedIOException iioEx)
/*     */     {
/* 121 */       System.err.println("InterruptedIOException: " + iioEx);
/*     */       try {
/* 123 */         Thread.sleep(60000L);
/*     */       } catch (InterruptedException iEx) {}
/* 125 */       return false;
/*     */     }
/*     */     catch (IOException ioEx) {
/* 128 */       System.err.println("Failed I/O to " + this.serverName + " : " + ioEx);
/*     */       
/*     */       try
/*     */       {
/* 132 */         Thread.sleep(60000L);
/*     */       } catch (InterruptedException iEx) {}
/* 134 */       return false;
/*     */     }
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 140 */     String[] stnCode = { "SHT", "SHZ" };
/* 141 */     CLissRTPort CLissRTPort1 = new CLissRTPort("localhost", 5000, "root", "liss", stnCode);
/*     */   }
/*     */ }
