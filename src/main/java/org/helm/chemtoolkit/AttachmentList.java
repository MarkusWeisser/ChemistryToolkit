/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author chistyakov
 *
 */
public class AttachmentList extends ArrayList<Attachment> {

  /**
   * 
   */
  private static final long serialVersionUID = -8489331560640955474L;

  public AttachmentList() {
    super();
  }

  @Override
  public Attachment get(int index) {
    return super.get(index).cloneAttachment();
  }

  public AttachmentList cloneList() {
    AttachmentList cloned = new AttachmentList();
    for (int i = 0; i < this.size(); i++) {
      cloned.add(this.get(i).cloneAttachment());
    }
    Collections.sort(cloned);
    return cloned;

  }

}
