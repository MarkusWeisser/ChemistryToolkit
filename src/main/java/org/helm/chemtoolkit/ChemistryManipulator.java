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

import org.openscience.cdk.exception.CDKException;

/**
 * @author chistyakov
 *
 */
public interface ChemistryManipulator {
	enum InputType {
		SMILES, MOLFILE
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
	public String convert(String data, InputType type) throws CTKException;

	/**
	 * 
	 * @param smiles
	 * @return true if smiles valid
	 * @throws CTKException
	 */
	public boolean validateSMILES(String smiles) throws CTKException;

	/**
	 * 
	 * @param smiles
	 * @return org.helm.chemtoolkit.MoleculeInfo object
	 * @throws CTKException
	 *             general ChemToolKit exception passed to HELMToolKit
	 */

	public MoleculeInfo getMoleculeInfo(String smiles) throws CTKException;

	/**
	 * 
	 * @param smiles
	 * @return MolFile string
	 * @throws CTKException
	 *             general ChemToolKit exception passed to HELMToolKit
	 * @throws Exception
	 *             java exception
	 */

	public String convertSMILES2MolFile(String smiles) throws CTKException;

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

	public String convertMolFile2SMILES(String molfile) throws CTKException;

	/**
	 * 
	 * @param smiles
	 * @return canonicalized smiles
	 * @throws CTKException
	 *             general ChemToolkit exception passed to HELMToolkit
	 */
	public String canonicalize(String smiles) throws CTKException, CTKSmilesException;

  public Molecule merge(Molecule first, MolAtom firstRgroup, Molecule second, MolAtom secondRgroup);

}
