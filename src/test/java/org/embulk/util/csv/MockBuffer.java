/*
 * Copyright 2021 The Embulk project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.embulk.util.csv;

import java.util.Arrays;
import org.embulk.spi.Buffer;

final class MockBuffer extends Buffer {
    public MockBuffer(byte[] wrap, int offset, int capacity) {
        this.array = wrap;
        this.offset = offset;
        this.capacity = capacity;
        this.filled = offset;
        if (array.length < offset + capacity) {
            // TODO
            throw new IllegalStateException("capacity out of bound");
        }
    }

    @SuppressWarnings("deprecation")  // Buffer#copyOf(byte[]) is deprecated.
    public static Buffer copyOf(byte[] src) {
        return copyOf(src, 0, src.length);
    }

    @SuppressWarnings("deprecation")  // Buffer#copyOf(byte[], int, int) is deprecated.
    public static Buffer copyOf(byte[] src, int index, int length) {
        return wrap(Arrays.copyOfRange(src, index, length));
    }

    @SuppressWarnings("deprecation")  // Buffer#wrap(byte[]) is deprecated.
    public static Buffer wrap(byte[] src) {
        return wrap(src, 0, src.length);
    }

    @SuppressWarnings("deprecation")  // Buffer#wrap(byte[], int, int) is deprecated.
    public static Buffer wrap(byte[] src, int offset, int size) {
        return new MockBuffer(src, offset, size).limit(size);
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[] array() {
        return array;
    }

    @Override
    public int offset() {
        return offset;
    }

    @Override
    public Buffer offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public int limit() {
        return filled - offset;
    }

    @Override
    public Buffer limit(int limit) {
        if (capacity < limit) {
            // TODO
            throw new IllegalStateException("limit index out of bound: capacity=" + capacity + " limit=" + limit);
        }
        this.filled = offset + limit;
        return this;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void setBytes(int index, byte[] source, int sourceIndex, int length) {
        System.arraycopy(source, sourceIndex, array, offset + index, length);
    }

    @SuppressWarnings("deprecation")  // Calling Buffer#array().
    @Override
    public void setBytes(int index, Buffer source, int sourceIndex, int length) {
        setBytes(index, source.array(), source.offset() + sourceIndex, length);
    }

    @Override
    public void getBytes(int index, byte[] dest, int destIndex, int length) {
        System.arraycopy(array, offset + index, dest, destIndex, length);
    }

    @SuppressWarnings("deprecation")  // Calling Buffer#array().
    @Override
    public void getBytes(int index, Buffer dest, int destIndex, int length) {
        getBytes(index, dest.array(), dest.offset() + destIndex, length);
    }

    @Override
    public void release() {}

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MockBuffer)) {
            return false;
        }
        MockBuffer o = (MockBuffer) other;

        // TODO optimize
        if (limit() != o.limit()) {
            return false;
        }
        int i = this.offset();
        int io = o.offset();
        while (i < filled) {
            if (array[i] != o.array[io]) {
                return false;
            }
            i++;
            io++;
        }
        return true;
    }

    @Override
    public int hashCode() {
        // TODO optimize
        int result = 1;
        for (int i = offset; i < filled; i++) {
            result = 31 * result + array[i];
        }
        return result;
    }

    private final byte[] array;
    private final int capacity;

    private int offset;
    private int filled;
}
