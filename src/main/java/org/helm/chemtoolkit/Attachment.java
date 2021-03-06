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

/**
 * @author chistyakov
 *
 */
public class Attachment implements Comparable<Attachment> {

  private String id;

  private String label;

  private String name;

  private String smiles;

  public int getCurrentIndex() {
    int result = 0;
    String[] array = label.split("R");
    try {
      result = Integer.parseInt(array[1]);
    } catch (NumberFormatException e) {
      //
    }
    return result;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSmiles() {
    return smiles;
  }

  public void setSmiles(String smiles) {
    this.smiles = smiles;
  }

  public void changeIndex(int index) {
    String currIndex = String.valueOf(getCurrentIndex());
    String toIndex = String.valueOf(index);
    this.id = this.id.replace(currIndex, toIndex);
    this.label = this.label.replace(currIndex, toIndex);
    this.smiles = this.smiles.replace(currIndex, toIndex);

  }

  public Attachment cloneAttachment() {
    return new Attachment(this.id, this.label, this.name, this.smiles);

  }

  public Attachment(String id, String label, String name, String smiles) {
    this.id = id;
    this.label = label;
    this.name = name;
    this.smiles = smiles;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Attachment o) {
    int result = this.label.compareTo(o.getLabel());
    if (result != 0)
      return result;
    result = this.getCurrentIndex() - o.getCurrentIndex();
    if (result != 0) {
      return result / Math.abs(result);
    }
    return 0;
  }

}
