/*      */ package cn.gd.seismology.liss.client;
/*      */ 
/*      */ import cn.gd.seismology.liss.message.Result;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.net.InetAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class LissClient
/*      */ {
/*   33 */   private static String cvsId = "$Id: LissClient.java,v 1.15 2004/03/30 15:30:08 hwh Exp $";
/*      */ 
/*   40 */   private LissControlSocket control = null;
/*   46 */   private Socket data = null;
/*   51 */   private Socket rtData = null;
/*   56 */   private LissTransferType transferType = null;
/*   61 */   private ServerSocket rtDataServerSocket = null;
/*   62 */   private ServerSocket dataServerSocket = null;
/*   63 */   private boolean bRTServerPassiveMode = true;
/*   64 */   private boolean bServerPassiveMode = true;
/*   69 */   private int timeOut = 120000;
/*      */ 
/*      */   public LissClient(String remoteHost, int port)
/*      */     throws IOException, LissException
/*      */   {
/*   79 */     this.control = new LissControlSocket(remoteHost, port);
/*      */   }
/*      */   
/*      */   public LissClient(String remoteHost) throws IOException, LissException {
/*   83 */     this.control = new LissControlSocket(remoteHost);
/*      */   }
/*      */ 
/*      */   public LissClient(InetAddress remoteAddr, int port)
/*      */     throws IOException, LissException
/*      */   {
/*   95 */     this.control = new LissControlSocket(remoteAddr, port);
/*      */   }
/*      */   
/*      */   public LissClient(InetAddress remoteAddr) throws IOException, LissException {
/*   99 */     this.control = new LissControlSocket(remoteAddr);
/*      */   }
/*      */   
/*      */   LissControlSocket getLissControlSocket() {
/*  103 */     return this.control;
/*      */   }
/*      */ 
/*      */   public void setTimeOut(int ms)
/*      */   {
/*  110 */     this.timeOut = ms;
/*  111 */     this.control.setTimeOut(this.timeOut);
/*      */   }
/*      */   
/*      */   public float getVersion() {
/*  115 */     return this.control.version;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getSocketConnectionInfo()
/*      */   {
/*  121 */     return this.control.getSocketConnectionInfo();
/*      */   }
/*      */ 
/*      */   public synchronized void login(String user, String password)
/*      */     throws IOException, LissException
/*      */   {
/*  134 */     LissResponse response = this.control.sendCommand("USER " + user);
/*  135 */     this.control.validateReply(response, "331");
/*  136 */     response = this.control.sendCommand("PASS " + password);
/*  137 */     this.control.validateReply(response, "230");
/*      */   }
/*      */ 
/*      */   public synchronized void user(String user)
/*      */     throws IOException, LissException
/*      */   {
/*  150 */     LissResponse reply = this.control.sendCommand("USER " + user);
/*      */     
/*      */ 
/*  153 */     String[] validCodes = { "230", "331" };
/*      */     
/*  155 */     this.control.validateReply(reply, validCodes);
/*      */   }
/*      */ 
/*      */   public synchronized void password(String password)
/*      */     throws IOException, LissException
/*      */   {
/*  169 */     LissResponse reply = this.control.sendCommand("PASS " + password);
/*      */     
/*      */ 
/*  172 */     String[] validCodes = { "230", "202" };
/*      */     
/*  174 */     this.control.validateReply(reply, validCodes);
/*      */   }
/*      */   
/*      */   public synchronized void noop() throws IOException, LissException {
/*  178 */     LissResponse reply = this.control.sendCommand("NOOP");
/*  179 */     this.control.validateReply(reply, "200");
/*      */   }
/*      */   
/*  182 */   public synchronized void chpass(String newPass) throws IOException, LissException { LissResponse reply = this.control.sendCommand("CPAS " + newPass);
/*  183 */     this.control.validateReply(reply, "200");
/*      */   }
/*      */   
/*      */   public synchronized String stat_monitor() throws IOException, LissException {
/*  187 */     LissResponse reply = this.control.sendCommand("STAT MONITOR");
/*  188 */     this.control.validateReply(reply, "211");
/*  189 */     return reply.getMessage();
/*      */   }
/*      */   
/*      */   public synchronized String stat_active() throws IOException, LissException {
/*  193 */     LissResponse reply = this.control.sendCommand("STAT ACTIVE");
/*  194 */     this.control.validateReply(reply, "211");
/*  195 */     return reply.getMessage();
/*      */   }
/*      */   
/*      */   public synchronized String stat_log() throws IOException, LissException {
/*  199 */     LissResponse reply = this.control.sendCommand("STAT LOG");
/*  200 */     this.control.validateReply(reply, "211");
/*  201 */     return reply.getMessage();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void port(String dataType, String host, int port)
/*      */     throws IOException, LissException
/*      */   {
/*  209 */     StringBuffer command = new StringBuffer("PORT " + dataType + " ");
/*  210 */     command.append(host.replace('.', ','));
/*  211 */     command.append(',');
/*  212 */     command.append(port / 256);
/*  213 */     command.append(',');
/*  214 */     command.append(port % 256);
/*  215 */     LissResponse reply = this.control.sendCommand(command.toString());
/*  216 */     this.control.validateReply(reply, "200");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized String pasv(String dataType)
/*      */     throws IOException, LissException
/*      */   {
/*  224 */     LissResponse reply = this.control.sendCommand("PASV " + dataType + " ");
/*  225 */     String[] validCode1 = { "227", "228" };
/*      */     
/*  227 */     this.control.validateReply(reply, validCode1);
/*      */ 
/*  237 */     String message = reply.getMessage();
/*  238 */     int bracket1 = message.indexOf('(');
/*  239 */     int bracket2 = message.indexOf(')');
/*  240 */     return message.substring(bracket1 + 1, bracket2);
/*      */   }
/*      */   
/*      */   public void setRTServerPassiveMode(boolean bPassive) {
/*  244 */     this.bRTServerPassiveMode = bPassive;
/*      */   }
/*      */   
/*      */   public void setServerPassiveMode(boolean bPassive) {
/*  248 */     this.bServerPassiveMode = bPassive;
/*      */   }
/*      */ 
/*      */   public synchronized String[] getAllStationCode()
/*      */     throws IOException, LissException
/*      */   {
/*  258 */     nrtClean();
/*      */     
/*  260 */     if (this.bServerPassiveMode) {
/*  261 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  263 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  266 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  269 */     LissResponse reply = this.control.sendCommand("SLST");
/*      */     
/*      */ 
/*  272 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  274 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  277 */     if (this.dataServerSocket != null) {
/*  278 */       this.data = this.dataServerSocket.accept();
/*  279 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*  281 */     BufferedReader bufReader = new BufferedReader(new InputStreamReader(this.data.getInputStream()));
/*      */     
/*  283 */     Vector vec = new Vector();
/*  284 */     String line = null;
/*  285 */     while ((line = bufReader.readLine()) != null) {
/*  286 */       vec.addElement(line);
/*      */     }
/*  288 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  290 */     reply = this.control.readReply();
/*  291 */     this.control.validateReply(reply, validCodes2);
/*  292 */     String[] stns = new String[vec.size()];
/*  293 */     int i = 0;
/*  294 */     Enumeration enu = vec.elements();
/*  295 */     while (enu.hasMoreElements()) {
/*  296 */       stns[(i++)] = ((String)enu.nextElement());
/*      */     }
/*  298 */     bufReader.close();
/*  299 */     this.data.close();
/*  300 */     enu = null;
/*  301 */     vec = null;
/*  302 */     return stns;
/*      */   }
/*      */ 
/*      */   public synchronized String[] getBufferInfo(String[] stnCode, boolean bNew)
/*      */     throws IOException, LissException
/*      */   {
/*  312 */     nrtClean();
/*      */     
/*  314 */     if (this.bServerPassiveMode) {
/*  315 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  317 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  320 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  323 */     StringBuffer cmd = new StringBuffer();
/*  324 */     if (bNew) {
/*  325 */       cmd.append("GETN ");
/*      */     } else
/*  327 */       cmd.append("GETO ");
/*  328 */     for (int i = 0; i < stnCode.length; i++) {
/*  329 */       cmd.append(stnCode[i]);
/*  330 */       cmd.append(" ");
/*      */     }
/*  332 */     LissResponse reply = this.control.sendCommand(cmd.toString());
/*      */     
/*      */ 
/*  335 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  337 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  340 */     if (this.dataServerSocket != null) {
/*  341 */       this.data = this.dataServerSocket.accept();
/*  342 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*  344 */     BufferedReader bufReader = new BufferedReader(new InputStreamReader(this.data.getInputStream()));
/*      */     
/*  346 */     Vector vec = new Vector();
/*  347 */     String line = null;
/*  348 */     while ((line = bufReader.readLine()) != null) {
/*  349 */       vec.addElement(line);
/*      */     }
/*  351 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  353 */     reply = this.control.readReply();
/*  354 */     this.control.validateReply(reply, validCodes2);
/*  355 */     String[] infos = new String[vec.size()];
/*  356 */     int i = 0;
/*  357 */     Enumeration enu = vec.elements();
/*  358 */     while (enu.hasMoreElements()) {
/*  359 */       infos[(i++)] = ((String)enu.nextElement());
/*      */     }
/*  361 */     bufReader.close();
/*  362 */     this.data.close();
/*  363 */     enu = null;
/*  364 */     vec = null;
/*  365 */     return infos;
/*      */   }
/*      */ 
/*      */   public synchronized String[] getLostIDInfo(String stnCode)
/*      */     throws IOException, LissException
/*      */   {
/*  375 */     nrtClean();
/*      */     
/*  377 */     if (this.bServerPassiveMode) {
/*  378 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  380 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  383 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  386 */     LissResponse reply = this.control.sendCommand("GETL " + stnCode);
/*      */     
/*  388 */     if (reply.getReturnCode().equals("455")) {
/*  389 */       return new String[0];
/*      */     }
/*      */     
/*  392 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  394 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  397 */     if (this.dataServerSocket != null) {
/*  398 */       this.data = this.dataServerSocket.accept();
/*  399 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     
/*  402 */     BufferedInputStream bIn = new BufferedInputStream(new DataInputStream(this.data.getInputStream()));
/*      */     
/*  404 */     ByteBuffer buf = readAllBytes(bIn);
/*      */     
/*      */ 
/*  407 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  409 */     reply = this.control.readReply();
/*  410 */     this.control.validateReply(reply, validCodes2);
/*  411 */     bIn.close();
/*  412 */     this.data.close();
/*  413 */     String IDstr = new String(buf.resultBuf, 0, buf.bufsize);
/*  414 */     StringTokenizer st = new StringTokenizer(IDstr, " \r\n");
/*  415 */     String[] IDs = new String[st.countTokens()];
/*  416 */     int n = 0;
/*  417 */     while (st.hasMoreTokens()) {
/*  418 */       IDs[(n++)] = st.nextToken();
/*      */     }
/*  420 */     return IDs;
/*      */   }
/*      */ 
/*      */   public synchronized BufferInfo getBufferInfo(String stnCode)
/*      */     throws IOException, LissException
/*      */   {
/*  430 */     nrtClean();
/*      */     
/*  432 */     if (this.bServerPassiveMode) {
/*  433 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  435 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  438 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  441 */     LissResponse reply = this.control.sendCommand("GETI " + stnCode);
/*      */     
/*      */ 
/*  444 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  446 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  449 */     if (this.dataServerSocket != null) {
/*  450 */       this.data = this.dataServerSocket.accept();
/*  451 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*  453 */     BufferedReader bufReader = new BufferedReader(new InputStreamReader(this.data.getInputStream()));
/*      */     
/*  455 */     BufferInfo bufferInfo = new BufferInfo();
/*  456 */     String line = null;
/*  457 */     while ((line = bufReader.readLine()) != null) {
/*  458 */       bufferInfo.addElement(line);
/*      */     }
/*  460 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  462 */     reply = this.control.readReply();
/*  463 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*  465 */     bufReader.close();
/*  466 */     this.data.close();
/*  467 */     return bufferInfo;
/*      */   }
/*      */ 
/*      */   public synchronized byte[] getOneMiniSeedRecord(String[] stnCode, String[] seqNumber)
/*      */     throws IOException, LissException
/*      */   {
/*  480 */     nrtClean();
/*      */     
/*  482 */     if (this.bServerPassiveMode) {
/*  483 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  485 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  488 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  491 */     StringBuffer cmd = new StringBuffer();
/*  492 */     cmd.append("GETM ");
/*  493 */     if (stnCode.length > seqNumber.length) {
/*  494 */       System.err.println("In LissClient.getOneMiniSeedRecord, stnCode.length NEQ seqNumber");
/*      */       
/*  496 */       return null;
/*      */     }
/*  498 */     for (int i = 0; i < stnCode.length; i++) {
/*  499 */       cmd.append(stnCode[i]);
/*  500 */       cmd.append(" ");
/*  501 */       cmd.append(seqNumber[i]);
/*  502 */       cmd.append(" ");
/*      */     }
/*  504 */     LissResponse reply = this.control.sendCommand(cmd.toString());
/*  505 */     if (reply.getReturnCode().equals("551")) {
/*  506 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  510 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  512 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  515 */     if (this.dataServerSocket != null) {
/*  516 */       this.data = this.dataServerSocket.accept();
/*  517 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     
/*  520 */     BufferedInputStream bIn = new BufferedInputStream(new DataInputStream(this.data.getInputStream()));
/*      */     
/*  522 */     ByteBuffer buf = readAllBytes(bIn);
/*      */     
/*      */ 
/*  525 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  527 */     reply = this.control.readReply();
/*  528 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*  530 */     bIn.close();
/*  531 */     this.data.close();
/*  532 */     byte[] ret = buf.resultBuf;
/*  533 */     buf = null;
/*  534 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void setOneMiniSeedRecord(byte[] bytes)
/*      */     throws IOException, LissException
/*      */   {
/*  542 */     nrtClean();
/*      */     
/*  544 */     if (this.bServerPassiveMode) {
/*  545 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  547 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  550 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  553 */     LissResponse reply = this.control.sendCommand("SETM");
/*      */     
/*      */ 
/*  556 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  558 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  561 */     if (this.dataServerSocket != null) {
/*  562 */       this.data = this.dataServerSocket.accept();
/*  563 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     
/*  566 */     DataOutputStream out = new DataOutputStream(this.data.getOutputStream());
/*      */     
/*      */ 
/*  569 */     out.write(bytes, 0, bytes.length);
/*      */     
/*      */ 
/*  572 */     out.flush();
/*  573 */     out.close();
/*      */     
/*      */ 
/*  576 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  578 */     reply = this.control.readReply();
/*  579 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/*  583 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */   }
/*      */   
/*      */   public synchronized void sendResult(String infoType, Result[] results) throws IOException, LissException
/*      */   {
/*  590 */     nrtClean();
/*      */     
/*  592 */     if (this.bServerPassiveMode) {
/*  593 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  595 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  598 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  601 */     LissResponse reply = this.control.sendCommand("SNDR " + infoType);
/*      */     
/*      */ 
/*  604 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  606 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  609 */     if (this.dataServerSocket != null) {
/*  610 */       this.data = this.dataServerSocket.accept();
/*  611 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     
/*  614 */     DataOutputStream out = new DataOutputStream(this.data.getOutputStream());
/*      */     
/*  616 */     for (int i = 0; i < results.length; i++)
/*      */     {
/*  618 */       out.write(results[i].getBytes());
/*      */     }
/*      */     
/*  621 */     out.flush();
/*  622 */     out.close();
/*      */     
/*      */ 
/*  625 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  627 */     reply = this.control.readReply();
/*  628 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/*  632 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */   }
/*      */   
/*      */   public synchronized String sendResult(String infoType, byte[] bytes)
/*      */     throws IOException, LissException
/*      */   {
/*  646 */     nrtClean();
/*      */     
/*  648 */     if (this.bServerPassiveMode) {
/*  649 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  651 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  654 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  657 */     LissResponse reply = this.control.sendCommand("SNDR " + infoType);
/*      */     
/*      */ 
/*  660 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  662 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  665 */     if (this.dataServerSocket != null) {
/*  666 */       this.data = this.dataServerSocket.accept();
/*  667 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     
/*  670 */     DataOutputStream out = new DataOutputStream(this.data.getOutputStream());
/*      */     
/*      */ 
/*  673 */     out.write(bytes, 0, bytes.length);
/*      */     
/*      */ 
/*  676 */     out.flush();
/*  677 */     out.close();
/*      */     
/*      */ 
/*  680 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  682 */     reply = this.control.readReply();
/*  683 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/*  687 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*  690 */     return reply.getMessage();
/*      */   }
/*      */   
/*      */   public synchronized LissInputStream retrieveResult(String infoType)
/*      */     throws IOException, LissException
/*      */   {
/*  702 */     nrtClean();
/*  703 */     if (this.bServerPassiveMode) {
/*  704 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  706 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  709 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  712 */     LissResponse reply = this.control.sendCommand("RCVR " + infoType);
/*      */     
/*      */ 
/*  715 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  717 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*  719 */     if (this.dataServerSocket != null) {
/*  720 */       this.data = this.dataServerSocket.accept();
/*  721 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*  723 */     return new LissInputStream(this.data.getInputStream(), this);
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized LissInputStream getMiniSeedBuffer(String stnCode, String startTimeSqlStr, int seconds)
/*      */     throws IOException, LissException
/*      */   {
/*  732 */     nrtClean();
/*      */     
/*  734 */     if (this.bServerPassiveMode) {
/*  735 */       this.data = this.control.createPassiveDataSocket("NRT");
/*      */       
/*  737 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  740 */       this.dataServerSocket = this.control.createDataServerSocket("NRT");
/*      */     }
/*      */     
/*  743 */     LissResponse reply = this.control.sendCommand("GETB " + stnCode + " " + startTimeSqlStr + " " + seconds + " NRT");
/*      */     
/*      */ 
/*  746 */     if (reply.getReturnCode().equals("551")) {
/*  747 */       return null;
/*      */     }
/*  749 */     if (reply.getReturnCode().equals("455")) {
/*  750 */       return null;
/*      */     }
/*      */     
/*  753 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  755 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  758 */     if (this.dataServerSocket != null) {
/*  759 */       this.data = this.dataServerSocket.accept();
/*  760 */       this.data.setSoTimeout(this.timeOut);
/*      */     }
/*      */     
/*  763 */     return new LissInputStream(this.data.getInputStream(), this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized LissInputStream retrieveRealTimeStream(String type, String[] stnCodes)
/*      */     throws IOException, LissException
/*      */   {
/*  774 */     rtClean();
/*  775 */     StringBuffer stns = new StringBuffer();
/*  776 */     for (int i = 0; i < stnCodes.length; i++) {
/*  777 */       stns.append(' ');
/*  778 */       stns.append(stnCodes[i]);
/*      */     }
/*  780 */     if (this.bRTServerPassiveMode) {
/*  781 */       this.rtData = this.control.createPassiveDataSocket("RT");
/*      */       
/*  783 */       this.rtData.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  786 */       this.rtDataServerSocket = this.control.createDataServerSocket("RT");
/*      */     }
/*      */     
/*  789 */     LissResponse reply = this.control.sendCommand("RETR " + type + stns.toString());
/*      */     
/*      */ 
/*  792 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  794 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*  796 */     if (this.rtDataServerSocket != null) {
/*  797 */       this.rtData = this.rtDataServerSocket.accept();
/*  798 */       this.rtData.setSoTimeout(this.timeOut);
/*      */     }
/*  800 */     return new LissInputStream(this.rtData.getInputStream(), this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LissInputStream retrieveRealTimeStream(String[] stnCodes)
/*      */     throws IOException, LissException
/*      */   {
/*  811 */     return retrieveRealTimeStream("", stnCodes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized LissOutputStream storeRealTimeStream(String[] stnCodes)
/*      */     throws IOException, LissException
/*      */   {
/*  819 */     rtClean();
/*  820 */     StringBuffer stns = new StringBuffer();
/*  821 */     for (int i = 0; i < stnCodes.length; i++) {
/*  822 */       stns.append(' ');
/*  823 */       stns.append(stnCodes[i]);
/*      */     }
/*      */     
/*  826 */     if (this.bRTServerPassiveMode) {
/*  827 */       this.rtData = this.control.createPassiveDataSocket("RT");
/*      */       
/*  829 */       this.rtData.setSoTimeout(this.timeOut);
/*      */     }
/*      */     else {
/*  832 */       this.rtDataServerSocket = this.control.createDataServerSocket("RT");
/*      */     }
/*      */     
/*      */ 
/*  836 */     LissResponse reply = this.control.sendCommand("STOR" + stns.toString());
/*      */     
/*      */ 
/*  839 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  841 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  844 */     if (this.rtDataServerSocket != null) {
/*  845 */       this.rtData = this.rtDataServerSocket.accept();
/*  846 */       this.rtData.setSoTimeout(this.timeOut);
/*      */     }
/*  848 */     return new LissOutputStream(this.rtData.getOutputStream(), this);
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
/*      */   public synchronized String gets(String stnCode)
/*      */     throws IOException, LissException
/*      */   {
/*  862 */     this.data = this.control.createPassiveDataSocket("NRT");
/*  863 */     this.data.setSoTimeout(this.timeOut);
/*      */     
/*  865 */     DataInputStream in = new DataInputStream(this.data.getInputStream());
/*  866 */     BufferedInputStream bIn = new BufferedInputStream(in);
/*      */     
/*      */ 
/*  869 */     LissResponse reply = this.control.sendCommand("GETS " + stnCode);
/*      */     
/*      */ 
/*  872 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  874 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  877 */     int chunksize = 4096;
/*  878 */     byte[] chunk = new byte[chunksize];
/*      */     
/*      */ 
/*      */ 
/*  882 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(10240);
/*      */     
/*      */     int count;
/*  885 */     while ((count = bIn.read(chunk, 0, chunksize)) >= 0) {
/*      */       int i;
/*  887 */       bos.write(chunk, 0, i);
/*      */     }
/*  889 */     bos.close();
/*      */     
/*      */ 
/*  892 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  894 */     reply = this.control.readReply();
/*  895 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/*  899 */       bIn.close();
/*  900 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*  903 */     return bos.toString();
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
/*      */   public synchronized void sets(File localStnInfo, String stnCode)
/*      */     throws IOException, LissException
/*      */   {
/*  918 */     this.data = this.control.createPassiveDataSocket("NRT");
/*  919 */     this.data.setSoTimeout(this.timeOut);
/*  920 */     DataOutputStream out = new DataOutputStream(this.data.getOutputStream());
/*      */     
/*      */ 
/*  923 */     LissResponse reply = this.control.sendCommand("SETS " + stnCode);
/*      */     
/*      */ 
/*  926 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  928 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  931 */     FileInputStream input = new FileInputStream(localStnInfo);
/*  932 */     byte[] buf = new byte['Ȁ'];
/*      */     
/*      */ 
/*  935 */     int count = 0;
/*  936 */     Date now = new Date();
/*      */     try {
/*  938 */       Thread.sleep(1L);
/*      */     }
/*      */     catch (InterruptedException iEx) {}
/*  941 */     while ((count = input.read(buf)) > 0) {
/*  942 */       out.write(buf, 0, count);
/*      */     }
/*  944 */     input.close();
/*      */     
/*      */ 
/*  947 */     out.flush();
/*  948 */     out.close();
/*      */     
/*      */ 
/*  951 */     String[] validCodes2 = { "226", "250" };
/*      */     
/*  953 */     reply = this.control.readReply();
/*  954 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/*  958 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void put(String localPath, String remoteFile)
/*      */     throws IOException, LissException
/*      */   {
/*  975 */     this.data = this.control.createPassiveDataSocket("NRT");
/*  976 */     this.data.setSoTimeout(this.timeOut);
/*  977 */     DataOutputStream out = new DataOutputStream(this.data.getOutputStream());
/*      */     
/*      */ 
/*  980 */     LissResponse reply = this.control.sendCommand("STOR " + remoteFile);
/*      */     
/*      */ 
/*  983 */     String[] validCodes1 = { "125", "150" };
/*      */     
/*  985 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*  988 */     FileInputStream input = new FileInputStream(localPath);
/*  989 */     byte[] buf = new byte['Ȁ'];
/*      */     
/*      */ 
/*  992 */     int count = 0;
/*  993 */     while ((count = input.read(buf)) > 0) {
/*  994 */       out.write(buf, 0, count);
/*      */     }
/*  996 */     input.close();
/*      */     
/*      */ 
/*  999 */     out.flush();
/* 1000 */     out.close();
/*      */     
/*      */ 
/* 1003 */     String[] validCodes2 = { "226", "250" };
/*      */     
/* 1005 */     reply = this.control.readReply();
/* 1006 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/* 1010 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   public synchronized void put(byte[] bytes, String remoteFile)
/*      */     throws IOException, LissException
/*      */   {
/* 1027 */     this.data = this.control.createPassiveDataSocket("NRT");
/* 1028 */     this.data.setSoTimeout(this.timeOut);
/* 1029 */     DataOutputStream out = new DataOutputStream(this.data.getOutputStream());
/*      */     
/*      */ 
/* 1032 */     LissResponse reply = this.control.sendCommand("STOR " + remoteFile);
/*      */     
/*      */ 
/* 1035 */     String[] validCodes1 = { "125", "150" };
/*      */     
/* 1037 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/* 1040 */     out.write(bytes, 0, bytes.length);
/*      */     
/*      */ 
/* 1043 */     out.flush();
/* 1044 */     out.close();
/*      */     
/*      */ 
/* 1047 */     String[] validCodes2 = { "226", "250" };
/*      */     
/* 1049 */     reply = this.control.readReply();
/* 1050 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/* 1054 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */   }
/*      */ 
/*      */   public synchronized void get(String localPath, String remoteFile)
/*      */     throws IOException, LissException
/*      */   {
/* 1071 */     this.data = this.control.createPassiveDataSocket("NRT");
/* 1072 */     this.data.setSoTimeout(this.timeOut);
/* 1073 */     DataInputStream in = new DataInputStream(this.data.getInputStream());
/* 1074 */     BufferedInputStream bIn = new BufferedInputStream(in);
/*      */     
/*      */ 
/* 1077 */     LissResponse reply = this.control.sendCommand("RETR " + remoteFile);
/*      */     
/*      */ 
/* 1080 */     String[] validCodes1 = { "125", "150" };
/*      */     
/* 1082 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/* 1085 */     int chunksize = 4096;
/* 1086 */     byte[] chunk = new byte[chunksize];
/*      */     
/*      */ 
/*      */ 
/* 1090 */     BufferedOutputStream out = null;
/* 1091 */     out = new BufferedOutputStream(new FileOutputStream(localPath, false));
/*      */     
/*      */     int count;
/* 1094 */     while ((count = bIn.read(chunk, 0, chunksize)) >= 0) {
/*      */       int i;
/* 1096 */       out.write(chunk, 0, i);
/*      */     }
/* 1098 */     out.close();
/*      */     
/*      */ 
/* 1101 */     String[] validCodes2 = { "226", "250" };
/*      */     
/* 1103 */     reply = this.control.readReply();
/* 1104 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/* 1108 */       bIn.close();
/* 1109 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   public synchronized byte[] get(String remoteFile)
/*      */     throws IOException, LissException
/*      */   {
/* 1127 */     this.data = this.control.createPassiveDataSocket("NRT");
/* 1128 */     this.data.setSoTimeout(this.timeOut);
/* 1129 */     DataInputStream in = new DataInputStream(this.data.getInputStream());
/* 1130 */     BufferedInputStream bIn = new BufferedInputStream(in);
/*      */     
/*      */ 
/* 1133 */     LissResponse reply = this.control.sendCommand("RETR " + remoteFile);
/*      */     
/*      */ 
/* 1136 */     String[] validCodes1 = { "125", "150" };
/*      */     
/* 1138 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/* 1141 */     int chunksize = 4096;
/* 1142 */     byte[] chunk = new byte[chunksize];
/* 1143 */     byte[] resultBuf = new byte[chunksize];
/* 1144 */     byte[] temp = null;
/*      */     
/* 1146 */     int bufsize = 0;
/*      */     
/*      */     int count;
/* 1149 */     while ((count = bIn.read(chunk, 0, chunksize)) >= 0)
/*      */     {
/*      */       int i;
/* 1152 */       temp = new byte[bufsize + i];
/*      */       
/*      */ 
/* 1155 */       System.arraycopy(resultBuf, 0, temp, 0, bufsize);
/*      */       
/*      */ 
/* 1158 */       System.arraycopy(chunk, 0, temp, bufsize, i);
/*      */       
/*      */ 
/* 1161 */       resultBuf = temp;
/*      */       
/*      */ 
/* 1164 */       bufsize += i;
/*      */     }
/*      */     
/*      */ 
/* 1168 */     String[] validCodes2 = { "226", "250" };
/*      */     
/* 1170 */     reply = this.control.readReply();
/* 1171 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/* 1175 */       bIn.close();
/* 1176 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */     
/* 1180 */     return resultBuf;
/*      */   }
/*      */ 
/*      */   public synchronized String list(String mask)
/*      */     throws IOException, LissException
/*      */   {
/* 1192 */     this.data = this.control.createPassiveDataSocket("NRT");
/* 1193 */     this.data.setSoTimeout(this.timeOut);
/* 1194 */     InputStreamReader in = new InputStreamReader(this.data.getInputStream());
/* 1195 */     BufferedReader bIn = new BufferedReader(in);
/*      */     
/*      */ 
/* 1198 */     LissResponse reply = this.control.sendCommand("NLST " + mask);
/*      */     
/*      */ 
/* 1201 */     String[] validCodes1 = { "125", "150" };
/*      */     
/* 1203 */     this.control.validateReply(reply, validCodes1);
/*      */     
/*      */ 
/*      */ 
/* 1207 */     int chunksize = 4096;
/* 1208 */     char[] chunk = new char[chunksize];
/* 1209 */     char[] resultBuf = new char[chunksize];
/* 1210 */     char[] temp = null;
/*      */     
/* 1212 */     int bufsize = 0;
/*      */     
/*      */     int count;
/* 1215 */     while ((count = bIn.read(chunk, 0, chunksize)) >= 0)
/*      */     {
/*      */       int i;
/* 1218 */       temp = new char[bufsize + i];
/*      */       
/*      */ 
/* 1221 */       System.arraycopy(resultBuf, 0, temp, 0, bufsize);
/*      */       
/*      */ 
/* 1224 */       System.arraycopy(chunk, 0, temp, bufsize, i);
/*      */       
/*      */ 
/* 1227 */       resultBuf = temp;
/*      */       
/*      */ 
/* 1230 */       bufsize += i;
/*      */     }
/*      */     
/*      */ 
/* 1234 */     String[] validCodes2 = { "226", "250" };
/*      */     
/* 1236 */     reply = this.control.readReply();
/* 1237 */     this.control.validateReply(reply, validCodes2);
/*      */     
/*      */     try
/*      */     {
/* 1241 */       bIn.close();
/* 1242 */       this.data.close();
/*      */     }
/*      */     catch (IOException ignore) {}
/*      */     
/* 1246 */     return new String(resultBuf);
/*      */   }
/*      */   
/*      */ 
/*      */   public void debugResponses(boolean on)
/*      */   {
/* 1257 */     this.control.debugResponses(on);
/*      */   }
/*      */   
/*      */ 
/*      */   public LissTransferType getType()
/*      */   {
/* 1268 */     return this.transferType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void setType(LissTransferType type)
/*      */     throws IOException, LissException
/*      */   {
/* 1281 */     String typeStr = LissTransferType.ASCII_CHAR;
/* 1282 */     if (type.equals(LissTransferType.BINARY)) {
/* 1283 */       typeStr = LissTransferType.BINARY_CHAR;
/*      */     }
/*      */     
/* 1286 */     LissResponse reply = this.control.sendCommand("TYPE " + typeStr);
/* 1287 */     this.control.validateReply(reply, "200");
/*      */     
/*      */ 
/* 1290 */     this.transferType = type;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void delete(String remoteFile)
/*      */     throws IOException, LissException
/*      */   {
/* 1302 */     LissResponse reply = this.control.sendCommand("DELE " + remoteFile);
/* 1303 */     this.control.validateReply(reply, "250");
/*      */   }
/*      */ 
/*      */   public synchronized void rmdir(String dir)
/*      */     throws IOException, LissException
/*      */   {
/* 1314 */     LissResponse reply = this.control.sendCommand("RMD " + dir);
/* 1315 */     this.control.validateReply(reply, "250");
/*      */   }
/*      */ 
/*      */   public synchronized void mkdir(String dir)
/*      */     throws IOException, LissException
/*      */   {
/* 1326 */     LissResponse reply = this.control.sendCommand("MKD " + dir);
/* 1327 */     this.control.validateReply(reply, "257");
/*      */   }
/*      */ 
/*      */   public synchronized void abortRealTimeStreamTransport()
/*      */     throws IOException, LissException
/*      */   {
/* 1339 */     LissResponse reply = this.control.sendCommand("ABOR");
/* 1340 */     this.control.validateReply(reply, "226");
/* 1341 */     rtClean();
/*      */   }
/*      */ 
/*      */   public synchronized void quit()
/*      */     throws IOException, LissException
/*      */   {
/* 1350 */     LissResponse reply = this.control.sendCommand("QUIT\r\n");
/* 1351 */     this.control.validateReply(reply, "221");
/* 1352 */     rtClean();
/* 1353 */     nrtClean();
/* 1354 */     this.control.logout();
/* 1355 */     this.control = null;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void shutdown()
/*      */     throws IOException, LissException
/*      */   {
/* 1362 */     LissResponse reply = this.control.sendCommand("SHUT\r\n");
/* 1363 */     System.err.println(reply.getMessage());
/* 1364 */     this.control.validateReply(reply, "221");
/* 1365 */     rtClean();
/* 1366 */     nrtClean();
/* 1367 */     this.control.logout();
/* 1368 */     this.control = null;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void reboot()
/*      */     throws IOException, LissException
/*      */   {
/* 1375 */     LissResponse reply = this.control.sendCommand("REBT\r\n");
/* 1376 */     System.err.println(reply.getMessage());
/* 1377 */     this.control.validateReply(reply, "221");
/* 1378 */     rtClean();
/* 1379 */     nrtClean();
/* 1380 */     this.control.logout();
/* 1381 */     this.control = null;
/*      */   }
/*      */   
/*      */   public synchronized void sendRETR(StringBuffer stns)
/*      */     throws IOException, LissException
/*      */   {
/* 1387 */     String[] validCodes1 = { "125", "150" };
/*      */     
/* 1389 */     LissResponse reply = this.control.sendCommand("RETR" + stns.toString());
/* 1390 */     this.control.validateReply(reply, validCodes1);
/*      */   }
/*      */   
/*      */   public synchronized void sendSTOR(StringBuffer stns)
/*      */     throws IOException, LissException
/*      */   {
/* 1396 */     String[] validCodes1 = { "125", "150" };
/*      */     
/* 1398 */     LissResponse reply = this.control.sendCommand("STOR" + stns.toString());
/* 1399 */     this.control.validateReply(reply, validCodes1);
/*      */   }
/*      */   
/*      */   public String getDetailHeader() {
/* 1403 */     return this.control.getDetailHeader();
/*      */   }
/*      */   
/*      */   public String getLocalHostAddress() {
/* 1407 */     return this.control.getLocalHostAddress();
/*      */   }
/*      */ 
/*      */   String getRemoteHostName()
/*      */   {
/* 1416 */     return this.control.getRemoteHostName();
/*      */   }
/*      */ 
/*      */   void checkResponse(String[] validCodes)
/*      */     throws IOException, LissException
/*      */   {
/* 1427 */     LissResponse reply = this.control.readReply();
/* 1428 */     this.control.validateReply(reply, validCodes);
/*      */   }
/*      */   
/*      */ 
/*      */   void closeDataSocket()
/*      */     throws IOException, LissException
/*      */   {
/* 1435 */     String[] validCodes2 = { "226", "250" };
/*      */     
/* 1437 */     checkResponse(validCodes2);
/*      */   }
/*      */   
/*      */   protected void closeTransferSocket() throws IOException {
/* 1441 */     if (this.data != null)
/* 1442 */       this.data.close();
/* 1443 */     if (this.rtData != null)
/* 1444 */       this.rtData.close();
/*      */   }
/*      */   
/*      */   private void rtClean() {
/*      */     try {
/* 1449 */       if (this.rtData != null) {
/* 1450 */         this.rtData.close();
/* 1451 */         this.rtData = null;
/*      */       }
/* 1453 */       if (this.rtDataServerSocket != null) {
/* 1454 */         this.rtDataServerSocket.close();
/* 1455 */         this.rtDataServerSocket = null;
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {}
/*      */   }
/*      */   
/*      */   private void nrtClean() {
/*      */     try {
/* 1463 */       if (this.data != null) {
/* 1464 */         this.data.close();
/* 1465 */         this.data = null;
/*      */       }
/* 1467 */       if (this.dataServerSocket != null) {
/* 1468 */         this.dataServerSocket.close();
/* 1469 */         this.dataServerSocket = null;
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {}
/*      */   }
/*      */   
/*      */   private ByteBuffer readAllBytes(BufferedInputStream bIn)
/*      */     throws IOException
/*      */   {
/* 1478 */     ByteBuffer buf = new ByteBuffer();
/* 1479 */     byte[] chunk = new byte[buf.chunksize];
/* 1480 */     byte[] temp = null;
/*      */     
/*      */     int count;
/*      */     
/* 1484 */     while ((count = bIn.read(chunk, 0, buf.chunksize)) >= 0)
/*      */     {
/*      */       int i;
/* 1487 */       temp = new byte[buf.bufsize + i];
/*      */       
/*      */ 
/* 1490 */       System.arraycopy(buf.resultBuf, 0, temp, 0, buf.bufsize);
/*      */       
/*      */ 
/* 1493 */       System.arraycopy(chunk, 0, temp, buf.bufsize, i);
/*      */       
/*      */ 
/* 1496 */       buf.resultBuf = temp;
/* 1497 */       temp = null;
/*      */       
/*      */ 
/* 1500 */       buf.bufsize += i;
/*      */     }
/* 1502 */     chunk = null;
/* 1503 */     return buf;
/*      */   }
/*      */   
/*      */   class ByteBuffer {
/* 1507 */     public int chunksize = 256;
/* 1508 */     public byte[] resultBuf = new byte[0];
/* 1509 */     public int bufsize = 0;
/*      */     
/*      */     ByteBuffer() {}
/*      */   }
/*      */ }
