package com.sauce.utils;

import com.sauce.config.Config;

public final class SlowMotion {
    private SlowMotion() {}

    /*
        This method is just for demo purposes in real case scenarios no one should do this.
        slow.mo: Set to 0 to disable visual inspection pauses (for fast execution)
        Set to 250-500 for demo/debugging to see test actions highlighted
        This variable is located under resources/application.properties
        slow.mo=500
     */
    public static void intentionalWait() {

        long ms = Config.slowMoMs();
        if (ms <= 0) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during slow-mo intentionalWait", e);
        }
    }
}

