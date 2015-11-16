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
package org.helm.chemtoolkit.cdk;
/**
 * @author chistyakov
 *
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.ChemistryManipulator;
import org.helm.chemtoolkit.MoleculeInfo;
import org.helm.chemtoolkit.ChemistryManipulator.InputType;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.SMILESWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class CDKManipulatorImpl implements ChemistryManipulator {
	private static final String SMILES_EXTENSION_SEPARATOR_REGEX = "\\|";

	/**
	 * 
	 * @param smiles
	 *            extended SMILES (Chemaxon)
	 * @return normalized SMILES
	 */
	private String normalize(String smiles) {
		String result = null;
		String[] components = smiles.split(SMILES_EXTENSION_SEPARATOR_REGEX);
		result = components[0];

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see org.helm.chemtoolkit.ChemistryManipulator#validateSMILES(java.lang.
	 * String)
	 */
	public boolean validateSMILES(String smiles) {
		smiles = normalize(smiles);
		SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
		try {
			@SuppressWarnings("unused")
			// nothing TODO with molecule, only used for validation
			IAtomContainer molecule = smilesParser.parseSmiles(smiles);

		} catch (InvalidSmilesException e) {

			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see org.helm.chemtoolkit.ChemistryManipulator#getMoleculeInfo(java.lang.
	 * String)
	 */
	public MoleculeInfo getMoleculeInfo(String smiles) throws CTKException {
		smiles = normalize(smiles);
		MoleculeInfo moleculeInfo = new MoleculeInfo();
		SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
		try {

			IAtomContainer molecule = smilesParser.parseSmiles(smiles);
			moleculeInfo.setMolecularWeight(AtomContainerManipulator.getNaturalExactMass(molecule));
			moleculeInfo.setMolecularFormula(
					MolecularFormulaManipulator.getString(MolecularFormulaManipulator.getMolecularFormula(molecule)));

			moleculeInfo.setExactMass(MolecularFormulaManipulator
					.getMajorIsotopeMass(MolecularFormulaManipulator.getMolecularFormula(molecule)));

		} catch (InvalidSmilesException e) {
			throw new CTKSmilesException("smiles is invalid", e);

		}
		return moleculeInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.ChemistryManipulator#convertSMILES2MolFile(java.lang
	 * .String)
	 */
	public String convertSMILES2MolFile(String smiles) throws CTKException {
		String result = null;
		smiles = normalize(smiles);
		try (StringWriter stringWriter = new StringWriter();
				MDLV2000Writer writer = new MDLV2000Writer(stringWriter);) {
			try {
				SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
				IAtomContainer molecule = smilesParser.parseSmiles(smiles);
				StructureDiagramGenerator sdg = new StructureDiagramGenerator();
				sdg.setMolecule(molecule);
				sdg.generateCoordinates();
				molecule = sdg.getMolecule();
				writer.writeMolecule(molecule);
				result = stringWriter.toString();
			} catch (InvalidSmilesException e) {
				throw new CTKSmilesException("invalid smiles", e);
			} catch (CDKException e) {
				throw new CTKException("unable to generate coordinates", e);
			} catch (Exception e) {
				throw new CTKException("unable to write molecule", e);
			}
		} catch (IOException e) {
			throw new CTKException("unable to invoke the MDL writer", e);
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.ChemistryManipulator#convertMolFile2SMILES(java.lang
	 * .String)
	 */
	public String convertMolFile2SMILES(String molfile) throws CTKException {
		String result = null;

		try (StringWriter stringWriter = new StringWriter();
				SMILESWriter writer = new SMILESWriter(stringWriter);
				StringReader stringReader = new StringReader(molfile);
				MDLV2000Reader reader = new MDLV2000Reader(stringReader)) {
			IAtomContainer molecule = (IAtomContainer) reader
					.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));
			writer.write(molecule);
			result = stringWriter.toString();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			throw new CTKException("illegal argument", e);
		} catch (CDKException e) {
			System.out.println(e.getMessage());
			throw new CTKException("Unable to get a molecule from molfile", e);
		} catch (IOException e) {
			throw new CTKException("unable to invoke the MDL writers/readers", e);

		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.ChemistryManipulator#convert(java.lang.String,
	 * org.helm.chemtoolkit.ChemistryManipulator.InputType)
	 */
	@Override
	public String convert(String data, InputType type) throws CTKException {
		String result = null;

		switch (type) {
		case SMILES:
			result = convertSMILES2MolFile(data);
			break;
		case MOLFILE:
			result = convertMolFile2SMILES(data);
			break;
		default:
			break;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.ChemistryManipulator#canonicalize(java.lang.String)
	 */
	@Override
	public String canonicalize(String smiles) throws CTKException, CTKSmilesException {
		smiles = normalize(smiles);
		SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer molecule;
		String result = null;
		try {
			molecule = parser.parseSmiles(smiles);
			SmilesGenerator generator = SmilesGenerator.unique();
			result = generator.create(molecule);
		} catch (InvalidSmilesException e) {
			throw new CTKSmilesException("invalid SMILES", e);
		} catch (CDKException e) {
			throw new CTKException("unable to canonicalize SMILES", e);
		}
		return result;
	}

}
