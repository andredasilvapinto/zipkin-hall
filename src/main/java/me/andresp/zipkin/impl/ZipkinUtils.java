package me.andresp.zipkin.impl;

import me.andresp.zipkin.api.ZipkinData;
import me.andresp.zipkin.impl.ZipkinHeaders.Sampled;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class ZipkinUtils {

  private static final int MIN_LEVEL = 0;
  private static final int MAX_LEVEL = 1000;

  private static final int HEX_RADIX = 16;

  private static final ThreadLocalRandom samplingRandom = ThreadLocalRandom.current();

  // Can be arbitrarily slow (depends on the amount of entropy in the OS)
  // Used for long (complete 64-bit range) ID generation
  private static final ThreadLocal<SecureRandom> secureRandomTl = new ThreadLocal<SecureRandom>() {
    @Override
    public SecureRandom initialValue() {
      return new SecureRandom();
    }
  };

  static {
    secureRandomTl.set(new SecureRandom());
  }

  private ZipkinUtils() {}

  private static boolean shouldTrace(int samplingLevel) {
    return samplingLevel > 0 && samplingRandom.nextInt(MIN_LEVEL, MAX_LEVEL) < samplingLevel;
  }

  public static ZipkinData sample(String spanName, String flags, int samplingLevel) {
    if (shouldTrace(samplingLevel)) {
      return generateZipkinRequestData(spanName, flags);
    } else {
      return ZipkinDataImpl.disabled();
    }
  }

  private static ZipkinData generateZipkinRequestData(String spanName, String flags) {
    String traceId = String.valueOf(getRandomLong());
    String spanId = String.valueOf(getRandomLong());

    return new ZipkinDataImpl(traceId, spanId, null, Sampled.enabled(), flags, spanName);
  }

  public static Optional<String> fromHex(Optional<String> hexValue) {
    // Long.parseLong receives signed longs, but Long.toHexString uses unsigned longs, so we need to use BigInteger
    // in order to parse the unsigned string created by Long.toHexString and then obtain the value without raising
    // an NumberFormatException caused by long overflow.
    return hexValue.map(v -> new BigInteger(v, HEX_RADIX).toString());
  }

  public static Optional<String> toHex(Optional<String> value) {
    return value.map(v -> new BigInteger(v).toString(HEX_RADIX));
  }

  public static ZipkinData generateChild(ZipkinData parent, String spanName) {
    return new ZipkinDataImpl(parent.getTraceId(), String.valueOf(getRandomLong()),
      parent.getSpanId(), parent.getSampled(), parent.getFlags(), spanName);
  }

  private static long getRandomLong() {
    byte[] rndBytes = new byte[8];
    secureRandomTl.get().nextBytes(rndBytes);
    return ByteBuffer.wrap(rndBytes).getLong();
  }
}

