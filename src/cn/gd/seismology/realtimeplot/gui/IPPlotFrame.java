/*     */ package cn.gd.seismology.realtimeplot.gui;
/*     */ 
/*     */ import cn.gd.seismology.realtimestream.MiniSeedStreamInf;
/*     */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollBar;
/*     */ 
/*     */ public class IPPlotFrame extends JFrame implements cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener
/*     */ {
/*  31 */   boolean bRecord = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  36 */   JMenuItem jMenuFileExit = new JMenuItem();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   Hashtable tbl = new Hashtable();
/*  44 */   Hashtable firstTbl = new Hashtable();
/*     */   
/*     */ 
/*  47 */   MiniSeedStreamInf mssi = null;
/*  48 */   JMenu jMenu1 = new JMenu();
/*  49 */   JMenuItem jMenuItem1 = new JMenuItem();
/*  50 */   WavePaint[] wps = null;
/*  51 */   int DisChn = 6;
/*  52 */   int begDisChn = 0;
/*     */   
/*     */ 
/*     */ 
/*  56 */   JPanel mainPanel = new JPanel();
/*     */   
/*  58 */   JPanel wpControlPanel = new JPanel();
/*  59 */   Box wpPanel = new Box(1);
/*  60 */   JScrollBar jScrollBarV = new JScrollBar();
/*  61 */   Box boxHBar = new Box(0);
/*  62 */   JScrollBar jScrollBarH = new JScrollBar(0);
/*     */   
/*  64 */   JPanel controlPanel = new JPanel();
/*  65 */   JComboBox jComboBoxTrk = new JComboBox();
/*     */   String[] chanCode;
/*     */   public static final String cvsid = "$Id: IPPlotFrame.java,v 1.6 2003/08/09 02:37:38 hwh Exp $";
/*     */   
/*     */   public IPPlotFrame() {
/*  70 */     enableEvents(64L);
/*     */     try {
/*  72 */       loadConfiguration();
/*  73 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/*  76 */       ex.printStackTrace();
/*     */     } }
/*     */   
/*     */   JMenuItem jmItemStartRecord;
/*     */   JMenuItem jmItemStopRecord;
/*  81 */   public IPPlotFrame(String title) { super(title);
/*  82 */     enableEvents(64L);
/*     */     try {
/*  84 */       loadConfiguration();
/*  85 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/*  88 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void jbInit() throws Exception {
/*  93 */     getContentPane().setLayout(new java.awt.BorderLayout());
/*  94 */     getContentPane().add(this.mainPanel, "Center");
/*  95 */     getContentPane().add(this.controlPanel, "North");
/*     */     
/*  97 */     this.mainPanel.setLayout(new java.awt.BorderLayout());
/*  98 */     this.mainPanel.add(this.wpControlPanel, "Center");
/*  99 */     this.controlPanel.add(this.jComboBoxTrk, null);
/*     */     
/* 101 */     this.wpControlPanel.setLayout(new java.awt.BorderLayout());
/* 102 */     this.wpControlPanel.add(this.wpPanel, "Center");
/* 103 */     this.wpControlPanel.add(this.jScrollBarV, "East");
/*     */     
/* 105 */     this.wpControlPanel.add(this.boxHBar, "South");
/*     */     
/* 107 */     this.boxHBar.add(this.jScrollBarH);
/* 108 */     int hight = this.jScrollBarH.getPreferredSize().height;
/* 109 */     this.boxHBar.add(Box.createRigidArea(new Dimension(hight, hight)));
/*     */     
/*     */ 
/* 112 */     this.jComboBoxTrk.setMaximumSize(new Dimension(50, 24));
/* 113 */     this.jComboBoxTrk.setPreferredSize(new Dimension(50, 24));
/* 114 */     this.jComboBoxTrk.setMinimumSize(new Dimension(50, 24));
/* 115 */     this.jComboBoxTrk.addItem("3");
/* 116 */     this.jComboBoxTrk.addItem("6");
/* 117 */     this.jComboBoxTrk.addItem("9");
/* 118 */     this.jComboBoxTrk.addItem("12");
/* 119 */     this.jComboBoxTrk.addItem("15");
/* 120 */     this.jComboBoxTrk.addItem("30");
/* 121 */     this.jComboBoxTrk.addItem("all");
/* 122 */     this.jComboBoxTrk.addItemListener(new java.awt.event.ItemListener() {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 124 */         IPPlotFrame.this.jComboBoxTrk_itemStateChanged(e);
/*     */       }
/* 126 */     });
/* 127 */     this.jComboBoxTrk.setSelectedItem("6");
/*     */     
/*     */ 
/* 130 */     this.jScrollBarV.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
/*     */       public void adjustmentValueChanged(AdjustmentEvent e) {
/* 132 */         IPPlotFrame.this.jScrollBarV_adjustmentValueChanged(e);
/*     */       }
/*     */       
/* 135 */     });
/* 136 */     this.jScrollBarH.setEnabled(false);
/*     */     
/*     */ 
/* 139 */     this.jmenuFile = new JMenu("文件");
/* 140 */     this.jmItemStartRecord = new JMenuItem("开始DSP-ASCII纪录...");
/* 141 */     setTitle("IP波形显示");
/* 142 */     this.jMenu1.setText("设置");
/* 143 */     this.jMenuItem1.setText("设置");
/* 144 */     this.jMenuItem1.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 146 */         IPPlotFrame.this.jMenuItem1_actionPerformed(e);
/*     */       }
/* 148 */     });
/* 149 */     this.jmenuFile.add(this.jmItemStartRecord);
/* 150 */     this.jmItemStartRecord.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 152 */         IPPlotFrame.this.jmItemStartRecord_actionPerformed(e);
/*     */       }
/* 154 */     });
/* 155 */     this.jmItemStopRecord = new JMenuItem("开始DSP-ASCII纪录");
/* 156 */     this.jmItemStopRecord.setEnabled(false);
/* 157 */     this.jmItemStopRecord.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 159 */         IPPlotFrame.this.jmItemStopRecord_actionPerformed(e);
/*     */       }
/*     */       
/* 162 */     });
/* 163 */     this.jmenuFile.add(this.jmItemStopRecord);
/*     */     
/* 165 */     this.jMenuFileExit.setText("退出");
/* 166 */     this.jMenuFileExit.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 168 */         IPPlotFrame.this.jMenuFileExit_actionPerformed(e);
/*     */       }
/* 170 */     });
/* 171 */     this.jmenuFile.add(this.jMenuFileExit);
/*     */     
/* 173 */     this.jmenubar = new JMenuBar();
/* 174 */     this.jmenubar.add(this.jmenuFile);
/* 175 */     this.jmenubar.add(this.jMenu1);
/* 176 */     this.jMenu1.add(this.jMenuItem1);
/* 177 */     setJMenuBar(this.jmenubar);
/*     */   }
/*     */   
/*     */   public void jMenuFileExit_actionPerformed(ActionEvent e)
/*     */   {
/* 182 */     System.exit(0);
/*     */   }
/*     */   
/*     */   protected void processWindowEvent(java.awt.event.WindowEvent e)
/*     */   {
/* 187 */     super.processWindowEvent(e);
/* 188 */     if (e.getID() == 201) {
/* 189 */       jMenuFileExit_actionPerformed(null);
/*     */     }
/*     */   }
/*     */   
/*     */   synchronized void jmItemStartRecord_actionPerformed(ActionEvent e) {
/* 194 */     this.bRecord = true;
/* 195 */     this.jmItemStartRecord.setEnabled(false);
/* 196 */     this.jmItemStopRecord.setEnabled(true);
/*     */     try {
/* 198 */       for (int i = 0; i < this.netID.length; i++) {
/* 199 */         String key = this.netID[i] + "_" + this.stnCode[i] + "_" + this.locID[i] + "_" + this.chanCode[i];
/* 200 */         PrintWriter pw = new PrintWriter(new java.io.FileOutputStream(key));
/* 201 */         this.tbl.put(key, pw);
/* 202 */         this.firstTbl.put(key, new Boolean(true));
/*     */       }
/*     */     }
/*     */     catch (IOException ioEx) {
/* 206 */       ioEx.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   synchronized void jmItemStopRecord_actionPerformed(ActionEvent e) {
/* 211 */     this.bRecord = false;
/* 212 */     this.jmItemStartRecord.setEnabled(true);
/* 213 */     this.jmItemStopRecord.setEnabled(false);
/* 214 */     for (int i = 0; i < this.netID.length; i++) {
/* 215 */       String key = this.netID[i] + "_" + this.stnCode[i] + "_" + this.locID[i] + "_" + this.chanCode[i];
/* 216 */       PrintWriter pw = (PrintWriter)this.tbl.get(key);
/* 217 */       pw.flush();
/* 218 */       pw.close();
/* 219 */       this.tbl.remove(key);
/* 220 */       this.firstTbl.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStationInfo(String[] netID, String[] stnCode, String[] locID, String[] chanCode) {
/* 225 */     this.netID = netID;
/* 226 */     this.stnCode = stnCode;
/* 227 */     this.locID = locID;
/* 228 */     this.chanCode = chanCode;
/*     */   }
/*     */   
/*     */   public synchronized void miniSeedEvent(cn.gd.seismology.realtimestream.event.MiniSeedPortEvent MiniSeedPortEvent)
/*     */   {
/* 233 */     GenericMiniSeedRecord gmsRec = MiniSeedPortEvent.getGenericMiniSeedRecord();
/* 234 */     if (gmsRec == null) {
/* 235 */       return;
/*     */     }
/* 237 */     if (this.bRecord) {
/* 238 */       String net = gmsRec.getNetwork();
/* 239 */       String stnCode = gmsRec.getStation();
/* 240 */       String locID = gmsRec.getLocID();
/* 241 */       String chanCode = gmsRec.getChannel();
/* 242 */       SimpleDateFormat dateFmt = new SimpleDateFormat("MM-dd-yyy");
/* 243 */       SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss.ss");
/*     */       
/* 245 */       String key = net + "_" + stnCode + "_" + locID + "_" + chanCode;
/*     */       
/*     */ 
/* 248 */       Boolean bFirst = (Boolean)this.firstTbl.get(key);
/* 249 */       PrintWriter pw = (PrintWriter)this.tbl.get(key);
/* 250 */       if (pw == null)
/* 251 */         return;
/* 252 */       if (bFirst.booleanValue()) {
/* 253 */         this.firstTbl.remove(key);
/* 254 */         this.firstTbl.put(key, new Boolean(false));
/*     */         
/* 256 */         pw.println("DATASET " + stnCode);
/* 257 */         pw.println("VERSION NXT");
/* 258 */         pw.println("SERIES " + chanCode);
/* 259 */         pw.println("DATE " + dateFmt.format(gmsRec.getStartTime()));
/* 260 */         pw.println("TIME " + timeFmt.format(gmsRec.getStartTime()));
/* 261 */         pw.println("FILE_TYPE ASCII");
/* 262 */         pw.println("NUM_SAMPS ALL");
/* 263 */         pw.println("INTERVAL " + 1.0D / gmsRec.getSampleRate());
/* 264 */         pw.println("X_OFFSET 0");
/* 265 */         pw.println("MAX_VAL 8388608");
/* 266 */         pw.println("MIN_VAL -8388608");
/* 267 */         pw.println("VERT_UNITS Counts");
/* 268 */         pw.println("HORZ_UNITS Sec");
/* 269 */         pw.println("COMMENT ");
/* 270 */         pw.println("DATA ");
/*     */       }
/* 272 */       gmsRec.decompress();
/* 273 */       int[] samps = gmsRec.getUData();
/* 274 */       int i = 0;
/* 275 */       for (;;) { pw.println(samps[i]);i++;
/* 274 */         if (i >= samps.length)
/*     */           break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   JMenu jmenuFile;
/*     */   JMenuBar jmenubar;
/*     */   String[] locID;
/*     */   String[] netID;
/*     */   String[] stnCode;
/* 285 */   private void loadConfiguration() { if (this.mssi != null)
/* 286 */       this.mssi.close();
/* 287 */     Properties props = new Properties();
/*     */     try {
/* 289 */       props.load(new java.io.FileInputStream("IPPlot.properties"));
/*     */     }
/*     */     catch (IOException ioEx) {
/* 292 */       ioEx.printStackTrace();
/*     */     }
/*     */     
/* 295 */     boolean bTCP = true;
/* 296 */     String connectionType = props.getProperty("IPPlot.connection.type", "TCP");
/* 297 */     if (connectionType == null) {
/* 298 */       bTCP = true;
/*     */     } else {
/* 300 */       if (connectionType.equalsIgnoreCase("TCP"))
/* 301 */         bTCP = true;
/* 302 */       if (connectionType.equalsIgnoreCase("UDP"))
/* 303 */         bTCP = false;
/*     */     }
/* 305 */     String host = props.getProperty("IPPlot.connection.host", "127.0.0.1");
/* 306 */     int port = Integer.parseInt(props.getProperty("IPPlot.connection.port", "5000"));
/* 307 */     String user = props.getProperty("IPPlot.connection.user", "ipplot");
/* 308 */     String password = props.getProperty("IPPlot.connection.password", "ipplot");
/*     */     
/* 310 */     String line = props.getProperty("IPPlot.channel");
/* 311 */     StringTokenizer st = new StringTokenizer(line, " +");
/*     */     
/* 313 */     String[] netID = new String[st.countTokens()];
/* 314 */     String[] stnCode = new String[st.countTokens()];
/* 315 */     String[] locID = new String[st.countTokens()];
/* 316 */     String[] chanCode = new String[st.countTokens()];
/* 317 */     int j = 0;
/* 318 */     while (st.hasMoreTokens()) {
/* 319 */       StringTokenizer st0 = new StringTokenizer(st.nextToken(), " /");
/* 320 */       netID[j] = st0.nextToken();
/* 321 */       stnCode[j] = st0.nextToken();
/* 322 */       locID[j] = st0.nextToken();
/* 323 */       chanCode[(j++)] = st0.nextToken();
/*     */     }
/* 325 */     setStationInfo(netID, stnCode, locID, chanCode);
/*     */     
/* 327 */     this.wps = new WavePaint[stnCode.length];
/* 328 */     for (int i = 0; i < this.wps.length; i++) {
/* 329 */       this.wps[i] = new WavePaint();
/* 330 */       this.wps[i].setWaveData(stnCode[i], locID[i], chanCode[i]);
/* 331 */       this.wps[i].setDisChn(this.DisChn);
/*     */     }
/*     */     
/* 334 */     for (int i = 0; i < this.wps.length; i++) {
/* 335 */       this.wpPanel.add(this.wps[i]);
/*     */     }
/* 337 */     if (bTCP) {
/* 338 */       this.mssi = new cn.gd.seismology.realtimestream.CLissRTPort(host, port, user, password, stnCode) {
/*     */         public boolean accept(GenericMiniSeedRecord gmsr) {
/* 340 */           double srate = gmsr.getSampleRate();
/* 341 */           if ((srate < 39.0D) || (srate > 501.0D)) {
/* 342 */             return false;
/*     */           }
/* 344 */           return true;
/*     */         }
/*     */       };
/*     */     } else {
/* 348 */       this.mssi = new cn.gd.seismology.realtimestream.UDPPort(host, port, stnCode) {
/*     */         public boolean accept(GenericMiniSeedRecord gmsr) {
/* 350 */           double srate = gmsr.getSampleRate();
/* 351 */           if ((srate < 39.0D) || (srate > 501.0D)) {
/* 352 */             return false;
/*     */           }
/* 354 */           return true;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 359 */     this.mssi.addMiniSeedPortEventListener(this);
/* 360 */     for (int i = 0; i < this.wps.length; i++) {
/* 361 */       this.mssi.addMiniSeedPortEventListener(this.wps[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   void setExtenalPort(MiniSeedStreamInf amssi) {
/* 366 */     if (this.mssi != null) {
/* 367 */       this.mssi.close();
/* 368 */       this.mssi.removeMiniSeedPortEventListener(this);
/* 369 */       for (int i = 0; i < this.wps.length; i++) {
/* 370 */         this.mssi.removeMiniSeedPortEventListener(this.wps[i]);
/*     */       }
/*     */     }
/* 373 */     this.mssi = amssi;
/*     */     
/* 375 */     this.mssi.addMiniSeedPortEventListener(this);
/* 376 */     for (int i = 0; i < this.wps.length; i++) {
/* 377 */       this.mssi.addMiniSeedPortEventListener(this.wps[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   void showDialog(java.awt.Dialog dlg)
/*     */   {
/* 383 */     Dimension dlgSize = dlg.getSize();
/* 384 */     Dimension frmSize = getSize();
/* 385 */     java.awt.Point loc = getLocation();
/* 386 */     dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
/* 387 */     dlg.show();
/*     */   }
/*     */   
/*     */   void jMenuItem1_actionPerformed(ActionEvent e) {
/* 391 */     SetupDialog setDlg = new SetupDialog(this, "设置...", true);
/* 392 */     showDialog(setDlg);
/* 393 */     if (setDlg.isReload()) {
/* 394 */       getContentPane().removeAll();
/* 395 */       loadConfiguration();
/* 396 */       validate();
/*     */     }
/* 398 */     System.exit(0);
/*     */   }
/*     */   
/*     */   void jComboBoxTrk_itemStateChanged(ItemEvent e)
/*     */   {
/* 403 */     String selectedItem = (String)this.jComboBoxTrk.getSelectedItem();
/* 404 */     if (selectedItem == "all") {
/* 405 */       this.DisChn = this.wps.length;
/*     */     } else {
/* 407 */       this.DisChn = Integer.parseInt(selectedItem);
/* 408 */       if (this.DisChn > this.wps.length)
/* 409 */         this.DisChn = this.wps.length;
/*     */     }
/* 411 */     this.jScrollBarV.setValues(0, this.DisChn, 0, this.wps.length);
/* 412 */     this.jScrollBarV.setBlockIncrement(this.DisChn - 1);
/*     */     
/* 414 */     for (int i = 0; i < this.wps.length; i++) {
/* 415 */       this.wps[i].setDisChn(this.DisChn);
/*     */     }
/*     */     
/* 418 */     this.wpPanel.removeAll();
/* 419 */     for (int i = this.begDisChn; i < this.begDisChn + this.DisChn; i++) {
/* 420 */       this.wpPanel.add(this.wps[i], null);
/*     */     }
/*     */     
/* 423 */     this.wpPanel.validate();
/*     */   }
/*     */   
/*     */   void jScrollBarV_adjustmentValueChanged(AdjustmentEvent e)
/*     */   {
/* 428 */     this.wpPanel.validate();
/*     */     try {
/* 430 */       Thread.sleep(50L);
/*     */     }
/*     */     catch (InterruptedException ex) {}
/*     */     
/* 434 */     this.begDisChn = this.jScrollBarV.getValue();
/* 435 */     this.wpPanel.removeAll();
/* 436 */     for (int i = this.begDisChn; i < this.begDisChn + this.DisChn; i++) {
/* 437 */       this.wpPanel.add(this.wps[i], null);
/*     */     }
/*     */     
/* 440 */     this.wpPanel.validate();
/*     */     try {
/* 442 */       Thread.sleep(50L);
/*     */     }
/*     */     catch (InterruptedException ex) {}
/*     */   }
/*     */ }
