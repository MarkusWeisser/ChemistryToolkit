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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chistyakov
 *
 */
public abstract class AbstractMolecule implements IChemObjectBase {

  public enum Flag {
    NONE, PROCESSED
  }

  private static final Logger LOG = LoggerFactory.getLogger(AbstractMolecule.class);

  protected AttachmentList attachments;

  protected List<IAtomBase> atoms;

  public AttachmentList getAttachments() {
    return attachments.cloneList();
  }

  public void setAttachments(AttachmentList attachments) {
    this.attachments = attachments.cloneList();
  }

  protected AbstractMolecule() {
  }

  public Map<String, IAtomBase> getRgroups() throws CTKException {
    Map<String, IAtomBase> rgroupMap = new HashMap<String, IAtomBase>();
    List<IAtomBase> atoms = getIAtomArray();
    for (int i = 0; i < atoms.size(); i++) {
      int rId = atoms.get(i).getRgroup();
      if (rId > 0) {
        rgroupMap.put("R" + rId, atoms.get(i));
      }
    }
    return rgroupMap;
  }

  public static int getIdFromLabel(String label) {
    int result = 0;
    String[] array = label.split("R");
    try {
      result = Integer.parseInt(array[1]);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      //
    }
    return result;
  }

  public IAtomBase getRGroupAtom(int groupId, boolean rgatom) throws CTKException {
    IAtomBase result = null;
    for (IAtomBase atom : getIAtomArray()) {
      if (atom.getRgroup() == groupId) {
        if (rgatom) {
          result = atom;
        } else {
          IBondBase bond = atom.getIBond(0);

          if (bond.getIAtom1().compare(atom)) {
            result = bond.getIAtom2();
          } else
            result = bond.getIAtom1();
        }

      }
    }

    return result;

  }

  public void clearFlags() {
    for (IAtomBase atom : getIAtomArray())
      atom.setFlag(Flag.NONE);
  }

  public abstract Object getMolecule();

  public abstract void dearomatize() throws CTKException;

  public abstract void generateCoordinates(int dem) throws CTKException;

  public abstract void removeINode(IAtomBase node) throws CTKException;

  public abstract void addIBase(IChemObjectBase object);

  public abstract void removeIBase(IChemObjectBase object);

  public abstract List<IBondBase> getIBondArray();

  public abstract AbstractMolecule cloneMolecule() throws CTKException;

  public abstract void changeAtomLabel(int index, int toIndex) throws CTKException;

  /**
   * check a given atom connections
   * 
   * @param atom to check
   * @return true if a atom has a single stereo connection
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public abstract boolean isSingleStereo(IAtomBase atom) throws CTKException;

  /**
   * removes a given attachment from molecule
   * 
   * @param toRemove a atom to be removed
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public void removeAttachment(IAtomBase toRemove) throws CTKException {

    Map<String, IAtomBase> groups = getRgroups();
    for (String key : groups.keySet()) {

      if (groups.get(key).compare(toRemove)) {
        for (int i = 0; i < attachments.size(); i++) {
          if (attachments.get(i).getLabel().equals(key)) {
            attachments.remove(i);
            break;
          }
        }
      }
    }
  }

  public List<IAtomBase> getIAtomArray() {
    return atoms;
  }

}
