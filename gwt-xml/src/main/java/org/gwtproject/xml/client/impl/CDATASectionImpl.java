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

/** This class implements the CDATASectionImpl interface. */
class CDATASectionImpl extends TextImpl implements CDATASection {

  @JsType(isNative = true, name = "Object", namespace = JsPackage.GLOBAL)
  static class NativeCDATASectionImpl extends NativeTextImpl {}

  protected CDATASectionImpl(NativeCDATASectionImpl o) {
    super(o);
  }

  /**
   * This method returns the string representation of this <code>CDATASectionImpl</code>.
   *
   * @return the string representation of this <code>CDATASectionImpl</code>.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder b = new StringBuilder("<![CDATA[");
    b.append(getData());
    b.append("]]>");
    return b.toString();
  }
}
