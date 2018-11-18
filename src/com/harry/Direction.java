package com.harry;

/**
 * @program: five
 * @description:
 * @author: Harry
 **/

public enum Direction {
    TOP(0), TOPRIGHT(1), RIGHT(2), DOWNRIGHT(3), DOWN(4), DOWNLEFT(5), LEFT(6), TOPLEFT(7);

    private int index;

    Direction(int i) {
        this.index = i;
    }

    public int getIndex() {
        return index;
    }
}
