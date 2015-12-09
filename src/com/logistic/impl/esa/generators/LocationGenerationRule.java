package com.logistic.impl.esa.generators;


/**
 * Created by snake on 09.12.15.
 */
public class LocationGenerationRule {
    private int indexMin;
    private int indexMax;

    private int diffX = 0;
    private int diffY = 0;
    private int multWidth = 1;
    private int multHeight = 1;

    LocationGenerationRule(int indexMin, int indexMax, int multWidth, int multHeight, int diffX, int diffY) {
        this.indexMin = indexMin;
        this.indexMax = indexMax;
        this.multWidth = multWidth;
        this.multHeight = multHeight;
        this.diffX = diffX;
        this.diffY = diffY;
    }

    public boolean canApply(int index) {
        return index >= indexMin && index < indexMax;
    }

    public int[] apply(int zoneWidth, int zoneHeight) {
        return new int[] {zoneWidth * multWidth + diffX, zoneHeight * multHeight + diffY};
    }
}
