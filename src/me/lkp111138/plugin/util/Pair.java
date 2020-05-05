package me.lkp111138.plugin.util;

public class Pair<L, R> {
    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public Pair() {

    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}