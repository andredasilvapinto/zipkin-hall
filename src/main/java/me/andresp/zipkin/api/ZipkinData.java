package me.andresp.zipkin.api;

public interface ZipkinData {
  String getTraceId();

  String getSpanId();

  String getParentSpanId();

  String getSampled();

  String getFlags();

  String getSpanName();
}
