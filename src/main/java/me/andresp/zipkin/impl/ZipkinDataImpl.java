package me.andresp.zipkin.impl;

import me.andresp.zipkin.api.ZipkinData;

import javax.annotation.Nullable;

public class ZipkinDataImpl implements ZipkinData {

  private static final ZipkinData unsampled = new ZipkinDataImpl(null, null, null, ZipkinHeaders.Sampled.disabled(), null, null);

  private String traceId;
  private String spanId;
  private String parentSpanId;
  private String sampled;
  private String flags;
  private String spanName;

  public ZipkinDataImpl(@Nullable String traceId, @Nullable String spanId, @Nullable String parentSpanId,
                        @Nullable String sampled, @Nullable String flags, @Nullable String spanName) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.sampled = sampled;
    this.flags = flags;
    this.spanName = spanName;
  }

  @Override
  public String getTraceId() {
    return traceId;
  }

  @Override
  public String getSpanId() {
    return spanId;
  }

  @Override
  public String getParentSpanId() {
    return parentSpanId;
  }

  @Override
  public String getSampled() {
    return sampled;
  }

  @Override
  public String getFlags() {
    return flags;
  }

  @Override
  public String getSpanName() {
    return spanName;
  }

  public static ZipkinData disabled() {
    return unsampled;
  }
}
