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
import org.gwtproject.xml.client.CDATASection;
import org.gwtproject.xml.client.Comment;
import org.gwtproject.xml.client.DOMException;
import org.gwtproject.xml.client.Document;
import org.gwtproject.xml.client.DocumentFragment;
import org.gwtproject.xml.client.Element;
import org.gwtproject.xml.client.Node;
import org.gwtproject.xml.client.NodeList;
import org.gwtproject.xml.client.ProcessingInstruction;
import org.gwtproject.xml.client.Text;
import org.gwtproject.xml.client.impl.CDATASectionImpl.NativeCDATASectionImpl;
import org.gwtproject.xml.client.impl.CommentImpl.NativeCommentImpl;
import org.gwtproject.xml.client.impl.ElementImpl.NativeElementImpl;
import org.gwtproject.xml.client.impl.ProcessingInstructionImpl.NativeProcessingInstructionImpl;
import org.gwtproject.xml.client.impl.TextImpl.NativeTextImpl;

/** This class wraps the native Document object. */
class DocumentImpl extends NodeImpl implements Document {

  @JsType(isNative = true, name = "Node", namespace = JsPackage.GLOBAL)
  static class NativeDocumentImpl extends NativeNodeImpl {

    NativeElementImpl documentElement;

    native NativeCommentImpl createComment(String data);

    native NativeCDATASectionImpl createCDATASection(String data);

    native NativeDocumentImpl createDocumentFragment();

    native NativeElementImpl createElement(String tag);

    native NativeProcessingInstructionImpl createProcessingInstruction(String target, String data);

    native NativeTextImpl createTextNode(String data);

    native NativeElementImpl getElementById(String id);

    native NativeElementImpl nodeFromID(String id);

    native NativeNodeImpl importNode(NativeNodeImpl importedNode, boolean deep);
  }

  private final NativeDocumentImpl document;

  protected DocumentImpl(NativeDocumentImpl o) {
    super(o);
    this.document = o;
  }

  /**
   * This function delegates to the native method <code>createCDATASection</code> in XMLParserImpl.
   */
  @Override
  public CDATASection createCDATASection(String data) {
    try {
      return (CDATASection) NodeImpl.build(document.createCDATASection(data));
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_CHARACTER_ERR, e, this);
    }
  }

  /** This function delegates to the native method <code>createComment</code> in XMLParserImpl. */
  @Override
  public Comment createComment(String data) {
    try {
      return (Comment) NodeImpl.build(document.createComment(data));
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_CHARACTER_ERR, e, this);
    }
  }

  /**
   * This function delegates to the native method <code>createDocumentFragment</code> in
   * XMLParserImpl.
   */
  @Override
  public DocumentFragment createDocumentFragment() {
    try {
      return (DocumentFragment) NodeImpl.build(document.createDocumentFragment());
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_CHARACTER_ERR, e, this);
    }
  }

  /** This function delegates to the native method <code>createElement</code> in XMLParserImpl. */
  @Override
  public Element createElement(String tagName) {
    try {
      return (Element) NodeImpl.build(document.createElement(tagName));
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_CHARACTER_ERR, e, this);
    }
  }

  /**
   * This function delegates to the native method <code>createProcessingInstruction</code> in
   * XMLParserImpl.
   */
  @Override
  public ProcessingInstruction createProcessingInstruction(String target, String data) {
    try {
      return (ProcessingInstruction)
          NodeImpl.build(document.createProcessingInstruction(target, data));
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_CHARACTER_ERR, e, this);
    }
  }

  /** This function delegates to the native method <code>createTextNode</code> in XMLParserImpl. */
  @Override
  public Text createTextNode(String data) {
    try {
      return (Text) NodeImpl.build(document.createTextNode(data));
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_CHARACTER_ERR, e, this);
    }
  }

  /**
   * This function delegates to the native method <code>getDocumentElement</code> in XMLParserImpl.
   */
  @Override
  public Element getDocumentElement() {
    return (Element) NodeImpl.build(document.documentElement);
  }

  /** This function delegates to the native method <code>getElementById</code> in XMLParserImpl. */
  @Override
  public Element getElementById(String elementId) {
    return (Element) NodeImpl.build(XMLParserImpl.getElementById(document, elementId));
  }

  /**
   * This function delegates to the native method <code>getElementsByTagName</code> in
   * XMLParserImpl.
   */
  @Override
  public NodeList getElementsByTagName(String tagName) {
    return new NodeListImpl(XMLParserImpl.getElementsByTagName(node, tagName));
  }

  /** This function delegates to the native method <code>importNode</code> in XMLParserImpl. */
  @Override
  public Node importNode(Node importedNode, boolean deep) {
    NodeImpl actualNode = (NodeImpl) importedNode;
    try {
      return NodeImpl.build(XMLParserImpl.importNode(document, actualNode.node, deep));
    } catch (Exception e) {
      throw new DOMNodeException(DOMException.INVALID_STATE_ERR, e, this);
    }
  }
}
