/*
* Derived from original work for Betfair under Apache License, Version 2.0
*/

package me.andresp.zipkin.impl;

import com.twitter.zipkin.gen.*;
import me.andresp.zipkin.api.ZipkinData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ZipkinAnnotationsStore {

  private static final int SHORT_SIZE_B = Short.SIZE / 8;
  private static final int INT_SIZE_B = Integer.SIZE / 8;
  private static final int LONG_SIZE_B = Long.SIZE / 8;
  private static final int DOUBLE_SIZE_B = Double.SIZE / 8;

  private static final ByteBuffer TRUE_BB = ByteBuffer.wrap(new byte[]{1});
  private static final ByteBuffer FALSE_BB = ByteBuffer.wrap(new byte[]{0});


  private ZipkinData zipkinData;
  private List<Annotation> annotations;
  private List<BinaryAnnotation> binaryAnnotations;
  private Endpoint endpoint;

  ZipkinAnnotationsStore(@Nonnull ZipkinData zipkinData, @Nonnull Endpoint endpoint) {
    this.zipkinData = zipkinData;
    this.annotations = new ArrayList<>();
    this.binaryAnnotations = new ArrayList<>();
    this.endpoint = endpoint;

    Objects.requireNonNull(zipkinData);
    Objects.requireNonNull(endpoint);
  }


  // PUBLIC METHODS

  /**
   * Append annotation with value s and timestamp now.
   *
   * @param s Annotation value to emit
   * @return this object
   */
  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String s) {
    long timestampMicro = TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
    return addAnnotation(timestampMicro, s);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, @Nonnull String value) {
    // Using default charset
    ByteBuffer wrappedValue = ByteBuffer.wrap(value.getBytes());
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.STRING, endpoint);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, short value) {
    ByteBuffer wrappedValue = ByteBuffer.allocate(SHORT_SIZE_B).putShort(value);
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.I16, endpoint);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, int value) {
    ByteBuffer wrappedValue = ByteBuffer.allocate(INT_SIZE_B).putInt(value);
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.I32, endpoint);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, long value) {
    ByteBuffer wrappedValue = ByteBuffer.allocate(LONG_SIZE_B).putLong(value);
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.I64, endpoint);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, double value) {
    ByteBuffer wrappedValue = ByteBuffer.allocate(DOUBLE_SIZE_B).putDouble(value);
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.DOUBLE, endpoint);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, boolean value) {
    ByteBuffer wrappedValue = value ? TRUE_BB : FALSE_BB;
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.BOOL, endpoint);
  }

  @Nonnull
  public ZipkinAnnotationsStore addAnnotation(@Nonnull String key, byte[] value) {
    ByteBuffer wrappedValue = ByteBuffer.wrap(value);
    return addBinaryAnnotation(key, wrappedValue, AnnotationType.BYTES, endpoint);
  }


  // PACKAGE-PRIVATE METHODS

  @Nonnull
  ZipkinAnnotationsStore addAnnotation(long timestampMicro, @Nonnull String s) {
    Objects.requireNonNull(s);
    Annotation annotation = new Annotation(timestampMicro, s);
    if (endpoint != null) {
      // endpoint is optional - current version of zipkin web doesn't show spans without host though
      annotation.setHost(endpoint);
    }
    annotations.add(annotation);
    return this;
  }

  @Nonnull
  Span generateSpan() {
    Span span = new Span(Long.parseLong(zipkinData.getTraceId()), zipkinData.getSpanName(),
      Long.parseLong(zipkinData.getSpanId()), annotations, binaryAnnotations);
    if (zipkinData.getParentSpanId() != null) {
      span.setParent_id(Long.parseLong(zipkinData.getParentSpanId()));
    }
    return span;
  }


  // PRIVATE METHODS

  @Nonnull
  private ZipkinAnnotationsStore addBinaryAnnotation(@Nonnull String key, @Nonnull ByteBuffer byteBuffer,
                                                     @Nonnull AnnotationType annotationType, @Nullable Endpoint endpoint) {
    BinaryAnnotation binaryAnnotation = new BinaryAnnotation(key, byteBuffer, annotationType);
    if (endpoint != null) {
      // endpoint is optional - current version of zipkin web doesn't show spans without host though
      binaryAnnotation.setHost(endpoint);
    }
    binaryAnnotations.add(binaryAnnotation);
    return this;
  }
}

