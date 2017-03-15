/*     */ package cn.gd.seismology.realtimeplot.gui;
/*     */ 
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEvent;
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener;
/*     */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SimpleJFrame
/*     */   extends JFrame
/*     */   implements MiniSeedPortEventListener
/*     */ {
/* 543 */   boolean bRecord = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 554 */   Hashtable tbl = new Hashtable();
/* 555 */   Hashtable firstTbl = new Hashtable();
/*     */   String[] chanCode;
/*     */   JMenuItem jmItemStartRecord;
/*     */   
/* 559 */   public SimpleJFrame() { try { jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/* 562 */       ex.printStackTrace();
/*     */     } }
/*     */   
/*     */   JMenuItem jmItemStopRecord;
/*     */   
/* 567 */   public SimpleJFrame(String title) { super(title);
/*     */     try {
/* 569 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/* 572 */       ex.printStackTrace();
/*     */     } }
/*     */   
/*     */   JMenu jmenuFile;
/*     */   JMenuBar jmenubar;
/*     */   
/* 578 */   private void jbInit() throws Exception { this.jmenuFile = new JMenu("File");
/* 579 */     this.jmItemStartRecord = new JMenuItem("Start Record...");
/* 580 */     this.jmenuFile.add(this.jmItemStartRecord);
/* 581 */     this.jmItemStartRecord.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 583 */         SimpleJFrame.this.jmItemStartRecord_actionPerformed(e);
/*     */       }
/* 585 */     });
/* 586 */     this.jmItemStopRecord = new JMenuItem("Stop Record");
/* 587 */     this.jmItemStopRecord.setEnabled(false);
/* 588 */     this.jmItemStopRecord.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 590 */         SimpleJFrame.this.jmItemStopRecord_actionPerformed(e);
/*     */       }
/*     */       
/* 593 */     });
/* 594 */     this.jmenuFile.add(this.jmItemStopRecord);
/*     */     
/* 596 */     this.jmenubar = new JMenuBar();
/* 597 */     this.jmenubar.add(this.jmenuFile);
/* 598 */     setJMenuBar(this.jmenubar); }
/*     */   
/*     */   String[] locID;
/*     */   
/* 602 */   synchronized void jmItemStartRecord_actionPerformed(ActionEvent e) { this.bRecord = true;
/* 603 */     this.jmItemStartRecord.setEnabled(false);
/* 604 */     this.jmItemStopRecord.setEnabled(true);
/*     */     try {
/* 606 */       for (int i = 0; i < this.netID.length; i++) {
/* 607 */         String key = this.netID[i] + "_" + this.stnCode[i] + "_" + this.locID[i] + "_" + this.chanCode[i];
/* 608 */         PrintWriter pw = new PrintWriter(new FileOutputStream(key));
/* 609 */         this.tbl.put(key, pw);
/* 610 */         this.firstTbl.put(key, new Boolean(true));
/*     */       }
/*     */     }
/*     */     catch (IOException ioEx) {
/* 614 */       ioEx.printStackTrace();
/*     */     } }
/*     */   
/*     */   String[] netID;
/*     */   String[] stnCode;
/* 619 */   synchronized void jmItemStopRecord_actionPerformed(ActionEvent e) { this.bRecord = false;
/* 620 */     this.jmItemStartRecord.setEnabled(true);
/* 621 */     this.jmItemStopRecord.setEnabled(false);
/* 622 */     for (int i = 0; i < this.netID.length; i++) {
/* 623 */       String key = this.netID[i] + "_" + this.stnCode[i] + "_" + this.locID[i] + "_" + this.chanCode[i];
/* 624 */       PrintWriter pw = (PrintWriter)this.tbl.get(key);
/* 625 */       pw.flush();
/* 626 */       pw.close();
/* 627 */       this.tbl.remove(key);
/* 628 */       this.firstTbl.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStationInfo(String[] netID, String[] stnCode, String[] locID, String[] chanCode) {
/* 633 */     this.netID = netID;
/* 634 */     this.stnCode = stnCode;
/* 635 */     this.locID = locID;
/* 636 */     this.chanCode = chanCode;
/*     */   }
/*     */   
/*     */   public synchronized void miniSeedEvent(MiniSeedPortEvent MiniSeedPortEvent)
/*     */   {
/* 641 */     GenericMiniSeedRecord gmsRec = MiniSeedPortEvent.getGenericMiniSeedRecord();
/* 642 */     if (gmsRec == null) {
/* 643 */       return;
/*     */     }
/* 645 */     if (this.bRecord) {
/* 646 */       String net = gmsRec.getNetwork();
/* 647 */       String stnCode = gmsRec.getStation();
/* 648 */       String locID = gmsRec.getLocID();
/* 649 */       String chanCode = gmsRec.getChannel();
/* 650 */       SimpleDateFormat dateFmt = new SimpleDateFormat("MM-dd-yyy");
/* 651 */       SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss.ss");
/*     */       
/* 653 */       String key = net + "_" + stnCode + "_" + locID + "_" + chanCode;
/*     */       
/*     */ 
/* 656 */       Boolean bFirst = (Boolean)this.firstTbl.get(key);
/* 657 */       PrintWriter pw = (PrintWriter)this.tbl.get(key);
/* 658 */       if (pw == null)
/* 659 */         return;
/* 660 */       if (bFirst.booleanValue()) {
/* 661 */         this.firstTbl.remove(key);
/* 662 */         this.firstTbl.put(key, new Boolean(false));
/*     */         
/* 664 */         pw.println("DATASET " + stnCode);
/* 665 */         pw.println("VERSION NXT");
/* 666 */         pw.println("SERIES " + chanCode);
/* 667 */         pw.println("DATE " + dateFmt.format(gmsRec.getStartTime()));
/* 668 */         pw.println("TIME " + timeFmt.format(gmsRec.getStartTime()));
/* 669 */         pw.println("FILE_TYPE ASCII");
/* 670 */         pw.println("NUM_SAMPS ALL");
/* 671 */         pw.println("INTERVAL " + 1.0D / gmsRec.getSampleRate());
/* 672 */         pw.println("X_OFFSET 0");
/* 673 */         pw.println("MAX_VAL 8388608");
/* 674 */         pw.println("MIN_VAL -8388608");
/* 675 */         pw.println("VERT_UNITS Counts");
/* 676 */         pw.println("HORZ_UNITS Sec");
/* 677 */         pw.println("COMMENT ");
/* 678 */         pw.println("DATA ");
/*     */       }
/* 680 */       gmsRec.decompress();
/* 681 */       int[] samps = gmsRec.getUData();
/* 682 */       int i = 0;
/* 683 */       for (;;) { pw.println(samps[i]);i++;
/* 682 */         if (i >= samps.length) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimeplot/gui/SimpleJFrame.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */