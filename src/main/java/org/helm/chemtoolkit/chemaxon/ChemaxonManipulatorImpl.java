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
package org.helm.chemtoolkit.chemaxon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.ChemistryManipulator;
import org.helm.chemtoolkit.MoleculeInfo;
import org.helm.chemtoolkit.ChemistryManipulator.InputType;

import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.ElementalAnalyserPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.MolAtom;
import chemaxon.struc.Molecule;

/**
 * @author chistyakov
 *
 */
public class ChemaxonManipulatorImpl implements ChemistryManipulator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.ChemistryManipulator#convert(java.lang.String,
	 * org.helm.chemtoolkit.ChemistryManipulator.InputType)
	 */
	@Override
	public String convert(String data, InputType type) throws CTKException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.ChemistryManipulator#validateSMILES(java.lang.
	 * String)
	 */
	@Override
	public boolean validateSMILES(String smiles) throws CTKException {
		Molecule mol;
		try {
			mol = getMolecule(smiles);
			for (int i = 0; i < mol.getAtomCount(); i++) {
				MolAtom a = mol.getAtom(i);
				a.valenceCheck();
				if (a.hasValenceError()) {
					return false;
				}

			}
		} catch (IOException e) {
			throw new CTKException("unable to get molecule from SMILES", e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.ChemistryManipulator#getMoleculeInfo(java.lang.
	 * String)
	 */
	@Override
	public MoleculeInfo getMoleculeInfo(String smiles) throws CTKException {

		Molecule molecule;
		try {
			molecule = getMolecule(smiles);
			ElementalAnalyserPlugin plugin = new ElementalAnalyserPlugin();
			plugin.setDoublePrecision(2);
			plugin.setMolecule(molecule);
			plugin.run();
			MoleculeInfo moleculeInfo = new MoleculeInfo();
			moleculeInfo.setMolecularFormula(plugin.getFormula());
			moleculeInfo.setMolecularWeight(plugin.getMass());
			moleculeInfo.setExactMass(plugin.getExactMass());
		} catch (IOException e) {
			throw new CTKException("unable to get molecule from SMILES", e);
		} catch (PluginException e) {
			throw new CTKException("unable to analyse molecule", e);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.ChemistryManipulator#convertSMILES2MolFile(java.lang
	 * .String)
	 */
	@Override
	public String convertSMILES2MolFile(String smiles) throws CTKException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.ChemistryManipulator#convertMolFile2SMILES(java.lang
	 * .String)
	 */
	@Override
	public String convertMolFile2SMILES(String molfile) throws CTKException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.ChemistryManipulator#canonicalize(java.lang.String)
	 */
	@Override
	public String canonicalize(String smiles) throws CTKException, CTKSmilesException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * convert SMILES to Molecule
	 * 
	 * @param smiles
	 *            input SMILES string
	 * @return Molecule object
	 * @throws java.io.IOException
	 */

	private Molecule getMolecule(String smiles) throws IOException {
		Molecule molecule = null;
		if (null != smiles) {
			InputStream is = new ByteArrayInputStream(smiles.getBytes());
			MolImporter importer = new MolImporter(is);
			molecule = importer.read();
		}
		return molecule;
	}

}
