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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chistyakov
 *
 */
public abstract class AbstractChemistryManipulator {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractChemistryManipulator.class);

  protected static final String SMILES_EXTENSION_SEPARATOR_REGEX = "\\|";

  public enum StType {
    SMILES, MOLFILE, SEQUENCE
  }

  public enum OutputType {
    PNG("PNG"), GIF("GIF"), JPG("JPG");
    private final String value;

    private OutputType(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  /**
   * 
   * @param molecule
   * @return
   * @throws CTKException
   */
  public abstract String convertMolecule(AbstractMolecule container, StType type) throws CTKException;

  /**
   * 
   * @param data SMILES or Molfile
   * @param type type of input data
   * @return SMILES or Molfile
   * @throws Exception
   * @throws CTKException
   */
  public abstract String convert(String data, StType type) throws CTKException;

  /**
   * 
   * @param smiles
   * @return true if smiles valid
   * @throws CTKException
   */
  public abstract boolean validateSMILES(String smiles) throws CTKException;

  /**
   * 
   * @param smiles
   * @return org.helm.chemtoolkit.MoleculeInfo object
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */

  public abstract MoleculeInfo getMoleculeInfo(AbstractMolecule container) throws CTKException;

  /**
   * 
   * @param smiles
   * @return MolFile string
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   * @throws Exception java exception
   */

  /**
   * 
   * @param molfile
   * @return SMILES string
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   * @throws IOException java exception
   * @throws CDKException
   */

  /**
   * 
   * @param smiles
   * @return canonicalized smiles
   * @throws CTKException general ChemToolkit exception passed to HELMToolkit
   */
  public abstract String canonicalize(String data) throws CTKException, CTKSmilesException;

  /**
   * 
   * @param molFile
   * @param width
   * @param height
   * @param rgb
   * @return
   * @throws CTKException
   */
  public abstract byte[] renderMol(String molFile, OutputType outputType, int width, int height, int rgb)
      throws CTKException;

  /**
   * 
   * @param sequence
   * @param outputType
   * @param width
   * @param height
   * @param rgb
   * @return
   * @throws CTKException
   */
  public abstract byte[] renderSequence(String sequence, OutputType outputType, int width, int height, int rgb)
      throws CTKException;

  public abstract AbstractMolecule getMolecule(String smiles, AttachmentList attachments) throws IOException,
      CTKException;

  /**
   * 
   * @param first
   * @param firstRgroup
   * @param second
   * @param secondRgroup
   * @return
   * @throws CTKException
   * 
   */
  public AbstractMolecule merge(AbstractMolecule firstContainer, IAtomBase firstRgroup,
      AbstractMolecule secondContainer,
      IAtomBase secondRgroup) throws CTKException {

    if (firstContainer.isSingleStereo(firstRgroup) && secondContainer.isSingleStereo(secondRgroup)) {
      throw new CTKException("Both R atoms are connected to chiral centers");
    }
    firstContainer.dearomatize();
    secondContainer.dearomatize();
    IAtomBase atom1 = getNeighborAtom(firstRgroup);
    IAtomBase atom2 = getNeighborAtom(secondRgroup);

    firstContainer.removeAttachment(firstRgroup);
    secondContainer.removeAttachment(secondRgroup);
    setStereoInformation(firstContainer, firstRgroup, secondContainer, secondRgroup, atom1, atom2);
    firstContainer.removeINode(firstRgroup);
    secondContainer.removeINode(secondRgroup);

    AttachmentList mergedAttachments = mergeAttachments(firstContainer, secondContainer);

    firstContainer.addIBase(secondContainer);

    firstContainer.setAttachments(mergedAttachments);
    firstContainer.generateCoordinates();
    // }
    return firstContainer;
  }

  /**
   * @param firstContainer
   * @param firstRgroup
   * @param secondContainer
   * @param secondRgroup
   * @param atom1
   * @param atom2
   * @throws CTKException
   */
  protected boolean setStereoInformation(AbstractMolecule firstContainer, IAtomBase firstRgroup,
      AbstractMolecule secondContainer, IAtomBase secondRgroup, IAtomBase atom1, IAtomBase atom2) throws CTKException {
    IStereoElementBase stereo = null;
    boolean result = false;
    if (firstContainer.isSingleStereo(firstRgroup)) {
      stereo = getStereoInformation(firstContainer, firstRgroup, atom1);
    }
    if (secondContainer.isSingleStereo(secondRgroup)) {
      stereo = getStereoInformation(secondContainer, secondRgroup, atom2);
    }
    if (stereo != null) {
      firstContainer.addIBase(stereo);
      result = true;
    }
    return result;
  }

  /**
   * 
   * @param extendedSmiles
   * @return
   */

  protected LinkedHashMap<Integer, String> getRGroupsFromExtendedSmiles(String extendedSmiles) {
    extendedSmiles = getExtension(extendedSmiles);
    LinkedHashMap<Integer, String> list = new LinkedHashMap<Integer, String>();
    if (extendedSmiles != null) {
      Integer currIndex = 0;
      char[] items = extendedSmiles.toCharArray();
      List<Integer> indexes = new ArrayList<Integer>();
      // currIndex = currIndex + extendedSmiles.indexOf("_R");
      while (extendedSmiles.indexOf("_R", currIndex) > 0) {
        currIndex = extendedSmiles.indexOf("_R", currIndex);
        indexes.add(currIndex);
        currIndex++;
      }

      for (int k = 0; k < items.length; k++) {
        if (items[k] == 'R') {
          indexes.add(currIndex + k);
          currIndex++;
        }
      }

      String[] tokens = extendedSmiles.split("R", -1);
      if (tokens.length > 1) {
        for (int i = 1; i < tokens.length; i++) {
          String token = tokens[i];
          char[] chars = token.toCharArray();
          String numbers = "";
          for (int j = 0; j < chars.length; j++) {
            String letter = String.valueOf(chars[j]);
            if (letter.matches("[0-9]")) {
              numbers += letter;
            } else {
              break;
            }
          }

          if (numbers.length() > 0) {
            // numbers = "R" + numbers;
            Integer value = Integer.valueOf(numbers);
            list.put(indexes.get(value - 1) - value * 3, "R" + numbers);
          }
        }
      }
    }

    return list;
  }

  protected IAtomBase getNeighborAtom(IAtomBase rgroup) throws CTKException {
    IAtomBase atom = null;
    if (rgroup.getIBondCount() == 1) {
      IBondBase bond = rgroup.getIBond(0);

      if ((bond.getIAtom1()).compare(rgroup)) {
        atom = bond.getIAtom2();

      } else {
        atom = bond.getIAtom1();

      }

    }

    return atom;

  }

  protected String getExtension(String smiles) {
    String result = null;
    try {

      String[] components = smiles.split(SMILES_EXTENSION_SEPARATOR_REGEX);
      result = components[1];
    } catch (ArrayIndexOutOfBoundsException e) {
// not extended SMILES
    }

    return result;
  }

  /**
   * @param atom1
   * @param atom2
   * @return
   * @throws CTKException
   */
  protected abstract IBondBase bindAtoms(IAtomBase atom1, IAtomBase atom2) throws CTKException;

  /**
   * @param first
   * @param second
   * @return
   * @throws CTKException
   */

  protected AttachmentList mergeAttachments(AbstractMolecule container, AbstractMolecule secondContainer)
      throws CTKException {
    AttachmentList result = new AttachmentList();
    int index = 1;
    for (Attachment item : container.getAttachments()) {
      Attachment a = item.cloneAttachment();
      container.changeAtomLabel(a.getCurrentIndex(), index);
      a.changeIndex(index);
      result.add(a);
      index++;
    }

    for (Attachment item : secondContainer.getAttachments()) {
      Attachment a = item.cloneAttachment();
      secondContainer.changeAtomLabel(a.getCurrentIndex(), index);
      a.changeIndex(index);
      result.add(a);
      index++;
    }
    container.clearFlags();
    secondContainer.clearFlags();
    return result;
  }

  // public abstract void changeAtomLabel(AbstractMolecule container, int index, int toIndex);

  /**
   * @param molecule
   * @param rGroup
   * @param atom
   * @return
   * @throws CTKException
   */
  protected abstract IStereoElementBase getStereoInformation(AbstractMolecule container, IAtomBase rGroup,
      IAtomBase atom)
          throws CTKException;

}
