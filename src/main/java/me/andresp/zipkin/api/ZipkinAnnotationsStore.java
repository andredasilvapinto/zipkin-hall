package me.andresp.zipkin.api;

import me.andresp.zipkin.impl.ZipkinAnnotationsStoreImpl;

import javax.annotation.Nonnull;

public interface ZipkinAnnotationsStore {
  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String s);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, @Nonnull String value);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, short value);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, int value);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, long value);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, double value);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, boolean value);

  @Nonnull
  ZipkinAnnotationsStoreImpl addAnnotation(@Nonnull String key, byte[] value);
}
