package fh.kl.wamomu.meta;

import java.util.Date;

/**
 * Meta Klasse, in der Alle Daten für eine Messung gespeichert werden
 */
public class measurement {

    int measurementID;
    double mvalue;
    Date date;
    Date time;

    /**
     * Konstruktor
     * @param measurementID
     * @param mvalue
     * @param date
     * @param time
     */
    public measurement(int measurementID, double mvalue, Date date, Date time) {
        this.measurementID = measurementID;
        this.mvalue = mvalue;
        this.date = date;
        this.time = time;
    }


    public double getmvalue() {
        return mvalue;
    }

    public Date getDate() {
        return date;
    }

    public Date getTime() {
        return time;
    }

}
