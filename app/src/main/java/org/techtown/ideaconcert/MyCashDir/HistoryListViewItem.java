package org.techtown.ideaconcert.MyCashDir;

public class HistoryListViewItem {
    String date, cash;
    boolean purchase;

    public HistoryListViewItem(String date, String cash, boolean purchase) {
        this.date = date;
        this.cash = cash;
        this.purchase = purchase;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }
}
