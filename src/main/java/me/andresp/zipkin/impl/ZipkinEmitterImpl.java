/*
* Derived from original work for Betfair under Apache License, Version 2.0
*/

package me.andresp.zipkin.impl;

import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import com.twitter.zipkin.gen.Endpoint;
import me.andresp.zipkin.api.ZipkinAnnotationsStore;
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
  @Nonnull
  public ZipkinAnnotationsStore buildAnnotationsStore(@Nonnull ZipkinData zipkinData) {
    Objects.requireNonNull(zipkinData);
    return new ZipkinAnnotationsStoreImpl(zipkinData, endpoint);
  }

  /**
   * Emit a pre-populated storage of annotations to Zipkin
   *
   * @param zipkinAnnotationsStore Zipkin annotations storage representing the entire list of annotations to emit
   */
  public void emitAnnotations(@Nonnull ZipkinAnnotationsStoreImpl zipkinAnnotationsStore) {
    Objects.requireNonNull(zipkinAnnotationsStore);
    zipkinSpanCollector.collect(zipkinAnnotationsStore.generate());
  }


  // Single annotation emission methods

  public void emitServerReceive(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, SERVER_RECV);
  }

  public void emitServerSend(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, SERVER_SEND);
  }

  public void emitClientSend(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, CLIENT_SEND);
  }

  public void emitClientReceive(@Nonnull ZipkinData zipkinData) {
    emitAnnotation(zipkinData, CLIENT_RECV);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String s) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, s).addAnnotation(s);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, @Nonnull String value) {
    Objects.requireNonNull(value);
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, short value) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, int value) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, long value) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, double value) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, boolean value) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  public void emitAnnotation(@Nonnull ZipkinData zipkinData, @Nonnull String key, byte[] value) {
    ZipkinAnnotationsStoreImpl store = prepareEmission(zipkinData, key).addAnnotation(key, value);
    emitAnnotations(store);
  }

  @Nonnull
  private ZipkinAnnotationsStore prepareEmission(@Nonnull ZipkinData zipkinData, @Nonnull String s) {
    Objects.requireNonNull(s);
    return buildAnnotationsStore(zipkinData);
  }
}
