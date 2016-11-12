/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package java.nio.charset;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import javaemul.internal.EmulatedCharset;

/**
 * A minimal emulation of {@link Charset}.
 */
public abstract class Charset implements Comparable<Charset> { // NOPMD

  private static final class AvailableCharsets {
    private static final SortedMap<String, Charset> CHARSETS;

    static {
      final SortedMap<String, Charset> map = new TreeMap<>();
      map.put(EmulatedCharset.ISO_8859_1.name(), EmulatedCharset.ISO_8859_1);
      map.put(EmulatedCharset.ISO_LATIN_1.name(), EmulatedCharset.ISO_LATIN_1);
      map.put(EmulatedCharset.UTF_8.name(), EmulatedCharset.UTF_8);
      CHARSETS = Collections.unmodifiableSortedMap(map);
    }
  }

  public static SortedMap<String, Charset> availableCharsets() {
    return AvailableCharsets.CHARSETS;
  }

  /**
   * get charset for name.
   *
   * @param charsetName name of the charset
   * @return coresponding charset
   */
  public static Charset forName(String charsetName) { // NOPMD
    if (charsetName == null) {
      throw new IllegalArgumentException("Null charset name");
    }

    charsetName = charsetName.toUpperCase();
    if (EmulatedCharset.ISO_8859_1.name().equals(charsetName)) {
      return EmulatedCharset.ISO_8859_1;
    } else if (EmulatedCharset.ISO_LATIN_1.name().equals(charsetName)) {
      return EmulatedCharset.ISO_LATIN_1;
    } else if (EmulatedCharset.UTF_8.name().equals(charsetName)) {
      return EmulatedCharset.UTF_8;
    }

    if (!charsetName.matches("^[A-Za-z0-9][\\w-:\\.\\+]*$")) { // NOPMD
      throw new IllegalCharsetNameException(charsetName);
    } else {
      throw new UnsupportedCharsetException(charsetName);
    }
  }

  private final String name; // NOPMD

  protected Charset(final String pname, final String[] paliasesIgnored) { // NOPMD
    this.name = pname;
  }

  public final String name() {
    return this.name;
  }

  @Override
  public final int compareTo(final Charset that) {
    return this.name.compareToIgnoreCase(that.name);
  }

  @Override
  public final int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public final boolean equals(final Object pobject) {
    if (pobject == this) {
      return true;
    }
    if (!(pobject instanceof Charset)) {
      return false;
    }
    final Charset that = (Charset) pobject;
    return this.name.equals(that.name);
  }

  @Override
  public final String toString() {
    return this.name;
  }

  public final CharsetEncoder newEncoder() {
    return new CharsetEncoder(this);
  }

  public static Charset defaultCharset() {
    return EmulatedCharset.UTF_8;
  }
}
