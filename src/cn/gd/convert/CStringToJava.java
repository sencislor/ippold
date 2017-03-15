/*    */ package cn.gd.convert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CStringToJava
/*    */ {
/*    */   public static final String cvsid = "$Id: CStringToJava.java,v 1.3 2002/01/23 03:01:37 hwh Exp $";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void tailToZero(byte[] bStr, int nEnd)
/*    */   {
/* 23 */     bStr[(bStr.length - 1)] = 0;
/* 24 */     int beg = 0;
/* 25 */     while (bStr[(beg++)] != 0) {}
/*    */     
/*    */ 
/* 28 */     for (int i = beg; i < nEnd; i++) {
/* 29 */       bStr[i] = 0;
/*    */     }
/*    */   }
/*    */   
/*    */   public static String pad(String aString, int len, char padChar) {
/* 34 */     StringBuffer buf = new StringBuffer(aString);
/* 35 */     int alen = len - aString.length();
/* 36 */     for (int i = 0; i < alen; i++)
/* 37 */       buf.append(padChar);
/* 38 */     return buf.substring(0, len);
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/convert/CStringToJava.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */