/*
 * Copyright 2017 Patrick Favre-Bulle
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package at.favre.lib.bytes;

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class UtilTest {
    private static final byte[] EMPTY = {};
    private static final byte[] ARRAY1 = {(byte) 1};
    private static final byte[] ARRAY234 = {(byte) 2, (byte) 3, (byte) 4};

    @Test
    public void testIndexOf() {
        assertEquals(-1, indexOf(EMPTY, (byte) 1));
        assertEquals(-1, indexOf(ARRAY1, (byte) 2));
        assertEquals(-1, indexOf(ARRAY234, (byte) 1));
        assertEquals(0, indexOf(
                new byte[]{(byte) -1}, (byte) -1));
        assertEquals(0, indexOf(ARRAY234, (byte) 2));
        assertEquals(1, indexOf(ARRAY234, (byte) 3));
        assertEquals(2, indexOf(ARRAY234, (byte) 4));
        assertEquals(1, indexOf(
                new byte[]{(byte) 2, (byte) 3, (byte) 2, (byte) 3},
                (byte) 3));
    }

    private int indexOf(byte[] empty, byte b) {
        return Util.Byte.indexOf(empty, new byte[]{b}, 0, empty.length);
    }

    @Test
    public void testIndexOf_arrayTarget() {
        assertEquals(-1, Util.Byte.indexOf(EMPTY, EMPTY, 0, EMPTY.length));
        assertEquals(-1, Util.Byte.indexOf(ARRAY234, EMPTY, 0, ARRAY234.length));
        assertEquals(-1, Util.Byte.indexOf(EMPTY, ARRAY234, 0, EMPTY.length));
        assertEquals(-1, Util.Byte.indexOf(ARRAY234, ARRAY1, 0, ARRAY234.length));
        assertEquals(-1, Util.Byte.indexOf(ARRAY1, ARRAY234, 0, ARRAY1.length));
        assertEquals(0, Util.Byte.indexOf(ARRAY1, ARRAY1, 0, ARRAY1.length));
        assertEquals(0, Util.Byte.indexOf(ARRAY234, ARRAY234, 0, ARRAY234.length));
        assertEquals(0, Util.Byte.indexOf(
                ARRAY234, new byte[]{(byte) 2, (byte) 3}, 0, 2));
        assertEquals(1, Util.Byte.indexOf(
                ARRAY234, new byte[]{(byte) 3, (byte) 4}, 0, 2));
        assertEquals(1, Util.Byte.indexOf(ARRAY234, new byte[]{(byte) 3}, 0, ARRAY234.length));
        assertEquals(2, Util.Byte.indexOf(ARRAY234, new byte[]{(byte) 4}, 0, ARRAY234.length));
        assertEquals(1, Util.Byte.indexOf(new byte[]{(byte) 2, (byte) 3, (byte) 3, (byte) 3, (byte) 3}, new byte[]{(byte) 3}, 0, 5));
        assertEquals(2, Util.Byte.indexOf(
                new byte[]{(byte) 2, (byte) 3, (byte) 2,
                        (byte) 3, (byte) 4, (byte) 2, (byte) 3},
                new byte[]{(byte) 2, (byte) 3, (byte) 4}
                , 0, 7));
        assertEquals(1, Util.Byte.indexOf(
                new byte[]{(byte) 2, (byte) 2, (byte) 3,
                        (byte) 4, (byte) 2, (byte) 3, (byte) 4},
                new byte[]{(byte) 2, (byte) 3, (byte) 4}
                , 0, 7));
        assertEquals(-1, Util.Byte.indexOf(
                new byte[]{(byte) 4, (byte) 3, (byte) 2},
                new byte[]{(byte) 2, (byte) 3, (byte) 4}
                , 0, 2));
    }

    @Test
    public void testLastIndexOf() {
        assertEquals(-1, lastIndexOf(EMPTY, (byte) 1));
        assertEquals(-1, lastIndexOf(ARRAY1, (byte) 2));
        assertEquals(-1, lastIndexOf(ARRAY234, (byte) 1));
        assertEquals(0, lastIndexOf(
                new byte[]{(byte) -1}, (byte) -1));
        assertEquals(0, lastIndexOf(ARRAY234, (byte) 2));
        assertEquals(1, lastIndexOf(ARRAY234, (byte) 3));
        assertEquals(2, lastIndexOf(ARRAY234, (byte) 4));
        assertEquals(3, lastIndexOf(
                new byte[]{(byte) 2, (byte) 3, (byte) 2, (byte) 3},
                (byte) 3));
    }

    private int lastIndexOf(byte[] empty, byte b) {
        return Util.Byte.lastIndexOf(empty, b, 0, empty.length);
    }

    @Test
    public void testConcat() {
        assertTrue(Arrays.equals(EMPTY, Util.Byte.concat()));
        assertTrue(Arrays.equals(EMPTY, Util.Byte.concat(EMPTY)));
        assertTrue(Arrays.equals(EMPTY, Util.Byte.concat(EMPTY, EMPTY, EMPTY)));
        assertTrue(Arrays.equals(ARRAY1, Util.Byte.concat(ARRAY1)));
        assertNotSame(ARRAY1, Util.Byte.concat(ARRAY1));
        assertTrue(Arrays.equals(ARRAY1, Util.Byte.concat(EMPTY, ARRAY1, EMPTY)));
        assertTrue(Arrays.equals(
                new byte[]{(byte) 1, (byte) 1, (byte) 1},
                Util.Byte.concat(ARRAY1, ARRAY1, ARRAY1)));
        assertTrue(Arrays.equals(
                new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4},
                Util.Byte.concat(ARRAY1, ARRAY234)));
    }

    @Test
    public void testToArray() {
        // need explicit type parameter to avoid javac warning!?
        List<Byte> none = Arrays.asList();
        assertTrue(Arrays.equals(EMPTY, Util.Converter.toArray(none)));

        List<Byte> one = Arrays.asList((byte) 1);
        assertTrue(Arrays.equals(ARRAY1, Util.Converter.toArray(one)));

        byte[] array = {(byte) 0, (byte) 1, (byte) 0x55};

        List<Byte> three = Arrays.asList((byte) 0, (byte) 1, (byte) 0x55);
        assertTrue(Arrays.equals(array, Util.Converter.toArray(three)));

        assertTrue(Arrays.equals(array, Util.Converter.toArray(Util.Converter.toList(array))));
    }

    @Test
    public void testToArray_withNull() {
        List<Byte> list = Arrays.asList((byte) 0, (byte) 1, null);
        try {
            Util.Converter.toArray(list);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void testToArray_withConversion() {
        byte[] array = {(byte) 0, (byte) 1, (byte) 2};

        List<Byte> bytes = Arrays.asList((byte) 0, (byte) 1, (byte) 2);
        assertTrue(Arrays.equals(array, Util.Converter.toArray(bytes)));
    }

    @Test
    public void testAsList_toArray_roundTrip() {
        byte[] array = {(byte) 0, (byte) 1, (byte) 2};
        List<Byte> list = Util.Converter.toList(array);
        byte[] newArray = Util.Converter.toArray(list);

        // Make sure it returned a copy
        list.set(0, (byte) 4);
        assertTrue(Arrays.equals(
                new byte[]{(byte) 0, (byte) 1, (byte) 2}, newArray));
        newArray[1] = (byte) 5;
        assertEquals((byte) 1, (byte) list.get(1));
    }

    @Test
    // This test stems from a real bug found by andrewk
    public void testAsList_subList_toArray_roundTrip() {
        byte[] array = {(byte) 0, (byte) 1, (byte) 2, (byte) 3};
        List<Byte> list = Util.Converter.toList(array);
        assertTrue(Arrays.equals(new byte[]{(byte) 1, (byte) 2},
                Util.Converter.toArray(list.subList(1, 3))));
        assertTrue(Arrays.equals(new byte[]{},
                Util.Converter.toArray(list.subList(2, 2))));
    }

    @Test(expected = IllegalStateException.class)
    public void readFromStream() {
        Util.File.readFromStream(null, -1);
    }

    @Test
    public void concatVararg() {
        assertArrayEquals(new byte[]{1}, Util.Byte.concatVararg((byte) 1, null));
    }

    @Test
    public void testReverse() {
        testReverse(new byte[]{}, new byte[]{});
        testReverse(new byte[]{1}, new byte[]{1});
        testReverse(new byte[]{1, 2}, new byte[]{2, 1});
        testReverse(new byte[]{3, 1, 1}, new byte[]{1, 1, 3});
        testReverse(new byte[]{-1, 1, -2, 2}, new byte[]{2, -2, 1, -1});
    }

    @Test
    public void testReverseIndexed() {
        testReverse(new byte[]{}, 0, 0, new byte[]{});
        testReverse(new byte[]{1}, 0, 1, new byte[]{1});
        testReverse(new byte[]{1, 2}, 0, 2, new byte[]{2, 1});
        testReverse(new byte[]{3, 1, 1}, 0, 2, new byte[]{1, 3, 1});
        testReverse(new byte[]{3, 1, 1}, 0, 1, new byte[]{3, 1, 1});
        testReverse(new byte[]{-1, 1, -2, 2}, 1, 3, new byte[]{-1, -2, 1, 2});
    }

    private static void testReverse(byte[] input, byte[] expectedOutput) {
        input = Arrays.copyOf(input, input.length);
        Util.Byte.reverse(input, 0, input.length);
        assertTrue(Arrays.equals(expectedOutput, input));
    }

    private static void testReverse(byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
        input = Arrays.copyOf(input, input.length);
        Util.Byte.reverse(input, fromIndex, toIndex);
        assertTrue(Arrays.equals(expectedOutput, input));
    }

    @Test
    public void testLeftShift() {
        byte[] test = new byte[]{0, 0, 1, 0};
        assertArrayEquals(new byte[]{0, 1, 0, 0}, Util.Byte.shiftLeft(new byte[]{0, 0, -128, 0}, 1));
        assertArrayEquals(new byte[]{0, 1, 0, 0}, Util.Byte.shiftLeft(new byte[]{0, 0, 64, 0}, 2));
        assertArrayEquals(new byte[]{1, 1, 1, 0}, Util.Byte.shiftLeft(new byte[]{-128, -128, -128, -128}, 1));
        assertArrayEquals(new byte[]{0, 0, 2, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 1));
        assertArrayEquals(new byte[]{0, 0, 4, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 2));
        assertArrayEquals(new byte[]{0, 0, 8, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 3));
        assertArrayEquals(new byte[]{0, 1, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 8));
        assertArrayEquals(new byte[]{0, 2, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 9));
        assertArrayEquals(new byte[]{1, 0, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 16));
        assertArrayEquals(new byte[]{2, 0, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 17));
        assertArrayEquals(new byte[]{-128, 0, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 23));
        assertArrayEquals(new byte[]{0, 0, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 24));
        assertArrayEquals(new byte[]{0, 0, 0, 0}, Util.Byte.shiftLeft(Bytes.from(test).array(), 24));

        assertSame(test, Util.Byte.shiftLeft(test, 1));
    }

    @Test
    @Ignore
    public void testLeftShiftAgainstRefImpl() {
        for (int i = 0; i < 1000; i++) {
            int shift = 1;
            Bytes rnd = Bytes.random(4 + new Random().nextInt(14));

            byte[] expected = Bytes.from(new BigInteger(rnd.array()).shiftLeft(shift).toByteArray()).resize(rnd.length(), BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_MAX_LENGTH).array();
            byte[] actual = Bytes.from(Util.Byte.shiftLeft(rnd.copy().array(), shift)).resize(rnd.length(), BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_MAX_LENGTH).array();

            System.out.println("Original  \t" + rnd.encodeBinary() + " << " + shift);
            System.out.println("Expected \t" + Bytes.wrap(expected).encodeBinary());
            System.out.println("Actual   \t" + Bytes.wrap(actual).encodeBinary() + "\n\n");

            assertArrayEquals(expected, actual);
            assertEquals(Bytes.wrap(expected).encodeHex(), Bytes.wrap(actual).encodeHex());
        }
    }

    @Test
    public void testRightShift() {
        byte[] test = new byte[]{0, 0, 16, 0};
        assertEquals(0b01111110, 0b11111101 >>> 1);
        assertArrayEquals(new byte[]{0b00101101, (byte) 0b01111110}, Util.Byte.shiftRight(new byte[]{0b01011010, (byte) 0b11111101}, 1));
        assertArrayEquals(new byte[]{0, -128, -128, -128}, Util.Byte.shiftRight(new byte[]{1, 1, 1, 1}, 1));
        assertArrayEquals(new byte[]{0, -128, 66, 0}, Util.Byte.shiftRight(new byte[]{2, 1, 8, 2}, 2));
        assertArrayEquals(new byte[]{0, -128, 66, 0}, new BigInteger(new byte[]{2, 1, 8, 2}).shiftRight(2).toByteArray());
        assertArrayEquals(new byte[]{0, 0, 0, -128}, Util.Byte.shiftRight(Bytes.from(test).array(), 5));
        assertArrayEquals(new byte[]{0, 0, 0, -128}, Util.Byte.shiftRight(new byte[]{0, 0, 1, 0}, 1));
        assertArrayEquals(new byte[]{0, 0, 8, 0}, Util.Byte.shiftRight(Bytes.from(test).array(), 1));
        assertArrayEquals(new byte[]{0, 0, 4, 0}, Util.Byte.shiftRight(Bytes.from(test).array(), 2));
        assertArrayEquals(new byte[]{0, 0, 2, 0}, Util.Byte.shiftRight(Bytes.from(test).array(), 3));
        assertArrayEquals(new byte[]{0, 0, 1, 0}, Util.Byte.shiftRight(Bytes.from(test).array(), 4));

        assertSame(test, Util.Byte.shiftRight(test, 1));
    }

    @Test
    public void testRightShiftAgainstRefImpl() {
        for (int i = 0; i < 1000; i++) {
            int shift = new Random().nextInt(64);
            Bytes rnd = Bytes.random(4 + new Random().nextInt(12));
            if (!rnd.bitAt(rnd.lengthBit() - 1)) { //only unsigned
                byte[] expected = Bytes.from(new BigInteger(rnd.array()).shiftRight(shift).toByteArray()).resize(rnd.length(), BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_MAX_LENGTH).array();
                byte[] actual = Bytes.from(Util.Byte.shiftRight(rnd.copy().array(), shift)).resize(rnd.length(), BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_MAX_LENGTH).array();

//                System.out.println("Original  \t" + rnd.encodeBinary() + " >> " + shift);
//                System.out.println("Expected \t" + Bytes.wrap(expected).encodeBinary());
//                System.out.println("Actual   \t" + Bytes.wrap(actual).encodeBinary() + "\n\n");

                assertArrayEquals(expected, actual);
                assertEquals(Bytes.wrap(expected).encodeHex(), Bytes.wrap(actual).encodeHex());
            }
        }
    }

    @Test
    public void testCharToByteArray() {
        Charset[] charsets = new Charset[]{StandardCharsets.UTF_8, StandardCharsets.US_ASCII, StandardCharsets.UTF_16};
        for (Charset charset : charsets) {
            checkCharArrayToByteArray("".toCharArray(), charset);
            checkCharArrayToByteArray("A".toCharArray(), charset);
            checkCharArrayToByteArray("12".toCharArray(), charset);
            checkCharArrayToByteArray("XYZ".toCharArray(), charset);
            checkCharArrayToByteArray("abcdefg".toCharArray(), charset);
            checkCharArrayToByteArray("71oh872gdl2dhp81g".toCharArray(), charset);

        }

        checkCharArrayToByteArray("யe2ாமறிந்தиют убSîne klâwenasd1".toCharArray(), StandardCharsets.UTF_8);
    }

    private void checkCharArrayToByteArray(char[] subject, Charset charset) {
        for (int lenI = 1; lenI < subject.length + 1; lenI++) {
            for (int offsetI = 0; offsetI < subject.length; offsetI++) {
                if (offsetI + lenI > subject.length) break;
                byte[] bytes = Util.Converter.charToByteArray(subject, charset, offsetI, lenI);
                assertEquals(Bytes.wrap(bytes), Bytes.wrap(new String(subject).substring(offsetI, offsetI + lenI).getBytes(charset)));
            }
        }
        compareArrayToByteArrayWithoutOffset(subject, charset);
    }

    private void compareArrayToByteArrayWithoutOffset(char[] subject, Charset charset) {
        assertArrayEquals(Util.Converter.charToByteArray(subject, charset, 0, subject.length), new String(subject).getBytes(charset));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharToByteArrayIllegalOffset() {
        Util.Converter.charToByteArray("abcdef".toCharArray(), StandardCharsets.UTF_8, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharToByteArrayIllegalLength() {
        Util.Converter.charToByteArray("abcdef".toCharArray(), StandardCharsets.UTF_8, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharToByteArrayIllegalOffsetPlusLength() {
        Util.Converter.charToByteArray("abcdef".toCharArray(), StandardCharsets.UTF_8, 4, 3);
    }
}
