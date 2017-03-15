/*     */ package edu.iris.miniseedutils.steim;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChannelMask
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int FLAG_BAND_B = 23;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FLAG_BAND_E = 20;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FLAG_BAND_H = 22;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FLAG_BAND_L = 25;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FLAG_BAND_M = 24;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FLAG_BAND_R = 28;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FLAG_BAND_S = 21;
/*     */   
/*     */ 
/*     */   public static final int FLAG_BAND_U = 27;
/*     */   
/*     */ 
/*     */   public static final int FLAG_BAND_V = 26;
/*     */   
/*     */ 
/*     */   public static final int FLAG_COMPONENT_E = 9;
/*     */   
/*     */ 
/*     */   public static final int FLAG_COMPONENT_N = 8;
/*     */   
/*     */ 
/*     */   public static final int FLAG_COMPONENT_Z = 10;
/*     */   
/*     */ 
/*     */   public static final int FLAG_DATA_TYPE_SEISMIC = 4;
/*     */   
/*     */ 
/*     */   public static final int FLAG_DATA_TYPE_STRONG = 5;
/*     */   
/*     */ 
/*     */   public static final int FLAG_TRANSMIT_CONTINUE = 0;
/*     */   
/*     */ 
/*     */   public static final int FLAG_TRANSMIT_TRIGGER = 1;
/*     */   
/*     */ 
/*     */   public static final String cvsid = "$Id: ChannelMask.java,v 1.4 2004/02/08 05:41:23 hwh Exp $";
/*     */   
/*     */ 
/*     */   private int mask;
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelMask getFullChannelMask()
/*     */   {
/*  72 */     ChannelMask chanMask = new ChannelMask();
/*  73 */     chanMask.set();
/*  74 */     return chanMask;
/*     */   }
/*     */   
/*     */   public ChannelMask() {
/*  78 */     this.mask = 0;
/*     */   }
/*     */   
/*  81 */   public ChannelMask(int mask) { this.mask = mask; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clean()
/*     */   {
/*  88 */     this.mask = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clean(int flag)
/*     */   {
/*  95 */     this.mask &= (1 << flag ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void set(int flag)
/*     */   {
/* 102 */     this.mask |= 1 << flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void set()
/*     */   {
/* 109 */     this.mask = 535824179;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelMask get(String mask)
/*     */   {
/* 116 */     ChannelMask chanMask = null;
/* 117 */     if (mask.equalsIgnoreCase("ALL")) {
/* 118 */       chanMask = getFullChannelMask();
/* 119 */     } else if (mask.equalsIgnoreCase("CONTINUE")) {
/* 120 */       chanMask = new ChannelMask(535824177);
/* 121 */     } else if (mask.equalsIgnoreCase("TRIGGER")) {
/* 122 */       chanMask = new ChannelMask(535824178);
/* 123 */     } else if (mask.equalsIgnoreCase("SEISMIC")) {
/* 124 */       chanMask = new ChannelMask(535824147);
/* 125 */     } else if (mask.equalsIgnoreCase("STRONG")) {
/* 126 */       chanMask = new ChannelMask(535824163);
/* 127 */     } else if (mask.equalsIgnoreCase("NS")) {
/* 128 */       chanMask = new ChannelMask(535822643);
/* 129 */     } else if (mask.equalsIgnoreCase("EW")) {
/* 130 */       chanMask = new ChannelMask(535822899);
/* 131 */     } else if (mask.equalsIgnoreCase("UD")) {
/* 132 */       chanMask = new ChannelMask(535823411);
/* 133 */     } else if (mask.equalsIgnoreCase("E")) {
/* 134 */       chanMask = new ChannelMask(1050419);
/* 135 */     } else if (mask.equalsIgnoreCase("S")) {
/* 136 */       chanMask = new ChannelMask(2098995);
/* 137 */     } else if (mask.equalsIgnoreCase("H")) {
/* 138 */       chanMask = new ChannelMask(4196147);
/* 139 */     } else if (mask.equalsIgnoreCase("B")) {
/* 140 */       chanMask = new ChannelMask(8390451);
/* 141 */     } else if (mask.equalsIgnoreCase("M")) {
/* 142 */       chanMask = new ChannelMask(16779059);
/* 143 */     } else if (mask.equalsIgnoreCase("L")) {
/* 144 */       chanMask = new ChannelMask(33556275);
/* 145 */     } else if (mask.equalsIgnoreCase("V")) {
/* 146 */       chanMask = new ChannelMask(67110707);
/* 147 */     } else if (mask.equalsIgnoreCase("U")) {
/* 148 */       chanMask = new ChannelMask(134219571);
/* 149 */     } else if (mask.equalsIgnoreCase("R")) {
/* 150 */       chanMask = new ChannelMask(268437299);
/* 151 */     } else if ((mask.startsWith("0X")) || (mask.startsWith("0x"))) {
/*     */       try {
/* 153 */         chanMask = new ChannelMask(Integer.parseInt(mask.substring(2), 16));
/*     */       }
/*     */       catch (NumberFormatException nfEx) {
/* 156 */         chanMask = null;
/*     */       }
/*     */     }
/* 159 */     return chanMask;
/*     */   }
/*     */   
/*     */ 
/*     */   void set(String chanCode, String locID)
/*     */   {
/* 165 */     char bnd = chanCode.charAt(0);
/* 166 */     switch (bnd) {
/*     */     case 'E': 
/* 168 */       set(20);
/* 169 */       break;
/*     */     case 'S': 
/* 171 */       set(21);
/* 172 */       break;
/*     */     case 'H': 
/* 174 */       set(22);
/* 175 */       break;
/*     */     case 'B': 
/* 177 */       set(23);
/* 178 */       break;
/*     */     case 'M': 
/* 180 */       set(24);
/* 181 */       break;
/*     */     case 'L': 
/* 183 */       set(25);
/* 184 */       break;
/*     */     case 'V': 
/* 186 */       set(26);
/* 187 */       break;
/*     */     case 'U': 
/* 189 */       set(27);
/* 190 */       break;
/*     */     case 'R': 
/* 192 */       set(28);
/* 193 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 198 */     char type = chanCode.charAt(1);
/* 199 */     switch (type) {
/*     */     case 'A': 
/* 201 */       set(5);
/* 202 */       break;
/*     */     default: 
/* 204 */       set(4);
/*     */     }
/*     */     
/*     */     
/* 208 */     char cmp = chanCode.charAt(2);
/* 209 */     switch (cmp) {
/*     */     case 'N': 
/* 211 */       set(8);
/* 212 */       break;
/*     */     case 'E': 
/* 214 */       set(9);
/* 215 */       break;
/*     */     case 'Z': 
/* 217 */       set(10);
/* 218 */       break;
/*     */     }
/*     */     
/*     */     
/*     */     try
/*     */     {
/* 224 */       int nLocID = Integer.parseInt(locID);
/* 225 */       if (nLocID < 50) {
/* 226 */         set(0);
/*     */       } else {
/* 228 */         set(1);
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException ex) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 239 */     if ((this.mask & 0x3) == 0)
/* 240 */       return false;
/* 241 */     if ((this.mask & 0x30) == 0)
/* 242 */       return false;
/* 243 */     if ((this.mask & 0x700) == 0)
/* 244 */       return false;
/* 245 */     if ((this.mask & 0x1FF00000) == 0)
/* 246 */       return false;
/* 247 */     return true;
/*     */   }
/*     */   
/*     */   public boolean contain(ChannelMask cm) {
/* 251 */     if ((this.mask & 0x3 & cm.mask) == 0)
/* 252 */       return false;
/* 253 */     if ((this.mask & 0x30 & cm.mask) == 0)
/* 254 */       return false;
/* 255 */     if ((this.mask & 0x700 & cm.mask) == 0)
/* 256 */       return false;
/* 257 */     if ((this.mask & 0x1FF00000 & cm.mask) == 0)
/* 258 */       return false;
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   public void add(ChannelMask cm) {
/* 263 */     this.mask |= cm.mask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void remove(ChannelMask cm)
/*     */   {
/* 270 */     this.mask &= (cm.mask ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean get(int flag)
/*     */   {
/* 277 */     boolean result = false;
/* 278 */     result = (this.mask & 1 << flag) != 0;
/* 279 */     return result;
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 283 */     ChannelMask result = null;
/*     */     try {
/* 285 */       result = (ChannelMask)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException e)
/*     */     {
/* 289 */       throw new InternalError();
/*     */     }
/* 291 */     result.mask = this.mask;
/* 292 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 299 */     String hex = Integer.toHexString(this.mask);
/* 300 */     int lft = 8 - hex.length();
/* 301 */     String pad = "";
/* 302 */     for (int i = 0; i < lft; i++)
/* 303 */       pad = pad + "0";
/* 304 */     return pad + hex;
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/miniseedutils/steim/ChannelMask.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */