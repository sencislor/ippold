/*     */ package cn.gd.seismology.liss.client;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LissResponse
/*     */ {
/*     */   public static final char REGARDING_AUTHENTICATION = '3';
/*     */   public static final char REGARDING_CONNECTION = '2';
/*     */   public static final char REGARDING_FILE_SYSTEM = '5';
/*     */   public static final char REGARDING_INFORMATION = '1';
/*     */   public static final char REGARDING_SYNTAX = '0';
/*     */   public static final char REGARDING_UNSPECIFIED = '4';
/*     */   public static final char REPLY_PERMANENT_NEGATIVE_COMPLETION = '5';
/*     */   public static final char REPLY_POSITIVE_COMPLETION = '2';
/*     */   public static final char REPLY_POSITIVE_INTERMEDIARY = '3';
/*     */   public static final char REPLY_POSITIVE_PRELIMINARY = '1';
/*     */   public static final char REPLY_TRANSIENT_NEGATIVE_COMPLETION = '4';
/*     */   private String message;
/*     */   private String returnCode;
/*     */   
/*     */   protected LissResponse(BufferedReader in)
/*     */     throws IOException
/*     */   {
/*  39 */     setMessage(in);
/*     */   }
/*     */   
/*     */   protected LissResponse(InputStream istr) throws IOException {
/*  43 */     setMessage(istr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  54 */     return this.message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReturnCode()
/*     */   {
/*  62 */     return this.returnCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  70 */     return this.message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPositivePreliminary()
/*     */   {
/*  78 */     return this.returnCode.charAt(0) == '1';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPositiveCompletion()
/*     */   {
/*  86 */     return this.returnCode.charAt(0) == '2';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPositiveIntermediary()
/*     */   {
/*  94 */     return this.returnCode.charAt(0) == '3';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTransientNegativeCompletion()
/*     */   {
/* 102 */     return this.returnCode.charAt(0) == '4';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPermanentNegativeCompletion()
/*     */   {
/* 110 */     return this.returnCode.charAt(0) == '5';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegardingSyntax()
/*     */   {
/* 119 */     return this.returnCode.charAt(1) == '0';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegardingInformation()
/*     */   {
/* 127 */     return this.returnCode.charAt(1) == '1';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegardingConnection()
/*     */   {
/* 135 */     return this.returnCode.charAt(1) == '2';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegardingAuthentication()
/*     */   {
/* 144 */     return this.returnCode.charAt(1) == '3';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegardingFileSystem()
/*     */   {
/* 153 */     return this.returnCode.charAt(1) == '5';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setMessage1(BufferedReader in)
/*     */     throws IOException
/*     */   {
/* 174 */     StringBuffer buffer = new StringBuffer();
/*     */     for (;;)
/*     */     {
/*     */       try {
/* 178 */         Thread.sleep(10L);
/*     */       }
/*     */       catch (InterruptedException exc) {}
/* 176 */       while (in.ready())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */         String line = in.readLine();
/* 184 */         if (this.returnCode == null)
/* 185 */           this.returnCode = line.substring(0, 3);
/* 186 */         buffer.append(line);
/* 187 */         buffer.append('\n');
/* 188 */         if ((line.charAt(3) == ' ') && 
/* 189 */           (this.returnCode.equals(line.substring(0, 3)))) break label104;
/*     */       } }
/*     */     label104:
/* 192 */     this.message = buffer.toString();
/*     */   }
/*     */   
/*     */   private void setMessage(BufferedReader reader) throws IOException {
/* 196 */     String line = reader.readLine();
/* 197 */     if (line == null)
/* 198 */       throw new EOFException("Server side close");
/* 199 */     StringBuffer reply = new StringBuffer(line);
/* 200 */     if (reply.length() < 3)
/* 201 */       throw new EOFException("Server side close 1");
/* 202 */     this.returnCode = reply.toString().substring(0, 3);
/* 203 */     reply.append("\r\n");
/*     */     
/*     */ 
/*     */ 
/* 207 */     if (reply.charAt(3) == '-') {
/* 208 */       boolean complete = false;
/* 209 */       while (!complete) {
/* 210 */         line = reader.readLine();
/* 211 */         if (line == null)
/* 212 */           throw new EOFException("Server side close");
/* 213 */         if ((line.substring(0, 3).equals(this.returnCode)) && (line.charAt(3) == ' '))
/*     */         {
/*     */ 
/* 216 */           reply.append(line);
/*     */           
/* 218 */           complete = true;
/*     */         }
/*     */         else {
/* 221 */           reply.append(line + "\r\n");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 227 */     this.message = reply.toString();
/*     */   }
/*     */   
/*     */   private void setMessage(InputStream istr) throws IOException {
/* 231 */     ByteArrayOutputStream ostr = new ByteArrayOutputStream();
/* 232 */     byte[] buffer = new byte['Ѐ'];
/*     */     for (;;) {
/* 234 */       int read = istr.read(buffer);
/* 235 */       if (read == -1) {
/*     */         break;
/*     */       }
/* 238 */       ostr.write(buffer, 0, read);
/*     */     }
/* 240 */     ostr.close();
/* 241 */     this.message = ostr.toString();
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/liss/client/LissResponse.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */