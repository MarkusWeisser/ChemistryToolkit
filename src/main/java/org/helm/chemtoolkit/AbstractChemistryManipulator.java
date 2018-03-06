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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chistyakov
 *
 */
public abstract class AbstractChemistryManipulator {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractChemistryManipulator.class);

  protected static final String SMILES_EXTENSION_SEPARATOR_REGEX = "\\|";

  /**
   * 
   * {@code StType} chemical notation type
   * 
   * @author <a href="mailto:chistyakov@quattro-research.com">Dmitry Chistyakov</a>
   * @version $Id$
   */
  public enum StType {
    SMILES, MOLFILE, SEQUENCE
  }

  /**
   * 
   * {@code OutputType} image type
   * 
   * @author <a href="mailto:chistyakov@quattro-research.com">Dmitry Chistyakov</a>
   * @version $Id$
   */

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
   * produces chemical notation for input molecule
   * 
   * @param container chemical molecule
   * @param type of chemical notation
   * @return String that represents input molecule
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public abstract String convertMolecule(AbstractMolecule container, StType type) throws CTKException;

  
  public abstract String convertMolIntoSmilesWithAtomMapping(String molfile) throws CTKException;
  /**
   * convert input notation to another
   * 
   * @param data chemical notation to convert
   * @param type type of input data instance of {@link StType}
   * @return chemical notation
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public abstract String convert(String data, StType type) throws CTKException;
  
  /**
   * convert extended smiles format to smiles with atom mappings
   * 
   * @param data chemical notation to convert
   * @return chemical notation
   */
  public String convertExtendedSmiles(String data){
	  if(data != null){
	  Pattern pattern = Pattern.compile("\\[\\*\\]|\\*");
	  Matcher matcher = pattern.matcher(data);
	  	if(matcher  != null){
	  		String smiles = data.split("\\|")[0];
	  		List<Integer> rgroupInformation = extractRgroups(data);
	  		StringBuilder sb = new StringBuilder();
	  		int start = 0;
	  		int index = 0;
	  		String rGroup = "";
	  		while(matcher.find() && rgroupInformation.size() > 0){
	  			rGroup = smiles.substring(start, matcher.end());
	  			rGroup = rGroup.replace(matcher.group(),  "[H:"  +rgroupInformation.get(index) + "]");
	  			sb.append(rGroup);
	  			index ++;
	  			start = matcher.end();
	  		}
	  		if( start < smiles.length()){
	  			sb.append(smiles.substring(start));
	  		}
	  		return sb.toString();
	  	}
	  }
	  return data;
  }
  
  /**
   * extract Rgroups from extended smiles
   * 
   * @param data extended smiles
   * @return List of Rgroups
   */
  private List<Integer> extractRgroups(String data){
	  Pattern pattern = Pattern.compile("R[1-9]\\d*");
	  Matcher matcher = pattern.matcher(data);
	  List<Integer> listValues = new ArrayList<Integer>();
	  
	  while(matcher.find()){
		listValues.add(Integer.parseInt(matcher.group().split("R")[1]));  
	  }
	  return listValues;
  }
  
  /**
   * 
   * @param smiles to validate
   * @return true if smiles valid
   */
  public abstract boolean validateSMILES(String smiles);

  /**
   * returns molecule info like molecular formula,exact mass and molecular weight
   * 
   * @param container input AbstractMolecule
   * @return org.helm.chemtoolkit.MoleculeInfo object
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */

  public abstract MoleculeInfo getMoleculeInfo(AbstractMolecule container) throws CTKException;

  /**
   * returns canonical smiles
   * 
   * @param data smiles to canonicalize
   * @return canonicalized smiles
   * @throws CTKException general ChemToolkit exception passed to HELMToolkit
   * @throws CTKSmilesException if the smiles is not valid
   */
  public abstract String canonicalize(String data) throws CTKException, CTKSmilesException;

  /**
   * renders a image of given molecule
   * 
   * @param molFile for rendering
   * @param outputType a image type instance of {@link OutputType}
   * @param width the image width
   * @param height the image height
   * @param rgb a color code of image background
   * @return image
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public abstract byte[] renderMol(String molFile, OutputType outputType, int width, int height, int rgb)
      throws CTKException;

  /**
   * renders a image of molecule
   * 
   * @param sequence for rendering
   * @param outputType a image type instance of {@link OutputType}
   * @param width the image width
   * @param height the image height
   * @param rgb a color code of image background
   * @return image
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public abstract byte[] renderSequence(String sequence, OutputType outputType, int width, int height, int rgb)
      throws CTKException;

  /**
   * returns a molecule i)nstance of {@link AbstractMolecule}
   * 
   * @param smiles smiles string
   * @param attachments instance of {@link AttachmentList}
   * @return molecule instance of {@link AbstractMolecule}
   * @throws IOException smiles can not be converted to a molecule
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  public abstract AbstractMolecule getMolecule(String smiles, AttachmentList attachments) throws IOException,
      CTKException;

  /**
   * merges second molecule to first using given rGroups
   * 
   * @param firstContainer a first molecule to merge instance of {@link AbstractMolecule}
   * @param firstRgroup atom of first molecule to be removed, the connected atom is used for merging, instance of
   *          {@link IAtomBase}
   * @param secondContainer a second molecule to merge, instance of {@link AbstractMolecule}
   * @param secondRgroup of second molecule to be removed, the connected atom is used for merging, instance of
   *          {@link IAtomBase}
   * @return merged molecule instance of {@link AbstractMolecule}
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   * 
   */
  public AbstractMolecule merge(AbstractMolecule firstContainer, IAtomBase firstRgroup,
      AbstractMolecule secondContainer,
      IAtomBase secondRgroup) throws CTKException {

    if (firstContainer.isSingleStereo(firstRgroup) && secondContainer.isSingleStereo(secondRgroup)) {
      throw new CTKException("Both R atoms are connected to chiral centers");
    }
    if (firstContainer == secondContainer) {
      firstContainer.dearomatize();
      secondContainer.dearomatize();

      IAtomBase atom1 = getNeighborAtom(firstRgroup);
      IAtomBase atom2 = getNeighborAtom(secondRgroup);
      firstContainer.removeAttachment(firstRgroup);
      firstContainer.removeAttachment(secondRgroup);
      setStereoInformation(firstContainer, firstRgroup, firstContainer, secondRgroup, atom1, atom2);
      firstContainer.removeINode(firstRgroup);
      firstContainer.removeINode(secondRgroup);
    } else {

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

    }

    return firstContainer;
  }

  /**
   * recycles and set stereo information on firstContaner
   * 
   * @param firstContainer a first molecule instance of {@link AbstractMolecule}
   * @param firstRgroup atom to remove, instance of {@link IAtomBase}
   * @param secondContainer a second molecule, instance of {@link AbstractMolecule}
   * @param secondRgroup atom to remove, instance of {@link IAtomBase}
   * @param atom1 atom connected to firstGroup
   * @param atom2 atom connected to secondGroup
   * @return true, if stereo information was set, false otherwise
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  protected boolean setStereoInformation(AbstractMolecule firstContainer, IAtomBase firstRgroup,
      AbstractMolecule secondContainer, IAtomBase secondRgroup, IAtomBase atom1, IAtomBase atom2) throws CTKException {
    IStereoElementBase stereo = null;
    boolean result = false;
    if (firstContainer.isSingleStereo(firstRgroup)) {
      stereo = getStereoInformation(firstContainer, firstRgroup, atom2, atom1);

    }
    if (secondContainer.isSingleStereo(secondRgroup)) {
      stereo = getStereoInformation(secondContainer, secondRgroup, atom1, atom2);

    }
    if (stereo != null) {

      firstContainer.addIBase(stereo);

      result = true;
    }
    return result;
  }

  /**
   * 
   * @param smiles  extended smiles
   * @return List containing all rgroups from the given smiles
   */

  protected List<String> getRGroupsFromExtendedSmiles(String smiles) {
  String  extendedSmiles = getExtension(smiles);
    List<String> list = new ArrayList<>();
    if (extendedSmiles != null) {
      Integer currIndex = 0;
      char[] items = extendedSmiles.toCharArray();
      List<Integer> indexes = new ArrayList<Integer>();

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
            list.add("R" + numbers);
          }
        }
      }
    }
    
    if(extendedSmiles == null){
    	Pattern pattern = Pattern.compile("\\[\\*:([1-9]\\d*)\\]|\\[\\w+:([1-9]\\d*)");
    	Matcher matcher = pattern.matcher(smiles);

  	  
  	  while(matcher.find()){
  		  String info = "";
  		  if(matcher.group(1) != null){
  			  info = matcher.group(1);
  		  }
  		  if(matcher.group(2) != null){
  			  info = matcher.group(2);
  		  }
  		  
  		  list.add("R" + info);
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
   * @param atom1 first atom
   * @param atom2 second atom
   * @return bond betwenn the given atoms
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  protected abstract IBondBase bindAtoms(IAtomBase atom1, IAtomBase atom2) throws CTKException;

  /**
   * @param container first molecule
   * @param secondContainer second molecule
   * @return AttachmentList containing all attachments from the merged molecules
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
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

  /**
   * @param container molecule
   * @param rGroup rgroup of the given molecule
   * @param atom1 first atom
   * @param atom2 second atom
   * @return StereoElement of the given molecule
   * @throws CTKException general ChemToolKit exception passed to HELMToolKit
   */
  protected abstract IStereoElementBase getStereoInformation(AbstractMolecule container, IAtomBase rGroup,
      IAtomBase atom1, IAtomBase atom2)
          throws CTKException;
  
  
 

}
