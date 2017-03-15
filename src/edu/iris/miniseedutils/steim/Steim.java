/*      */ package edu.iris.miniseedutils.steim;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Steim
/*      */ {
/*      */   public static final String cvsid = "$Id: Steim.java,v 1.2 2001/12/19 07:02:45 hwh Exp $";
/*      */   protected static final boolean STATISTICS = true;
/*      */   protected static final int PEEKELEMS = 150;
/*      */   protected static final int WORDS_PER_FRAME = 16;
/*      */   protected static final int WORD_SIZE = 4;
/*      */   protected static final int MAXSAMPPERWORD = 9;
/*      */   protected static final int ICONSTCODE = -536870912;
/*      */   protected static final int ICONSTMASK = 536870911;
/*      */   protected static final int ICONSTSIGN = 268435456;
/*      */   protected static final int NOTDIFFERENCE = -939524096;
/*      */   protected static final int SECONDDIFF = Integer.MIN_VALUE;
/*      */   protected static final int REPLACEMENTMASK = 1879048192;
/*      */   protected static final int UNPACKSIZE = 150;
/*      */   protected static final int UNPACKFINAL = 148;
/*      */   protected static final int EDF_OK = 0;
/*      */   protected static final int EDF_INTEGRESYNC = 1;
/*      */   protected static final int EDF_INTEGFAIL = 2;
/*      */   protected static final int EDF_SECSUBCODE = 4;
/*      */   protected static final int EDF_TWOBLOCK = 8;
/*      */   protected static final int EDF_OVERRUN = 16;
/*      */   protected static final int EDF_REPLACEMENT = 32;
/*      */   protected static final int EDF_LASTERROR = 64;
/*      */   protected static final int EDF_COUNTERROR = 128;
/*      */   protected static final int EDF_FATAL = 62;
/*      */   protected static final int EDF_NONFATAL = 193;
/*      */   protected static final int MAXTOTALFRAMESPERRECORD = 64;
/*      */   protected static final int GENERICHDRL = 64;
/*      */   protected static final int B7X4 = 8;
/*      */   protected static final int B9X3 = 24;
/*      */   protected static final int B6X5 = 1;
/*      */   protected static final int B5X6 = 0;
/*      */   protected static final int B5X12 = 0;
/*      */   protected static final int B3X20 = 1;
/*      */   protected static final int B3X10 = 3;
/*      */   protected static final int B2X15 = 2;
/*      */   protected static final int B1X30 = 1;
/*      */   protected static final int HUGE = Integer.MAX_VALUE;
/*      */   protected static final int LARGE = 1073741823;
/*      */   protected static final int BIG = 536870911;
/*      */   protected static final int GRAND = 16777215;
/*      */   protected static final int FAT = 8388607;
/*      */   protected static final int F8X3 = 16777216;
/*      */   protected static final int F6X4 = 33554432;
/*      */   protected static final int F4X6 = 50331648;
/*      */   protected static final int F3X8 = 67108864;
/*      */   protected static final int F2X12 = 83886080;
/*      */   protected static final int F1X24 = 100663296;
/*      */   
/*      */   public static short makeShort(byte left, byte right)
/*      */   {
/*  122 */     short out = (short)(left << 8);
/*  123 */     if (right < 0) {
/*  124 */       out = (short)(out + (256 + right));
/*      */     } else {
/*  126 */       out = (short)(out + right);
/*      */     }
/*  128 */     return out;
/*      */   }
/*      */   
/*      */   public static short makeShort(byte left, byte right, boolean swapFlag) {
/*      */     short out;
/*  133 */     if (swapFlag) {
/*  134 */       out = makeShort(right, left);
/*      */     } else {
/*  136 */       out = makeShort(left, right);
/*      */     }
/*  138 */     return out;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int makeInt(byte leftest, byte left, byte right, byte rightest)
/*      */   {
/*  144 */     short out_left = makeShort(leftest, left);
/*  145 */     short out_right = makeShort(right, rightest);
/*  146 */     int out = out_left << 16;
/*  147 */     if (out_right < 0) {
/*  148 */       out += 65536 + out_right;
/*      */     } else {
/*  150 */       out += out_right;
/*      */     }
/*  152 */     return out;
/*      */   }
/*      */   
/*      */   public static int makeInt(byte leftest, byte left, byte right, byte rightest, boolean swapFlag) { int out;
/*  156 */     if (swapFlag) {
/*  157 */       out = makeInt(rightest, right, left, leftest);
/*      */     } else {
/*  159 */       out = makeInt(leftest, left, right, rightest);
/*      */     }
/*  161 */     return out;
/*      */   }
/*      */   
/*      */   public static float makeFloat(byte leftest, byte left, byte right, byte rightest) {
/*  165 */     int intVal = makeInt(leftest, left, right, rightest);
/*  166 */     return Float.intBitsToFloat(intVal);
/*      */   }
/*      */   
/*      */   public static float makeFloat(byte leftest, byte left, byte right, byte rightest, boolean swapFlag)
/*      */   {
/*  171 */     int intVal = makeInt(leftest, left, right, rightest, swapFlag);
/*  172 */     return Float.intBitsToFloat(intVal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void endianFlipFrame(int[] framep)
/*      */   {
/*  180 */     for (int i = 0; i < 16; i++) {
/*  181 */       framep[i] = endianflip(framep[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int endianflip(int e)
/*      */   {
/*  192 */     short lshort = (short)(e >> 16);
/*  193 */     short rshort = (short)(e & 0xFFFF);
/*      */     
/*  195 */     byte leftest = (byte)(lshort >> 8);
/*  196 */     byte left = (byte)(lshort & 0xFF);
/*  197 */     byte right = (byte)(rshort >> 8);
/*  198 */     byte rightest = (byte)(rshort & 0xFF);
/*      */     
/*  200 */     return makeInt(rightest, right, left, leftest);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private short swapb(short s)
/*      */   {
/*  211 */     byte left = (byte)(s >> 8);
/*  212 */     byte right = (byte)(s & 0xFF);
/*      */     
/*  214 */     return makeShort(right, left);
/*      */   }
/*      */   
/*      */ 
/*      */   private static abstract interface CodeType
/*      */   {
/*      */     public static final int c9x3 = 0;
/*      */     
/*      */     public static final int c7x4 = 1;
/*      */     
/*      */     public static final int c6x5 = 2;
/*      */     
/*      */     public static final int c5x6 = 3;
/*      */     
/*      */     public static final int c4x8 = 4;
/*      */     
/*      */     public static final int c3x10 = 5;
/*      */     
/*      */     public static final int c5x12 = 6;
/*      */     
/*      */     public static final int c2x15 = 7;
/*      */     
/*      */     public static final int c2x16 = 8;
/*      */     
/*      */     public static final int c1x30 = 9;
/*      */     
/*      */     public static final int c1x32 = 10;
/*      */     
/*      */     public static final int c8x3 = 11;
/*      */     
/*      */     public static final int c6x4 = 12;
/*      */     
/*      */     public static final int c4x6 = 13;
/*      */     
/*      */     public static final int c3x8 = 14;
/*      */     
/*      */     public static final int c2x12 = 15;
/*      */     
/*      */     public static final int c1x24 = 16;
/*      */     public static final int c3x20 = 17;
/*      */   }
/*      */   
/*      */   private class CompressSeqType
/*      */   {
/*      */     short next;
/*      */     short scan;
/*      */     int ccVal;
/*      */     int bc;
/*      */     int cbits;
/*      */     char shift;
/*      */     int mask;
/*      */     int disc;
/*      */     short fit;
/*      */     
/*      */     public CompressSeqType() {}
/*      */     
/*      */     public CompressSeqType(int next, int scan, int ccVal, int bc, int cbits, char shift, int mask, int disc, short fit)
/*      */     {
/*  272 */       this.next = ((short)next);
/*  273 */       this.scan = ((short)scan);
/*  274 */       this.ccVal = ccVal;
/*  275 */       this.bc = bc;
/*  276 */       this.cbits = cbits;
/*  277 */       this.shift = shift;
/*  278 */       this.mask = mask;
/*  279 */       this.disc = disc;
/*  280 */       this.fit = fit;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class DecompBitType
/*      */   {
/*      */     short samps;
/*      */     
/*      */     char postshift;
/*      */     int mask;
/*      */     int hibit;
/*      */     int neg;
/*      */     
/*      */     public DecompBitType() {}
/*      */     
/*      */     public DecompBitType(short samps, char postshift, int mask, int hibit, int neg)
/*      */     {
/*  298 */       this.samps = samps;
/*  299 */       this.postshift = postshift;
/*  300 */       this.mask = mask;
/*  301 */       this.hibit = hibit;
/*  302 */       this.neg = neg;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class FlagRepType
/*      */   {
/*      */     int fword;
/*      */     int repcode;
/*      */     
/*      */     public FlagRepType() {}
/*      */     
/*      */     public FlagRepType(int fword, int repcode)
/*      */     {
/*  316 */       this.fword = fword;
/*  317 */       this.repcode = repcode;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   short compressFrame(CompressionContinuity ccp, short difference, short level, short reserve, short flip)
/*      */   {
/*  325 */     CompressSeqType[] compseq = { new CompressSeqType(1, 4, 4, 1, 0, '\b', 255, 127, 8), new CompressSeqType(2, 2, 8, 2, 0, '\020', 65535, 32767, 16), new CompressSeqType(-1, 1, 10, 3, 0, ' ', -1, Integer.MAX_VALUE, 32), new CompressSeqType(4, 7, 1, 3, 8, '\004', 15, 7, 4), new CompressSeqType(5, 6, 2, 3, 1, '\005', 31, 15, 5), new CompressSeqType(6, 5, 3, 3, 0, '\006', 63, 31, 6), new CompressSeqType(7, 4, 4, 1, 0, '\b', 255, 127, 8), new CompressSeqType(8, 3, 5, 2, 3, '\n', 1023, 511, 10), new CompressSeqType(9, 2, 7, 2, 2, '\017', 32767, 16383, 15), new CompressSeqType(-1, 1, 9, 2, 1, '\036', 1073741823, 536870911, 30), new CompressSeqType(11, 9, 0, 3, 24, '\003', 7, 3, 3), new CompressSeqType(12, 7, 1, 3, 8, '\004', 15, 7, 4), new CompressSeqType(13, 6, 2, 3, 1, '\005', 31, 15, 5), new CompressSeqType(14, 5, 3, 3, 0, '\006', 63, 31, 6), new CompressSeqType(15, 4, 4, 1, 0, '\b', 255, 127, 8), new CompressSeqType(16, 3, 5, 2, 3, '\n', 1023, 511, 10), new CompressSeqType(17, 5, 6, 2, 0, '\f', 4095, 2047, 12), new CompressSeqType(18, 2, 7, 2, 2, '\017', 32767, 16383, 15), new CompressSeqType(19, 2, 8, 0, 0, '\020', 65535, 32767, 16), new CompressSeqType(20, 3, 17, 2, 1, '\024', 1048575, 524287, 20), new CompressSeqType(-1, 1, 9, 2, 1, '\036', 1073741823, 536870911, 30), new CompressSeqType(22, 8, 11, 0, 16777216, '\003', 7, 3, 3), new CompressSeqType(23, 6, 12, 0, 33554432, '\004', 15, 7, 4), new CompressSeqType(24, 4, 13, 0, 50331648, '\006', 63, 31, 6), new CompressSeqType(25, 3, 14, 0, 67108864, '\b', 255, 127, 8), new CompressSeqType(26, 2, 15, 0, 83886080, '\f', 4095, 2047, 12), new CompressSeqType(-1, 1, 16, 0, 100663296, '\030', 16777215, 8388607, 24) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  354 */     FlagRepType[] commonflags = { new FlagRepType(357913941, 1342177280), new FlagRepType(715827882, 1610612736), new FlagRepType(1073741823, 1879048192) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  363 */     int[] diffs = new int[9];
/*  364 */     int[] s = new int[11];
/*      */     
/*      */ 
/*  367 */     short ctabx = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  374 */     short ctabhi = 0;short ctablo = 0;short ctabw = 0;short ctabfit = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  382 */     int flag_word = 0;
/*  383 */     short total_in_peek_buffer_at_entry = ccp.peek_total;
/*  384 */     short number_of_samples = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  390 */     int samp_1 = ccp.peeks[ccp.next_out];
/*  391 */     int samp_2 = ccp.peeks[((ccp.next_out + 1) % 150)];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  401 */     if (reserve >= 16)
/*  402 */       reserve = 15;
/*  403 */     short block = 1;
/*  404 */     int block_code; while (block < reserve + 1) {
/*  405 */       if (level < 3)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  410 */         block_code = 0;
/*  411 */         switch (block) {
/*      */         case 1: 
/*  413 */           if (ccp.peek_total >= 1) {
/*  414 */             ccp.framebuf.cfp[block] = samp_1;
/*      */           } else
/*  416 */             ccp.framebuf.cfp[block] = 0;
/*  417 */           break;
/*      */         default: 
/*  419 */           ccp.framebuf.cfp[block] = 0;
/*  420 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/*  430 */         block_code = 3;
/*  431 */         switch (block) {
/*      */         case 1: 
/*  433 */           if (ccp.peek_total >= 1) {
/*  434 */             ccp.framebuf.cfp[block] = (samp_1 & 0x1FFFFFFF | 0xE0000000);
/*      */           } else
/*  436 */             ccp.framebuf.cfp[block] = -939524096;
/*  437 */           break;
/*      */         case 2: 
/*  439 */           if (ccp.peek_total >= 2) {
/*  440 */             ccp.framebuf.cfp[block] = (samp_2 & 0x1FFFFFFF | 0xE0000000);
/*      */           } else
/*  442 */             ccp.framebuf.cfp[block] = -939524096;
/*  443 */           break;
/*      */         default: 
/*  445 */           ccp.framebuf.cfp[block] = -939524096;
/*      */         }
/*      */         
/*      */       }
/*  449 */       flag_word = (flag_word << 2) + block_code;
/*  450 */       block = (short)(block + 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  459 */     switch (level) {
/*      */     case 3: 
/*  461 */       ctablo = 10;
/*  462 */       ctabhi = 20;
/*  463 */       ctabx = 13;
/*  464 */       break;
/*      */     case 2: 
/*  466 */       ctablo = 3;
/*  467 */       ctabhi = 9;
/*  468 */       ctabx = 5;
/*  469 */       break;
/*      */     case 1: 
/*  471 */       ctablo = 0;
/*  472 */       ctabhi = 2;
/*  473 */       ctabx = 0;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  480 */     while (block < 16) {
/*  481 */       s[0] = ccp.last_2;
/*  482 */       s[1] = ccp.last_1;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       short empty;
/*      */       
/*      */ 
/*      */ 
/*  491 */       if (ccp.peek_total <= 0) {
/*  492 */         empty = 0;
/*      */       } else {
/*  494 */         empty = -1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  504 */       short hiscan = 0;
/*  505 */       done = false;
/*  506 */       ctabfit = -1;
/*  507 */       ctabw = ctabx;
/*      */       do {
/*  509 */         sp = compseq[ctabx];
/*  510 */         t_scan = sp.scan;
/*  511 */         int t_disc = sp.disc;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  522 */         if (ccp.peek_total >= t_scan) {
/*  523 */           if (difference == 1) {
/*  524 */             while (hiscan < t_scan) {
/*  525 */               s[(hiscan + 2)] = ccp.peeks[((ccp.next_out + hiscan) % 150)];
/*  526 */               diffs[hiscan] = (s[(hiscan + 2)] - s[(hiscan + 1)]);
/*  527 */               hiscan = (short)(hiscan + 1);
/*      */             }
/*      */           } else
/*  530 */             while (hiscan < t_scan) {
/*  531 */               s[(hiscan + 2)] = ccp.peeks[((ccp.next_out + hiscan) % 150)];
/*  532 */               diffs[hiscan] = (s[(hiscan + 2)] - (s[(hiscan + 1)] << 1) + s[hiscan]);
/*  533 */               hiscan = (short)(hiscan + 1);
/*      */             }
/*      */         } else
/*  536 */           while (hiscan < t_scan) {
/*  537 */             short pop = (short)((ccp.next_out + hiscan) % 150);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  542 */             if ((pop == ccp.next_in) && (ccp.peek_total < 150) && (empty < 0)) {
/*  543 */               empty = hiscan;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  551 */             if (empty < 0) {
/*  552 */               s[(hiscan + 2)] = ccp.peeks[pop];
/*  553 */               if (difference == 1) {
/*  554 */                 diffs[hiscan] = (s[(hiscan + 2)] - s[(hiscan + 1)]);
/*      */               } else
/*  556 */                 diffs[hiscan] = (s[(hiscan + 2)] - (s[(hiscan + 1)] << 1) + s[hiscan]);
/*      */             } else {
/*  558 */               s[(hiscan + 2)] = 0;
/*  559 */               diffs[hiscan] = 0;
/*      */             }
/*  561 */             hiscan = (short)(hiscan + 1);
/*      */           }
/*  563 */         if (empty < 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  569 */           for (i = 0; i < t_scan; i = (short)(i + 1)) {
/*  570 */             if (Math.abs(diffs[i]) > t_disc)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  578 */               if (ctabfit < 0) {
/*  579 */                 ctabx = sp.next; break;
/*      */               }
/*  581 */               ctabx = ctabfit;
/*  582 */               done = true;
/*      */               
/*  584 */               break; }
/*  585 */             if (i == t_scan - 1)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  591 */               if (ctabx > ctabw) {
/*  592 */                 done = true; break;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  600 */               if (ctabx > ctablo) {
/*  601 */                 ctabfit = ctabx;
/*  602 */                 ctabx = (short)(ctabx - 1); break;
/*      */               }
/*  604 */               done = true;
/*  605 */               break;
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/*  611 */         else if (empty > 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  619 */           ctabw = ctabx;
/*  620 */           ctabfit = -1;
/*  621 */           while ((ctabx >= 0) && (compseq[ctabx].scan - 1 >= empty)) {
/*  622 */             ctabx = compseq[ctabx].next;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  627 */           empty = -1;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  634 */           ctabx = ctabhi;
/*  635 */           break;
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  641 */       while ((ctabx >= 0) && (!done));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  649 */       if (ctabx < 0) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  657 */       if ((block == 15) && ((compseq[ctabx].ccVal == 6) || (compseq[ctabx].ccVal == 17)))
/*      */       {
/*  659 */         ctabx = compseq[ctabx].next;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  664 */       sp = compseq[ctabx];
/*  665 */       short t_scan = sp.scan;
/*  666 */       int t_mask = sp.mask;
/*  667 */       ccp.last_2 = s[t_scan];
/*  668 */       ccp.last_1 = s[(t_scan + 1)];
/*  669 */       ccp.peek_total = ((short)(ccp.peek_total - t_scan));
/*  670 */       ccp.next_out = ((short)((ccp.next_out + t_scan) % 150));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  677 */       if (ccp.peek_total < 0) {
/*  678 */         if (level < 3) {
/*  679 */           block_code = 0;
/*  680 */           ccp.framebuf.cfp[block] = 0;
/*      */         } else {
/*  682 */           block_code = 3;
/*  683 */           ccp.framebuf.cfp[block] = -939524096;
/*      */ 
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  695 */         ccp.finalVal = ccp.last_1;
/*  696 */         block_code = sp.bc;
/*  697 */         ccp.fits[sp.fit] += t_scan;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  705 */         short i2 = 0;
/*  706 */         accum = sp.cbits;
/*  707 */         switch (sp.ccVal) {
/*      */         case 6: 
/*      */         case 17: 
/*  710 */           char t_halfshift = (char)(sp.shift >> '\001');
/*  711 */           for (accum = 0; 
/*  712 */               i2 < t_scan >> 1; 
/*  713 */               accum = accum << sp.shift | t_mask & diffs[i2]) i2 = (short)(i2 + 1);
/*  714 */           accum = accum << t_halfshift | (t_mask & diffs[i2]) >> t_halfshift;
/*  715 */           ccp.framebuf.cfp[block] = accum;
/*  716 */           flag_word = (flag_word << 2) + block_code;
/*  717 */           block = (short)(block + 1);
/*  718 */           accum = sp.cbits;
/*  719 */           i2 = (short)(i2 + 1);accum = accum << t_halfshift | t_mask >> t_halfshift & diffs[i2];
/*      */         }
/*  721 */         char t_shift = sp.shift;
/*  722 */         for (; i2 < t_scan; 
/*  723 */             accum = accum << t_shift | t_mask & diffs[i2]) i2 = (short)(i2 + 1);
/*  724 */         ccp.framebuf.cfp[block] = accum;
/*      */       }
/*      */       
/*      */ 
/*  728 */       flag_word = (flag_word << 2) + block_code;
/*  729 */       block = (short)(block + 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  736 */     while (block < 16) {
/*  737 */       if (level < 3) {
/*  738 */         block_code = 0;
/*  739 */         ccp.framebuf.cfp[block] = 0;
/*      */       } else {
/*  741 */         block_code = 3;
/*  742 */         ccp.framebuf.cfp[block] = -939524096;
/*      */       }
/*  744 */       flag_word = (flag_word << 2) + block_code;
/*  745 */       block = (short)(block + 1);
/*      */     }
/*      */     
/*      */ 
/*      */     int diffbit;
/*      */     
/*      */ 
/*  752 */     if (difference == 1) {
/*  753 */       diffbit = 0;
/*      */     } else {
/*  755 */       diffbit = Integer.MIN_VALUE;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  760 */     short clipped_total = ccp.peek_total;
/*  761 */     if (clipped_total < 0)
/*  762 */       clipped_total = 0;
/*  763 */     number_of_samples = (short)(total_in_peek_buffer_at_entry - clipped_total);
/*  764 */     ccp.framebuf.cfp[0] = (flag_word | diffbit);
/*  765 */     ccp.frames += 1;
/*      */     
/*      */ 
/*      */ 
/*  769 */     if (flip != 0) {
/*  770 */       endianFlipFrame(ccp.framebuf.cfp);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  775 */     if (reserve >= 15) {
/*  776 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  780 */     if (ctabx < 0) {
/*  781 */       if (number_of_samples == 0) {
/*  782 */         return -1;
/*      */       }
/*  784 */       return number_of_samples;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  789 */     if ((level != 3) || (ccp.peek_total < 8)) {
/*  790 */       return number_of_samples;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  795 */     short i = 0;
/*  796 */     while (i < 3) {
/*  797 */       if (flag_word == commonflags[i].fword) {
/*      */         break;
/*      */       }
/*  800 */       i = (short)(i + 1); }
/*  801 */     if (i >= 3)
/*  802 */       return number_of_samples;
/*  803 */     int flag_replacement = commonflags[i].repcode;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  810 */     s[0] = ccp.last_2;
/*  811 */     s[1] = ccp.last_1;
/*  812 */     for (i = 0; i < 8; i = (short)(i + 1)) {
/*  813 */       s[(i + 2)] = ccp.peeks[((ccp.next_out + i) % 150)];
/*  814 */       if (difference == 1) {
/*  815 */         diffs[i] = (s[(i + 2)] - s[(i + 1)]);
/*      */       } else {
/*  817 */         diffs[i] = (s[(i + 2)] - (s[(i + 1)] << 1) + s[i]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  822 */     ctabx = 21;
/*  823 */     boolean done = false;
/*  824 */     while ((ctabx >= 0) && (!done)) {
/*  825 */       sp = compseq[ctabx];
/*  826 */       for (i = 0; i < sp.scan; i = (short)(i + 1)) {
/*  827 */         if (Math.abs(diffs[i]) > sp.disc) {
/*  828 */           ctabx = sp.next;
/*  829 */           break; }
/*  830 */         if (i == sp.scan - 1) {
/*  831 */           done = true;
/*  832 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  839 */     if (ctabx < 0) {
/*  840 */       return number_of_samples;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  845 */     ccp.squeezed_flags += 1;
/*  846 */     CompressSeqType sp = compseq[ctabx];
/*  847 */     ccp.last_2 = s[sp.scan];
/*  848 */     ccp.last_1 = s[(sp.scan + 1)];
/*  849 */     ccp.finalVal = ccp.last_1;
/*  850 */     ccp.peek_total = ((short)(ccp.peek_total - sp.scan));
/*  851 */     number_of_samples = (short)(number_of_samples + sp.scan);
/*  852 */     ccp.next_out = ((short)((ccp.next_out + sp.scan) % 150));
/*  853 */     ccp.fits[sp.fit] += sp.scan;
/*  854 */     int accum = 0;
/*  855 */     for (i = 0; i < sp.scan; i = (short)(i + 1))
/*  856 */       accum = accum << sp.shift | sp.mask & diffs[i];
/*  857 */     flag_word = flag_replacement | sp.cbits | accum & 0xFFFFFF;
/*      */     
/*      */ 
/*      */ 
/*  861 */     if (flip != 0) {
/*  862 */       ccp.framebuf.cfp[0] = endianflip(flag_word | diffbit);
/*      */     } else
/*  864 */       ccp.framebuf.cfp[0] = (flag_word | diffbit);
/*  865 */     return number_of_samples;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   int extend29(int c)
/*      */   {
/*  873 */     c &= 0x1FFFFFFF;
/*  874 */     if ((c & 0x10000000) != 0)
/*  875 */       c -= 536870912;
/*  876 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int unpack(DecompBitType p, int[] wunpk, short start, short stop, int accum)
/*      */   {
/*  886 */     for (short k = start; k >= stop; k = (short)(k - 1)) {
/*  887 */       int work = accum & p.mask;
/*  888 */       if ((work & p.hibit) != 0)
/*  889 */         work -= p.neg;
/*  890 */       wunpk[k] = work;
/*  891 */       accum >>= p.postshift;
/*      */     }
/*  893 */     return accum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   short decompressFrame(CompressedFrame framep, int[] unpacked, short finalVal, short lastframesize, short level, short recursion, short flip, short ignore, short statcode)
/*      */   {
/*  939 */     FlagRepType[] commonflags = { new FlagRepType(357913941, 1342177280), new FlagRepType(715827882, 1610612736), new FlagRepType(1073741823, 1879048192) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  944 */     DecompBitType[] decomptab = { new DecompBitType(5, '\f', 4095, 2048, 4096), new DecompBitType(1, '\000', 1073741823, 536870912, 1073741824), new DecompBitType(2, '\017', 32767, 16384, 32768), new DecompBitType(3, '\n', 1023, 512, 1024), new DecompBitType(5, '\006', 63, 32, 64), new DecompBitType(6, '\005', 31, 16, 32), new DecompBitType(7, '\004', 15, 8, 16), new DecompBitType(9, '\003', 7, 4, 8), new DecompBitType(3, '\024', 1048575, 524288, 1048576), new DecompBitType(4, '\b', 255, 128, 256), new DecompBitType(2, '\020', 65535, 32768, 65536), new DecompBitType(1, '\000', -1, 0, 0) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  958 */     DecompBitType[] squeezeddecomptab = { new DecompBitType(0, '\000', 0, 0, 0), new DecompBitType(8, '\003', 7, 4, 8), new DecompBitType(6, '\004', 15, 8, 16), new DecompBitType(4, '\006', 63, 32, 64), new DecompBitType(3, '\b', 255, 128, 256), new DecompBitType(2, '\f', 4095, 2048, 4096), new DecompBitType(1, '\030', 16777215, 8388608, 16777216), new DecompBitType(0, '\000', 0, 0, 0) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  971 */     short[] flags = new short[16];
/*      */     
/*      */ 
/*      */ 
/*  975 */     int[] wunpk = new int[9];
/*      */     
/*      */ 
/*      */ 
/*  979 */     int samp_1 = 0;int samp_2 = 0;
/*      */     
/*      */ 
/*      */ 
/*  983 */     CompressedFrame eframe = new CompressedFrame();
/*  984 */     short scode = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  995 */     if (flip != 0) {
/*  996 */       eframe.setFrameArray(framep.getFrameArray());
/*  997 */       framep = eframe;
/*  998 */       endianFlipFrame(framep.cfp);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1007 */     finalVal = 0;
/* 1008 */     statcode = 0;
/*      */     
/*      */ 
/*      */ 
/* 1012 */     short embeddedcount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1020 */     if (recursion == 0) {
/* 1021 */       if (lastframesize > 0) {
/* 1022 */         unpacked[0] = unpacked[lastframesize];
/* 1023 */         unpacked[1] = unpacked[(lastframesize + 1)];
/*      */       } else {
/* 1025 */         unpacked[1] = 0;
/* 1026 */         unpacked[0] = 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1033 */     short fp = 0;
/* 1034 */     int flag_word = framep.cfp[0];
/*      */     
/*      */ 
/*      */ 
/*      */     short difference;
/*      */     
/*      */ 
/* 1041 */     if ((flag_word & 0x80000000) != 0) {
/* 1042 */       difference = 2;
/*      */     } else {
/* 1044 */       difference = 1;
/*      */     }
/*      */     
/*      */ 
/* 1048 */     short i = 0;
/* 1049 */     while (i < 3) {
/* 1050 */       if ((flag_word & 0x70000000) == commonflags[i].repcode) {
/*      */         break;
/*      */       }
/* 1053 */       i = (short)(i + 1);
/*      */     }
/*      */     
/*      */     int squeezed_flagword;
/*      */     
/* 1058 */     if (i < 3) {
/* 1059 */       squeezed_flagword = flag_word;
/* 1060 */       flag_word = commonflags[i].fword;
/*      */     } else {
/* 1062 */       squeezed_flagword = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1067 */     for (i = 15; i >= 1; i = (short)(i - 1)) {
/* 1068 */       flags[i] = ((short)(flag_word & 0x3));
/* 1069 */       flag_word >>= 2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1074 */     short blockindex = 1;
/* 1075 */     short subcode; DecompBitType dcp; int accum; short j; while (blockindex < 16) {
/* 1076 */       short integrate = 0;
/* 1077 */       short blockcode = flags[blockindex];
/* 1078 */       int block = framep.cfp[blockindex];
/* 1079 */       if (((level < 3) && (blockcode == 0)) || ((level == 3) && (block == -939524096)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1086 */         if ((level < 3) && (ignore < blockindex)) {
/* 1087 */           switch (blockindex) {
/*      */           case 1: 
/* 1089 */             samp_1 = block;
/* 1090 */             embeddedcount = 1;
/* 1091 */             break;
/*      */           case 2: 
/* 1093 */             unpacked[''] = block;
/* 1094 */             finalVal = 148;
/*      */           }
/*      */         }
/* 1097 */       } else if ((level == 3) && (blockcode == 3) && ((block & 0xE0000000) == -536870912))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1105 */         if (ignore < blockindex) {
/* 1106 */           switch (blockindex) {
/*      */           case 1: 
/* 1108 */             samp_1 = extend29(block);
/* 1109 */             embeddedcount = 1;
/* 1110 */             break;
/*      */           case 2: 
/* 1112 */             samp_2 = extend29(block);
/* 1113 */             embeddedcount = 2;
/* 1114 */             break;
/*      */           case 3: 
/* 1116 */             unpacked[''] = extend29(block);
/* 1117 */             finalVal = 148;
/*      */           }
/*      */         }
/*      */       } else {
/* 1121 */         if (blockcode == 1)
/*      */         {
/*      */ 
/*      */ 
/* 1125 */           subcode = 9;
/* 1126 */         } else if (((level == 1) && (blockcode == 2)) || ((level == 3) && (blockcode == 0)))
/*      */         {
/*      */ 
/*      */ 
/* 1130 */           subcode = 10;
/* 1131 */         } else if ((level == 1) && (blockcode == 3))
/*      */         {
/*      */ 
/*      */ 
/* 1135 */           subcode = 11;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1146 */           subcode = (short)((blockcode - 2 << 2) + (block >> 30 & 0x3));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1151 */         dcp = decomptab[subcode];
/* 1152 */         accum = block;
/* 1153 */         if (subcode > 0) {
/* 1154 */           unpack(dcp, wunpk, (short)(dcp.samps - 1), (short)0, accum);
/* 1155 */           integrate = dcp.samps;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/* 1161 */         else if (blockindex < 15) {
/* 1162 */           blockindex = (short)(blockindex + 1);
/* 1163 */           int nextblock = framep.cfp[blockindex];
/* 1164 */           short twoblocksubcode = (short)(nextblock >> 30 & 0x3);
/* 1165 */           switch (twoblocksubcode) {
/*      */           case 0: 
/*      */           case 1: 
/* 1168 */             dcp = decomptab[(8 * twoblocksubcode)];
/* 1169 */             short halfshift = (short)(dcp.postshift >> '\001');
/* 1170 */             short middle = (short)(dcp.samps >> 1);
/* 1171 */             int halfmask = dcp.mask >> halfshift;
/* 1172 */             wunpk[middle] = (accum & halfmask);
/* 1173 */             accum >>= halfshift;
/* 1174 */             unpack(dcp, wunpk, (short)(middle - 1), (short)0, accum);
/* 1175 */             accum = nextblock;
/* 1176 */             accum = unpack(dcp, wunpk, (short)(dcp.samps - 1), (short)(middle + 1), accum);
/*      */             
/* 1178 */             accum = accum & halfmask | wunpk[middle] << halfshift;
/* 1179 */             unpack(dcp, wunpk, middle, middle, accum);
/* 1180 */             integrate = dcp.samps;
/* 1181 */             break;
/*      */           default: 
/* 1183 */             statcode = (short)(statcode | 0x4);
/* 1184 */             break;
/*      */           }
/*      */         } else {
/* 1187 */           statcode = (short)(statcode | 0x8);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1194 */       if (fp + integrate > 148) {
/* 1195 */         statcode = (short)(statcode | 0x10);
/* 1196 */       } else if (integrate > 0)
/* 1197 */         if (difference == 1) {
/* 1198 */           for (j = 0; j < integrate; j = (short)(j + 1)) {
/* 1199 */             unpacked[(fp + 2)] = (wunpk[j] + unpacked[(fp + 1)]);
/* 1200 */             fp = (short)(fp + 1);
/*      */           }
/*      */         } else
/* 1203 */           for (j = 0; j < integrate; j = (short)(j + 1)) {
/* 1204 */             unpacked[(fp + 2)] = (wunpk[j] + (unpacked[(fp + 1)] << 1) - unpacked[fp]);
/* 1205 */             fp = (short)(fp + 1);
/*      */           }
/* 1207 */       blockindex = (short)(blockindex + 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1213 */     if (squeezed_flagword != 0) {
/* 1214 */       subcode = (short)(squeezed_flagword >> 24 & 0x7);
/* 1215 */       dcp = squeezeddecomptab[subcode];
/* 1216 */       if (dcp.samps > 0) {
/* 1217 */         if (fp + dcp.samps > 148) {
/* 1218 */           statcode = (short)(statcode | 0x10);
/*      */         } else {
/* 1220 */           accum = squeezed_flagword & 0xFFFFFF;
/* 1221 */           unpack(dcp, wunpk, (short)(dcp.samps - 1), (short)0, accum);
/* 1222 */           for (j = 0; j < dcp.samps; j = (short)(j + 1)) {
/* 1223 */             if (difference == 1) {
/* 1224 */               unpacked[(fp + 2)] = (wunpk[j] + unpacked[(fp + 1)]);
/*      */             } else
/* 1226 */               unpacked[(fp + 2)] = (wunpk[j] + (unpacked[(fp + 1)] << 1) - unpacked[fp]);
/* 1227 */             fp = (short)(fp + 1);
/*      */           }
/*      */         }
/*      */       } else {
/* 1231 */         statcode = (short)(statcode | 0x20);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1241 */     if (((embeddedcount >= 1) && (unpacked[2] != samp_1)) || ((embeddedcount >= 2) && (unpacked[3] != samp_2)))
/*      */     {
/* 1243 */       if (recursion == 0) {
/* 1244 */         if (fp > 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1249 */           if (lastframesize > 0)
/* 1250 */             statcode = (short)(statcode | 0x1);
/* 1251 */           if (difference == 1) {
/* 1252 */             unpacked[1] = (unpacked[1] + samp_1 - unpacked[2]);
/*      */           } else {
/* 1254 */             if ((fp < 2) && (embeddedcount < 2))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1261 */               unpacked[3] = 0;
/* 1262 */               samp_2 = 0;
/*      */             }
/* 1264 */             unpacked[0] = (-3 * unpacked[2] + 3 * samp_1 + 2 * unpacked[3] - 2 * samp_2 + unpacked[0]);
/* 1265 */             unpacked[1] = (unpacked[3] - samp_2 - 2 * unpacked[2] + 2 * samp_1 + unpacked[1]);
/*      */           }
/* 1267 */           fp = decompressFrame(framep, unpacked, finalVal, (short)0, level, (short)(recursion + 1), (short)0, ignore, scode);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1275 */         fp = -1;
/* 1276 */         statcode = (short)(statcode | 0x2);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1281 */     return fp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean dfErrorFatal(short statcode, PrintStream ps)
/*      */   {
/* 1302 */     if (ps != null) {
/* 1303 */       if ((statcode & 0xFF) != 0)
/* 1304 */         ps.println("");
/* 1305 */       if ((statcode & 0x4) != 0)
/* 1306 */         ps.println("decompress frame: illegal secondary subcode spanning two blocks.");
/* 1307 */       if ((statcode & 0x8) != 0)
/* 1308 */         ps.println("decompress frame: illegal two-block code at end of frame.");
/* 1309 */       if ((statcode & 0x10) != 0)
/* 1310 */         ps.println("decompress frame: unpacked buffer overrun.");
/* 1311 */       if ((statcode & 0x20) != 0)
/* 1312 */         ps.println("decompress frame: illegal flag-word replacement subcode.");
/* 1313 */       if ((statcode & 0x1) != 0)
/* 1314 */         ps.println("decompress frame: expansion error at integration constant check.");
/* 1315 */       if ((statcode & 0x2) != 0)
/* 1316 */         ps.println("decompress frame: expansion failed. frame internally damaged.");
/* 1317 */       if ((statcode & 0x40) != 0)
/* 1318 */         ps.println("decompress record: last sample does not agree with decompression.");
/* 1319 */       if ((statcode & 0x80) != 0)
/* 1320 */         ps.println("decompress record: sample count disagreement.");
/*      */     }
/* 1322 */     return (statcode & 0x3E) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void fix(CompressionContinuity ccp)
/*      */   {
/* 1347 */     int s1 = ccp.peeks[ccp.next_out];
/* 1348 */     int s0 = ccp.last_1;
/* 1349 */     int diff = s1 - s0;
/* 1350 */     if (diff < 0) {
/* 1351 */       if (diff < -536870911) {
/* 1352 */         diff = -536870911;
/*      */       }
/*      */     }
/* 1355 */     else if (diff > 536870911)
/* 1356 */       diff = 536870911;
/* 1357 */     s1 = diff + s0;
/* 1358 */     ccp.peeks[ccp.next_out] = s1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void clearCompression(CompressionContinuity ccp, short level)
/*      */   {
/* 1370 */     for (short i = 0; i < 33; i = (short)(i + 1))
/* 1371 */       ccp.fits[i] = 0;
/* 1372 */     ccp.squeezed_flags = 0;
/* 1373 */     ccp.next_in = 0;
/* 1374 */     ccp.peek_total = 0;
/* 1375 */     ccp.next_out = 0;
/* 1376 */     ccp.last_1 = 0;
/* 1377 */     ccp.last_2 = 0;
/* 1378 */     ccp.frames = 0;
/* 1379 */     switch (level) {
/*      */     case 1: 
/* 1381 */       ccp.peek_threshold = 62;
/* 1382 */       break;
/*      */     case 2: 
/* 1384 */       ccp.peek_threshold = 107;
/* 1385 */       break;
/*      */     case 3: 
/* 1387 */       ccp.peek_threshold = 145;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   CompressionContinuity initCompression(short level)
/*      */   {
/* 1400 */     CompressionContinuity ccp = new CompressionContinuity();
/* 1401 */     clearCompression(ccp, level);
/* 1402 */     return ccp;
/*      */   }
/*      */   
/*      */   int finalSample(CompressionContinuity ccp)
/*      */   {
/* 1407 */     return ccp.finalVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   short peekThresholdAvail(CompressionContinuity ccp)
/*      */   {
/* 1415 */     return (short)(ccp.peek_total - ccp.peek_threshold);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   short peekContents(CompressionContinuity ccp)
/*      */   {
/* 1427 */     return ccp.peek_total;
/*      */   }
/*      */   
/*      */   int frames(CompressionContinuity ccp) {
/* 1431 */     return ccp.frames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   short blocksPadded(CompressionContinuity ccp)
/*      */   {
/* 1439 */     if (ccp.peek_total < 0) {
/* 1440 */       return (short)Math.abs(ccp.peek_total);
/*      */     }
/* 1442 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   short peekWrite(CompressionContinuity ccp, int[] samples, short numwrite)
/*      */   {
/* 1450 */     short written = 0;
/* 1451 */     int count = 0;
/*      */     
/* 1453 */     while ((ccp.peek_total < 150) && (written < numwrite)) {
/* 1454 */       ccp.peeks[ccp.next_in] = samples[(count++)];
/* 1455 */       ccp.next_in = ((short)((ccp.next_in + 1) % 150)); CompressionContinuity 
/* 1456 */         tmp41_40 = ccp;tmp41_40.peek_total = ((short)(tmp41_40.peek_total + 1));
/* 1457 */       written = (short)(written + 1);
/*      */     }
/* 1459 */     return written;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void insertConstant(CompressedFrame framep, int sample, short level, short flip, short blockindex, boolean force)
/*      */   {
/* 1474 */     if (flip != 0)
/* 1475 */       framep.cfp[blockindex] = endianflip(framep.cfp[blockindex]);
/* 1476 */     if (level < 3) {
/* 1477 */       if ((force) || (framep.cfp[blockindex] == 0)) {
/* 1478 */         framep.cfp[blockindex] = sample;
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */     }
/* 1485 */     else if ((force) || (framep.cfp[blockindex] == -939524096)) {
/* 1486 */       framep.cfp[blockindex] = (sample & 0x1FFFFFFF | 0xE0000000);
/*      */     }
/*      */     
/*      */ 
/* 1490 */     if (flip != 0) {
/* 1491 */       framep.cfp[blockindex] = endianflip(framep.cfp[blockindex]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   AdaptivityControl initAdaptivity(short diff, short fpt, short fpp, short level, short flip)
/*      */   {
/* 1527 */     AdaptivityControl adp = new AdaptivityControl();
/*      */     
/*      */ 
/*      */ 
/* 1531 */     if ((adp.ccp = initCompression(level)) == null) {
/* 1532 */       return null;
/*      */     }
/* 1534 */     adp.bestdiff = 0;
/* 1535 */     adp.trials = 0;
/* 1536 */     adp.firstframe = 0;
/* 1537 */     adp.difference = diff;
/* 1538 */     adp.framespertrial = fpt;
/* 1539 */     adp.framesperpackage = fpp;
/* 1540 */     adp.level = level;
/* 1541 */     adp.flip = flip;
/* 1542 */     return adp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   short compressAdaptively(AdaptivityControl adp)
/*      */   {
/* 1558 */     CompressionContinuity[] saved = new CompressionContinuity[2];
/*      */     
/*      */ 
/*      */     short reserve;
/*      */     
/* 1563 */     if (adp.ccp.frames % adp.framesperpackage == adp.firstframe) {
/* 1564 */       if (adp.level < 3) {
/* 1565 */         reserve = 2;
/*      */       } else
/* 1567 */         reserve = 3;
/*      */     } else
/* 1569 */       reserve = 0;
/* 1570 */     short used; if (adp.level < 3)
/*      */     {
/*      */ 
/*      */ 
/* 1574 */       used = compressFrame(adp.ccp, adp.difference, adp.level, reserve, adp.flip);
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/* 1582 */       if (adp.ccp.frames % adp.framesperpackage == adp.firstframe) {
/* 1583 */         adp.bestdiff = 0;
/* 1584 */         adp.trials = adp.framespertrial;
/*      */       }
/* 1586 */       if (adp.trials != 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1593 */         saved[0] = adp.ccp;
/* 1594 */         short used1 = compressFrame(adp.ccp, (short)1, (short)3, reserve, adp.flip);
/* 1595 */         saved[1] = adp.ccp;
/* 1596 */         adp.ccp = saved[0];
/* 1597 */         used = compressFrame(adp.ccp, (short)2, (short)3, reserve, adp.flip);
/* 1598 */         if (used <= used1) {
/* 1599 */           adp.ccp = saved[1];
/* 1600 */           used = used1;
/*      */         } else {
/* 1602 */           adp.bestdiff = ((short)(adp.bestdiff + 1)); }
/* 1603 */         AdaptivityControl tmp206_205 = adp;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1603 */         if ((tmp206_205.trials = (short)(tmp206_205.trials - 1)) == 0) {
/* 1604 */           adp.difference = ((short)(2 * adp.bestdiff >= adp.framespertrial ? 2 : 1));
/*      */         }
/*      */       } else {
/* 1607 */         used = compressFrame(adp.ccp, adp.difference, (short)3, reserve, adp.flip);
/*      */       } }
/* 1609 */     return used;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void clearGenericDecompression(DecompressionContinuity dcp)
/*      */   {
/* 1626 */     dcp.numdecomp = 0;
/* 1627 */     dcp.samp_1 = 0;
/* 1628 */     dcp.samp_2 = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   DecompressionContinuity initGenericDecompression()
/*      */   {
/* 1641 */     DecompressionContinuity dcp = new DecompressionContinuity();
/* 1642 */     clearGenericDecompression(dcp);
/* 1643 */     return dcp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int decompressGenericRecord(GenericDataRecord gdr, int[] udata, short statcode, DecompressionContinuity dcp, short firstframe, int headertotal, short level, short flip, short dataframes)
/*      */   {
/* 1677 */     int[] samples = new int[''];
/* 1678 */     short finalVal = 0;short dstat = 0;
/*      */     
/*      */ 
/* 1681 */     int finalsample = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1686 */     if (dcp.numdecomp > 0) {
/* 1687 */       samples[dcp.numdecomp] = dcp.samp_1;
/* 1688 */       samples[(dcp.numdecomp + 1)] = dcp.samp_2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1693 */     int total_in_record = 0;
/* 1694 */     short havefinal = 0;
/* 1695 */     short ignore = 0;
/* 1696 */     statcode = 0;
/*      */     
/*      */ 
/*      */ 
/* 1700 */     short framecount = (short)(dataframes - firstframe);
/*      */     
/*      */ 
/*      */ 
/* 1704 */     int frameIdx = 0;
/* 1705 */     while (framecount-- > 0) {
/* 1706 */       CompressedFrame framep = gdr.frames[(frameIdx++)];
/*      */       
/*      */       short thisdecompress;
/*      */       
/* 1710 */       if ((thisdecompress = decompressFrame(framep, samples, finalVal, dcp.numdecomp, level, (short)0, flip, ignore, dstat)) > 0)
/*      */       {
/* 1712 */         dcp.numdecomp = thisdecompress;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1718 */       statcode = (short)(statcode | dstat);
/* 1719 */       if (dfErrorFatal(statcode, (PrintStream)null)) {
/* 1720 */         return -1;
/*      */       }
/*      */       
/*      */ 
/* 1724 */       ignore = 2;
/* 1725 */       if (level > 2) {
/* 1726 */         ignore = 3;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1731 */       havefinal = (short)(havefinal + finalVal);
/* 1732 */       if (finalVal > 0) {
/* 1733 */         finalsample = samples[finalVal];
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1739 */       for (short i = 1; i <= thisdecompress; i = (short)(i + 1)) {
/* 1740 */         if (total_in_record >= udata.length) break;
/* 1741 */         udata[(total_in_record++)] = samples[(i + 1)];
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1749 */     if ((havefinal != 0) && 
/* 1750 */       (dcp.numdecomp != 0) && 
/* 1751 */       (finalsample != samples[(dcp.numdecomp + 1)]))
/* 1752 */       statcode = (short)(statcode | 0x40);
/* 1753 */     if ((headertotal >= 0) && (headertotal != total_in_record)) {
/* 1754 */       statcode = (short)(statcode | 0x80);
/*      */     }
/*      */     
/*      */ 
/* 1758 */     if (dcp.numdecomp > 0) {
/* 1759 */       dcp.samp_1 = samples[dcp.numdecomp];
/* 1760 */       dcp.samp_2 = samples[(dcp.numdecomp + 1)];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1765 */     return total_in_record;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   short compressGenericRecord(GenericDataRecordControl gdp, short firstframe)
/*      */   {
/* 1792 */     if (gdp.adp.ccp.frames >= gdp.adp.framesperpackage) {
/* 1793 */       return (short)gdp.adp.ccp.frames;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1798 */     CompressedFrame framep = gdp.gdr.frames[gdp.adp.ccp.frames];
/* 1799 */     short used = compressAdaptively(gdp.adp);
/* 1800 */     framep.setFrameArray(gdp.adp.ccp.framebuf.getFrameArray());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1805 */     if (used > 0) {
/* 1806 */       gdp.nsamples += used;
/*      */     } else {
/* 1808 */       fix(gdp.adp.ccp);
/*      */     }
/*      */     
/*      */ 
/* 1812 */     if (gdp.adp.ccp.frames >= gdp.adp.framesperpackage)
/*      */     {
/*      */       short blockindex;
/*      */       
/* 1816 */       if (gdp.adp.level < 3) {
/* 1817 */         blockindex = 2;
/*      */       } else {
/* 1819 */         blockindex = 3;
/*      */       }
/* 1821 */       insertConstant(gdp.gdr.frames[firstframe], finalSample(gdp.adp.ccp), gdp.adp.level, gdp.adp.flip, blockindex, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1827 */     return (short)gdp.adp.ccp.frames;
/*      */   }
/*      */   
/*      */   int genericRecordSamples(GenericDataRecordControl gdp) {
/* 1831 */     return gdp.nsamples;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void clearGenericCompression(GenericDataRecordControl gdp, short framesreserved)
/*      */   {
/* 1843 */     gdp.nsamples = 0;
/* 1844 */     gdp.adp.ccp.frames = framesreserved;
/* 1845 */     gdp.adp.firstframe = framesreserved;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   GenericDataRecordControl initGenericCompression(short diff, short fpt, short fpp, short level, short flip, GenericDataRecord gdr)
/*      */   {
/* 1872 */     GenericDataRecordControl gdp = new GenericDataRecordControl();
/*      */     
/* 1874 */     if ((gdp.adp = initAdaptivity(diff, fpt, fpp, level, flip)) == null) {
/* 1875 */       return null;
/*      */     }
/* 1877 */     gdp.gdr = gdr;
/* 1878 */     return gdp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1983 */   private byte[] fourByteBuff = new byte[4];
/* 1984 */   private byte[] twoByteBuff = new byte[2];
/*      */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/miniseedutils/steim/Steim.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */