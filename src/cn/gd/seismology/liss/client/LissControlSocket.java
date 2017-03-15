/*     */ package cn.gd.seismology.liss.client;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class LissControlSocket
/*     */ {
/*  25 */   float version = -1.0F;
/*     */ 
/*  41 */   private Socket controlSock = null;
/*     */ 
/*  47 */   private boolean debugResponses = false;
/*     */ 
/*  52 */   private Writer writer = null;
/*     */ 
/*  58 */   private BufferedReader reader = null;
/*     */ 
/*  63 */   private String connectionInfo = "";
/*     */ 
/*  68 */   private int timeOut = 160000;
/*     */   
/*  73 */   private String remoteHostAddress = null;
/*     */   
/*  78 */   private int remotePort = -1;
/*     */   
/*  83 */   private String localHostAddress = null;
/*     */   
/*  88 */   private int localPort = -1;
/*     */   
/*     */   private static final int CONTROL_PORT = 5000;
/*     */   
/*     */   static final String EOL = "\r\n";
/*     */   public static final String NRT_DATA_TRANSPORT = "NRT";
/*     */   public static final String RT_DATA_TRANSPORT = "RT";
/*     */   
/*     */   public LissControlSocket(String remoteHost, int port)
/*     */     throws IOException, LissException
/*     */   {
/*  99 */     this.controlSock = new Socket(remoteHost, port);
/*     */     
/* 101 */     this.controlSock.setSoTimeout(this.timeOut);
/* 102 */     this.connectionInfo = (this.controlSock.getLocalAddress().getHostName() + ":" + this.controlSock.getLocalPort() + "->" + this.controlSock.getInetAddress().getHostName() + ":" + this.controlSock.getPort());
/*     */     
/* 106 */     this.connectionInfo = ("Established " + this.connectionInfo + " at " + new Date());
/*     */     
/* 108 */     initStreams();
/* 109 */     validateConnection();
/*     */   }
/*     */   
/*     */   public LissControlSocket(String remoteHost) throws IOException, LissException
/*     */   {
/* 114 */     this(remoteHost, 5000);
/*     */   }
/*     */   
/*     */   public LissControlSocket(InetAddress remoteAddr, int port)
/*     */     throws IOException, LissException
/*     */   {
/* 125 */     this.controlSock = new Socket(remoteAddr, port);
/* 126 */     this.controlSock.setSoTimeout(this.timeOut);
/* 127 */     initStreams();
/* 128 */     validateConnection();
/*     */   }
/*     */   
/*     */   public LissControlSocket(InetAddress remoteAddr) throws IOException, LissException
/*     */   {
/* 133 */     this(remoteAddr, 5000);
/*     */   }
/*     */   
/*     */   public void setTimeOut(int ms) {
/* 137 */     this.timeOut = ms;
/*     */   }
/*     */   
/*     */   public String getSocketConnectionInfo() {
/* 141 */     return this.connectionInfo;
/*     */   }
/*     */   
/*     */   private void validateConnection()
/*     */     throws IOException, LissException
/*     */   {
/* 150 */     LissResponse reply = readReply();
/* 151 */     validateReply(reply, "220");
/* 152 */     String hello = reply.getMessage();
/* 153 */     int idx = hello.indexOf("Version ");
/* 154 */     if (idx != -1) {
/*     */       try {
/* 156 */         String sver = hello.substring(idx + 8, idx + 11);
/* 157 */         this.version = Float.parseFloat(sver);
/*     */       }
/*     */       catch (Exception ex) {}
/*     */     }
/*     */   }
/*     */   
/*     */   private void initStreams()
/*     */     throws IOException
/*     */   {
/* 173 */     InputStream is = this.controlSock.getInputStream();
/* 174 */     this.reader = new BufferedReader(new InputStreamReader(is));
/*     */     
/*     */ 
/* 177 */     OutputStream os = this.controlSock.getOutputStream();
/* 178 */     this.writer = new OutputStreamWriter(os);
/*     */   }
/*     */   
/*     */   String getRemoteHostName()
/*     */   {
/* 188 */     InetAddress addr = this.controlSock.getInetAddress();
/* 189 */     return addr.getHostName();
/*     */   }
/*     */   
/*     */   public void logout()
/*     */     throws IOException
/*     */   {
/* 196 */     this.writer.close();
/* 197 */     this.reader.close();
/* 198 */     this.controlSock.close();
/*     */   }
/*     */   
/*     */   ServerSocket createDataServerSocket(String dataType) throws IOException, LissException
/*     */   {
/* 203 */     ServerSocket dataServerSocket = new ServerSocket(0);
/* 204 */     dataServerSocket.setSoTimeout(this.timeOut);
/* 205 */     StringBuffer command = new StringBuffer("PORT " + dataType + " ");
/* 206 */     String host = this.controlSock.getLocalAddress().getHostAddress();
/* 207 */     command.append(host.replace('.', ','));
/* 208 */     int port = dataServerSocket.getLocalPort();
/* 209 */     command.append(',');
/* 210 */     command.append(port / 256);
/* 211 */     command.append(',');
/* 212 */     command.append(port % 256);
/* 213 */     LissResponse reply = sendCommand(command.toString());
/* 214 */     validateReply(reply, "200");
/* 215 */     return dataServerSocket;
/*     */   }
/*     */   
/*     */   Socket createPassiveDataSocket(String dataType)
/*     */     throws IOException, LissException
/*     */   {
/* 230 */     LissResponse reply = sendCommand("PASV " + dataType + " ");
/* 231 */     String[] validCode1 = { "227", "228" };
/*     */     
/* 233 */     validateReply(reply, validCode1);
/*     */     
/* 243 */     String message = reply.getMessage();
/* 244 */     int bracket1 = message.indexOf('(');
/* 245 */     int bracket2 = message.indexOf(')');
/* 246 */     String ipData = message.substring(bracket1 + 1, bracket2);
/* 247 */     int[] parts = new int[6];
/*     */     
/* 249 */     int len = ipData.length();
/* 250 */     int partCount = 0;
/* 251 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 254 */     for (int i = 0; (i < len) && (partCount <= 6); i++)
/*     */     {
/* 256 */       char ch = ipData.charAt(i);
/* 257 */       if (Character.isDigit(ch)) {
/* 258 */         buf.append(ch);
/* 259 */       } else if (ch != ',') {
/* 260 */         throw new LissException("Malformed PASV reply: " + reply);
/*     */       }
/*     */       
/* 264 */       if ((ch == ',') || (i + 1 == len)) {
/*     */         try {
/* 266 */           parts[(partCount++)] = Integer.parseInt(buf.toString());
/* 267 */           buf.setLength(0);
/*     */         }
/*     */         catch (NumberFormatException ex) {
/* 270 */           throw new LissException("Malformed PASV reply: " + reply);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 277 */     String ipAddress = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
/*     */     
/* 281 */     int port = (parts[4] << 8) + parts[5];
/*     */     
/* 284 */     Socket dSocket = new Socket(ipAddress, port);
/* 285 */     dSocket.setSoTimeout(this.timeOut);
/* 286 */     return dSocket;
/*     */   }
/*     */   
/*     */   public LissResponse sendCommand(String command)
/*     */     throws IOException
/*     */   {
/* 298 */     this.writer.write(command + "\r\n");
/* 299 */     this.writer.flush();
/*     */     
/* 301 */     return readReply();
/*     */   }
/*     */   
/*     */   LissResponse readReply()
/*     */     throws IOException
/*     */   {
/* 315 */     return new LissResponse(this.reader);
/*     */   }
/*     */   
/*     */   public void validateReply(LissResponse reply, String expectedReplyCode)
/*     */     throws IOException, LissException
/*     */   {
/* 332 */     String replyCode = reply.getReturnCode();
/*     */     
/*     */ 
/* 335 */     if (!replyCode.equals(expectedReplyCode)) {
/* 336 */       throw new LissException(reply.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void validateReply(LissResponse reply, String[] expectedReplyCodes)
/*     */     throws IOException, LissException
/*     */   {
/* 354 */     String replyCode = reply.getReturnCode();
/*     */     
/* 356 */     for (int i = 0; i < expectedReplyCodes.length; i++) {
/* 357 */       if (replyCode.equals(expectedReplyCodes[i])) {
/* 358 */         return;
/*     */       }
/*     */     }
/* 361 */     throw new LissException(reply.getMessage());
/*     */   }
/*     */   
/*     */   void debugResponses(boolean on)
/*     */   {
/* 371 */     this.debugResponses = on;
/*     */   }
/*     */   
/*     */   public String getLocalHostAddress() {
/* 375 */     if (this.localHostAddress == null) {
/* 376 */       this.localHostAddress = this.controlSock.getLocalAddress().getHostAddress();
/*     */     }
/* 378 */     return this.localHostAddress;
/*     */   }
/*     */   
/*     */   public int getLocalHostPort() {
/* 382 */     if (this.localPort == -1) {
/* 383 */       this.localPort = this.controlSock.getLocalPort();
/*     */     }
/* 385 */     return this.localPort;
/*     */   }
/*     */   
/*     */   String getRemoteHostAddress()
/*     */   {
/* 390 */     if (this.remoteHostAddress == null) {
/* 391 */       this.remoteHostAddress = this.controlSock.getInetAddress().getHostAddress();
/*     */     }
/* 393 */     return this.remoteHostAddress;
/*     */   }
/*     */   
/*     */   int getRemoteHostPort() {
/* 397 */     if (this.remotePort == -1) {
/* 398 */       this.remotePort = this.controlSock.getPort();
/*     */     }
/* 400 */     return this.remotePort;
/*     */   }
/*     */   
/*     */   public String getDetailHeader() {
/* 404 */     String strUsr = "";
/* 405 */     return getLocalHostAddress() + ":" + getLocalHostPort() + "/" + getRemoteHostName() + ":" + getRemoteHostPort();
/*     */   }
/*     */ }

