/*     */ package cn.gd.convert;
/*     */ 
/*     */ import java.awt.Point;
/*     */ 
/*     */ public class CoordinateTran
/*     */ {
/*     */   public static final String cvsid = "$Id: CoordinateTran.java,v 1.3 2003/02/03 07:24:46 hwh Exp $";
/*     */   Point OPixel;
/*     */   float XScale;
/*     */   float YScale;
/*     */   float Ox;
/*     */   float Oy;
/*  13 */   IllegalArgumentException coordinateTranException = new IllegalArgumentException("The Scale argument must be positive!");
/*  14 */   final int maxInt = 8192;
/*  15 */   final int minInt = 57344;
/*     */   
/*     */   public CoordinateTran(Point aOPixel, float aOx, float aOy, float aXScale, float aYScale)
/*     */   {
/*  19 */     if (aXScale <= 0.0D) {
/*  20 */       System.err.println("XScale: " + aXScale);
/*  21 */       throw this.coordinateTranException;
/*     */     }
/*  23 */     if (aYScale <= 0.0D) {
/*  24 */       System.err.println("YScale: " + aYScale);
/*  25 */       throw this.coordinateTranException;
/*     */     }
/*     */     
/*  28 */     this.OPixel = aOPixel;
/*  29 */     this.Ox = aOx;
/*  30 */     this.Oy = aOy;
/*  31 */     this.XScale = aXScale;
/*  32 */     this.YScale = aYScale;
/*     */   }
/*     */   
/*     */   public CoordinateTran()
/*     */   {
/*  37 */     this.OPixel = new Point(0, 0);
/*  38 */     this.Ox = 0.0F;
/*  39 */     this.Oy = 0.0F;
/*  40 */     this.XScale = 1.0F;
/*  41 */     this.YScale = 1.0F;
/*     */   }
/*     */   
/*     */   public void setTran(Point aOPixel, float aOx, float aOy, float aXScale, float aYScale) {
/*  45 */     if (aXScale <= 0.0D) {
/*  46 */       System.err.println("XScale: " + aXScale);
/*  47 */       throw this.coordinateTranException;
/*     */     }
/*  49 */     if (aYScale <= 0.0D) {
/*  50 */       System.err.println("YScale: " + aYScale);
/*  51 */       throw this.coordinateTranException;
/*     */     }
/*  53 */     this.OPixel = aOPixel;
/*  54 */     this.Ox = aOx;
/*  55 */     this.Oy = aOy;
/*  56 */     this.XScale = aXScale;
/*  57 */     this.YScale = aYScale;
/*     */   }
/*     */   
/*     */   public void setXScale(float aXScale)
/*     */   {
/*  62 */     if (aXScale <= 0.0D) {
/*  63 */       System.err.println("XScale: " + aXScale);
/*  64 */       throw this.coordinateTranException;
/*     */     }
/*  66 */     this.XScale = aXScale;
/*     */   }
/*     */   
/*  69 */   public void setYScale(float aYScale) { if (aYScale <= 0.0D) {
/*  70 */       System.err.println("YScale: " + aYScale);
/*  71 */       throw this.coordinateTranException;
/*     */     }
/*  73 */     this.YScale = aYScale;
/*     */   }
/*     */   
/*     */   public float getXScale() {
/*  77 */     return this.XScale;
/*     */   }
/*     */   
/*  80 */   public float getYScale() { return this.YScale; }
/*     */   
/*     */   public Point getOPixel()
/*     */   {
/*  84 */     return this.OPixel;
/*     */   }
/*     */   
/*  87 */   public void setOPixel(Point aOPixel) { this.OPixel = aOPixel; }
/*     */   
/*     */   public void setOx(float aOx) {
/*  90 */     this.Ox = aOx;
/*     */   }
/*     */   
/*  93 */   public float getOx() { return this.Ox; }
/*     */   
/*     */   public void setOy(float aOy) {
/*  96 */     this.Oy = aOy;
/*     */   }
/*     */   
/*  99 */   public float getOy() { return this.Oy; }
/*     */   
/*     */   public Point toPixel(float X, float Y)
/*     */   {
/* 103 */     Point Pixel = new Point(0, 0);
/* 104 */     Pixel.x = (this.OPixel.x + Math.round((X - this.Ox) / this.XScale));
/* 105 */     int temp = Math.round((Y - this.Oy) / this.YScale);
/* 106 */     Pixel.y = (this.OPixel.y - temp);
/* 107 */     if (Pixel.y > 8192)
/* 108 */       Pixel.y = 8192;
/* 109 */     if (Pixel.y < 57344)
/* 110 */       Pixel.y = 57344;
/* 111 */     return Pixel;
/*     */   }
/*     */   
/*     */   public Point toPixel(int X, int Y) {
/* 115 */     Point Pixel = new Point(0, 0);
/* 116 */     Pixel.x = (this.OPixel.x + Math.round((X - this.Ox) / this.XScale));
/* 117 */     int temp = Math.round((Y - this.Oy) / this.YScale);
/* 118 */     Pixel.y = (this.OPixel.y - temp);
/* 119 */     if (Pixel.y > 8192)
/* 120 */       Pixel.y = 8192;
/* 121 */     if (Pixel.y < 57344)
/* 122 */       Pixel.y = 57344;
/* 123 */     return Pixel;
/*     */   }
/*     */   
/*     */   public Point toPixel(int X, double Y) {
/* 127 */     Point Pixel = new Point(0, 0);
/* 128 */     Pixel.x = (this.OPixel.x + Math.round((X - this.Ox) / this.XScale));
/* 129 */     int temp = (int)Math.round((Y - this.Oy) / this.YScale);
/* 130 */     Pixel.y = (this.OPixel.y - temp);
/* 131 */     if (Pixel.y > 8192)
/* 132 */       Pixel.y = 8192;
/* 133 */     if (Pixel.y < 57344)
/* 134 */       Pixel.y = 57344;
/* 135 */     return Pixel;
/*     */   }
/*     */   
/*     */   public float toOrgX(Point Pixel)
/*     */   {
/* 140 */     return this.Ox + (Pixel.x - this.OPixel.x) * this.XScale;
/*     */   }
/*     */   
/* 143 */   public float toOrgY(Point Pixel) { return this.Oy + (this.OPixel.y - Pixel.y) * this.YScale; }
/*     */   
/*     */   public String toString()
/*     */   {
/* 147 */     return "[" + this.OPixel + "]--(" + this.Ox + "," + this.Oy + ")--{" + this.XScale + "," + this.YScale + "}";
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/convert/CoordinateTran.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */