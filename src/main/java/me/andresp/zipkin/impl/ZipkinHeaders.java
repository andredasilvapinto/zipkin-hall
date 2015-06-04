package me.andresp.zipkin.impl;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ZipkinHeaders {

  public static final String SAMPLED = "X-B3-Sampled";
  public static final String TRACE_ID = "X-B3-TraceId";
  public static final String SPAN_ID = "X-B3-SpanId";
  public static final String PARENT_SPAN_ID = "X-B3-ParentSpanId";
  public static final String FLAGS = "X-B3-Flags";

  private ZipkinHeaders() {
  }

  public final static class Sampled {
    private static final String TRUE = "1";
    private static final String FALSE = "0";

    private Sampled() {
    }

    public static Optional<String> sampledToString(@Nullable Boolean sampled) {
      return Optional.ofNullable(sampled).map(v -> v ? TRUE : FALSE);
    }

    public static Optional<Boolean> sampledToBoolean(@Nullable String sampled) {
      return Optional.ofNullable(sampled).map(TRUE::equals);
    }

    public static boolean isEnabled(@Nullable String sampled) {
      return TRUE.equals(sampled);
    }

    public static String enabled() {
      return TRUE;
    }

    public static String disabled() {
      return FALSE;
    }
  }
}
