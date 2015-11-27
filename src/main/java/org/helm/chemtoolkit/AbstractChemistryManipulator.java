/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.helm.chemtoolkit.AbstractChemistryManipulator.InputType;
import org.openscience.cdk.exception.CDKException;

import chemaxon.struc.MolBond;

/**
 * @author chistyakov
 *
 */
public abstract class AbstractChemistryManipulator {

	public enum InputType {
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
	 * @param data
	 *            SMILES or Molfile
	 * @param type
	 *            type of input data
	 * @return SMILES or Molfile
	 * @throws Exception
	 * @throws CTKException
	 */
	public abstract String convert(String data, InputType type) throws CTKException;

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
	 * @throws CTKException
	 *             general ChemToolKit exception passed to HELMToolKit
	 */

	public abstract MoleculeInfo getMoleculeInfo(String smiles) throws CTKException;

	/**
	 * 
	 * @param smiles
	 * @return MolFile string
	 * @throws CTKException
	 *             general ChemToolKit exception passed to HELMToolKit
	 * @throws Exception
	 *             java exception
	 */

	public abstract String convertSMILES2MolFile(String smiles) throws CTKException;

	/**
	 * 
	 * @param molfile
	 * @return SMILES string
	 * @throws CTKException
	 *             general ChemToolKit exception passed to HELMToolKit
	 * @throws IOException
	 *             java exception
	 * @throws CDKException
	 */

	public abstract String convertMolFile2SMILES(String molfile) throws CTKException;

	/**
	 * 
	 * @param smiles
	 * @return canonicalized smiles
	 * @throws CTKException
	 *             general ChemToolkit exception passed to HELMToolkit
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

	/**
	 * 
	 * @param first
	 * @param firstRgroup
	 * @param second
	 * @param secondRgroup
	 * @return
	 */
	public abstract IMoleculeBase merge(IMoleculeBase first, IAtomBase firstRgroup, IMoleculeBase second, IAtomBase secondRgroup);

	/**
	 * 
	 * @param extendedSmiles
	 * @return
	 */

	public List<String> getRGroupsFromExtendedSmiles(String extendedSmiles) {
		List<String> list = new ArrayList<String>();
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
					numbers = "R" + numbers;
					list.add(numbers);
				}
			}
		}

		return list;
	}

	public IAtomBase removeRgroup(IMoleculeBase molecule, IAtomBase rgroup) {

		IAtomBase atom = null;
		if (rgroup.getIBondCount() == 1) {
			IBondBase bond = rgroup.getIBond(0);
			if (bond.getIAtom1().equals(rgroup)) {
				atom = bond.getIAtom2();
			} else {
				atom = bond.getIAtom1();
			}
			molecule.removeINode(rgroup);

		}
		return atom;
	}

}
