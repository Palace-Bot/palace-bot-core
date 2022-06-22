package org.github.palace.bot.asm;

import java.nio.ByteBuffer;

/**
 * @author jihongyuan
 * @date 2022/6/6 17:04
 */
public class ClassByteBuffer {
    private final ByteBuffer byteBuffer;

    public ClassByteBuffer(byte[] bytes) {
        this.byteBuffer = ByteBuffer.wrap(bytes, 0, bytes.length).asReadOnlyBuffer();
        this.byteBuffer.position(0);
    }

    public byte[] getU1() {
        return get(1);
    }

    public byte[] getU2() {
        return get(2);
    }

    public byte[] getU4() {
        return get(4);
    }

    public byte[] getU8() {
        return get(8);
    }

    public byte[] get(int len) {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = byteBuffer.get();
        }
        return bytes;
    }

}
