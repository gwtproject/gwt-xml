/*
 * Copyright © 2020 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.xml.client.impl;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.gwtproject.xml.client.Document;
import org.gwtproject.xml.client.impl.DocumentImpl.NativeDocumentImpl;
import org.gwtproject.xml.client.impl.ElementImpl.NativeElementImpl;
import org.gwtproject.xml.client.impl.NodeImpl.NativeNodeImpl;
import org.gwtproject.xml.client.impl.NodeListImpl.NativeNodeListImpl;

/** Native implementation associated with {@link org.gwtproject.xml.client.XMLParser}. */
public abstract class XMLParserImpl {

  /**
   * This class implements the methods for standard browsers that use the DOMParser model of XML
   * parsing.
   */
  private static class XMLParserImplStandard extends XMLParserImpl {

    @JsType(isNative = true, name = "DOMParser", namespace = JsPackage.GLOBAL)
    static class DOMParser {
      native NativeDocumentImpl parseFromString(String contents, String mimeType);
    }

    protected final DOMParser domParser = new DOMParser();

    @Override
    protected NativeDocumentImpl createDocumentImpl() {
      return JsHelper.createDocumentImpl();
    }

    @Override
    protected NativeElementImpl getElementByIdImpl(NativeDocumentImpl document, String id) {
      return document.getElementById(id);
    }

    @Override
    protected NativeNodeListImpl getElementsByTagNameImpl(NativeNodeImpl o, String tagName) {
      return o.getElementsByTagNameNS("*", tagName);
    }

    @Override
    protected String getPrefixImpl(NativeNodeImpl node) {
      String fullName = node.nodeName;
      if (fullName != null && fullName.indexOf(":") != -1) {
        return fullName.split(":", 2)[0];
      }
      return null;
    }

    @Override
    protected NativeNodeImpl importNodeImpl(
        NativeDocumentImpl document, NativeNodeImpl importedNode, boolean deep) {
      return document.importNode(importedNode, deep);
    }

    @Override
    protected NativeDocumentImpl parseImpl(String contents) {
      NativeDocumentImpl result = domParser.parseFromString(contents, "text/xml");
      NativeElementImpl rootTag = result.documentElement;
      if ("parsererror".equals(rootTag.tagName)
          && "http://www.mozilla.org/newlayout/xml/parsererror.xml".equals(rootTag.namespaceURI)) {
        throw new RuntimeException(rootTag.firstChild.data);
      }
      NativeNodeListImpl parseErrors = result.getElementsByTagName("parsererror");
      if (parseErrors.length > 0) {
        NativeNodeImpl error = parseErrors.item(0);
        if ("body".equals(error.parentNode.tagName)) {
          throw new RuntimeException(error.childNodes.item(1).innerHTML);
        }
      }
      return result;
    }

    @Override
    protected String toStringImpl(ProcessingInstructionImpl node) {
      return toStringImpl((NodeImpl) node);
    }

    @Override
    protected String toStringImpl(NodeImpl node) {
      return new XMLSerializer().serializeToString(node.node);
    }

    @JsType(isNative = true, name = "XMLSerializer", namespace = JsPackage.GLOBAL)
    private static class XMLSerializer {
      native String serializeToString(NativeNodeImpl node);
    }
  }

  private static XMLParserImpl impl;

  public static XMLParserImpl getInstance() {
    if (impl == null) {
      impl = createImpl();
    }

    return impl;
  }

  private static XMLParserImpl createImpl() {
    return new XMLParserImplStandard();
  }

  static NativeElementImpl getElementById(NativeDocumentImpl document, String id) {
    return impl.getElementByIdImpl(document, id);
  }

  static NativeNodeListImpl getElementsByTagName(NativeNodeImpl o, String tagName) {
    return impl.getElementsByTagNameImpl(o, tagName);
  }

  static String getPrefix(NativeNodeImpl node) {
    return impl.getPrefixImpl(node);
  }

  static NativeNodeImpl importNode(
      NativeDocumentImpl document, NativeNodeImpl importedNode, boolean deep) {
    return impl.importNodeImpl(document, importedNode, deep);
  }

  /** Not globally instantable. */
  XMLParserImpl() {}

  public final Document createDocument() {
    return (Document) NodeImpl.build(createDocumentImpl());
  }

  public final Document parse(String contents) {
    try {
      return (Document) NodeImpl.build(parseImpl(contents));
    } catch (Exception e) {
      throw new DOMParseException(contents, e);
    }
  }

  protected abstract NativeDocumentImpl createDocumentImpl();

  protected abstract NativeElementImpl getElementByIdImpl(NativeDocumentImpl document, String id);

  protected abstract NativeNodeListImpl getElementsByTagNameImpl(
      NativeNodeImpl node, String tagName);

  protected abstract String getPrefixImpl(NativeNodeImpl node);

  protected abstract NativeNodeImpl importNodeImpl(
      NativeDocumentImpl document, NativeNodeImpl importedNode, boolean deep);

  protected abstract NativeDocumentImpl parseImpl(String contents);

  abstract String toStringImpl(ProcessingInstructionImpl node);

  abstract String toStringImpl(NodeImpl node);
}
