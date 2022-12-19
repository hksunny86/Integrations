/*     */ package com.intellij.uiDesigner.core;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Util
/*     */ {
/*  22 */   private static final Dimension MAX_SIZE = new Dimension(2147483647, 2147483647);
/*     */   public static final int DEFAULT_INDENT = 10;
/*     */   
/*     */   public static Dimension getMinimumSize(Component component, GridConstraints constraints, boolean addIndent) {
/*     */     try {
/*  27 */       Dimension size = getSize(constraints.myMinimumSize, component.getMinimumSize());
/*  28 */       if (addIndent) {
/*  29 */         size.width += 10 * constraints.getIndent();
/*     */       }
/*  31 */       return size;
/*  32 */     } catch (NullPointerException npe) {
/*  33 */       return new Dimension(0, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dimension getMaximumSize(Component component, GridConstraints constraints, boolean addIndent) {
/*     */     try {
/*  42 */       Dimension size = getSize(constraints.myMaximumSize, MAX_SIZE);
/*  43 */       if (addIndent && size.width < MAX_SIZE.width) {
/*  44 */         size.width += 10 * constraints.getIndent();
/*     */       }
/*  46 */       return size;
/*     */     }
/*  48 */     catch (NullPointerException e) {
/*  49 */       return new Dimension(0, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Dimension getPreferredSize(Component component, GridConstraints constraints, boolean addIndent) {
/*     */     try {
/*  55 */       Dimension size = getSize(constraints.myPreferredSize, component.getPreferredSize());
/*  56 */       if (addIndent) {
/*  57 */         size.width += 10 * constraints.getIndent();
/*     */       }
/*  59 */       return size;
/*     */     }
/*  61 */     catch (NullPointerException e) {
/*  62 */       return new Dimension(0, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Dimension getSize(Dimension overridenSize, Dimension ownSize) {
/*  67 */     int overridenWidth = (overridenSize.width >= 0) ? overridenSize.width : ownSize.width;
/*  68 */     int overridenHeight = (overridenSize.height >= 0) ? overridenSize.height : ownSize.height;
/*  69 */     return new Dimension(overridenWidth, overridenHeight);
/*     */   }
/*     */   
/*     */   public static void adjustSize(Component component, GridConstraints constraints, Dimension size) {
/*  73 */     Dimension minimumSize = getMinimumSize(component, constraints, false);
/*  74 */     Dimension maximumSize = getMaximumSize(component, constraints, false);
/*     */     
/*  76 */     size.width = Math.max(size.width, minimumSize.width);
/*  77 */     size.height = Math.max(size.height, minimumSize.height);
/*     */     
/*  79 */     size.width = Math.min(size.width, maximumSize.width);
/*  80 */     size.height = Math.min(size.height, maximumSize.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int eliminate(int[] cellIndices, int[] spans, ArrayList<Integer> elimitated) {
/*  89 */     int size = cellIndices.length;
/*  90 */     if (size != spans.length) {
/*  91 */       throw new IllegalArgumentException("size mismatch: " + size + ", " + spans.length);
/*     */     }
/*  93 */     if (elimitated != null && elimitated.size() != 0) {
/*  94 */       throw new IllegalArgumentException("eliminated must be empty");
/*     */     }
/*     */     
/*  97 */     int cellCount = 0;
/*  98 */     for (int i = 0; i < size; i++) {
/*  99 */       cellCount = Math.max(cellCount, cellIndices[i] + spans[i]);
/*     */     }
/*     */     
/* 102 */     for (int cell = cellCount - 1; cell >= 0; cell--) {
/*     */ 
/*     */       
/* 105 */       boolean starts = false;
/* 106 */       boolean ends = false;
/*     */       int j;
/* 108 */       for (j = 0; j < size; j++) {
/* 109 */         if (cellIndices[j] == cell) {
/* 110 */           starts = true;
/*     */         }
/* 112 */         if (cellIndices[j] + spans[j] - 1 == cell) {
/* 113 */           ends = true;
/*     */         }
/*     */       } 
/*     */       
/* 117 */       if (!starts || !ends) {
/*     */ 
/*     */ 
/*     */         
/* 121 */         if (elimitated != null) {
/* 122 */           elimitated.add(new Integer(cell));
/*     */         }
/*     */ 
/*     */         
/* 126 */         for (j = 0; j < size; j++) {
/* 127 */           boolean decreaseSpan = (cellIndices[j] <= cell && cell < cellIndices[j] + spans[j]);
/* 128 */           boolean decreaseIndex = (cellIndices[j] > cell);
/*     */           
/* 130 */           if (decreaseSpan) {
/* 131 */             spans[j] = spans[j] - 1;
/*     */           }
/*     */           
/* 134 */           if (decreaseIndex) {
/* 135 */             cellIndices[j] = cellIndices[j] - 1;
/*     */           }
/*     */         } 
/*     */         
/* 139 */         cellCount--;
/*     */       } 
/*     */     } 
/* 142 */     return cellCount;
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\intelli\\uiDesigner\core\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */