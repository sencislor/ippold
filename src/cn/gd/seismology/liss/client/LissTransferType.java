/*    */ package cn.gd.seismology.liss.client;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LissTransferType
/*    */ {
/* 27 */   private static String cvsId = "$Id: LissTransferType.java,v 1.2 2004/02/21 01:57:09 hwh Exp $";
/*    */   
/*    */ 
/*    */ 
/*    */   static
/*    */   {
/* 33 */     ASCII = new LissTransferType();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 38 */     BINARY = new LissTransferType();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 43 */   static String ASCII_CHAR = "A";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 48 */   static String BINARY_CHAR = "I";
/*    */   public static LissTransferType ASCII;
/*    */   public static LissTransferType BINARY;
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/liss/client/LissTransferType.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */