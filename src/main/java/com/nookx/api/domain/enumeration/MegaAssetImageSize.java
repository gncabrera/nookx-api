package com.nookx.api.domain.enumeration;

import java.util.Locale;

/**
 * Stored image variant for {@link com.nookx.api.domain.MegaAsset} images.
 * Files use the pattern {@code {stem}_{nameInLowercase}.{ext}} (e.g. {@code uuid_thumb.jpg}).
 */
public enum MegaAssetImageSize {
    /** 200×200 max (fit inside, aspect ratio preserved, no upscaling). */
    THUMB(200, 200),
    /** Max width 800px, height proportional. */
    MEDIUM(800, Integer.MAX_VALUE),
    /** Stored original file (entity path). */
    ORIGINAL(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int maxWidth;
    private final int maxHeight;

    MegaAssetImageSize(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Suffix for variant filenames, e.g. {@code _thumb}; empty for {@link #ORIGINAL}.
     */
    public String filenameSuffix() {
        if (this == ORIGINAL) {
            return "";
        }
        return "_" + name().toLowerCase(Locale.ROOT);
    }

    public static MegaAssetImageSize fromApiValue(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("size is required");
        }
        return MegaAssetImageSize.valueOf(raw.trim().toUpperCase(Locale.ROOT));
    }
}
