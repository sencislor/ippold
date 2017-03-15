/*     */ package cn.gd.seismology.realtimeplot.awt;
/*     */ 
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEvent;
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener;
/*     */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuBar;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Hashtable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SimpleFrame
/*     */   extends Frame
/*     */   implements MiniSeedPortEventListener
/*     */ {
/* 536 */   boolean bRecord = false;
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
/* 548 */   Hashtable tbl = new Hashtable();
/* 549 */   Hashtable firstTbl = new Hashtable();
/*     */   String[] chanCode;
/*     */   MenuItem jmItemExit;
/*     */   
/* 553 */   public SimpleFrame() { try { jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/* 556 */       ex.printStackTrace(); } }
/*     */   
/*     */   MenuItem jmItemStartRecord;
/*     */   MenuItem jmItemStopRecord;
/*     */   
/* 561 */   public SimpleFrame(String title) { super(title);
/*     */     try {
/* 563 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/* 566 */       ex.printStackTrace();
/*     */     } }
/*     */   
/*     */   Menu jmenuFile;
/*     */   MenuBar jmenubar;
/*     */   
/* 572 */   private void jbInit() throws Exception { this.jmenuFile = new Menu("File");
/* 573 */     this.jmItemStartRecord = new MenuItem("Start Record...");
/*     */     
/* 575 */     this.jmItemStartRecord.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 577 */         SimpleFrame.this.jmItemStartRecord_actionPerformed(e);
/*     */       }
/* 579 */     });
/* 580 */     this.jmItemStopRecord = new MenuItem("Stop Record");
/* 581 */     this.jmItemStopRecord.setEnabled(false);
/* 582 */     this.jmItemStopRecord.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 584 */         SimpleFrame.this.jmItemStopRecord_actionPerformed(e);
/*     */       }
/*     */       
/*     */ 
/* 588 */     });
/* 589 */     this.jmItemExit = new MenuItem("Exit");
/* 590 */     this.jmenuFile.add(this.jmItemExit);
/* 591 */     this.jmItemExit.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 593 */         SimpleFrame.this.jmItemExit_actionPerformed(e);
/*     */       }
/*     */       
/* 596 */     });
/* 597 */     this.jmenubar = new MenuBar();
/* 598 */     this.jmenubar.add(this.jmenuFile);
/* 599 */     setMenuBar(this.jmenubar); }
/*     */   
/*     */   String[] locID;
/*     */   
/* 603 */   synchronized void jmItemStartRecord_actionPerformed(ActionEvent e) { this.bRecord = true;
/* 604 */     this.jmItemStartRecord.setEnabled(false);
/* 605 */     this.jmItemStopRecord.setEnabled(true);
/*     */     try {
/* 607 */       for (int i = 0; i < this.netID.length; i++) {
/* 608 */         String key = this.netID[i] + "_" + this.stnCode[i] + "_" + this.locID[i] + "_" + this.chanCode[i];
/* 609 */         PrintWriter pw = new PrintWriter(new FileOutputStream(key));
/* 610 */         this.tbl.put(key, pw);
/* 611 */         this.firstTbl.put(key, new Boolean(true));
/*     */       }
/*     */     }
/*     */     catch (IOException ioEx) {
/* 615 */       ioEx.printStackTrace();
/*     */     } }
/*     */   
/*     */   String[] netID;
/*     */   String[] stnCode;
/* 620 */   synchronized void jmItemStopRecord_actionPerformed(ActionEvent e) { this.bRecord = false;
/* 621 */     this.jmItemStartRecord.setEnabled(true);
/* 622 */     this.jmItemStopRecord.setEnabled(false);
/* 623 */     for (int i = 0; i < this.netID.length; i++) {
/* 624 */       String key = this.netID[i] + "_" + this.stnCode[i] + "_" + this.locID[i] + "_" + this.chanCode[i];
/* 625 */       PrintWriter pw = (PrintWriter)this.tbl.get(key);
/* 626 */       pw.flush();
/* 627 */       pw.close();
/* 628 */       this.tbl.remove(key);
/* 629 */       this.firstTbl.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   void jmItemExit_actionPerformed(ActionEvent e) {
/* 634 */     System.exit(0);
/*     */   }
/*     */   
/*     */   public void setStationInfo(String[] netID, String[] stnCode, String[] locID, String[] chanCode)
/*     */   {
/* 639 */     this.netID = netID;
/* 640 */     this.stnCode = stnCode;
/* 641 */     this.locID = locID;
/* 642 */     this.chanCode = chanCode;
/*     */   }
/*     */   
/*     */   public synchronized void miniSeedEvent(MiniSeedPortEvent MiniSeedPortEvent)
/*     */   {
/* 647 */     GenericMiniSeedRecord gmsRec = MiniSeedPortEvent.getGenericMiniSeedRecord();
/* 648 */     if (gmsRec == null) {
/* 649 */       return;
/*     */     }
/* 651 */     if (this.bRecord) {
/* 652 */       String net = gmsRec.getNetwork();
/* 653 */       String stnCode = gmsRec.getStation();
/* 654 */       String locID = gmsRec.getLocID();
/* 655 */       String chanCode = gmsRec.getChannel();
/* 656 */       SimpleDateFormat dateFmt = new SimpleDateFormat("MM-dd-yyy");
/* 657 */       SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss.ss");
/*     */       
/* 659 */       String key = net + "_" + stnCode + "_" + locID + "_" + chanCode;
/*     */       
/*     */ 
/* 662 */       Boolean bFirst = (Boolean)this.firstTbl.get(key);
/* 663 */       PrintWriter pw = (PrintWriter)this.tbl.get(key);
/* 664 */       if (pw == null)
/* 665 */         return;
/* 666 */       if (bFirst.booleanValue()) {
/* 667 */         this.firstTbl.remove(key);
/* 668 */         this.firstTbl.put(key, new Boolean(false));
/*     */         
/* 670 */         pw.println("DATASET " + stnCode);
/* 671 */         pw.println("VERSION NXT");
/* 672 */         pw.println("SERIES " + chanCode);
/* 673 */         pw.println("DATE " + dateFmt.format(gmsRec.getStartTime()));
/* 674 */         pw.println("TIME " + timeFmt.format(gmsRec.getStartTime()));
/* 675 */         pw.println("FILE_TYPE ASCII");
/* 676 */         pw.println("NUM_SAMPS ALL");
/* 677 */         pw.println("INTERVAL " + 1.0D / gmsRec.getSampleRate());
/* 678 */         pw.println("X_OFFSET 0");
/* 679 */         pw.println("MAX_VAL 8388608");
/* 680 */         pw.println("MIN_VAL -8388608");
/* 681 */         pw.println("VERT_UNITS Counts");
/* 682 */         pw.println("HORZ_UNITS Sec");
/* 683 */         pw.println("COMMENT ");
/* 684 */         pw.println("DATA ");
/*     */       }
/* 686 */       gmsRec.decompress();
/* 687 */       int[] samps = gmsRec.getUData();
/* 688 */       int i = 0;
/* 689 */       for (;;) { pw.println(samps[i]);i++;
/* 688 */         if (i >= samps.length) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimeplot/awt/SimpleFrame.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */