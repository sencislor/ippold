/*     */ package cn.gd.seismology.realtimestream;
/*     */ 
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEvent;
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener;
/*     */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.SocketException;
/*     */ import java.util.Date;
/*     */ import java.util.Vector;
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
/*     */ public class UDPPort
/*     */   implements MiniSeedStreamInf
/*     */ {
/*  35 */   private DatagramSocket dgSocket = null;
/*  36 */   protected String serverName = "127.0.0.1";
/*  37 */   protected int seedPort = 5050;
/*  38 */   private String[] stationCode = null;
/*  39 */   private boolean bConnectOK = false;
/*     */   public static final String cvsid = "$Id: UDPPort.java,v 1.5 2003/08/09 02:37:03 hwh Exp $";
/*     */   
/*     */   public UDPPort(String newServerName, int newSeedPort, String[] stationCode) {
/*  43 */     this.serverName = newServerName;
/*  44 */     this.seedPort = newSeedPort;
/*  45 */     this.stationCode = stationCode;
/*  46 */     sendSMinieedData(); }
/*     */   
/*     */   private transient Vector miniSeedPortEventListeners;
/*     */   private Thread sendSeedDataThread;
/*  50 */   public String[] getStationCode() { return this.stationCode; }
/*     */   
/*     */ 
/*     */   public boolean accept(GenericMiniSeedRecord gmsr) {
/*  54 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void addMiniSeedPortEventListener(MiniSeedPortEventListener l) {
/*  58 */     Vector v = this.miniSeedPortEventListeners == null ? new Vector(2) : (Vector)this.miniSeedPortEventListeners.clone();
/*  59 */     if (!v.contains(l)) {
/*  60 */       v.addElement(l);
/*  61 */       this.miniSeedPortEventListeners = v;
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void removeMiniSeedPortEventListener(MiniSeedPortEventListener l) {
/*  66 */     if ((this.miniSeedPortEventListeners != null) && (this.miniSeedPortEventListeners.contains(l))) {
/*  67 */       Vector v = (Vector)this.miniSeedPortEventListeners.clone();
/*  68 */       v.removeElement(l);
/*  69 */       this.miniSeedPortEventListeners = v;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireMiniSeedEvent(MiniSeedPortEvent e) {
/*  74 */     if (this.miniSeedPortEventListeners != null) {
/*  75 */       Vector listeners = this.miniSeedPortEventListeners;
/*  76 */       int count = listeners.size();
/*  77 */       for (int i = 0; i < count; i++) {
/*  78 */         ((MiniSeedPortEventListener)listeners.elementAt(i)).miniSeedEvent(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void sendSMinieedData() {
/*  84 */     this.sendSeedDataThread = new Thread() {
/*     */       public void run() {
/*  86 */         UDPPort.this.connectServer();
/*  87 */         GenericMiniSeedRecord gmsRec = null;
/*     */         for (;;) {
/*  89 */           gmsRec = UDPPort.this.getCurrentMiniSeedData();
/*  90 */           if (gmsRec != null)
/*     */           {
/*  92 */             if (UDPPort.this.accept(gmsRec)) {
/*  93 */               MiniSeedPortEvent e = new MiniSeedPortEvent(new Object(), gmsRec);
/*  94 */               UDPPort.this.fireMiniSeedEvent(e);
/*     */             } }
/*     */         }
/*     */       }
/*  98 */     };
/*  99 */     this.sendSeedDataThread.setName("SendSeedDataThread");
/* 100 */     this.sendSeedDataThread.start();
/*     */   }
/*     */   
/*     */   protected boolean connectServer() {
/* 104 */     System.out.println(new Date() + "\t" + "connecting " + this.serverName + ":" + this.seedPort + " UDP... In UDP Port");
/*     */     try {
/* 106 */       InetAddress group = InetAddress.getByName(this.serverName);
/*     */       
/* 108 */       if (group.isMulticastAddress())
/*     */       {
/* 110 */         MulticastSocket s = new MulticastSocket(this.seedPort);
/* 111 */         s.joinGroup(group);
/* 112 */         this.dgSocket = s;
/* 113 */         System.err.println("MultiCast:\t" + this.serverName + ":" + this.seedPort);
/*     */       }
/*     */       else
/*     */       {
/* 117 */         this.dgSocket = new DatagramSocket(this.seedPort);
/* 118 */         this.dgSocket.setSoTimeout(120000);
/*     */       }
/* 120 */       this.bConnectOK = true;
/*     */     }
/*     */     catch (IOException ioEx)
/*     */     {
/* 124 */       System.err.println(new Date() + "\t" + "Failed I/O to " + this.serverName + ":" + this.seedPort + " : " + ioEx);
/*     */       
/*     */       try
/*     */       {
/* 128 */         Thread.sleep(60000L);
/*     */       } catch (InterruptedException iEx) {}
/* 130 */       this.bConnectOK = false;
/* 131 */       return false;
/*     */     }
/* 133 */     System.out.println(new Date() + "\t" + "Connected " + this.serverName + ":" + this.seedPort);
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   public void close() {
/* 138 */     if (this.dgSocket != null) {
/* 139 */       this.dgSocket.disconnect();
/* 140 */       this.dgSocket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private GenericMiniSeedRecord getCurrentMiniSeedData() {
/* 145 */     if (!this.bConnectOK) {
/*     */       try {
/* 147 */         Thread.sleep(1000L);
/*     */       }
/*     */       catch (InterruptedException iEx) {}
/* 150 */       if (!connectServer())
/* 151 */         return null;
/*     */     }
/* 153 */     GenericMiniSeedRecord gmsr = null;
/*     */     try {
/* 155 */       int blkSize = 512;
/* 156 */       byte[] buf = new byte[blkSize];
/* 157 */       DatagramPacket dp = new DatagramPacket(buf, blkSize);
/* 158 */       this.dgSocket.receive(dp);
/* 159 */       gmsr = GenericMiniSeedRecord.buildMiniSeedRecord(buf);
/*     */     } catch (SocketException sEx) {
/* 161 */       sEx.printStackTrace();
/* 162 */       close();
/*     */       try {
/* 164 */         Thread.sleep(5000L);
/*     */       } catch (InterruptedException iEx) {}
/* 166 */       this.bConnectOK = false;
/* 167 */       return null;
/*     */     } catch (InterruptedIOException iioEx) {
/* 169 */       close();
/*     */       try {
/* 171 */         Thread.sleep(120000L);
/*     */       } catch (InterruptedException iEx) {}
/* 173 */       this.bConnectOK = false;
/* 174 */       return null;
/*     */     } catch (IOException ioEx) {
/* 176 */       close();
/* 177 */       System.err.println("IOException");
/* 178 */       ioEx.printStackTrace();
/* 179 */       this.bConnectOK = false;
/* 180 */       return null;
/*     */     }
/*     */     
/* 183 */     if (gmsr == null)
/* 184 */       this.bConnectOK = false;
/* 185 */     if (gmsr.getSampleRate() != 0.0D) {
/*     */       try {
/* 187 */         gmsr.decompress();
/*     */       } catch (Exception ex) {
/* 189 */         ex.printStackTrace();
/*     */         try {
/* 191 */           FileOutputStream fos = new FileOutputStream("decompressErr.bin");
/* 192 */           fos.write(gmsr.getBytes());
/* 193 */           fos.flush();
/* 194 */           fos.close();
/*     */         } catch (IOException ioex1) {
/* 196 */           ioex1.printStackTrace();
/*     */         }
/* 198 */         return null;
/*     */       }
/* 200 */       return gmsr;
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 206 */     UDPPort UDPPort1 = new UDPPort("127.0.0.1", 5050, new String[1]);
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimestream/UDPPort.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */