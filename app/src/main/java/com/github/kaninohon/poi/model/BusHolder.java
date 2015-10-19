package com.github.kaninohon.poi.model;

import com.squareup.otto.Bus;

public class BusHolder {
    static Bus mBus = new Bus();

    public static Bus get() {
        return mBus;
    }
}
