package com.chenjinchi.encode.util;

public enum Resolution {
    RESOLUTION_360P,
    RESOLUTION_720P,
    RESOLUTION_ORIGINAL;

    @Override
    public String toString() {
        switch (this) {
            case RESOLUTION_360P:
                return "360p";
            case RESOLUTION_720P:
                return "720p";
            case RESOLUTION_ORIGINAL:
                return "original";
        }
        return "original";
    }
}