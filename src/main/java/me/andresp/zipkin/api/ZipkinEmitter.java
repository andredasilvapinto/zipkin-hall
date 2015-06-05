package me.andresp.zipkin.api;

import me.andresp.zipkin.impl.ZipkinAnnotationsStore;

import javax.annotation.Nonnull;

public interface ZipkinEmitter {

  void emitServerReceive(@Nonnull ZipkinData zipkinData);

  void emitServerSend(@Nonnull ZipkinData zipkinData);

  void emitClientSend(@Nonnull ZipkinData zipkinData);

  void emitClientReceive(@Nonnull ZipkinData zipkinData);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String s);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, @Nonnull String value);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, short value);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, int value);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, long value);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, double value);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, boolean value);

  void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, byte[] value);

  ZipkinAnnotationsStore buildAnnotationsStore(@Nonnull ZipkinData zipkinData);

  void emitAnnotations(@Nonnull ZipkinAnnotationsStore zipkinAnnotationsStore);

}
