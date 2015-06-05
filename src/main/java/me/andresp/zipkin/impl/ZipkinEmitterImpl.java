/*
* Derived from original work for Betfair under Apache License, Version 2.0
*/

package me.andresp.zipkin.impl;

import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import com.twitter.zipkin.gen.Endpoint;
import me.andresp.zipkin.api.ZipkinData;
import me.andresp.zipkin.api.ZipkinEmitter;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.twitter.zipkin.gen.zipkinCoreConstants.*;

public class ZipkinEmitterImpl implements ZipkinEmitter {

  private Endpoint endpoint;

  private ZipkinSpanCollector zipkinSpanCollector;


  public ZipkinEmitterImpl(int serviceIPv4, int port, @Nonnull String serviceName, @Nonnull ZipkinSpanCollector zipkinSpanCollector) {
    Objects.requireNonNull(serviceName);
    Objects.requireNonNull(zipkinSpanCollector);

    this.zipkinSpanCollector = zipkinSpanCollector;
    this.endpoint = new Endpoint(serviceIPv4, (short) port, serviceName);
  }

  /**
   * The returning object should be used to emit more than 1 annotation at once. After adding your annotations you
   * will need to call emitAnnotations in order to send the span with all the annotations to Zipkin.
   *
   * @param zipkinData Zipkin request data
   * @return Zipkin annotations storage capable of merging multiple annotations per emission
   */
  @Override
  @Nonnull
  public ZipkinAnnotationsStore buildAnnotationsStore(@Nonnull ZipkinData zipkinData) {
    Objects.requireNonNull(zipkinData);
    return new ZipkinAnnotationsStore(zipkinData, endpoint);
  }

  /**
   * Emit a pre-populated storage of annotations to Zipkin
   *
   * @param zipkinAnnotationsStore Zipkin annotations storage representing the entire list of annotations to emit
   */
  @Override
  public void emitAnnotations(@Nonnull ZipkinAnnotationsStore zipkinAnnotationsStore) {
    Objects.requireNonNull(zipkinAnnotationsStore);
    zipkinSpanCollector.collect(zipkinAnnotationsStore.generateSpan());
  }


  // Single annotation emission methods
  @Override
  public void emitServerReceive(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, SERVER_RECV);
  }

  @Override
  public void emitServerSend(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, SERVER_SEND);
  }

  @Override
  public void emitClientSend(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, CLIENT_SEND);
  }

  @Override
  public void emitClientReceive(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, CLIENT_RECV);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String s) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, s).addAnnotation(s);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, @Nonnull String value) {
    Objects.requireNonNull(value);
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, short value) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, int value) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, long value) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, double value) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, boolean value) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Override
  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, byte[] value) {
    ZipkinAnnotationsStore store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Nonnull
  private ZipkinAnnotationsStore prepareEmission(@Nonnull ZipkinData zipkinData, @Nonnull String s) {
    Objects.requireNonNull(s);
    return buildAnnotationsStore(zipkinData);
  }
}
