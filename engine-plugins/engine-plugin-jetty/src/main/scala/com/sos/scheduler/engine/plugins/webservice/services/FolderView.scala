package com.sos.scheduler.engine.plugins.webservice.services

import com.fasterxml.jackson.annotation.JsonProperty
import com.sos.scheduler.engine.data.filebased.AbsolutePath
import java.net.URI
import javax.ws.rs.core.UriBuilder

class FolderView(_folderPath: AbsolutePath, _typeName: String, _entries: Iterable[FolderView.Entry]) {
  @JsonProperty def folderPath = _folderPath
  @JsonProperty def typeName = _typeName
  @JsonProperty def entries = _entries
}

object FolderView {
  def apply(names: Iterable[String], folderPath: AbsolutePath, typeName: String, baseUri: URI) = {
    def entry(name: String) = new Entry(name,
      UriBuilder.fromUri(baseUri).segment(typeName).queryParam(typeName, AbsolutePath.of(folderPath, name).toString).build())
    new FolderView(folderPath, typeName, names map entry)
  }

  case class Entry(name: String, uri: URI)
}
