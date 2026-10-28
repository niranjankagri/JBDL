package jbdl.session04.hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Hash functions used for lookup (hashCode) vs for security and integrity (SHA-256). */
public final class HashingFacts {

    private HashingFacts() {
    }

    /** String.hashCode, written out: s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1], int overflow allowed. */
    public static int stringHash(String text) {
        int h = 0;
        for (int i = 0; i < text.length(); i++) {
            h = 31 * h + text.charAt(i);
        }
        return h;
    }

    /** A key with a constant hashCode: legal (equal objects still have equal hashes) but every key collides. */
    public record BadKey(String name) {
        @Override
        public int hashCode() {
            return 42;
        }

        @Override
        public boolean equals(Object other) {
            // instanceof with a variable (Java 16+); && only runs the right side if the left is true
            return other instanceof BadKey key && key.name.equals(name);
        }
    }

    /** A mutable key: if its hashCode changes after insertion, the map looks in the wrong bucket. */
    public static final class MutableKey {
        private String name;

        public MutableKey(String name) {
            this.name = name;
        }

        /** Changes the field hashCode depends on: never do this to a key that is inside a map. */
        public void rename(String newName) {
            this.name = newName;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof MutableKey key && key.name.equals(name);
        }
    }

    /**
     * SHA-256: a cryptographic hash. 256-bit output, practically no collisions, one-way. Used for
     * file checksums and signatures (passwords need a slow hash like bcrypt instead, Session 16).
     */
    public static String sha256Hex(String text) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("every JDK has SHA-256", e);
        }
    }
}
