/*    */ package cn.gd.seismology.realtimeplot.gui;
/*    */ 
/*    */ import cn.gd.seismology.realtimestream.MiniSeedStreamInf;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Toolkit;
/*    */ 
/*    */ public class IPPlotClass
/*    */ {
/* 10 */   boolean packFrame = true;
/* 11 */   IPPlotFrame frame = null;
/*    */   public static final String cvsid = "$Id: IPPlotClass.java,v 1.4 2003/07/28 06:23:34 hwh Exp $";
/*    */   
/*    */   public IPPlotClass() {
/* 15 */     this.frame = new IPPlotFrame();
/* 16 */     this.frame.setSize(1024, 768);
/*    */     
/*    */ 
/* 19 */     if (this.packFrame) {
/* 20 */       this.frame.pack();
/*    */     }
/*    */     else {
/* 23 */       this.frame.validate();
/*    */     }
/*    */     
/* 26 */     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/* 27 */     Dimension frameSize = this.frame.getSize();
/* 28 */     if (frameSize.height > screenSize.height) {
/* 29 */       frameSize.height = screenSize.height;
/*    */     }
/* 31 */     if (frameSize.width > screenSize.width) {
/* 32 */       frameSize.width = screenSize.width;
/*    */     }
/* 34 */     this.frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
/* 35 */     this.frame.setVisible(true);
/*    */   }
/*    */   
/*    */   public void setExtenalPort(MiniSeedStreamInf amssi) {
/* 39 */     this.frame.setExtenalPort(amssi);
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 46 */       javax.swing.UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
/*    */     }
/*    */     catch (Exception e) {
/* 49 */       e.printStackTrace();
/*    */     }
/* 51 */     new IPPlotClass();
/*    */   }
/*    */ }

