/*    */ package edu.iris.miniseedutils.steim;
/*    */ 
/*    */ 
/*    */ public class CompressionContinuity
/*    */ {
/*    */   public static final String cvsid = "$Id: CompressionContinuity.java,v 1.2 2001/12/19 07:02:44 hwh Exp $";
/*    */   
/*    */   short next_in;
/*    */   
/*    */   short peek_total;
/*    */   
/*    */   short next_out;
/*    */   
/*    */   int frames;
/*    */   
/*    */   int last_1;
/*    */   int last_2;
/*    */   int finalVal;
/* 19 */   int[] peeks = new int[''];
/*    */   int squeezed_flags;
/* 21 */   int[] fits = new int[33];
/* 22 */   CompressedFrame framebuf = new CompressedFrame();
/*    */   short peek_threshold;
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/miniseedutils/steim/CompressionContinuity.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */