/*    */ package edu.iris.miniseedutils.steim;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompressedFrame
/*    */ {
/*    */   public static final String cvsid = "$Id: CompressedFrame.java,v 1.2 2001/12/19 07:02:44 hwh Exp $";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 15 */   int[] cfp = new int[16];
/*    */   
/*    */   protected int[] getFrameArray() {
/* 18 */     int[] retArray = new int[16];
/* 19 */     for (int i = 0; i < 16; i++) {
/* 20 */       retArray[i] = this.cfp[i];
/*    */     }
/* 22 */     return retArray;
/*    */   }
/*    */   
/*    */   protected boolean setFrameArray(int[] newArray) {
/* 26 */     if (newArray.length != 16) {
/* 27 */       return false;
/*    */     }
/* 29 */     this.cfp = new int[16];
/* 30 */     for (int i = 0; i < 16; i++) {
/* 31 */       this.cfp[i] = newArray[i];
/*    */     }
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/miniseedutils/steim/CompressedFrame.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */